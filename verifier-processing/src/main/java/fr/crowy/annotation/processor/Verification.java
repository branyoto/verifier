package fr.crowy.annotation.processor;

import fr.crowy.annotation.processor.accumulator.Accumulator;
import fr.crowy.annotation.processor.exception.InvalidClassException;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

@FunctionalInterface
public interface Verification {
    void with(Accumulator accumulator);

    static void verify(Verifiable verifiable, Class<? extends Accumulator> clazz) {
        try {
            var accumulator = clazz.getConstructor().newInstance();
            verifiable.verify().with(accumulator);
            accumulator.throwIfNotEmpty();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new InvalidClassException(clazz);
        }
    }

    record Positive(String key, int value) implements Verification {
        @Override
        public void with(Accumulator accumulator) {
            if (value < 0) {
                accumulator.add(key, value);
            }
        }
    }
}