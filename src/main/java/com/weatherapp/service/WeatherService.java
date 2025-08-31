package com.weatherapp.service;

import com.weatherapp.dto.WeatherData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * WeatherService - Business Logic Layer for Weather Operations
 * 
 * This service class contains all the business logic for fetching and
 * processing
 * weather data. It acts as an intermediary between the controller and external
 * APIs.
 * 
 * Key Responsibilities:
 * - Fetch weather data from OpenWeatherMap API
 * - Process and transform API responses
 * - Implement caching for performance
 * - Handle errors and edge cases
 * - Generate forecast data
 * 
 * Why we use RestTemplate:
 * - Simple HTTP client for making API calls
 * - Built-in support for JSON deserialization
 * - Easy error handling with HttpClientErrorException
 * - Configurable timeout and connection pooling
 * 
 * @author Weather App Team
 * @version 1.0
 */
@Service
@Slf4j
public class WeatherService {

    /**
     * RestTemplate for making HTTP requests to OpenWeatherMap API
     * Injected by Spring's dependency injection
     */
    @Autowired
    private RestTemplate restTemplate;

    /**
     * OpenWeatherMap API base URL
     * Injected from application.properties
     */
    @Value("${weather.api.base-url}")
    private String apiBaseUrl;

    /**
     * OpenWeatherMap API key
     * Injected from application.properties
     */
    @Value("${weather.api.key}")
    private String apiKey;

    /**
     * Temperature units (metric, imperial, kelvin)
     * Injected from application.properties
     */
    @Value("${weather.api.units}")
    private String units;

    /**
     * Get complete weather data for a city
     * 
     * This method fetches current weather, forecast, and AQI data for a given city.
     * It implements caching to improve performance for repeated requests.
     * 
     * The @Cacheable annotation enables caching for this method.
     * Results are cached based on the city name for 30 minutes.
     * 
     * @param cityName The name of the city to get weather for
     * @return WeatherData object containing all weather information
     */
    @Cacheable(value = "weather-cache", key = "#cityName.toLowerCase()")
    public WeatherData getWeatherData(String cityName) {
        long startTime = System.currentTimeMillis();
        log.info("Fetching weather data for city: {}", cityName);

        try {
            // Validate input
            if (cityName == null || cityName.trim().isEmpty()) {
                log.warn("Invalid city name provided: {}", cityName);
                return createErrorWeatherData("City name cannot be empty");
            }

            // Fetch current weather data
            WeatherData weatherData = fetchCurrentWeather(cityName);

            // Fetch forecast data
            fetchForecastData(weatherData);

            // Fetch AQI data
            fetchAQIData(weatherData);

            // Set metadata
            weatherData.setFetchedAt(LocalDateTime.now());
            weatherData.setCached(false);
            weatherData.setResponseTimeMs(System.currentTimeMillis() - startTime);

            log.info("Successfully fetched weather data for city: {}", cityName);
            return weatherData;

        } catch (HttpClientErrorException.NotFound e) {
            log.error("City not found: {}", cityName);
            return createErrorWeatherData("City not found: " + cityName);

        } catch (Exception e) {
            log.error("Error fetching weather data for city: {}", cityName, e);
            return createErrorWeatherData("Error fetching weather data: " + e.getMessage());
        }
    }

    /**
     * Resolve city name to coordinates using OpenWeatherMap Geocoding API
     * 
     * This method first calls the geocoding API to get precise coordinates
     * for the city, which helps resolve cities with multiple locations or
     * different spellings. It includes logic to find the most accurate match.
     * 
     * @param cityName The city name to resolve
     * @return Map containing lat and lon coordinates
     * @throws RuntimeException if city cannot be resolved
     */
    private Map<String, Object> resolveCityCoordinates(String cityName) {
        // Try different variations to get the most accurate result
        String[] searchVariations = {
            cityName,  // Original input
            cityName + ",IN",  // With country code
            cityName + ", India"  // With full country name
        };
        
        for (String searchTerm : searchVariations) {
            String geocodingUrl = String.format("http://api.openweathermap.org/geo/1.0/direct?q=%s&limit=5&appid=%s",
                    searchTerm, apiKey);

            log.debug("Making geocoding API request to: {}", geocodingUrl);

            try {
                List<Map<String, Object>> response = restTemplate.getForObject(geocodingUrl, List.class);
                
                if (response != null && !response.isEmpty()) {
                    // Find the best match based on state accuracy
                    Map<String, Object> bestMatch = findBestLocationMatch(response, cityName);
                    
                    if (bestMatch != null) {
                        log.info("Resolved city '{}' to coordinates: lat={}, lon={}, country={}, state={}",
                                cityName, bestMatch.get("lat"), bestMatch.get("lon"), 
                                bestMatch.get("country"), bestMatch.get("state"));
                        return bestMatch;
                    }
                }
            } catch (Exception e) {
                log.warn("Failed to resolve coordinates for search term: {}", searchTerm, e);
                // Continue to next variation
            }
        }
        
        // If all variations failed, try hardcoded coordinates for known cities
        Map<String, Object> hardcodedLocation = getHardcodedLocation(cityName);
        if (hardcodedLocation != null) {
            log.info("Using hardcoded coordinates for city: {}", cityName);
            return hardcodedLocation;
        }
        
        // If all variations failed, throw exception
        throw new RuntimeException("City not found: " + cityName);
    }
    
    /**
     * Find the best location match from multiple geocoding results
     * 
     * @param locations List of location results from geocoding API
     * @param originalCityName The original city name being searched
     * @return The best matching location or null if no good match found
     */
    private Map<String, Object> findBestLocationMatch(List<Map<String, Object>> locations, String originalCityName) {
        String lowerOriginalCity = originalCityName.toLowerCase();
        
        // Special handling for known cities with multiple locations
        if (lowerOriginalCity.contains("nanded")) {
            // For Nanded, specifically look for Maharashtra location
            for (Map<String, Object> location : locations) {
                String name = ((String) location.get("name")).toLowerCase();
                String state = (String) location.get("state");
                
                if (name.contains("nanded") && state != null && state.toLowerCase().contains("maharashtra")) {
                    log.info("Found Nanded in Maharashtra: {}", location);
                    return location;
                }
            }
            
            // If no Maharashtra match found, try to find the most likely Nanded
            for (Map<String, Object> location : locations) {
                String name = ((String) location.get("name")).toLowerCase();
                String state = (String) location.get("state");
                
                if (name.contains("nanded")) {
                    log.warn("Using Nanded location: {} in state: {}", name, state);
                    return location;
                }
            }
        }
        
        // Priority 1: Exact city name match with correct state
        for (Map<String, Object> location : locations) {
            String name = ((String) location.get("name")).toLowerCase();
            String state = (String) location.get("state");
            
            // Check if this is the exact city we're looking for
            if (name.equals(lowerOriginalCity.replace(", maharashtra", "").replace(", india", "").replace(",in", "").trim())) {
                return location;
            }
        }
        
        // Priority 2: Partial city name match
        for (Map<String, Object> location : locations) {
            String name = ((String) location.get("name")).toLowerCase();
            String state = (String) location.get("state");
            
            if (name.contains(lowerOriginalCity.replace(", maharashtra", "").replace(", india", "").replace(",in", "").trim())) {
                return location;
            }
        }
        
        // Priority 3: Return the first result if no better match found
        return locations.get(0);
    }
    
    /**
     * Get hardcoded coordinates for cities that the geocoding API might not resolve correctly
     * 
     * @param cityName The city name to look up
     * @return Map with hardcoded coordinates or null if not found
     */
    private Map<String, Object> getHardcodedLocation(String cityName) {
        String lowerCityName = cityName.toLowerCase();
        
        // Hardcoded coordinates for cities that might be misresolved
        if (lowerCityName.contains("nanded")) {
            Map<String, Object> nandedLocation = new java.util.HashMap<>();
            nandedLocation.put("name", "Nanded");
            nandedLocation.put("lat", 19.1539);
            nandedLocation.put("lon", 77.3021);
            nandedLocation.put("country", "IN");
            nandedLocation.put("state", "Maharashtra");
            return nandedLocation;
        }
        
        return null;
    }

    /**
     * Fetch current weather data from OpenWeatherMap API using coordinates
     * 
     * This method uses the geocoding API first to resolve city names to
     * coordinates, then fetches weather data using those coordinates.
     * This approach is more reliable for cities with multiple locations
     * or different spellings.
     * 
     * @param cityName The city name
     * @return WeatherData with current weather information
     */
    private WeatherData fetchCurrentWeather(String cityName) {
        // First resolve city to coordinates
        Map<String, Object> coordinates = resolveCityCoordinates(cityName);
        
        Double lat = (Double) coordinates.get("lat");
        Double lon = (Double) coordinates.get("lon");
        String resolvedCityName = (String) coordinates.get("name");
        String country = (String) coordinates.get("country");
        String state = (String) coordinates.get("state");

        // Use coordinates for weather API call
        String url = String.format("%s/weather?lat=%s&lon=%s&appid=%s&units=%s",
                apiBaseUrl, lat, lon, apiKey, units);

        log.debug("Making current weather API request to: {}", url);

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        if (response == null) {
            throw new RuntimeException("No response received from API");
        }

        WeatherData weatherData = extractCurrentWeatherData(response);
        
        // Update with resolved city information
        weatherData.setCityName(resolvedCityName);
        weatherData.setCountryCode(country);
        weatherData.setLatitude(lat);
        weatherData.setLongitude(lon);
        
        // Add state information if available
        if (state != null && !state.isEmpty()) {
            weatherData.setCityName(resolvedCityName + ", " + state);
        }

        return weatherData;
    }

    /**
     * Fetch forecast data from OpenWeatherMap API using coordinates
     * 
     * @param weatherData The weather data object to populate
     */
    private void fetchForecastData(WeatherData weatherData) {
        try {
            // Use coordinates for forecast API call for better accuracy
            String url = String.format("%s/forecast?lat=%s&lon=%s&appid=%s&units=%s",
                    apiBaseUrl, weatherData.getLatitude(), weatherData.getLongitude(), apiKey, units);

            log.debug("Making forecast API request to: {}", url);

            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            if (response != null) {
                extractForecastData(response, weatherData);
            }
        } catch (Exception e) {
            log.warn("Failed to fetch forecast data: {}", e.getMessage());
            // Don't fail the entire request if forecast fails
        }
    }

    /**
     * Fetch Air Quality Index data from OpenWeatherMap API
     * 
     * @param weatherData The weather data object to populate
     */
    private void fetchAQIData(WeatherData weatherData) {
        try {
            String url = String.format("%s/air_pollution?lat=%s&lon=%s&appid=%s",
                    apiBaseUrl, weatherData.getLatitude(),
                    weatherData.getLongitude(), apiKey);

            log.debug("Making AQI API request to: {}", url);

            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            if (response != null) {
                extractAQIData(response, weatherData);
            }
        } catch (Exception e) {
            log.warn("Failed to fetch AQI data: {}", e.getMessage());
            // Don't fail the entire request if AQI fails
        }
    }

    /**
     * Extract current weather data from API response
     * 
     * @param response The API response as a Map
     * @return WeatherData object with current weather information
     */
    @SuppressWarnings("unchecked")
    private WeatherData extractCurrentWeatherData(Map<String, Object> response) {
        WeatherData weatherData = new WeatherData();

        // Extract basic location information
        weatherData.setCityName((String) response.get("name"));

        Map<String, Object> sys = (Map<String, Object>) response.get("sys");
        if (sys != null) {
            weatherData.setCountryCode((String) sys.get("country"));

            // Handle sunrise/sunset timestamps
            Object sunriseObj = sys.get("sunrise");
            Object sunsetObj = sys.get("sunset");

            if (sunriseObj instanceof Integer) {
                weatherData.setSunrise(((Integer) sunriseObj).longValue());
            } else if (sunriseObj instanceof Long) {
                weatherData.setSunrise((Long) sunriseObj);
            }

            if (sunsetObj instanceof Integer) {
                weatherData.setSunset(((Integer) sunsetObj).longValue());
            } else if (sunsetObj instanceof Long) {
                weatherData.setSunset((Long) sunsetObj);
            }
        }

        // Extract coordinates
        Map<String, Object> coord = (Map<String, Object>) response.get("coord");
        if (coord != null) {
            Object latObj = coord.get("lat");
            Object lonObj = coord.get("lon");

            if (latObj instanceof Integer) {
                weatherData.setLatitude(((Integer) latObj).doubleValue());
            } else if (latObj instanceof Double) {
                weatherData.setLatitude((Double) latObj);
            }

            if (lonObj instanceof Integer) {
                weatherData.setLongitude(((Integer) lonObj).doubleValue());
            } else if (lonObj instanceof Double) {
                weatherData.setLongitude((Double) lonObj);
            }
        }

        // Extract main weather data
        Map<String, Object> main = (Map<String, Object>) response.get("main");
        if (main != null) {
            Object tempObj = main.get("temp");
            Object feelsLikeObj = main.get("feels_like");
            Object humidityObj = main.get("humidity");
            Object pressureObj = main.get("pressure");

            if (tempObj instanceof Integer) {
                weatherData.setTemperature(((Integer) tempObj).doubleValue());
            } else if (tempObj instanceof Double) {
                weatherData.setTemperature((Double) tempObj);
            }

            if (feelsLikeObj instanceof Integer) {
                weatherData.setFeelsLike(((Integer) feelsLikeObj).doubleValue());
            } else if (feelsLikeObj instanceof Double) {
                weatherData.setFeelsLike((Double) feelsLikeObj);
            }

            if (humidityObj instanceof Integer) {
                weatherData.setHumidity((Integer) humidityObj);
            } else if (humidityObj instanceof Long) {
                weatherData.setHumidity(((Long) humidityObj).intValue());
            }

            if (pressureObj instanceof Integer) {
                weatherData.setPressure((Integer) pressureObj);
            } else if (pressureObj instanceof Long) {
                weatherData.setPressure(((Long) pressureObj).intValue());
            }
        }

        // Extract wind information
        Map<String, Object> wind = (Map<String, Object>) response.get("wind");
        if (wind != null) {
            Object speedObj = wind.get("speed");
            Object degObj = wind.get("deg");

            if (speedObj instanceof Integer) {
                weatherData.setWindSpeed(((Integer) speedObj).doubleValue());
            } else if (speedObj instanceof Double) {
                weatherData.setWindSpeed((Double) speedObj);
            }

            if (degObj instanceof Integer) {
                weatherData.setWindDirection((Integer) degObj);
            } else if (degObj instanceof Long) {
                weatherData.setWindDirection(((Long) degObj).intValue());
            }
        }

        // Extract weather conditions
        if (response.get("weather") instanceof List) {
            List<Map<String, Object>> weatherList = (List<Map<String, Object>>) response.get("weather");
            if (!weatherList.isEmpty()) {
                Map<String, Object> weather = weatherList.get(0);
                weatherData.setWeatherCondition((String) weather.get("description"));
                weatherData.setWeatherIcon((String) weather.get("icon"));
            }
        }

        // Extract additional data
        Object visibilityObj = response.get("visibility");
        if (visibilityObj instanceof Integer) {
            weatherData.setVisibility((Integer) visibilityObj);
        } else if (visibilityObj instanceof Long) {
            weatherData.setVisibility(((Long) visibilityObj).intValue());
        }

        Map<String, Object> clouds = (Map<String, Object>) response.get("clouds");
        if (clouds != null) {
            Object allObj = clouds.get("all");
            if (allObj instanceof Integer) {
                weatherData.setCloudiness((Integer) allObj);
            } else if (allObj instanceof Long) {
                weatherData.setCloudiness(((Long) allObj).intValue());
            }
        }

        // Set temperature unit
        weatherData.setTemperatureUnit(
                units.equals("metric") ? "Celsius" : units.equals("imperial") ? "Fahrenheit" : "Kelvin");

        return weatherData;
    }

    /**
     * Extract forecast data from API response
     * 
     * @param response    The API response as a Map
     * @param weatherData The weather data object to populate
     */
    @SuppressWarnings("unchecked")
    private void extractForecastData(Map<String, Object> response, WeatherData weatherData) {
        List<Map<String, Object>> list = (List<Map<String, Object>>) response.get("list");
        if (list == null)
            return;

        List<WeatherData.HourlyForecast> hourlyForecast = new ArrayList<>();
        List<WeatherData.DailyForecast> dailyForecast = new ArrayList<>();

        for (Map<String, Object> item : list) {
            // Extract timestamp
            Object dtObj = item.get("dt");
            long timestamp = 0;
            if (dtObj instanceof Integer) {
                timestamp = ((Integer) dtObj).longValue();
            } else if (dtObj instanceof Long) {
                timestamp = (Long) dtObj;
            }

            LocalDateTime dateTime = LocalDateTime.ofInstant(
                    Instant.ofEpochSecond(timestamp), ZoneId.systemDefault());

            // Extract weather info
            Map<String, Object> main = (Map<String, Object>) item.get("main");
            List<Map<String, Object>> weather = (List<Map<String, Object>>) item.get("weather");
            Map<String, Object> wind = (Map<String, Object>) item.get("wind");

            if (main != null && !weather.isEmpty()) {
                WeatherData.HourlyForecast hourly = new WeatherData.HourlyForecast();
                hourly.setTime(dateTime);

                Object tempObj = main.get("temp");
                if (tempObj instanceof Integer) {
                    hourly.setTemperature(((Integer) tempObj).doubleValue());
                } else if (tempObj instanceof Double) {
                    hourly.setTemperature((Double) tempObj);
                }

                hourly.setWeatherCondition((String) weather.get(0).get("description"));
                hourly.setWeatherIcon((String) weather.get(0).get("icon"));

                Object humidityObj = main.get("humidity");
                if (humidityObj instanceof Integer) {
                    hourly.setHumidity((Integer) humidityObj);
                }

                if (wind != null) {
                    Object speedObj = wind.get("speed");
                    if (speedObj instanceof Integer) {
                        hourly.setWindSpeed(((Integer) speedObj).doubleValue());
                    } else if (speedObj instanceof Double) {
                        hourly.setWindSpeed((Double) speedObj);
                    }
                }

                // Extract precipitation data from rain field
                Map<String, Object> rain = (Map<String, Object>) item.get("rain");
                if (rain != null) {
                    Object rain3hObj = rain.get("3h");
                    if (rain3hObj instanceof Integer) {
                        hourly.setPrecipitation((Integer) rain3hObj);
                    } else if (rain3hObj instanceof Double) {
                        hourly.setPrecipitation(((Double) rain3hObj).intValue());
                    } else {
                        hourly.setPrecipitation(0);
                    }
                } else {
                    hourly.setPrecipitation(0);
                }

                hourlyForecast.add(hourly);
            }
        }

        weatherData.setHourlyForecast(hourlyForecast);
        weatherData.setDailyForecast(generateDailyForecast(hourlyForecast));
    }

    /**
     * Generate daily forecast from hourly data
     * 
     * @param hourlyForecast List of hourly forecasts
     * @return List of daily forecasts
     */
    private List<WeatherData.DailyForecast> generateDailyForecast(List<WeatherData.HourlyForecast> hourlyForecast) {
        List<WeatherData.DailyForecast> dailyForecast = new ArrayList<>();

        // Group by day and calculate min/max temperatures
        Map<String, List<WeatherData.HourlyForecast>> dailyGroups = new java.util.HashMap<>();

        for (WeatherData.HourlyForecast hourly : hourlyForecast) {
            String dayKey = hourly.getTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            dailyGroups.computeIfAbsent(dayKey, k -> new ArrayList<>()).add(hourly);
        }

        // Sort the daily groups by date to ensure proper chronological order
        List<String> sortedDayKeys = dailyGroups.keySet().stream()
                .sorted()
                .collect(java.util.stream.Collectors.toList());

        for (String dayKey : sortedDayKeys) {
            List<WeatherData.HourlyForecast> dayData = dailyGroups.get(dayKey);

            double minTemp = dayData.stream().mapToDouble(h -> h.getTemperature()).min().orElse(0);
            double maxTemp = dayData.stream().mapToDouble(h -> h.getTemperature()).max().orElse(0);

            // Use the most common weather condition for the day
            String mostCommonCondition = dayData.get(0).getWeatherCondition();
            String mostCommonIcon = dayData.get(0).getWeatherIcon();

            WeatherData.DailyForecast daily = new WeatherData.DailyForecast();
            daily.setDate(dayData.get(0).getTime());
            daily.setMinTemperature(minTemp);
            daily.setMaxTemperature(maxTemp);
            daily.setWeatherCondition(mostCommonCondition);
            daily.setWeatherIcon(mostCommonIcon);

            // Calculate average humidity and wind speed for the day
            double avgHumidity = dayData.stream()
                    .mapToDouble(h -> h.getHumidity() != null ? h.getHumidity() : 0)
                    .average()
                    .orElse(0);

            double avgWindSpeed = dayData.stream()
                    .mapToDouble(h -> h.getWindSpeed() != null ? h.getWindSpeed() : 0)
                    .average()
                    .orElse(0);

            daily.setHumidity((int) Math.round(avgHumidity));
            daily.setWindSpeed(avgWindSpeed);

            // Calculate average precipitation for the day
            double avgPrecipitation = dayData.stream()
                    .mapToDouble(h -> h.getPrecipitation() != null ? h.getPrecipitation() : 0)
                    .average()
                    .orElse(0);
            daily.setPrecipitation((int) Math.round(avgPrecipitation));

            dailyForecast.add(daily);
        }

        return dailyForecast;
    }

    /**
     * Extract AQI data from API response
     * 
     * @param response    The API response as a Map
     * @param weatherData The weather data object to populate
     */
    @SuppressWarnings("unchecked")
    private void extractAQIData(Map<String, Object> response, WeatherData weatherData) {
        List<Map<String, Object>> list = (List<Map<String, Object>>) response.get("list");
        if (list == null || list.isEmpty())
            return;

        Map<String, Object> data = list.get(0);
        Map<String, Object> components = (Map<String, Object>) data.get("components");
        Map<String, Object> main = (Map<String, Object>) data.get("main");

        if (main != null) {
            Object aqiObj = main.get("aqi");
            if (aqiObj instanceof Integer) {
                int aqi = (Integer) aqiObj;
                weatherData.setAqi(aqi);

                // Determine AQI category and color
                if (aqi <= 50) {
                    weatherData.setAqiCategory("Good");
                    weatherData.setAqiColor("#00E400");
                } else if (aqi <= 100) {
                    weatherData.setAqiCategory("Moderate");
                    weatherData.setAqiColor("#FFFF00");
                } else if (aqi <= 150) {
                    weatherData.setAqiCategory("Unhealthy for Sensitive Groups");
                    weatherData.setAqiColor("#FF7E00");
                } else if (aqi <= 200) {
                    weatherData.setAqiCategory("Unhealthy");
                    weatherData.setAqiColor("#FF0000");
                } else if (aqi <= 300) {
                    weatherData.setAqiCategory("Very Unhealthy");
                    weatherData.setAqiColor("#8F3F97");
                } else {
                    weatherData.setAqiCategory("Hazardous");
                    weatherData.setAqiColor("#7E0023");
                }
            }
        }
    }

    /**
     * Create error weather data object
     * 
     * @param errorMessage The error message
     * @return WeatherData object with error information
     */
    private WeatherData createErrorWeatherData(String errorMessage) {
        WeatherData weatherData = new WeatherData();
        weatherData.setErrorMessage(errorMessage);
        weatherData.setFetchedAt(LocalDateTime.now());
        weatherData.setCached(false);
        return weatherData;
    }
}
