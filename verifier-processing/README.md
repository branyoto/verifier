# Verification annotation

Ce qui est attendu :

- Une annotation s'appliquant sur les paramètres, fonctions et variables locales pour vérifier leurs conditions
- Une annotation "racine" représentant une condition sur un champ
- Plusieurs implémentations de l'annotation "racine" représentant des conditions plus précises (Positive, NotBlank, ...)
- Une annotation permettant de ne pas exécuter les tests sur l'objet

Annotations par défaut :

- Positive : vérifie que la value est positive
    - S'applique sur les types numériques et les collections de types numériques
- Negative : vérifie que la value est négative
    - S'applique sur les types numériques et les collections de types numériques
- Between(min, max) : vérifie que la value est entre min et max
    - il faut que min ou max soit défini
    - Si la valeur est nul provoque une erreur
    - S'applique sur les types numériques et les collections de types numériques
    - S'applique sur les comparables et les collections de comparables
- NotNull : vérifie que la valeur n'est pas null
    - S'applique sur tout type non primitif
- NotEmpty : vérifie que la valeur n'est pas null ou vide
    - S'applique sur les collections
    - S'applique sur les strings
- NotBlank : vérifie que la valeur n'est pas null ou vide ou composée de caractères vides
    - S'applique sur les strings

Source :

```java
// VerifiedClass.java
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

}

// VerifiedRecord.java
@Verified
public record VerifiedRecord(@Positive int pos, @Negative int neg, @NotBlank String nb) implements SomeInterface {

}

// Main.java
public class Main {
    @Verified
    public static void verifiedFunction(VerifiedRecord verifiedRecord) {
        // ...
    }

    @Verified(MyAccumulatorImpl.class)
    public static void verifiedParams(@Positive int x, VerifiedRecord verifiedRecord,
            @NotVerified VerifiedRecord unverifiedRecord) {
        // ...
    }

    public static void verifiedParams(@Positive int x, @Negative List<Integer> y, VerifiedRecord unverifiedRecord,
            @Verified VerifiedRecord verifiedRecord) {
        // ...
    }

    public static void main(String[] args) {
        verifiedFunction(/* ... */);
    }
}
```

Result :

```java


// VerifiedClass.java
public class VerifiedClass extends SomeAbstractClass implements SomeInterface, Verifiable {
    private final int x;
    private int y;
    private String notBlank;
    private List<Integer> integers;

    public Verification verify() {
        return Verification.and(
                Verification.negative("x", x),
                Verification.positive("y", y),
                Verification.notBlank("notBlank", notBlank),
                Verification.each("integers", integers, Verification::positive)
        );
    }
}

// VerifiedRecord.java
public record VerifiedRecord(@Positive int pos, @Negative int neg,
                             @NotBlank String nb) implements SomeInterface, Verifiable {
    public Verification verify() {
        return Verification.and(
                Verification.positive("pos", pos),
                Verification.negative("neg", neg),
                Verification.notBlank("nb", nb)
        );
    }
}

// Main.java
public class Main {
    public static void verifiedFunction(VerifiedRecord verifiedRecord) {
        Verification.verify("verifiedRecord", verifiedRecord, AccumulatorImpl.class);
        // ...
    }

    public static void verifiedParams(int x, VerifiedRecord verifiedRecord, VerifiedRecord unverifiedRecord) {
        Verification.verify(Verification.and(
                Verification.positive("x", x),
                Verification.and("verifiedRecord", verifiedRecord)
        ), MyAccumulatorImpl.class);
        // ...
    }

    public static void verifiedParams(int x, List<Integer> y, VerifiedRecord unverifiedRecord,
            VerifiedRecord verifiedRecord) {
        Verification.verify(Verification.and(
                Verification.positive("x", x),
                Verification.each("y", y, Verification::negative),
                Verification.and("verifiedRecord", verifiedRecord)
        ), AccumulatorImpl.class);
        // ...
    }

    public static void main(String[] args) {
        verifiedFunction(/* ... */);
    }
}
```

```java
public interface Accumulator {
    Accumulator withPrefix(String prefix);

    void add(String key, Object value);

    void throwIfNotEmpty();
}

public interface Verifiable {
    Verification verify();
}

@FunctionalInterface
public interface Verification {
    void verify(Accumulator accumulator);

    static void verify(Verifiable verifiable, Class<? extends Accumulator> clazz) {
        try {
            var accumulator = clazz.getConstructor().newInstance();
            verifiable.verify().with(accumulator);
            accumulator.throwIfNotEmpty();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new InvalidClassException(clazz);
        }
    }
    
    static Verification positive(String key, int value) {
        return a -> {
            if (value < 0) {
                a.add(key, value);
            }
        };
    }
}
```