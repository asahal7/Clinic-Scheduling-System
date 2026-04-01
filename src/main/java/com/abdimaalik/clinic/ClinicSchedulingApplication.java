package com.abdimaalik.clinic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.abdimaalik.clinic.repository")
public class ClinicSchedulingApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClinicSchedulingApplication.class, args);
    }
}