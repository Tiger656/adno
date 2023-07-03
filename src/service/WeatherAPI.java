package service;

import entity.City;
import entity.DailyTemp;

import java.util.List;
import java.util.Set;

public interface WeatherAPI {
    Set<City> getAllCitiesByIds(Set<String> cityIds);
    List<DailyTemp> getLastYearTemperature(String cityId); //2sec
}

