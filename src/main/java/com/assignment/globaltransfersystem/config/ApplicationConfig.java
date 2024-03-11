package com.assignment.globaltransfersystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * ApplicationConfig class will be using for bean definitions
 *
 * @author Gouthami Matavalam
 *
 */
@Configuration
public class ApplicationConfig {


    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
