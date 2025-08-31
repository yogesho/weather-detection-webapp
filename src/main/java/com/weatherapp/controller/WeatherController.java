package com.weatherapp.controller;

import com.weatherapp.dto.WeatherData;
import com.weatherapp.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * WeatherController - Web Controller for Weather Application
 * 
 * This controller handles all HTTP requests for the weather web application.
 * It serves JSP pages and manages the user interface flow.
 * 
 * Key Responsibilities:
 * - Handle GET requests for weather information
 * - Serve JSP pages (index.jsp, weather.jsp)
 * - Manage search history in session
 * - Pass data to JSP views using Model
 * - Handle errors and display appropriate messages
 * 
 * Why we use @Controller instead of @RestController:
 * - @Controller is for traditional web applications with views (JSP)
 * - @RestController is for REST APIs that return JSON/XML
 * - We need to return view names (JSP page names) to Spring MVC
 * 
 * @author Weather App Team
 * @version 1.0
 */
@Controller
@Slf4j
public class WeatherController {

    /**
     * WeatherService for fetching weather data
     * Injected by Spring's dependency injection
     */
    @Autowired
    private WeatherService weatherService;

    /**
     * Home page - Search form
     * 
     * This method handles the root URL ("/") and displays the search form.
     * It shows the index.jsp page where users can enter a city name.
     * 
     * @param model Spring MVC Model object for passing data to JSP
     * @param session HTTP session for storing search history
     * @return "index" - the name of the JSP page to render
     */
    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        log.info("Home page requested");
        
        // Get search history from session
        @SuppressWarnings("unchecked")
        List<String> searchHistory = (List<String>) session.getAttribute("searchHistory");
        if (searchHistory == null) {
            searchHistory = new ArrayList<>();
        }
        
        // Pass search history to the view
        model.addAttribute("searchHistory", searchHistory);
        model.addAttribute("pageTitle", "Weather Detection WebApp");
        
        return "index";
    }

    /**
     * Weather search endpoint
     * 
     * This method handles weather search requests ("/weather?city=London").
     * It fetches weather data for the specified city and displays it in weather.jsp.
     * 
     * @param city The city name from the request parameter
     * @param model Spring MVC Model object for passing data to JSP
     * @param session HTTP session for storing search history
     * @param request HTTP request object for logging
     * @return "weather" - the name of the JSP page to render
     */
    @GetMapping("/weather")
    public String getWeather(@RequestParam(required = false) String city, 
                           Model model, 
                           HttpSession session,
                           HttpServletRequest request) {
        
        log.info("Weather request received for city: {}", city);
        
        // Log request details for debugging
        String userIp = getClientIpAddress(request);
        String userAgent = request.getHeader("User-Agent");
        log.debug("Request from IP: {}, User-Agent: {}", userIp, userAgent);
        
        // Handle case when no city is provided
        if (city == null || city.trim().isEmpty()) {
            log.warn("No city provided in request");
            model.addAttribute("error", "Please enter a city name");
            model.addAttribute("pageTitle", "Weather Search - Error");
            
            // Get search history for the error page
            @SuppressWarnings("unchecked")
            List<String> searchHistory = (List<String>) session.getAttribute("searchHistory");
            if (searchHistory == null) {
                searchHistory = new ArrayList<>();
            }
            model.addAttribute("searchHistory", searchHistory);
            
            return "index";
        }
        
        try {
            // Fetch weather data from service
            WeatherData weatherData = weatherService.getWeatherData(city.trim());
            
            // Check if weather data is valid
            if (weatherData.isValid()) {
                log.info("Successfully retrieved weather data for city: {}", city);
                
                // Add weather data to model for JSP
                model.addAttribute("weather", weatherData);
                model.addAttribute("pageTitle", "Weather in " + weatherData.getCityName());
                
                // Update search history
                updateSearchHistory(session, city.trim());
                
                // Add search history to model
                @SuppressWarnings("unchecked")
                List<String> searchHistory = (List<String>) session.getAttribute("searchHistory");
                model.addAttribute("searchHistory", searchHistory);
                
                return "weather";
                
            } else {
                log.warn("Failed to retrieve weather data for city: {}", city);
                model.addAttribute("error", weatherData.getErrorMessage());
                model.addAttribute("pageTitle", "Weather Search - Error");
                
                // Get search history for the error page
                @SuppressWarnings("unchecked")
                List<String> searchHistory = (List<String>) session.getAttribute("searchHistory");
                if (searchHistory == null) {
                    searchHistory = new ArrayList<>();
                }
                model.addAttribute("searchHistory", searchHistory);
                
                return "index";
            }
            
        } catch (Exception e) {
            log.error("Error processing weather request for city: {}", city, e);
            
            // Provide more specific error messages based on the exception
            String errorMessage;
            if (e.getMessage() != null && e.getMessage().contains("City not found")) {
                errorMessage = "City not found: " + city + ". Try using format: City, CountryCode (e.g., Nanded,IN or Mumbai, India)";
            } else {
                errorMessage = "An unexpected error occurred. Please try again.";
            }
            
            model.addAttribute("error", errorMessage);
            model.addAttribute("pageTitle", "Weather Search - Error");
            
            // Get search history for the error page
            @SuppressWarnings("unchecked")
            List<String> searchHistory = (List<String>) session.getAttribute("searchHistory");
            if (searchHistory == null) {
                searchHistory = new ArrayList<>();
            }
            model.addAttribute("searchHistory", searchHistory);
            
            return "index";
        }
    }

    /**
     * Health check endpoint
     * 
     * This method provides a simple health check for the application.
     * It can be used by monitoring tools to verify the application is running.
     * 
     * @param model Spring MVC Model object for passing data to JSP
     * @return "health" - the name of the JSP page to render
     */
    @GetMapping("/health")
    public String health(Model model) {
        log.info("Health check requested");
        
        model.addAttribute("status", "UP");
        model.addAttribute("service", "Weather Detection WebApp");
        model.addAttribute("timestamp", new java.util.Date());
        model.addAttribute("pageTitle", "Health Check");
        
        return "health";
    }

    /**
     * Update search history in session
     * 
     * This method maintains a list of recently searched cities in the user's session.
     * It keeps the last 5 searches and prevents duplicates.
     * 
     * @param session HTTP session object
     * @param city The city name to add to history
     */
    private void updateSearchHistory(HttpSession session, String city) {
        @SuppressWarnings("unchecked")
        List<String> searchHistory = (List<String>) session.getAttribute("searchHistory");
        
        if (searchHistory == null) {
            searchHistory = new ArrayList<>();
        }
        
        // Remove the city if it already exists (to move it to the front)
        searchHistory.remove(city);
        
        // Add the city to the beginning of the list
        searchHistory.add(0, city);
        
        // Keep only the last 5 searches
        if (searchHistory.size() > 5) {
            searchHistory = searchHistory.subList(0, 5);
        }
        
        // Update the session
        session.setAttribute("searchHistory", searchHistory);
        
        log.debug("Updated search history: {}", searchHistory);
    }

    /**
     * Get client IP address from request
     * 
     * This method extracts the real IP address of the client,
     * handling cases where the application is behind a proxy or load balancer.
     * 
     * @param request HTTP request object
     * @return Client IP address as string
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0];
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
}
