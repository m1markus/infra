package ch.m1m.scan;

import io.quarkus.runtime.annotations.QuarkusMain;
import io.quarkus.runtime.Quarkus;

// build an ueber JAR
// ./mvnw package -Dquarkus.package.type=uber-jar

@QuarkusMain
public class Main {

    public static void main(String ... args) {
        System.out.println("Running main method");
        Quarkus.run(args);
    }
}
