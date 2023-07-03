package service.filter;

import entity.City;

import java.util.Set;
import java.util.stream.Collectors;

public class CityFilter {

    public Set<City> filterCitiesByPopulationMoreThan(Set<City> cities, Long population) {
        return cities.stream().filter(city -> city.getPopulation() > population).collect(Collectors.toSet());
    }
}
