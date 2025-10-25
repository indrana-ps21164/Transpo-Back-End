package com.transpo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TranspoApplication {
    public static void main(String[] args) {
        SpringApplication.run(TranspoApplication.class, args);
        System.out.println("Transpo backend started");
    }
}
