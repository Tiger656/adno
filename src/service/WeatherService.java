package service;

import entity.CityAggTemperature;
import entity.City;
import entity.DailyTemp;
import org.apache.commons.lang3.time.StopWatch;
import service.aggregator.AggregationType;
import service.aggregator.Aggregator;
import service.aggregator.AggregatorFactory;
import service.filter.CityFilter;

import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class WeatherService {

    private static final Long POPULATION = 50000L;
    private static final Integer NUM_OF_THREADS = Runtime.getRuntime().availableProcessors() - 1;
    private final WeatherAPI weatherAPI;
    private final CityFilter cityFilter; //Maybe here should create interface?

    public WeatherService(WeatherAPI weatherAPI, CityFilter cityFilter) {
        this.weatherAPI = weatherAPI;
        this.cityFilter = cityFilter;
    }

    public Set<CityAggTemperature> calculateAggregatedTempForCitiesForLastYear(Set<String> cityIds, AggregationType aggregationType) {
        Set<City> cities = retrieveCities(cityIds);
        cities = cityFilter.filterCitiesByPopulationMoreThan(cities, POPULATION);

        StopWatch stopWatch = new StopWatch(); //remove
        stopWatch.start(); //remove
        Map<String, List<DailyTemp>> dailyTempsByCities = retrieveYearDailyTemperatureForCities(cityIds);
        stopWatch.stop(); //remove
        System.out.println(stopWatch.getTime(TimeUnit.SECONDS)); //remove

        /*ConcurrentMap<String, List<DailyTemp>> dailyTempsByCities = new ConcurrentHashMap<>();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        cityIds.parallelStream().forEach(cityId -> {
            try {
                System.out.println("Thread name: " + Thread.currentThread().getName() + ", Attempt: " +  ". Start");  //remove
                Thread.sleep(TimeUnit.SECONDS.toMillis(1)); //remove
                System.out.println("Thread name: " + Thread.currentThread().getName() + ", Attempt: " +  ". Finish"); //remove
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            dailyTempsByCities.put(cityId, weatherAPI.getLastYearTemperature(cityId));
        });
        stopWatch.stop();
        System.out.println(stopWatch.getTime(TimeUnit.SECONDS));*/


        return aggregateTemperaturesForCities(cities, dailyTempsByCities, aggregationType);
    }

    private Set<CityAggTemperature> aggregateTemperaturesForCities(Set<City> cities, Map<String, List<DailyTemp>> dailyTempsByCities, AggregationType aggregationType) {
        return cities.stream().map(city -> new CityAggTemperature(
                city.getId(),
                city.getName(),
                city.getPopulation(),
                aggregationType.toString(),
                aggregateTemperature(
                        convertToSimpleValues(dailyTempsByCities.get(city.getId())),
                        aggregationType)
        )).collect(Collectors.toSet());
    }

    private List<Double> convertToSimpleValues(List<DailyTemp> dailyTemps) {
        if (Objects.nonNull(dailyTemps)) {
            return dailyTemps.stream().map(DailyTemp::getTemperature).collect(Collectors.toList());
        }
        return Collections.emptyList(); // Maybe I should improve my response?
    }


    private Double aggregateTemperature(List<Double> values, AggregationType aggregationType) {
        AggregatorFactory aggregatorFactory = new AggregatorFactory();
        Aggregator aggregator;
        try {
            aggregator = aggregatorFactory.createAggregator(aggregationType);
        } catch (Exception e) {
            //logging
            throw new RuntimeException(e); //throw NoSuchAggregatorException
        }
        return aggregator.aggregate(values);
    }

    private Map<String, List<DailyTemp>> retrieveYearDailyTemperatureForCities(Set<String> cityIds) {
        ForkJoinPool forkJoinPool = new ForkJoinPool(NUM_OF_THREADS);
        return forkJoinPool.invoke(new MyFork(weatherAPI, cityIds.stream().toList(), 0, cityIds.size() - 1));
    }

    private Set<City> retrieveCities(Set<String> cityIds) {
        return weatherAPI.getAllCitiesByIds(cityIds);
    }
}
