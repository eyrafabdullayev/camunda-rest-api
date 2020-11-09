package com.example.workflow;

import com.example.workflow.config.ApplicationConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(value = ApplicationConfiguration.class)
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class);
  }

}