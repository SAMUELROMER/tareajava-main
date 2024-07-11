package com.imgnube.tareajava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TareajavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(TareajavaApplication.class, args);
    }
}