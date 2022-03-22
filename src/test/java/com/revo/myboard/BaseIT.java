package com.revo.myboard;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;

@SpringBootTest(classes = MyboardApplication.class)
public abstract class BaseIT {

    private static final MySQLContainer mySQLContainer;
    private static final String SPRING_DB_URL_PROPERTY = "spring.datasource.url";
    private static final String SPRING_DB_PASSWORD_PROPERTY = "spring.datasource.password";
    private static final String SPRING_DB_USERNAME_PROPERTY = "spring.datasource.username";
    private static final String SPRING_DB_DRIVER_CLASS_NAME_PROPERTY = "spring.datasource.driverClassName";

    static {
        mySQLContainer = (MySQLContainer) new MySQLContainer("mysql:8.0")
                        .withReuse(true);
        mySQLContainer.start();
    }

    @DynamicPropertySource
    public static void setDatasourceProperties(final DynamicPropertyRegistry registry) {
        registry.add(SPRING_DB_URL_PROPERTY, mySQLContainer::getJdbcUrl);
        registry.add(SPRING_DB_PASSWORD_PROPERTY, mySQLContainer::getPassword);
        registry.add(SPRING_DB_USERNAME_PROPERTY, mySQLContainer::getUsername);
        registry.add(SPRING_DB_DRIVER_CLASS_NAME_PROPERTY, mySQLContainer::getDriverClassName);
    }
}