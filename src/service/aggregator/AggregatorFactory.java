package service.aggregator;

public class AggregatorFactory {

    public Aggregator createAggregator(AggregationType type) throws Exception {
        switch (type) {
            case MAX:
                return new MaxAggregator();
            default:
                throw new Exception(); //throw NoSuchAggregatorException
        }
    }

}