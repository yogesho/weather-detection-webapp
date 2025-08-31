package com.weatherapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * WeatherData - Data Transfer Object (DTO) for Weather Information
 * 
 * This class represents the complete weather data that will be displayed
 * in our JSP pages. It contains current weather, forecast, and AQI information.
 * 
 * Why we use DTOs:
 * - Separates data structure from database entities
 * - Provides clean data transfer between layers
 * - Allows us to customize data for specific views
 * - Makes the API more maintainable and flexible
 * 
 * @author Weather App Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherData {

    // =============================================================================
    // BASIC LOCATION INFORMATION
    // =============================================================================

    /**
     * City name (e.g., "London", "New York")
     */
    private String cityName;

    /**
     * Country code (e.g., "GB", "US")
     */
    private String countryCode;

    /**
     * Latitude coordinate
     */
    private Double latitude;

    /**
     * Longitude coordinate
     */
    private Double longitude;

    // =============================================================================
    // CURRENT WEATHER INFORMATION
    // =============================================================================

    /**
     * Current temperature in the configured unit (Celsius/Fahrenheit)
     */
    private Double temperature;

    /**
     * Temperature unit (Celsius, Fahrenheit, Kelvin)
     */
    private String temperatureUnit;

    /**
     * "Feels like" temperature (accounts for humidity and wind)
     */
    private Double feelsLike;

    /**
     * Humidity percentage (0-100)
     */
    private Integer humidity;

    /**
     * Atmospheric pressure in hPa
     */
    private Integer pressure;

    /**
     * Weather condition description (e.g., "clear sky", "rain")
     */
    private String weatherCondition;

    /**
     * Weather icon code (e.g., "01d", "10n")
     */
    private String weatherIcon;

    // =============================================================================
    // WIND INFORMATION
    // =============================================================================

    /**
     * Wind speed in m/s
     */
    private Double windSpeed;

    /**
     * Wind direction in degrees (0-360)
     */
    private Integer windDirection;

    // =============================================================================
    // VISIBILITY AND CLOUDS
    // =============================================================================

    /**
     * Visibility in meters
     */
    private Integer visibility;

    /**
     * Cloudiness percentage (0-100)
     */
    private Integer cloudiness;

    // =============================================================================
    // SUN INFORMATION
    // =============================================================================

    /**
     * Sunrise time as Unix timestamp
     */
    private Long sunrise;

    /**
     * Sunset time as Unix timestamp
     */
    private Long sunset;

    // =============================================================================
    // AIR QUALITY INDEX (AQI)
    // =============================================================================

    /**
     * Air Quality Index value (1-500)
     */
    private Integer aqi;

    /**
     * AQI category (Good, Moderate, Unhealthy, etc.)
     */
    private String aqiCategory;

    /**
     * AQI color for UI display
     */
    private String aqiColor;

    // =============================================================================
    // FORECAST INFORMATION
    // =============================================================================

    /**
     * Hourly forecast data for the next 24 hours
     */
    private List<HourlyForecast> hourlyForecast;

    /**
     * Daily forecast data for the next 7 days
     */
    private List<DailyForecast> dailyForecast;

    // =============================================================================
    // METADATA
    // =============================================================================

    /**
     * When this weather data was fetched
     */
    private LocalDateTime fetchedAt;

    /**
     * Whether this data was served from cache
     */
    private Boolean cached;

    /**
     * Response time in milliseconds
     */
    private Long responseTimeMs;

    /**
     * Error message if any occurred
     */
    private String errorMessage;

    // =============================================================================
    // UTILITY METHODS FOR JSP COMPATIBILITY
    // =============================================================================

    /**
     * Get fetchedAt as Date for JSP formatting
     * This method converts LocalDateTime to Date for compatibility with
     * fmt:formatDate
     * 
     * @return Date object representing when the data was fetched
     */
    public Date getFetchedAtAsDate() {
        if (fetchedAt == null) {
            return null;
        }
        return Date.from(fetchedAt.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Get sunrise as Date for JSP formatting
     * This method converts Unix timestamp to Date for compatibility with
     * fmt:formatDate
     * 
     * @return Date object representing sunrise time
     */
    public Date getSunriseAsDate() {
        if (sunrise == null) {
            return null;
        }
        return new Date(sunrise * 1000); // Convert seconds to milliseconds
    }

    /**
     * Get sunset as Date for JSP formatting
     * This method converts Unix timestamp to Date for compatibility with
     * fmt:formatDate
     * 
     * @return Date object representing sunset time
     */
    public Date getSunsetAsDate() {
        if (sunset == null) {
            return null;
        }
        return new Date(sunset * 1000); // Convert seconds to milliseconds
    }

    // =============================================================================
    // INNER CLASSES FOR FORECAST DATA
    // =============================================================================

    /**
     * HourlyForecast - Represents weather data for a specific hour
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HourlyForecast {
        private LocalDateTime time;
        private Double temperature;
        private String weatherCondition;
        private String weatherIcon;
        private Integer humidity;
        private Double windSpeed;
        private Integer precipitation;

        /**
         * Get time as Date for JSP formatting
         * This method converts LocalDateTime to Date for compatibility with
         * fmt:formatDate
         * 
         * @return Date object representing the forecast time
         */
        public Date getTimeAsDate() {
            if (time == null) {
                return null;
            }
            return Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
        }
    }

    /**
     * DailyForecast - Represents weather data for a specific day
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailyForecast {
        private LocalDateTime date;
        private Double minTemperature;
        private Double maxTemperature;
        private String weatherCondition;
        private String weatherIcon;
        private Integer humidity;
        private Double windSpeed;
        private Integer precipitation;

        /**
         * Get date as Date for JSP formatting
         * This method converts LocalDateTime to Date for compatibility with
         * fmt:formatDate
         * 
         * @return Date object representing the forecast date
         */
        public Date getDateAsDate() {
            if (date == null) {
                return null;
            }
            return Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
        }
    }

    // =============================================================================
    // UTILITY METHODS
    // =============================================================================

    /**
     * Get temperature in Fahrenheit
     * 
     * @return Temperature in Fahrenheit
     */
    public Double getTemperatureFahrenheit() {
        if (temperature == null)
            return null;
        if ("Fahrenheit".equals(temperatureUnit)) {
            return temperature;
        }
        // Convert from Celsius to Fahrenheit
        return (temperature * 9 / 5) + 32;
    }

    /**
     * Get temperature in Celsius
     * 
     * @return Temperature in Celsius
     */
    public Double getTemperatureCelsius() {
        if (temperature == null)
            return null;
        if ("Celsius".equals(temperatureUnit)) {
            return temperature;
        }
        // Convert from Fahrenheit to Celsius
        return (temperature - 32) * 5 / 9;
    }

    /**
     * Get weather icon URL for OpenWeatherMap
     * 
     * @return Complete URL for the weather icon
     */
    public String getWeatherIconUrl() {
        if (weatherIcon == null)
            return null;
        return "https://openweathermap.org/img/wn/" + weatherIcon + "@2x.png";
    }

    /**
     * Get wind direction as cardinal direction (N, NE, E, SE, S, SW, W, NW)
     * 
     * @return Wind direction as cardinal direction
     */
    public String getWindDirectionCardinal() {
        if (windDirection == null)
            return null;

        String[] directions = { "N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE",
                "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW" };
        int index = (int) Math.round(windDirection / 22.5) % 16;
        return directions[index];
    }

    /**
     * Check if the weather data is valid (no errors)
     * 
     * @return true if weather data is valid, false otherwise
     */
    public boolean isValid() {
        return errorMessage == null || errorMessage.isEmpty();
    }
}
