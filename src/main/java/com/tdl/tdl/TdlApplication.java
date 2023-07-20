package com.tdl.tdl;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableBatchProcessing  // 배치 기능 활성화
@EnableScheduling
public class TdlApplication {

    public static void main(String[] args) {
        SpringApplication.run(TdlApplication.class, args);
    }

}
