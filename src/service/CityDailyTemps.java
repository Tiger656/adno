package service;

import dto.DailyTemp;

import java.util.List;

public record CityDailyTemps(String cityId, List<DailyTemp> dailyTemps) {
}
