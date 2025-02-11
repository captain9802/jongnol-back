package com.example.jongnolback;

import com.example.jongnolback.common.FileUtils;
import com.example.jongnolback.configuration.AmazonConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableConfigurationProperties(AmazonConfiguration.class)
public class JongnolBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(JongnolBackApplication.class, args);
    }

}
