package entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class CityAggTemperature extends City {

    private Double aggregatedTemperature;

    private String aggregationType;

    public CityAggTemperature(String id, String name, Integer population, String aggregationTypeInteger, Double aggregatedTemperature) {
        super(id, name, population);
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
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        CityAggTemperature that = (CityAggTemperature) o;

        return new EqualsBuilder().appendSuper(super.equals(o)).append(aggregatedTemperature, that.aggregatedTemperature).append(aggregationType, that.aggregationType).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).appendSuper(super.hashCode()).append(aggregatedTemperature).append(aggregationType).toHashCode();
    }

    @Override
    public String toString() {
        return "CityAggTemperature{" +
                "aggregatedTemperature=" + aggregatedTemperature +
                ", aggregationType='" + aggregationType + '\'' +
                '}';
    }
}
