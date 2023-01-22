package fr.crowy.annotation.processor;

import com.google.auto.service.AutoService;
import fr.crowy.annotation.processor.verification.Positive;
import fr.crowy.annotation.processor.verification.Verified;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ExecutableType;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@SupportedAnnotationTypes("fr.crowy.annotation.processor.verification.Verified")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class VerifiedProcessor extends AbstractProcessor {
    private static final Map<ElementKind, Consumer<List<? extends Element>>> processors = Map.of(
            ElementKind.CLASS, VerifiedProcessor::processClasses,
            ElementKind.RECORD, VerifiedProcessor::processClasses,
            ElementKind.INTERFACE, VerifiedProcessor::cannotProcess,
            ElementKind.PARAMETER, VerifiedProcessor::notYetImplemented,
            ElementKind.FIELD, VerifiedProcessor::notYetImplemented,
            ElementKind.METHOD, VerifiedProcessor::processMethods
    );

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        for (TypeElement annotation : annotations) {
            Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);
            System.out.println("-----------------------------------");
            System.out.println(annotatedElements);
            var annotatedMethods = annotatedElements.stream()
                    .collect(Collectors.groupingBy(Element::getKind));
            System.out.println(annotatedMethods);
            annotatedMethods.forEach((k,v) -> processors.getOrDefault(k, VerifiedProcessor::cannotProcess).accept(v));
            System.out.println("-----------------------------------");
        }
        return true;
    }

    private static void processMethods(List<? extends Element> methods) {
        for (var method : methods) {
            var executable = (ExecutableElement) method;
            System.out.println(method.getKind() + " - " + method.getSimpleName() + ":");
            var verifications = new ArrayList<Verification>();
            for (var param : executable.getParameters()) {
                System.out.println("\t\t" + "-----------------------------------");
                System.out.println("\t\t" + param);
                System.out.println("\t\t" + param.getAnnotationMirrors().stream().map(AnnotationMirror::getAnnotationType).toList());
                System.out.println("\t\t" + "-----------------------------------");
            }
            System.out.println(verifications);
            System.out.println("-----------------------------------");
        }
    }
    private static void processClasses(List<? extends Element> classes) {
        System.out.println("Process classes " + classes);
    }
    private static void cannotProcess(List<? extends Element> elements) {
        throw new IllegalArgumentException("Cannot process " + elements); // todo : custom exception
    }
    private static void notYetImplemented(List<? extends Element> elements) {
        System.out.println("Not yet implementation for " + elements); // Todo : add implementation
    }
}