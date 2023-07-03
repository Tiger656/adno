package service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorServiceManager {

    public ExecutorService createFixedThreadPool(Integer threads) {
        return Executors.newFixedThreadPool(threads);
    }
}
