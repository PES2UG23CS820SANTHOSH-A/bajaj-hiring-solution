package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

    @Autowired
    private BajajService bajajService;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println("\nStarting Bajaj Finserv Health Qualifier flow...");
        try {
            bajajService.startProcess();
            System.out.println("\nJAR built and executed successfully! Ready for submission.");
        } catch (Exception e) {
            System.err.println("\nError occurred during process: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
