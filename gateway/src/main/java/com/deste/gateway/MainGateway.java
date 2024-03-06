package com.deste.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MainGateway {

    public static void main(String[] args) {
        SpringApplication.run(MainGateway.class, args);
    }

}