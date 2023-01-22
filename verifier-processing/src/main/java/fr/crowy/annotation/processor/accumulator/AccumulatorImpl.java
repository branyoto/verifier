package fr.crowy.annotation.processor.accumulator;

import java.util.HashMap;

public class AccumulatorImpl implements Accumulator {
    private final HashMap<String, Object> elements = new HashMap<>();

    @Override
    public Accumulator withPrefix(String prefix) {
        return new AccumulatorImpl() {
            @Override
            public void add(String key, Object value) {
                AccumulatorImpl.this.add(prefix + "." + key, value);
            }
        };
    }

    @Override
    public void add(String key, Object value) {
        elements.put(key, value);
    }

    @Override
    public void throwIfNotEmpty() {
        if (elements.isEmpty()) {
            throw new RuntimeException(elements.toString());
        }
    }
}
