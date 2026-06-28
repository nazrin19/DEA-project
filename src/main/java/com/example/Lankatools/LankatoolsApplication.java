package com.example.Lankatools;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync; // Added for background email tasks
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAsync // 🟢 ACTIVATED: Allows @Async in BookingController to run notifications seamlessly
public class LankatoolsApplication {
    public static void main(String[] args) {
        SpringApplication.run(LankatoolsApplication.class, args);
    }
}