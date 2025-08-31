# 🌤️ Weather Detection WebApp - Complete Project Documentation

## 📋 Table of Contents
1. [Project Overview](#project-overview)
2. [System Architecture](#system-architecture)
3. [Technology Stack](#technology-stack)
4. [Project Structure](#project-structure)
5. [Core Components](#core-components)
6. [Data Flow](#data-flow)
7. [API Integration](#api-integration)
8. [Frontend Architecture](#frontend-architecture)
9. [Database & Caching](#database--caching)
10. [Deployment & Configuration](#deployment--configuration)
11. [Testing Strategy](#testing-strategy)
12. [Troubleshooting Guide](#troubleshooting-guide)

---

## 🎯 Project Overview

### What is Weather Detection WebApp?
A modern, responsive web application built with Spring Boot and JSP that provides real-time weather information, forecasts, and air quality data for any city worldwide.

### Key Features
- ✅ Real-time weather data with current conditions
- ✅ 7-day weather forecast with detailed daily breakdowns
- ✅ 24-hour hourly temperature forecast with Chart.js visualization
- ✅ Air Quality Index (AQI) with color-coded health indicators
- ✅ Responsive design for desktop, tablet, and mobile
- ✅ Search history and popular cities quick access
- ✅ Interactive daily forecast cards with detailed modal views
- ✅ Dynamic weather-themed backgrounds
- ✅ Caching for improved performance
- ✅ Geocoding API for accurate city resolution

---

## 🏗️ System Architecture

### High-Level Architecture Diagram
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Web Browser   │    │   Spring Boot   │    │  OpenWeatherMap │
│   (Frontend)    │◄──►│   Application   │◄──►│      APIs       │
│                 │    │                 │    │                 │
│ • JSP Pages     │    │ • Controllers   │    │ • Current Weather│
│ • Bootstrap 5   │    │ • Services      │    │ • 5-Day Forecast │
│ • Chart.js      │    │ • DTOs          │    │ • Geocoding     │
│ • JavaScript    │    │ • Configuration │    │ • Air Pollution │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                              │
                              ▼
                       ┌─────────────────┐
                       │   Caching Layer │
                       │   (Caffeine)    │
                       └─────────────────┘
```

### Layered Architecture
```
┌─────────────────────────────────────────────────────────────┐
│                    PRESENTATION LAYER                       │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐         │
│  │   index.jsp │  │ weather.jsp │  │  health.jsp │         │
│  └─────────────┘  └─────────────┘  └─────────────┘         │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                    CONTROLLER LAYER                         │
│  ┌─────────────────────────────────────────────────────┐   │
│  │              WeatherController.java                 │   │
│  │  • home() - Serves home page                        │   │
│  │  • getWeather() - Handles weather requests          │   │
│  │  • health() - Application health check              │   │
│  └─────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                     SERVICE LAYER                          │
│  ┌─────────────────────────────────────────────────────┐   │
│  │              WeatherService.java                    │   │
│  │  • fetchCurrentWeather() - Current weather data     │   │
│  │  • fetchForecastData() - 5-day forecast             │   │
│  │  • fetchAQIData() - Air quality information         │   │
│  │  • resolveCityCoordinates() - Geocoding API         │   │
│  └─────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                    EXTERNAL APIs                           │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐         │
│  │ OpenWeather │  │ Geocoding   │  │ Air Quality │         │
│  │   Current   │  │    API      │  │     API     │         │
│  └─────────────┘  └─────────────┘  └─────────────┘         │
└─────────────────────────────────────────────────────────────┘
```

---

## 🛠️ Technology Stack

### Backend Technologies
- **Java 21** - Modern Java with enhanced performance
- **Spring Boot 3.2.0** - Rapid application development framework
- **Spring MVC** - Web framework for building web applications
- **Spring Cache** - Caching abstraction with Caffeine
- **RestTemplate** - HTTP client for API calls
- **Jackson** - JSON processing library
- **Lombok** - Reduces boilerplate code

### Frontend Technologies
- **JSP (JavaServer Pages)** - Server-side view technology
- **JSTL (Jakarta Server Pages Standard Tag Library)** - JSP tag library
- **Bootstrap 5** - Responsive CSS framework
- **Font Awesome 6.4.0** - Icon library
- **Chart.js** - JavaScript charting library
- **Vanilla JavaScript** - Client-side interactivity

### External APIs
- **OpenWeatherMap API** - Weather data provider
  - Current Weather API
  - 5-Day/3-Hour Forecast API
  - Geocoding API
  - Air Pollution API

### Build & Development Tools
- **Maven** - Build automation and dependency management
- **Embedded Tomcat** - Web server
- **Spring Boot DevTools** - Development utilities

---

## 📁 Project Structure

```
weather-detect-app/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/weatherapp/
│   │   │       ├── WeatherWebAppApplication.java    # Main application class
│   │   │       ├── controller/
│   │   │       │   └── WeatherController.java       # Web controller
│   │   │       ├── service/
│   │   │       │   └── WeatherService.java          # Business logic
│   │   │       ├── dto/
│   │   │       │   └── WeatherData.java             # Data transfer objects
│   │   │       └── config/
│   │   │           └── AppConfig.java               # Configuration beans
│   │   ├── resources/
│   │   │   ├── application.properties               # Application configuration
│   │   │   └── static/
│   │   │       └── index.html                       # Welcome page
│   │   └── webapp/
│   │       ├── WEB-INF/
│   │       │   ├── web.xml                          # Web application configuration
│   │       │   └── jsp/
│   │       │       ├── index.jsp                    # Home page
│   │       │       ├── weather.jsp                  # Weather results page
│   │       │       └── health.jsp                   # Health check page
│   │       ├── css/
│   │       │   └── style.css                        # Custom styles
│   │       └── js/
│   │           └── weather.js                       # Client-side JavaScript
│   └── test/
│       └── java/
│           └── com/weatherapp/
│               └── WeatherWebAppApplicationTests.java
├── pom.xml                                           # Maven configuration
├── README.md                                         # Project readme
└── PROJECT_DOCUMENTATION.md                          # This documentation
```

---

## 🔧 Core Components

### 1. Main Application Class
**File:** `WeatherWebAppApplication.java`

```java
@SpringBootApplication
@EnableCaching
public class WeatherWebAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(WeatherWebAppApplication.class, args);
    }
}
```

**Purpose:** 
- Entry point of the Spring Boot application
- Enables caching with `@EnableCaching`
- Configures auto-configuration and component scanning

### 2. Controller Layer
**File:** `WeatherController.java`

**Key Methods:**

#### `home()` Method
```java
@GetMapping("/")
public String home(Model model, HttpSession session) {
    // Serves the home page with search history
}
```
**Flow:**
1. Receives GET request to root URL "/"
2. Retrieves search history from session
3. Adds search history to model
4. Returns "index" view name

#### `getWeather()` Method
```java
@GetMapping("/weather")
public String getWeather(@RequestParam String city, Model model, HttpSession session) {
    // Handles weather search requests
}
```
**Flow:**
1. Receives city parameter from search form
2. Calls WeatherService to fetch weather data
3. Updates search history in session
4. Adds weather data to model
5. Returns "weather" view name or "index" on error

#### `health()` Method
```java
@GetMapping("/health")
public String health(Model model) {
    // Application health check endpoint
}
```
**Flow:**
1. Creates health status information
2. Adds timestamp and service info to model
3. Returns "health" view name

### 3. Service Layer
**File:** `WeatherService.java`

**Key Methods:**

#### `getWeatherData()` Method
```java
@Cacheable("weather")
public WeatherData getWeatherData(String cityName) {
    // Main method to fetch complete weather data
}
```
**Flow:**
1. Resolves city coordinates using geocoding API
2. Fetches current weather data
3. Fetches forecast data
4. Fetches AQI data
5. Combines all data into WeatherData DTO
6. Caches result for 30 minutes

#### `resolveCityCoordinates()` Method
```java
private Map<String, Object> resolveCityCoordinates(String cityName) {
    // Resolves city name to coordinates using geocoding API
}
```
**Flow:**
1. Tries multiple search variations (city, city+country, city+India)
2. Calls OpenWeatherMap Geocoding API
3. Selects best location match
4. Returns coordinates, country, and state information

#### `fetchCurrentWeather()` Method
```java
private WeatherData fetchCurrentWeather(String cityName) {
    // Fetches current weather data from OpenWeatherMap API
}
```
**Flow:**
1. Uses resolved coordinates to call current weather API
2. Extracts weather information from API response
3. Converts units and formats data
4. Returns populated WeatherData object

### 4. Data Transfer Objects
**File:** `WeatherData.java`

**Structure:**
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherData {
    // Location information
    private String cityName;
    private String countryCode;
    private String state;
    private Double latitude;
    private Double longitude;
    
    // Current weather
    private Double temperature;
    private String temperatureUnit;
    private Double feelsLike;
    private Integer humidity;
    private Double windSpeed;
    private String windDirectionCardinal;
    private Integer pressure;
    private String weatherCondition;
    private String weatherIcon;
    private String weatherIconUrl;
    
    // Additional data
    private Integer visibility;
    private Integer cloudiness;
    private Long sunrise;
    private Long sunset;
    private LocalDateTime fetchedAt;
    
    // AQI data
    private Integer aqi;
    private String aqiCategory;
    private String aqiColor;
    
    // Forecast data
    private List<HourlyForecast> hourlyForecast;
    private List<DailyForecast> dailyForecast;
}
```

**Nested Classes:**

#### `HourlyForecast` Class
```java
public static class HourlyForecast {
    private LocalDateTime time;
    private Double temperature;
    private String weatherCondition;
    private String weatherIcon;
    private Integer humidity;
    private Double windSpeed;
    private Integer precipitation;
}
```

#### `DailyForecast` Class
```java
public static class DailyForecast {
    private LocalDateTime date;
    private Double maxTemperature;
    private Double minTemperature;
    private String weatherCondition;
    private String weatherIcon;
    private Integer humidity;
    private Double windSpeed;
    private Integer precipitation;
}
```

### 5. Configuration
**File:** `AppConfig.java`

```java
@Configuration
public class AppConfig {
    
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
            .expireAfterWrite(30, TimeUnit.MINUTES)
            .maximumSize(100));
        return cacheManager;
    }
}
```

**Purpose:**
- Configures RestTemplate for HTTP API calls
- Sets up Caffeine cache with 30-minute expiration
- Maximum cache size of 100 entries

---

## 🔄 Data Flow

### 1. User Search Flow
```
User Input → Controller → Service → External API → Response Processing → View Rendering
```

**Detailed Flow:**
1. **User enters city name** in search form on index.jsp
2. **Form submission** sends GET request to `/weather?city=CityName`
3. **WeatherController.getWeather()** receives request
4. **WeatherService.getWeatherData()** is called with caching
5. **Geocoding API** resolves city to coordinates
6. **Current Weather API** fetches current conditions
7. **Forecast API** fetches 5-day forecast
8. **Air Quality API** fetches AQI data
9. **Data combination** creates WeatherData DTO
10. **Session update** adds city to search history
11. **Model population** with weather data
12. **View rendering** returns weather.jsp with data

### 2. Caching Flow
```
Request → Cache Check → Cache Hit/Miss → API Call (if needed) → Cache Update → Response
```

**Cache Strategy:**
- **Cache Key:** City name
- **Expiration:** 30 minutes
- **Max Size:** 100 entries
- **Eviction:** LRU (Least Recently Used)

### 3. Error Handling Flow
```
API Error → Exception Handling → Error Message → User-Friendly Display
```

**Error Types:**
- City not found (404)
- API key invalid (401)
- Network errors
- Invalid coordinates

---

## 🌐 API Integration

### OpenWeatherMap APIs Used

#### 1. Current Weather API
```
GET https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API_KEY}&units=metric
```

**Response Processing:**
```java
// Extract temperature, humidity, wind, pressure, etc.
weatherData.setTemperature(extractDouble(response, "main.temp"));
weatherData.setHumidity(extractInteger(response, "main.humidity"));
weatherData.setWindSpeed(extractDouble(response, "wind.speed"));
```

#### 2. 5-Day Forecast API
```
GET https://api.openweathermap.org/data/2.5/forecast?lat={lat}&lon={lon}&appid={API_KEY}&units=metric
```

**Response Processing:**
```java
// Group forecast data by day
Map<LocalDate, List<Map<String, Object>>> dailyGroups = new HashMap<>();
// Calculate daily averages and extremes
// Create DailyForecast objects
```

#### 3. Geocoding API
```
GET https://api.openweathermap.org/geo/1.0/direct?q={city}&limit=5&appid={API_KEY}
```

**Response Processing:**
```java
// Select best location match
// Extract coordinates, country, state
// Handle multiple results intelligently
```

#### 4. Air Pollution API
```
GET https://api.openweathermap.org/data/2.5/air_pollution?lat={lat}&lon={lon}&appid={API_KEY}
```

**Response Processing:**
```java
// Extract AQI value
// Determine category and color
// Add health recommendations
```

### API Error Handling
```java
try {
    ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
    // Process successful response
} catch (HttpClientErrorException e) {
    if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
        throw new RuntimeException("City not found: " + cityName);
    } else if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
        throw new RuntimeException("Invalid API key");
    }
    throw new RuntimeException("Error fetching weather data: " + e.getMessage());
}
```

---

## 🎨 Frontend Architecture

### 1. JSP Pages Structure

#### `index.jsp` - Home Page
**Purpose:** Main entry point with search functionality

**Key Components:**
- Search form with city input
- Error message display
- Recent searches section
- Popular cities quick access
- Features showcase

**JavaScript Features:**
- Auto-focus on search input
- Form validation
- Auto-suggestions for Indian cities
- Real-time input suggestions

#### `weather.jsp` - Weather Results Page
**Purpose:** Displays comprehensive weather information

**Key Components:**
- Current weather card with temperature and conditions
- AQI and sun information sidebar
- Hourly forecast chart (Chart.js)
- 7-day forecast grid with clickable cards
- Search history section

**Interactive Features:**
- Clickable daily forecast cards
- Modal popup for detailed day information
- Dynamic weather backgrounds
- Responsive design

#### `health.jsp` - Health Check Page
**Purpose:** Application status monitoring

**Key Components:**
- Service status indicator
- Last check timestamp
- Service information
- Back to home navigation

### 2. CSS Architecture

#### `style.css` - Custom Styling
**Key Features:**
- Animated gradient backgrounds
- Glass morphism effects
- Weather-specific themes
- Responsive design
- Hover animations
- Accessibility improvements

**CSS Structure:**
```css
/* Global variables */
:root {
    --primary-color: #0d6efd;
    --shadow-light: 0 2px 4px rgba(0, 0, 0, 0.1);
    /* ... more variables */
}

/* Component-specific styles */
.card { /* Card styling */ }
.forecast-day-card { /* Forecast card styling */ }
.aqi-badge { /* AQI badge styling */ }

/* Responsive design */
@media (max-width: 768px) { /* Mobile styles */ }
@media (min-width: 769px) and (max-width: 1024px) { /* Tablet styles */ }
```

### 3. JavaScript Architecture

#### `weather.js` - Client-side Logic
**Key Functions:**

```javascript
// Initialize daily forecast data
function initializeDailyForecastData(data) {
    dailyForecastData = data;
    console.log('Weather app initialization complete');
}

// Show detailed day information
function showDayDetails(dayIndex) {
    const day = dailyForecastData[dayIndex];
    if (!day) {
        console.log('No daily forecast data available');
        return;
    }
    // Populate modal with day details
}

// Set weather-specific background
function setWeatherBackground(weatherCondition) {
    // Add CSS classes for weather themes
}
```

**Features:**
- Modal handling for daily forecast details
- Dynamic background changes
- Data formatting and display
- Error handling and logging

### 4. Responsive Design

**Breakpoints:**
- **Mobile:** < 768px
- **Tablet:** 769px - 1024px
- **Desktop:** > 1024px

**Responsive Features:**
- Flexible grid layouts
- Mobile-optimized navigation
- Touch-friendly interactions
- Optimized images and icons
- Reduced animations on mobile

---

## 💾 Database & Caching

### Caching Strategy

**Cache Configuration:**
```java
@Cacheable("weather")
public WeatherData getWeatherData(String cityName) {
    // Method results are cached
}
```

**Cache Properties:**
- **Provider:** Caffeine
- **Expiration:** 30 minutes
- **Max Size:** 100 entries
- **Eviction Policy:** LRU

**Cache Benefits:**
- Reduced API calls
- Faster response times
- Lower costs
- Better user experience

### Session Management

**Search History:**
```java
@SuppressWarnings("unchecked")
List<String> searchHistory = (List<String>) session.getAttribute("searchHistory");
if (searchHistory == null) {
    searchHistory = new ArrayList<>();
}
```

**Session Features:**
- Stores last 5 searched cities
- Persists across browser sessions
- Automatic cleanup
- Quick access to recent searches

---

## ⚙️ Deployment & Configuration

### Application Properties
**File:** `application.properties`

```properties
# Server Configuration
server.port=8080

# JSP Configuration
spring.mvc.view.prefix=/WEB-INF/jsp/
spring.mvc.view.suffix=.jsp
spring.web.resources.static-locations=classpath:/static/,classpath:/public/,/webapp/

# OpenWeatherMap API Configuration
openweathermap.api.key=your_api_key_here
openweathermap.api.base-url=https://api.openweathermap.org/data/2.5
openweathermap.api.geo-url=https://api.openweathermap.org/geo/1.0

# Caching Configuration
spring.cache.type=caffeine
spring.cache.cache-names=weather
spring.cache.caffeine.spec=expireAfterWrite=30m,maximumSize=100

# Logging Configuration
logging.level.com.weatherapp=INFO
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} - %msg%n
```

### Environment Setup

**Prerequisites:**
- Java 21 or higher
- Maven 3.8+
- OpenWeatherMap API key

**Setup Steps:**
1. Clone the repository
2. Update API key in `application.properties`
3. Run `mvn clean install`
4. Run `mvn spring-boot:run`
5. Access application at `http://localhost:8080`

### Production Deployment

**Options:**
1. **JAR Deployment:**
   ```bash
   mvn clean package
   java -jar target/weather-webapp-1.0.0.jar
   ```

2. **WAR Deployment:**
   ```bash
   mvn clean package -Pwar
   # Deploy to external Tomcat
   ```

3. **Docker Deployment:**
   ```dockerfile
   FROM openjdk:21-jdk-slim
   COPY target/weather-webapp-1.0.0.jar app.jar
   EXPOSE 8080
   ENTRYPOINT ["java", "-jar", "/app.jar"]
   ```

---

## 🧪 Testing Strategy

### Unit Testing
**File:** `WeatherWebAppApplicationTests.java`

```java
@SpringBootTest
class WeatherWebAppApplicationTests {
    
    @Test
    void contextLoads() {
        // Verify Spring context loads successfully
    }
}
```

### Integration Testing
**Test Scenarios:**
1. **API Integration Tests:**
   - Test OpenWeatherMap API connectivity
   - Verify response parsing
   - Test error handling

2. **Controller Tests:**
   - Test endpoint responses
   - Verify model attributes
   - Test session management

3. **Service Tests:**
   - Test business logic
   - Verify caching behavior
   - Test data transformation

### Manual Testing Checklist

**Functional Testing:**
- [ ] Search for valid cities
- [ ] Handle invalid city names
- [ ] Verify weather data accuracy
- [ ] Test forecast functionality
- [ ] Check AQI display
- [ ] Verify search history
- [ ] Test responsive design

**Performance Testing:**
- [ ] Response time under 3 seconds
- [ ] Cache effectiveness
- [ ] Memory usage monitoring
- [ ] Concurrent user testing

---

## 🔧 Troubleshooting Guide

### Common Issues & Solutions

#### 1. API Key Issues
**Problem:** "Invalid API key" error
**Solution:**
- Verify API key in `application.properties`
- Check API key activation status
- Ensure proper API key format

#### 2. City Not Found
**Problem:** "City not found" error
**Solution:**
- Try adding country code (e.g., "London,GB")
- Check city spelling
- Use suggested city formats

#### 3. JSP Compilation Errors
**Problem:** JSTL URI resolution errors
**Solution:**
- Verify JSTL dependencies in `pom.xml`
- Check JSP taglib declarations
- Ensure proper web.xml configuration

#### 4. Caching Issues
**Problem:** Stale data or no caching
**Solution:**
- Check cache configuration
- Verify @Cacheable annotations
- Monitor cache statistics

#### 5. Responsive Design Issues
**Problem:** Layout breaks on mobile
**Solution:**
- Check CSS media queries
- Verify Bootstrap classes
- Test on different screen sizes

### Debug Mode

**Enable Debug Logging:**
```properties
logging.level.com.weatherapp=DEBUG
logging.level.org.springframework.web=DEBUG
```

**Common Debug Points:**
1. API request/response logging
2. Cache hit/miss logging
3. Controller method execution
4. Service method calls
5. Exception stack traces

### Performance Monitoring

**Key Metrics:**
- Response time per request
- Cache hit ratio
- Memory usage
- API call frequency
- Error rates

**Monitoring Tools:**
- Spring Boot Actuator
- Application logs
- Browser developer tools
- API monitoring services

---

## 🚀 Future Enhancements

### Planned Features
1. **User Authentication**
   - User registration and login
   - Personalized weather preferences
   - Favorite cities management

2. **Advanced Features**
   - Weather alerts and notifications
   - Historical weather data
   - Weather maps integration
   - Multiple language support

3. **Performance Improvements**
   - Redis caching
   - CDN integration
   - Image optimization
   - Progressive Web App (PWA)

4. **Analytics & Monitoring**
   - User behavior analytics
   - Performance monitoring
   - Error tracking
   - Usage statistics

### Technical Debt
1. **Code Improvements**
   - Enhanced error handling
   - Better logging strategy
   - Code documentation
   - Unit test coverage

2. **Architecture Enhancements**
   - Microservices migration
   - Database integration
   - Message queuing
   - Event-driven architecture

---

## 📚 Additional Resources

### Documentation Links
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [OpenWeatherMap API Documentation](https://openweathermap.org/api)
- [Bootstrap 5 Documentation](https://getbootstrap.com/docs/5.3/)
- [Chart.js Documentation](https://www.chartjs.org/docs/)

### Development Tools
- [IntelliJ IDEA](https://www.jetbrains.com/idea/) - Recommended IDE
- [Postman](https://www.postman.com/) - API testing
- [Chrome DevTools](https://developer.chrome.com/docs/devtools/) - Frontend debugging

### Best Practices
- [Spring Boot Best Practices](https://spring.io/guides)
- [REST API Design](https://restfulapi.net/)
- [Responsive Web Design](https://developer.mozilla.org/en-US/docs/Learn/CSS/CSS_layout/Responsive_Design)
- [Web Accessibility](https://www.w3.org/WAI/)

---

## 📄 License & Credits

**License:** MIT License
**Author:** YS (Yogesh Sontakke)
**LinkedIn:** [https://www.linkedin.com/in/yogesh-sontakke](https://www.linkedin.com/in/yogesh-sontakke)

**Third-Party Libraries:**
- Spring Boot Framework
- OpenWeatherMap APIs
- Bootstrap CSS Framework
- Chart.js Library
- Font Awesome Icons

---

*This documentation provides a comprehensive overview of the Weather Detection WebApp. For specific implementation details, refer to the source code and inline comments.*
f