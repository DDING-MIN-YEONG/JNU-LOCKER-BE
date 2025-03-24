package com.jnulocker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class JnuLockerApplication {

    public static void main(String[] args) {
        SpringApplication.run(JnuLockerApplication.class, args);
    }
}
