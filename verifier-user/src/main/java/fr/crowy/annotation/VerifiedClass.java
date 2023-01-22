package fr.crowy.annotation;

import fr.crowy.annotation.processor.verification.Negative;
import fr.crowy.annotation.processor.verification.NotBlank;
import fr.crowy.annotation.processor.verification.Positive;
import fr.crowy.annotation.processor.verification.Verified;

import java.util.List;

@Verified
public class VerifiedClass extends SomeAbstractClass implements SomeInterface {
    @Negative
    private final int x;
    @Positive
    private int y;
    @NotBlank
    private String notBlank;
    @Positive
    private List<Integer> integers;

    public VerifiedClass(int x) {this.x = x;}

    public int getX() {
        return x;
    }
}