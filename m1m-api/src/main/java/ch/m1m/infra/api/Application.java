package ch.m1m.infra.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// https://www.baeldung.com/spring-boot-h2-database
// https://spring.io/guides/tutorials/rest/

@SpringBootApplication
public class Application {

	public static void main(String[] args) {

		SpringApplication.run(Application.class, args);
	}
}
