package dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Date;

public class DailyTemp {
    private Date date; // it's better to use localDateTime
    private double temperature;

    public DailyTemp(Date date, double temperature) {
        this.date = date;
        this.temperature = temperature;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        DailyTemp dailyTemp = (DailyTemp) o;

        return new EqualsBuilder().append(temperature, dailyTemp.temperature).append(date, dailyTemp.date).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(date).append(temperature).toHashCode();
    }

    @Override
    public String toString() {
        return "DailyTemp{" +
                "date=" + date +
                ", temperature=" + temperature +
                '}';
    }
}
