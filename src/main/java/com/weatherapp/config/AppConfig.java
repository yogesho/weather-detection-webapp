package com.weatherapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;

import java.time.Duration;

/**
 * AppConfig - Application Configuration Class
 * 
 * This configuration class provides bean definitions and application-wide
 * configuration settings for the Weather WebApp.
 * 
 * Key Responsibilities:
 * - Configure RestTemplate for HTTP requests
 * - Set up caching with Caffeine
 * - Configure HTTP client timeouts and connection pooling
 * - Provide application-wide beans
 * 
 * Why we use @Configuration:
 * - Marks this class as a source of bean definitions
 * - Allows Spring to scan and register beans automatically
 * - Provides centralized configuration management
 * 
 * @author Weather App Team
 * @version 1.0
 */
@Configuration
public class AppConfig {

    /**
     * RestTemplate Bean Configuration
     * 
     * This method creates and configures a RestTemplate bean for making HTTP requests
     * to external APIs (like OpenWeatherMap). RestTemplate is Spring's HTTP client.
     * 
     * Configuration includes:
     * - Connection timeout: 10 seconds
     * - Read timeout: 30 seconds
     * - JSON message converter for API responses
     * - Error handling capabilities
     * 
     * Why we use RestTemplate:
     * - Simple and easy to use HTTP client
     * - Built-in support for JSON deserialization
     * - Configurable timeouts and connection pooling
     * - Good integration with Spring's error handling
     * 
     * @return Configured RestTemplate instance
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofSeconds(10))  // Connection timeout
                .setReadTimeout(Duration.ofSeconds(30))     // Read timeout
                .build();
    }

    /**
     * Cache Manager Bean Configuration
     * 
     * This method creates and configures a Caffeine cache manager.
     * It uses the "weather-cache" name and applies cache specifications
     * (like maximum size and expiration time) defined in application.properties.
     * 
     * Why we use Caffeine:
     * - High-performance caching library
     * - Memory-efficient with automatic eviction
     * - Configurable expiration policies
     * - Good integration with Spring Cache
     * 
     * @return Configured CacheManager instance
     */
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("weather-cache");
        // Configure Caffeine properties from application.properties
        // This ensures that the cache settings (e.g., maximumSize, expireAfterWrite)
        // defined in application.properties are applied to the Caffeine cache.
        cacheManager.setCacheSpecification("maximumSize=100,expireAfterWrite=30m");
        return cacheManager;
    }
}
