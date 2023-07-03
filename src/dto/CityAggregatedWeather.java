package dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class CityAggregatedWeather {

    private final String id;
    private final String name;
    private final Integer population;
    private Double aggregatedTemperature;

    private String aggregationType;

    public CityAggregatedWeather(String id, String name, Integer population, String aggregationTypeInteger, Double aggregatedTemperature) {
        this.id = id;
        this.name = name;
        this.population = population;
        this.aggregatedTemperature = aggregatedTemperature;
        this.aggregationType = aggregationTypeInteger;
    }


    public Double getAggregatedTemperature() {
        return aggregatedTemperature;
    }

    public void setAggregatedTemperature(Double aggregatedTemperature) {
        this.aggregatedTemperature = aggregatedTemperature;
    }

    public String getAggregationType() {
        return aggregationType;
    }

    public void setAggregationType(String aggregationType) {
        this.aggregationType = aggregationType;
    }

    @Override
    public String toString() {
        return "CityAggregatedWeather{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", population=" + population +
                ", aggregatedTemperature=" + aggregatedTemperature +
                ", aggregationType='" + aggregationType + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        CityAggregatedWeather that = (CityAggregatedWeather) o;

        return new EqualsBuilder().append(id, that.id).append(name, that.name).append(population, that.population).append(aggregatedTemperature, that.aggregatedTemperature).append(aggregationType, that.aggregationType).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(name).append(population).append(aggregatedTemperature).append(aggregationType).toHashCode();
    }
}
