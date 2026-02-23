package com.dsaportal.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupLogger implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        System.out.println("==================================================");
        System.out.println("StartupLogger: Application is running!");
        System.out.println("Detected PORT env var: " + System.getenv("PORT"));
        System.out.println("==================================================");
    }
}
