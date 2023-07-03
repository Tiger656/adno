package service.aggregator;

public enum AggregationType {
    MAX("max"),
    MIN("min"),
    AVG("avg"),
    MEDIAN("median"),
    SUM("sum");

    public final String label;

    AggregationType(String label) {
        this.label = label;
    }
}