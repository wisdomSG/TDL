package com.tdl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TdlApplication {

    public static void main(String[] args) {
        SpringApplication.run(TdlApplication.class, args);
    }

}