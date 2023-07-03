package service.aggregator;

import java.util.List;

public interface Aggregator {
    Double aggregate(List<Double> values);
}
