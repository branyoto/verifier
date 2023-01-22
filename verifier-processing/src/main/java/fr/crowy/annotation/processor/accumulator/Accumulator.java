package fr.crowy.annotation.processor.accumulator;

public interface Accumulator {
    Accumulator withPrefix(String prefix);

    void add(String key, Object value);

    void throwIfNotEmpty();
}
