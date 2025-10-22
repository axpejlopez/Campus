package com.example.campusmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.example.campusmanager")
public class CampusmanagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(CampusmanagerApplication.class, args);
    }
}
