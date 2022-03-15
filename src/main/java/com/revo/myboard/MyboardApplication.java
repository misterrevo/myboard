package com.revo.myboard;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/*
 * Auto Created
 */

@Configuration
@PropertySource({"classpath:jwt.properties", "classpath:smtp.properties"})
@SpringBootApplication
@Slf4j
public class MyboardApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(MyboardApplication.class, args);
        } catch (Exception exception) {
            log.warn("Error in application, error message: " + exception.getMessage());
        }
    }

}
