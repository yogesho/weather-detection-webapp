<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${pageTitle} - Weather Detection WebApp</title>
    
    <!-- Bootstrap 5 CSS - Responsive framework for modern web design -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <!-- Font Awesome - Icons for better UI -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    
         <!-- Chart.js - For weather charts and graphs -->
     <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
     
     <!-- Custom CSS for styling -->
     <link rel="stylesheet" href="/css/style.css">
     
     <!-- Custom JavaScript -->
     <script src="/js/weather.js"></script>
    
    <!-- Favicon -->
    <link rel="icon" type="image/x-icon" href="/favicon.ico">
</head>
<body class="weather-app">
    <!-- Navigation Bar -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <div class="container">
            <!-- App Logo and Title -->
            <a class="navbar-brand" href="/">
                <i class="fas fa-cloud-sun me-2"></i>
                Weather Detection WebApp
            </a>
            
            <!-- Mobile Toggle Button -->
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            
            <!-- Navigation Links -->
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="/">
                            <i class="fas fa-home me-1"></i>Home
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/health">
                            <i class="fas fa-heartbeat me-1"></i>Health
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <!-- Main Content -->
    <main class="container my-5">
        <!-- Location Header -->
        <div class="row mb-4">
            <div class="col-12">
                <div class="d-flex justify-content-between align-items-center">
                    <div>
                        <h1 class="display-5 fw-bold text-primary mb-1">
                            <i class="fas fa-map-marker-alt me-2"></i>
                            ${weather.cityName}
                        </h1>
                                                <p class="text-muted mb-0">
                            ${weather.countryCode} •
                            <fmt:formatDate value="${weather.fetchedAtAsDate}" pattern="MMM dd, yyyy 'at' HH:mm"/>
                        </p>
                    </div>
                    <div class="text-end">
                        <button class="btn btn-outline-primary btn-sm" onclick="window.location.href='/'">
                            <i class="fas fa-search me-1"></i>New Search
                        </button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Current Weather Card -->
        <div class="row mb-4">
            <div class="col-lg-8">
                <div class="card shadow-lg border-0 current-weather-card">
                    <div class="card-body p-4">
                        <div class="row align-items-center">
                            <!-- Weather Icon and Temperature -->
                            <div class="col-md-6 text-center">
                                <div class="weather-icon-large mb-3">
                                    <img src="${weather.weatherIconUrl}" alt="${weather.weatherCondition}" class="weather-icon-img">
                                </div>
                                <h2 class="display-3 fw-bold text-primary mb-1">
                                    <fmt:formatNumber value="${weather.temperature}" maxFractionDigits="1"/>°
                                </h2>
                                <p class="text-muted mb-0">${weather.temperatureUnit}</p>
                                <p class="text-muted small">
                                    Feels like <fmt:formatNumber value="${weather.feelsLike}" maxFractionDigits="1"/>°
                                </p>
                            </div>
                            
                            <!-- Weather Details -->
                            <div class="col-md-6">
                                <h4 class="text-capitalize mb-3">${weather.weatherCondition}</h4>
                                
                                <div class="weather-details">
                                    <div class="row">
                                        <div class="col-6 mb-2">
                                            <div class="d-flex align-items-center">
                                                <i class="fas fa-tint text-info me-2"></i>
                                                <span>Humidity: ${weather.humidity}%</span>
                                            </div>
                                        </div>
                                        <div class="col-6 mb-2">
                                            <div class="d-flex align-items-center">
                                                <i class="fas fa-wind text-warning me-2"></i>
                                                <span>Wind: <fmt:formatNumber value="${weather.windSpeed}" maxFractionDigits="1"/> m/s</span>
                                            </div>
                                        </div>
                                        <div class="col-6 mb-2">
                                            <div class="d-flex align-items-center">
                                                <i class="fas fa-compress-alt text-secondary me-2"></i>
                                                <span>Pressure: ${weather.pressure} hPa</span>
                                            </div>
                                        </div>
                                        <div class="col-6 mb-2">
                                            <div class="d-flex align-items-center">
                                                <i class="fas fa-eye text-info me-2"></i>
                                                <span>Visibility: ${weather.visibility/1000} km</span>
                                            </div>
                                        </div>
                                        <div class="col-6 mb-2">
                                            <div class="d-flex align-items-center">
                                                <i class="fas fa-cloud text-secondary me-2"></i>
                                                <span>Clouds: ${weather.cloudiness}%</span>
                                            </div>
                                        </div>
                                        <div class="col-6 mb-2">
                                            <div class="d-flex align-items-center">
                                                <i class="fas fa-compass text-warning me-2"></i>
                                                <span>Wind Dir: ${weather.windDirectionCardinal}</span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            
            <!-- AQI and Sun Info Card -->
            <div class="col-lg-4">
                <div class="card shadow-lg border-0 h-100">
                    <div class="card-body p-4">
                        <!-- Air Quality Index -->
                        <c:if test="${not empty weather.aqi}">
                            <div class="mb-4">
                                <h5 class="card-title">
                                    <i class="fas fa-leaf me-2"></i>Air Quality
                                </h5>
                                <div class="aqi-badge" style="background-color: ${weather.aqiColor}">
                                    <span class="aqi-value">${weather.aqi}</span>
                                    <span class="aqi-category">${weather.aqiCategory}</span>
                                </div>
                            </div>
                        </c:if>
                        
                        <!-- Sunrise and Sunset -->
                        <div class="mb-4">
                            <h5 class="card-title">
                                <i class="fas fa-sun me-2"></i>Sun Info
                            </h5>
                            <div class="sun-info">
                                <div class="d-flex justify-content-between mb-2">
                                    <span><i class="fas fa-sunrise text-warning me-2"></i>Sunrise</span>
                                    <span>
                                        <fmt:formatDate value="${weather.sunriseAsDate}" pattern="HH:mm" timeZone="GMT"/>
                                    </span>
                                </div>
                                <div class="d-flex justify-content-between">
                                    <span><i class="fas fa-sunset text-orange me-2"></i>Sunset</span>
                                    <span>
                                        <fmt:formatDate value="${weather.sunsetAsDate}" pattern="HH:mm" timeZone="GMT"/>
                                    </span>
                                </div>
                            </div>
                        </div>
                        
                        <!-- Coordinates -->
                        <div>
                            <h5 class="card-title">
                                <i class="fas fa-map me-2"></i>Location
                            </h5>
                            <div class="coordinates">
                                <div class="d-flex justify-content-between mb-1">
                                    <span>Latitude:</span>
                                    <span>${weather.latitude}°</span>
                                </div>
                                <div class="d-flex justify-content-between">
                                    <span>Longitude:</span>
                                    <span>${weather.longitude}°</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Hourly Forecast Chart -->
        <c:if test="${not empty weather.hourlyForecast}">
            <div class="row mb-4">
                <div class="col-12">
                    <div class="card shadow-lg border-0">
                        <div class="card-header bg-light">
                            <h5 class="mb-0">
                                <i class="fas fa-chart-line me-2"></i>
                                24-Hour Temperature Forecast
                            </h5>
                        </div>
                        <div class="card-body">
                            <canvas id="hourlyChart" width="400" height="200"></canvas>
                        </div>
                    </div>
                </div>
            </div>
        </c:if>

                 <!-- Daily Forecast Grid -->
         <c:if test="${not empty weather.dailyForecast}">
             <!-- Debug info: Daily forecast count: ${weather.dailyForecast.size()} -->
             <div class="row mb-4">
                 <div class="col-12">
                     <div class="card shadow-lg border-0">
                         <div class="card-header bg-light">
                             <h5 class="mb-0">
                                 <i class="fas fa-calendar-alt me-2"></i>
                                 7-Day Forecast
                             </h5>
                         </div>
                         <div class="card-body">
                             <div class="row">
                                 <c:forEach var="day" items="${weather.dailyForecast}" varStatus="status">
                                     <div class="col-md-6 col-lg-3 mb-3">
                                         <div class="forecast-day-card text-center p-3 border rounded" 
                                              style="cursor: pointer;" 
                                              onclick="showDayDetails(${status.index})"
                                              data-day-index="${status.index}">
                                             <h6 class="fw-bold">
                                                 <fmt:formatDate value="${day.dateAsDate}" pattern="EEE, MMM dd"/>
                                             </h6>
                                             <div class="weather-icon-small mb-2">
                                                 <img src="https://openweathermap.org/img/wn/${day.weatherIcon != null ? day.weatherIcon : '01d'}.png" 
                                                      alt="${day.weatherCondition != null ? day.weatherCondition : 'Unknown'}" class="weather-icon-small-img">
                                             </div>
                                             <p class="text-capitalize small text-muted mb-2">${day.weatherCondition != null ? day.weatherCondition : 'Unknown'}</p>
                                             <div class="temperature-range">
                                                 <span class="text-danger fw-bold">
                                                     <fmt:formatNumber value="${day.maxTemperature != null ? day.maxTemperature : 0}" maxFractionDigits="0"/>°
                                                 </span>
                                                 <span class="text-muted mx-1">/</span>
                                                 <span class="text-primary fw-bold">
                                                     <fmt:formatNumber value="${day.minTemperature != null ? day.minTemperature : 0}" maxFractionDigits="0"/>°
                                                 </span>
                                             </div>
                                             <small class="text-muted">Click for details</small>
                                         </div>
                                     </div>
                                 </c:forEach>
                             </div>
                         </div>
                     </div>
                 </div>
             </div>
         </c:if>
 
         <!-- Day Details Modal -->
         <div class="modal fade" id="dayDetailsModal" tabindex="-1" aria-labelledby="dayDetailsModalLabel" aria-hidden="true">
             <div class="modal-dialog modal-lg">
                 <div class="modal-content">
                     <div class="modal-header bg-primary text-white">
                         <h5 class="modal-title" id="dayDetailsModalLabel">
                             <i class="fas fa-calendar-day me-2"></i>
                             <span id="modalDayTitle">Day Forecast Details</span>
                         </h5>
                         <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                     </div>
                     <div class="modal-body">
                         <div class="row">
                             <!-- Weather Icon and Temperature -->
                             <div class="col-md-4 text-center">
                                 <div class="weather-icon-large mb-3">
                                     <img id="modalWeatherIcon" src="" alt="" class="weather-icon-img">
                                 </div>
                                 <h2 class="display-4 fw-bold text-primary mb-1">
                                     <span id="modalMaxTemp"></span>° / <span id="modalMinTemp"></span>°
                                 </h2>
                                 <p class="text-capitalize text-muted mb-2" id="modalWeatherCondition"></p>
                             </div>
                             
                             <!-- Detailed Weather Information -->
                             <div class="col-md-8">
                                 <div class="row">
                                     <div class="col-6 mb-3">
                                         <div class="d-flex align-items-center">
                                             <i class="fas fa-tint text-info me-2"></i>
                                             <div>
                                                 <small class="text-muted">Humidity</small>
                                                 <div class="fw-bold" id="modalHumidity"></div>
                                             </div>
                                         </div>
                                     </div>
                                     <div class="col-6 mb-3">
                                         <div class="d-flex align-items-center">
                                             <i class="fas fa-wind text-warning me-2"></i>
                                             <div>
                                                 <small class="text-muted">Wind Speed</small>
                                                 <div class="fw-bold" id="modalWindSpeed"></div>
                                             </div>
                                         </div>
                                     </div>
                                     <div class="col-6 mb-3">
                                         <div class="d-flex align-items-center">
                                             <i class="fas fa-cloud-rain text-info me-2"></i>
                                             <div>
                                                 <small class="text-muted">Precipitation</small>
                                                 <div class="fw-bold" id="modalPrecipitation"></div>
                                             </div>
                                         </div>
                                     </div>
                                     <div class="col-6 mb-3">
                                         <div class="d-flex align-items-center">
                                             <i class="fas fa-thermometer-half text-danger me-2"></i>
                                             <div>
                                                 <small class="text-muted">Temperature Range</small>
                                                 <div class="fw-bold">
                                                     <span id="modalTempRange"></span>
                                                 </div>
                                             </div>
                                         </div>
                                     </div>
                                 </div>
                                 
                                 <!-- Additional Details -->
                                 <div class="mt-4">
                                     <h6 class="text-primary mb-3">
                                         <i class="fas fa-info-circle me-2"></i>Weather Summary
                                     </h6>
                                     <p class="text-muted" id="modalWeatherSummary"></p>
                                 </div>
                             </div>
                         </div>
                     </div>
                     <div class="modal-footer">
                         <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                     </div>
                 </div>
             </div>
         </div>
 
         <!-- Search History Section -->
        <c:if test="${not empty searchHistory and searchHistory.size() > 0}">
            <div class="row">
                <div class="col-12">
                    <div class="card shadow border-0">
                        <div class="card-header bg-light">
                            <h5 class="mb-0">
                                <i class="fas fa-history me-2"></i>
                                Recent Searches
                            </h5>
                        </div>
                        <div class="card-body">
                            <div class="row">
                                <c:forEach var="city" items="${searchHistory}" varStatus="status">
                                    <div class="col-md-6 col-lg-4 mb-2">
                                        <a href="/weather?city=${city}" class="btn btn-outline-primary btn-sm w-100">
                                            <i class="fas fa-map-marker-alt me-1"></i>
                                            ${city}
                                        </a>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </c:if>
    </main>

    <!-- Footer -->
    <footer class="bg-dark text-light py-4 mt-5">
        <div class="container">
            <div class="row">
                <div class="col-md-6">
                    <h6>Weather Detection WebApp</h6>
                    <p class="mb-0">Powered by OpenWeatherMap API</p>
                </div>
                <div class="col-md-6 text-md-end">
                    <p class="mb-0">
                        <i class="fas fa-code me-1"></i>
                        Built with Spring Boot + JSP + Bootstrap
                    </p>
                </div>
            </div>
            <div class="row mt-3">
                <div class="col-12 text-center">
                    <p class="mb-0">
                        <small>
                            All rights reserved © 2025 
                            <a href="https://www.linkedin.com/in/yogesh-sontakke" target="_blank" class="text-light text-decoration-none fw-bold">
                                <i class="fab fa-linkedin me-1"></i>YS
                            </a>
                        </small>
                    </p>
                </div>
            </div>
        </div>
    </footer>

    <!-- Bootstrap 5 JavaScript Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    
    <!-- Chart.js Configuration -->
    <c:if test="${not empty weather.hourlyForecast}">
        <script>
            // Chart.js configuration for hourly temperature forecast
            const ctx = document.getElementById('hourlyChart').getContext('2d');
            
            // Prepare data for the chart
            const labels = [
                <c:forEach var="hour" items="${weather.hourlyForecast}" varStatus="status">
                    '<fmt:formatDate value="${hour.timeAsDate}" pattern="HH:mm"/>'<c:if test="${!status.last}">,</c:if>
                </c:forEach>
            ];
            
            const temperatures = [
                <c:forEach var="hour" items="${weather.hourlyForecast}" varStatus="status">
                    ${hour.temperature}<c:if test="${!status.last}">,</c:if>
                </c:forEach>
            ];
            
            // Create the chart
            new Chart(ctx, {
                type: 'line',
                data: {
                    labels: labels,
                    datasets: [{
                        label: 'Temperature (°${weather.temperatureUnit})',
                        data: temperatures,
                        borderColor: 'rgb(75, 192, 192)',
                        backgroundColor: 'rgba(75, 192, 192, 0.2)',
                        tension: 0.1,
                        fill: true
                    }]
                },
                options: {
                    responsive: true,
                    plugins: {
                        legend: {
                            position: 'top',
                        },
                        title: {
                            display: true,
                            text: '24-Hour Temperature Forecast'
                        }
                    },
                    scales: {
                        y: {
                            beginAtZero: false,
                            title: {
                                display: true,
                                text: 'Temperature (°${weather.temperatureUnit})'
                            }
                        },
                        x: {
                            title: {
                                display: true,
                                text: 'Time'
                            }
                        }
                    }
                }
            });
        </script>
    </c:if>
    
                                       <!-- Initialize Daily Forecast Data -->
       <script>
           // Initialize daily forecast data for the external JavaScript file
           const forecastData = [
               <c:forEach var="day" items="${weather.dailyForecast}" varStatus="status">
                   {
                       date: '<fmt:formatDate value="${day.dateAsDate}" pattern="EEE, MMM dd"/>',
                       maxTemperature: ${day.maxTemperature != null ? day.maxTemperature : 0},
                       minTemperature: ${day.minTemperature != null ? day.minTemperature : 0},
                       weatherCondition: '${day.weatherCondition != null ? day.weatherCondition : "Unknown"}',
                       weatherIcon: '${day.weatherIcon != null ? day.weatherIcon : "01d"}',
                       humidity: ${day.humidity != null ? day.humidity : 0},
                       windSpeed: ${day.windSpeed != null ? day.windSpeed : 0},
                       precipitation: ${day.precipitation != null ? day.precipitation : 0}
                   }<c:if test="${!status.last}">,</c:if>
               </c:forEach>
           ];
           
           console.log('JSP: Forecast data prepared:', forecastData);
           
           // Initialize the external JavaScript with the data
           document.addEventListener('DOMContentLoaded', function() {
               console.log('JSP: DOM loaded, initializing with data...');
               if (typeof initializeDailyForecastData === 'function') {
                   console.log('JSP: Calling initializeDailyForecastData with', forecastData.length, 'items');
                   initializeDailyForecastData(forecastData);
               } else {
                   console.error('JSP: initializeDailyForecastData function not found');
               }
           });
       </script>
</body>
</html>
