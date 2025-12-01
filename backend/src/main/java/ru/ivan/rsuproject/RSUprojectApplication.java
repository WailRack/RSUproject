package ru.ivan.rsuproject;

import com.scalar.maven.webjar.ScalarAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = ScalarAutoConfiguration.class)
public class RSUprojectApplication {

    public static void main(String[] args) {
        SpringApplication.run(RSUprojectApplication.class, args);
    }

}
