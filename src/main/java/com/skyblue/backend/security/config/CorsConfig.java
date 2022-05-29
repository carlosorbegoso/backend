package com.skyblue.backend.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
public class CorsConfig implements WebFluxConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // allow path
                .allowedOrigins("http://localhost:4200") // Origins that allow cross-domain access
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE") // Method to allow requests
                .maxAge(10000) // Preflight Interval
                .allowedHeaders("*") // Allow header settings
                .allowCredentials(true); // whether to send cookies
    }
}
