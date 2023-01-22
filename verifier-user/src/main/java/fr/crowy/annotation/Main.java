package fr.crowy.annotation;

import fr.crowy.annotation.processor.verification.Negative;
import fr.crowy.annotation.processor.verification.NotVerified;
import fr.crowy.annotation.processor.verification.Positive;
import fr.crowy.annotation.processor.verification.Verified;

import java.util.List;

public class Main {
    @Verified
    public static void verifiedFunction(VerifiedRecord verifiedRecord) {
        // ...
    }

    @Verified(value = MyAccumulatorImpl.class)
    public static void verifiedParams(@Positive int x, VerifiedRecord verifiedRecord,
            @NotVerified VerifiedRecord unverifiedRecord) {
        // ...
    }

    public static void verifiedParams(@Positive int x, @Negative List<Integer> y, VerifiedRecord unverifiedRecord,
            @Verified VerifiedRecord verifiedRecord) {
        // ...
    }

    public static void main(String[] args) {
    }
}
