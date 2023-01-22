# Project

This project is a WIP.

This project is designed to be a simpler way to use annotation to ensure fields validation than jakarta.

This may have been already done by someone but the pleasure to implement myself this feature is too high.

Everything done here is new to me and **will never** be 100% completed nor reliable.

# Implementation

The verification is only done during a method call.

```java
// VerifiedClass.java
@Verified
class VerifiedClass {
    @Positive
    private final int x;

    public VerifiedClass(int x) {
        this.x = x;
    }
}

// VerifiedClass2.java
@Verified
class VerifiedClass2 {
    @Positive
    private final int x;

    @Verified
    public VerifiedClass2(@Positive int x) {
        this.x = x;
    }
}

// Main.java
class Main {
    @Verified
    private static void function(VerifiedClass verified) {
        // Verified at the start of the function
        // ...
    }

    private static void function(VerifiedClass2 verified) {
        // Not verified because the annotation is missing
        // ...
    }

    public static void main(String[] args) {
        var verified = new VerifiedClass(-1); // Not verified here
        var verified2 = new VerifiedClass2(-1); // Verified here

    }
}
```

The rules are simple :

- A method/constructor/class/record should have a @Verified annotation to be checked
- A field/parameter with an annotation derived from @Verification will be verified iif the enclosing
  method/constructor/class/record is annotated with @Verified
    - If a field/parameter is a type that is annotated with @Verified, it will be verified with or without an annotation
      on the field
    - To prevent the auto verification of verified type, you have two options:
        - use @NotVerified on the field/parameter `void function(@NotVerified VerifiedType notVerifiedParam)`
        - add the parameter `auto=false` to the annotation of the enclosing method/constructor/class/record.
          Ex: `@Verified(auto=false) void function(VerifiedType notVerifiedParam)`

Exemples :

```java
// ExempleClass.java
@Verified
class ExempleClass {
    @Positive
    int x;      // will be verified
    @Negative
    int y;      // will be verified
    @NotNull
    Object o;   // will be verified
    String foo; // will not be verified
}
```

During compilation :

- The classes/records annotated with @Verified implement Verifiable
    - The method verify is implemented using Verification#and() and every field annotated with a verification and every field that is a type annotated with @Verified
      - Except if annotated with @NotVerified or if the enclosing element has auto verification disabled

# Disclaimer

The annotation cannot be used for return type/value

# Ref

Annotation tutorial : https://www.baeldung.com/java-annotation-processing-builder
