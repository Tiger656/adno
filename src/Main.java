import dto.CityAggregatedWeather;
import service.ExecutorServiceManager;
import service.WeatherAPIImpl;
import service.WeatherService;
import service.aggregator.AggregationType;
import service.aggregator.AggregatorFactory;
import service.filter.CityFilter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
       /* threadLocal.set("Main Hello World"); WILL BE REMOVED
        Thread one = new ThreadOne();
        Thread two = new ThreadTwo();
        one.start();
        two.start();
        one.join();
        two.join();
        System.out.println("Main " + threadLocal.get());*/



        /*//
        Set<City> cities = new HashSet<>();

        Random random = new Random();
        String[] ids = {"ID1", "ID2", "ID3", "ID4", "ID5"}; // Example array of IDs
        String[] names = {"City1", "City2", "City3", "City4", "City5"}; // Example array of names

        for (int i = 0; i < 100; i++) {
            String randomId = ids[random.nextInt(ids.length)];
            String randomName = names[random.nextInt(names.length)];
            int randomPopulation = random.nextInt(1000000); // Example population range from 0 to 999,999

            City city = new City(randomId, randomName, randomPopulation);
            cities.add(city);
        }
        //

        //
        List<ArrayList<DailyTemp>> yearlyTemps = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            ArrayList<DailyTemp> dailyTemps = new ArrayList<>();

            // Set the initial date to January 1st of the current year
            Calendar calendar = new GregorianCalendar();
            calendar.set(Calendar.DAY_OF_YEAR, 1);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            for (int j = 0; j < 365; j++) {
                Date date = calendar.getTime();

                // Generate random temperature within a range (e.g., -10.0 to 40.0)
                double temperature = -10.0 + random.nextDouble() * 50.0;

                DailyTemp dailyTemp = new DailyTemp(date, temperature);
                dailyTemps.add(dailyTemp);

                // Move to the next day
                calendar.add(Calendar.DAY_OF_YEAR, 1);
            }
            yearlyTemps.add(dailyTemps);
        }
        //*/

        Set<String> idSet = new HashSet<>();

        for (int i = 0; i < 100; i++) {
            String id = String.valueOf(i);
            idSet.add(id);
        }

        WeatherService weatherService = new WeatherService(new WeatherAPIImpl(), new CityFilter(), new AggregatorFactory(), new ExecutorServiceManager());
        List<CityAggregatedWeather> a = weatherService.getTopCitiesByAggregatedTemperatureAndFilteredPopulation(idSet, AggregationType.MAX, 3);
        System.out.println(a);
    }
}

