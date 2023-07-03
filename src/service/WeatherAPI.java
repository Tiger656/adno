package service;

import dto.City;
import dto.DailyTemp;

import java.util.List;
import java.util.Set;

public interface WeatherAPI {
    Set<City> getAllCitiesByIds(Set<String> cityIds);
    List<DailyTemp> getLastYearTemperature(String cityId); //2sec
}

