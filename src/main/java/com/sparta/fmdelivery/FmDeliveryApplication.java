package com.sparta.fmdelivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FmDeliveryApplication {

    public static void main(String[] args) {
        SpringApplication.run(FmDeliveryApplication.class, args);
    }

}
