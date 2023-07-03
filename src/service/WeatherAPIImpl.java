package service;

import dto.City;
import dto.DailyTemp;

import java.util.*;
import java.util.concurrent.locks.Lock;

public class WeatherAPIImpl implements WeatherAPI{

    @Override
    public Set<City> getAllCitiesByIds(Set<String> cityIds) {
        Set<City> cities = new HashSet<>();

        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            String randomId = String.valueOf(i);
            String randomName = "City" + String.valueOf(i); //names[random.nextInt(names.length)];
            int randomPopulation = random.nextInt(1000000); // Example population range from 0 to 999,999

            City city = new City(randomId, randomName, randomPopulation);
            cities.add(city);
        }
        return cities;
    }

    @Override
    public List<DailyTemp> getLastYearTemperature(String cityId) {
       /*// lock.lock();
        Random random = new Random();

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
        //lock.unlock();
        return dailyTemps;*/
        Random random = new Random();
        double randomNumber = random.nextDouble() * 100.0 - 50.0;
        return Arrays.asList(new DailyTemp( new GregorianCalendar().getTime(), randomNumber));
    }
}
