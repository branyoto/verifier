package fr.crowy.annotation.processor.verification;

import fr.crowy.annotation.processor.accumulator.Accumulator;
import fr.crowy.annotation.processor.accumulator.AccumulatorImpl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE, ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.SOURCE)
public @interface Verified {
    Class<? extends Accumulator> value() default AccumulatorImpl.class;
}
