package com.yanndub.gpstracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestClient;

@EnableScheduling
@SpringBootApplication
public class GpsTrackerApplication {

    static void main(String[] args) {
        SpringApplication.run(GpsTrackerApplication.class, args);
    }

    @Bean
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }
}
