package com.weatherapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * WeatherWebAppApplication - Main Spring Boot Application Class
 * 
 * This is the entry point of our Weather Detection WebApp.
 * It uses Spring Boot's auto-configuration to set up the entire application.
 * 
 * Key Annotations:
 * - @SpringBootApplication: Combines @Configuration, @EnableAutoConfiguration, and @ComponentScan
 * - @EnableCaching: Activates Spring's caching capabilities for better performance
 * 
 * What this class does:
 * 1. Starts the embedded Tomcat server
 * 2. Configures Spring MVC for JSP support
 * 3. Sets up component scanning for our packages
 * 4. Enables caching for weather data
 * 5. Configures the application context
 * 
 * @author Weather App Team
 * @version 1.0
 */
@SpringBootApplication
@EnableCaching
public class WeatherWebAppApplication {

    /**
     * Main method - Application entry point
     * 
     * This method starts the Spring Boot application by:
     * 1. Creating the Spring Application context
     * 2. Starting the embedded web server (Tomcat)
     * 3. Loading all configurations and beans
     * 4. Making the application ready to handle HTTP requests
     * 
     * @param args Command line arguments (not used in this application)
     */
    public static void main(String[] args) {
        // Start the Spring Boot application
        SpringApplication.run(WeatherWebAppApplication.class, args);
        
        // Print startup messages for user feedback
        System.out.println("==========================================");
        System.out.println("🌤️  Weather Detection WebApp Started! 🌤️");
        System.out.println("==========================================");
        System.out.println("🌐 Server running on: http://localhost:8080");
        System.out.println("🔍 Search weather: http://localhost:8080/weather");
        System.out.println("📱 Responsive design: Works on desktop & mobile");
        System.out.println("⚡ Features: Real-time weather, forecasts, charts");
        System.out.println("==========================================");
    }
}
