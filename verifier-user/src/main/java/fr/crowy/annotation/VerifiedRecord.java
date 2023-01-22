package fr.crowy.annotation;

import fr.crowy.annotation.processor.verification.Negative;
import fr.crowy.annotation.processor.verification.NotBlank;
import fr.crowy.annotation.processor.verification.Positive;
import fr.crowy.annotation.processor.verification.Verified;

@Verified
public record VerifiedRecord(@Positive int pos, @Negative int neg, @NotBlank String nb) implements SomeInterface {

}