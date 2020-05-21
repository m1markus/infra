package ch.m1m.infra.authserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;

@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
public class Application {

	static private final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		log.info("Before spring run()...");
		SpringApplication.run(Application.class, args);
		log.info("After spring run()...");
	}
}
