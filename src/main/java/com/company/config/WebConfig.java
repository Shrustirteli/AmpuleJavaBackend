package com.company.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Apply to all endpoints
                .allowedOrigins("http://localhost:3000") // Frontend origin
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Include OPTIONS method
                .allowedHeaders("Authorization", "Content-Type") // Allow these headers
                .exposedHeaders("Authorization") // Expose Authorization header
                .allowCredentials(true) // Allow credentials (if using cookies)
                .maxAge(3600); // Cache the response for preflight requests
    }
}
