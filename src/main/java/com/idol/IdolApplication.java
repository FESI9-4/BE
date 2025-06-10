package com.idol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class IdolApplication {

    public static void main(String[] args) {
        SpringApplication.run(IdolApplication.class, args);
    }

}
