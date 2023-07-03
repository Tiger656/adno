package service;

import entity.DailyTemp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

public class MyFork extends RecursiveTask<Map<String, List<DailyTemp>>> {

    private static Integer numOfCPU = Runtime.getRuntime().availableProcessors();
    private static Integer numOfThreads = numOfCPU - 1;
    int fromValue, toValue;
    private List<String> cities;

    private WeatherAPI weatherAPI;

    public MyFork(WeatherAPI weatherAPI, List<String> cities, Integer fromValue, Integer toValue) {
        this.weatherAPI = weatherAPI;
        this.cities = cities;
        this.fromValue = fromValue;
        this.toValue = toValue;
    }
    @Override
    protected Map<String, List<DailyTemp>> compute() {
        if ((toValue - fromValue) + 1 <= cities.size()/numOfThreads) {
            Map<String, List<DailyTemp>> dailyTemps = new HashMap<>(); //Maybe Conccurent analog class?
            System.out.println("Worker: " + Thread.currentThread().getName() + "value from" + fromValue + "valueTo" + toValue); //Remove
            for (int i = fromValue; i <= toValue; i++) {

                try {
                    System.out.println("Thread name: " + Thread.currentThread().getName() + ", Attempt: " + i + ". Start"); //Remove
                    Thread.sleep(TimeUnit.SECONDS.toMillis(1)); //Remove
                    System.out.println("Thread name: " + Thread.currentThread().getName() + ", Attempt: " + i + ". Finish");// Remove
                    //dailyTemps.add(new ImmutablePair<>(cities.get(i), weatherAPI.getLastYearTemperature(cities.get(i)))); //ImmutablePair
                    dailyTemps.put(cities.get(i), weatherAPI.getLastYearTemperature(cities.get(i)));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            return dailyTemps;
        }
        int middle = (toValue + fromValue)/2;
        MyFork firstHalf = new MyFork(weatherAPI ,cities, fromValue, middle);
        firstHalf.fork();
        MyFork secondHalf = new MyFork(weatherAPI, cities, middle + 1, toValue);
        Map<String, List<DailyTemp>> secondHalfResult = secondHalf.compute();
        secondHalfResult.putAll(firstHalf.join());
        return secondHalfResult;
    }
}
