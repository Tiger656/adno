package service;

import dto.City;
import dto.CityAggregatedWeather;
import dto.DailyTemp;
import service.aggregator.AggregationType;
import service.aggregator.Aggregator;
import service.aggregator.AggregatorFactory;
import service.filter.CityFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class WeatherService {

    private static final Integer NUM_PROCESSORS = Runtime.getRuntime().availableProcessors();
    private static final Long POPULATION = 50000L;
    private final WeatherAPI weatherAPI;
    private final CityFilter cityFilter;
    private final AggregatorFactory aggregatorFactory;
    private final ExecutorServiceManager executorServiceManager;


    public WeatherService(WeatherAPI weatherAPI, CityFilter cityFilter, AggregatorFactory aggregatorFactory, ExecutorServiceManager executorServiceManager) {
        this.weatherAPI = weatherAPI;
        this.cityFilter = cityFilter;
        this.aggregatorFactory = aggregatorFactory;
        this.executorServiceManager = executorServiceManager;
    }

    public List<CityAggregatedWeather> getTopCitiesByAggregatedTemperatureAndFilteredPopulation(Set<String> cityIds, AggregationType aggregationType, Integer topCitiesAmount) {
        Set<City> cities = retrieveCities(cityIds);
        cities = cityFilter.filterCitiesByPopulationMoreThan(cities, POPULATION);

        List<CityDailyTemps> dailyTempsByCities = getCitiesDailyTempsForWholeYear(new ArrayList<>(cities));

        List<CityAggregatedWeather> citiesAggregatedWeather = aggregateCitiesTemperatures(cities, dailyTempsByCities, aggregationType);
        return getTopCities(citiesAggregatedWeather, topCitiesAmount);

    }

    private List<CityAggregatedWeather> getTopCities(List<CityAggregatedWeather> citiesAggregatedWeather, Integer top) {

        citiesAggregatedWeather.sort((o1, o2) -> Double.compare(o1.getAggregatedTemperature(), o2.getAggregatedTemperature()) * -1);
        return citiesAggregatedWeather.stream().limit(top).collect(Collectors.toList());
    }

    private List<CityAggregatedWeather> aggregateCitiesTemperatures(Set<City> cities, List<CityDailyTemps> dailyTempsByCities, AggregationType aggregationType) {
        Map<String, CityDailyTemps> citiesDailyTempsMap =
                dailyTempsByCities.stream().collect(Collectors.toMap(CityDailyTemps::cityId, value -> value));
        List<CityAggregatedWeather> citiesAggregWeather = new ArrayList<>();

        for (City city : cities) {
            CityDailyTemps cityDailyTemps = citiesDailyTempsMap.get(city.getId());
            List<Double> temps = unwrapDailyTemps(cityDailyTemps.dailyTemps());

            citiesAggregWeather.add(new CityAggregatedWeather(
                    city.getId(),
                    city.getName(),
                    city.getPopulation(),
                    aggregationType.toString(),
                    aggregateTemperature(temps, aggregationType)));
        }
        return citiesAggregWeather;
    }

    private List<Double> unwrapDailyTemps(List<DailyTemp> dailyTemps) {
        return dailyTemps.stream().map(DailyTemp::getTemperature).collect(Collectors.toList());
    }

    private Double aggregateTemperature(List<Double> values, AggregationType aggregationType) {
        try {
            Aggregator aggregator = aggregatorFactory.createAggregator(aggregationType);
            return aggregator.aggregate(values);
        } catch (Exception e) {
            //logging
            throw new RuntimeException(e); //throw NoSuchAggregatorException
        }
    }

    private List<CityDailyTemps> getCitiesDailyTempsForWholeYear(List<City> cities) {
        List<Future<CityDailyTemps>> futureCitiesDailyTemps = retrieveTemperaturesInParallel(cities);
        return waitDailyTemperatures(futureCitiesDailyTemps);
    }

    private List<CityDailyTemps> waitDailyTemperatures(List<Future<CityDailyTemps>> futureCitiesDailyTemps) {
        List<CityDailyTemps> cityDailyTemps = new ArrayList<>();
        for (Future<CityDailyTemps> cityDailyTempsFuture : futureCitiesDailyTemps) {
            try {
                cityDailyTemps.add(cityDailyTempsFuture.get());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
        return cityDailyTemps;
    }

    private List<Future<CityDailyTemps>> retrieveTemperaturesInParallel(List<City> cities) {
        ExecutorService executor = executorServiceManager.createFixedThreadPool(NUM_PROCESSORS * 3);
        List<Future<CityDailyTemps>> futureCitiesDailyTemps = new ArrayList<>();

        for (City city : cities) {
            Future<CityDailyTemps> future = executor.submit(() -> {
                List<DailyTemp> dailyTemps = weatherAPI.getLastYearTemperature(city.getId());
                return new CityDailyTemps(city.getId(), dailyTemps);
            });
            futureCitiesDailyTemps.add(future);
        }
        executor.shutdown();
        return futureCitiesDailyTemps;
    }

    private Set<City> retrieveCities(Set<String> cityIds) {
        return weatherAPI.getAllCitiesByIds(cityIds);
    }
}