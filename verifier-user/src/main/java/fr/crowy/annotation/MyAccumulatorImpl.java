package fr.crowy.annotation;

import fr.crowy.annotation.processor.accumulator.Accumulator;

import java.util.HashMap;

public class MyAccumulatorImpl implements Accumulator {
    private final HashMap<String, Object> elements = new HashMap<>();

    @Override
    public Accumulator withPrefix(String prefix) {
        return new MyAccumulatorImpl() {
            @Override
            public void add(String key, Object value) {
                MyAccumulatorImpl.this.add(prefix + "." + key, value);
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
            System.out.println(elements);
        }
    }
}
