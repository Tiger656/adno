package service.aggregator;

import java.util.Collections;
import java.util.List;

public class MaxAggregator implements Aggregator{
    @Override
    public Double aggregate(List<Double> values) {
        return Collections.max(values);
    }
}