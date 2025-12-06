package com.musiclibrary.webapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class WebConfig {
    
    @Value("${api.gateway.url}")
    private String apiGatewayUrl;
    
    @Value("${admin.service.url:http://localhost:8081}")
    private String adminServiceUrl;
    
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    
    @Bean
    public String apiGatewayUrl() {
        return apiGatewayUrl;
    }
    
    @Bean
    public String adminServiceUrl() {
        return adminServiceUrl;
    }
}
