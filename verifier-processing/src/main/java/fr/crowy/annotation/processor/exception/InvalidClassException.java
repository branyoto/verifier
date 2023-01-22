package fr.crowy.annotation.processor.exception;

public final class InvalidClassException extends RuntimeException {
    public InvalidClassException(Class<?> clazz) {
        super("The " + clazz + " does not have accessible parameterless constructor.");
    }
}
