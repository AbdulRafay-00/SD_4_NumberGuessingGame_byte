package dev.rafay.guessinggame;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class GuessingGameApplication {

    public static void main(String[] args) {
        SpringApplication.run(GuessingGameApplication.class, args);
    }
}
