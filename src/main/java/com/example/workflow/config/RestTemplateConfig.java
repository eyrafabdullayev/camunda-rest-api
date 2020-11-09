package com.example.workflow.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        Duration duration = Duration.ofSeconds(20L);
        return new RestTemplateBuilder()
                .setReadTimeout(duration)
                .setConnectTimeout(duration)
                .build();
    }
}
