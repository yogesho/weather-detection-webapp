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
    
    <!-- Custom CSS for styling -->
    <link rel="stylesheet" href="/css/style.css">
    
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
                        <a class="nav-link active" href="/">
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
        <!-- Hero Section -->
        <div class="row justify-content-center">
            <div class="col-lg-8 col-md-10">
                <!-- Welcome Card -->
                <div class="card shadow-lg border-0">
                    <div class="card-body text-center p-5">
                        <!-- Weather Icon -->
                        <div class="weather-icon mb-4">
                            <i class="fas fa-cloud-sun fa-4x text-primary"></i>
                        </div>
                        
                        <!-- Welcome Message -->
                        <h1 class="display-4 fw-bold text-primary mb-3">
                            Welcome to Weather Detection
                        </h1>
                        
                        <p class="lead text-muted mb-4">
                            Get real-time weather information, forecasts, and air quality data for any city worldwide.
                        </p>
                        
                        <!-- Search Form -->
                        <form action="/weather" method="GET" class="search-form">
                            <div class="input-group input-group-lg mb-3">
                                <!-- Search Input -->
                                <input type="text" 
                                       class="form-control" 
                                       name="city" 
                                       placeholder="Enter city name (e.g., London, New York, Tokyo)"
                                       value="${param.city}"
                                       required
                                       autocomplete="off">
                                
                                <!-- Search Button -->
                                <button class="btn btn-primary" type="submit">
                                    <i class="fas fa-search me-2"></i>Search Weather
                                </button>
                            </div>
                        </form>
                        
                        <!-- Error Message Display -->
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger alert-dismissible fade show mt-3" role="alert">
                                <i class="fas fa-exclamation-triangle me-2"></i>
                                <strong>City not found!</strong><br>
                                ${error}<br>
                                <small class="text-muted">
                                    💡 <strong>Tips:</strong> Try adding country code (e.g., "Nanded,IN" or "Nanded, India") 
                                    or check spelling. For Indian cities, try: "Mumbai, India", "Delhi, India", "Pune, India".
                                    <br>💡 <strong>Auto-suggestions:</strong> Type "nanded" and we'll suggest "Nanded,IN" automatically!
                                </small>
                                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                            </div>
                        </c:if>
                        
                        <!-- Features List -->
                        <div class="row mt-5">
                            <div class="col-md-4">
                                <div class="feature-item text-center">
                                    <i class="fas fa-thermometer-half fa-2x text-info mb-3"></i>
                                    <h5>Current Weather</h5>
                                    <p class="text-muted">Real-time temperature, humidity, and conditions</p>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="feature-item text-center">
                                    <i class="fas fa-chart-line fa-2x text-success mb-3"></i>
                                    <h5>Forecasts</h5>
                                    <p class="text-muted">Hourly and 7-day weather predictions</p>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="feature-item text-center">
                                    <i class="fas fa-wind fa-2x text-warning mb-3"></i>
                                    <h5>Air Quality</h5>
                                    <p class="text-muted">AQI data with health recommendations</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Search History Section -->
        <c:if test="${not empty searchHistory and searchHistory.size() > 0}">
            <div class="row justify-content-center mt-5">
                <div class="col-lg-8 col-md-10">
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

        <!-- Popular Cities Section -->
        <div class="row justify-content-center mt-5">
            <div class="col-lg-8 col-md-10">
                <div class="card shadow border-0">
                    <div class="card-header bg-light">
                        <h5 class="mb-0">
                            <i class="fas fa-star me-2"></i>
                            Popular Cities
                        </h5>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-md-6 col-lg-3 mb-2">
                                <a href="/weather?city=Mumbai, India" class="btn btn-outline-secondary btn-sm w-100">
                                    <i class="fas fa-building me-1"></i>Mumbai
                                </a>
                            </div>
                            <div class="col-md-6 col-lg-3 mb-2">
                                <a href="/weather?city=Delhi, India" class="btn btn-outline-secondary btn-sm w-100">
                                    <i class="fas fa-landmark me-1"></i>Delhi
                                </a>
                            </div>
                            <div class="col-md-6 col-lg-3 mb-2">
                                <a href="/weather?city=Pune, India" class="btn btn-outline-secondary btn-sm w-100">
                                    <i class="fas fa-mountain me-1"></i>Pune
                                </a>
                            </div>
                            <div class="col-md-6 col-lg-3 mb-2">
                                <a href="/weather?city=Bangalore, India" class="btn btn-outline-secondary btn-sm w-100">
                                    <i class="fas fa-laptop me-1"></i>Bangalore
                                </a>
                            </div>
                            <div class="col-md-6 col-lg-3 mb-2">
                                <a href="/weather?city=London" class="btn btn-outline-secondary btn-sm w-100">
                                    <i class="fas fa-umbrella me-1"></i>London
                                </a>
                            </div>
                            <div class="col-md-6 col-lg-3 mb-2">
                                <a href="/weather?city=New York" class="btn btn-outline-secondary btn-sm w-100">
                                    <i class="fas fa-building me-1"></i>New York
                                </a>
                            </div>
                            <div class="col-md-6 col-lg-3 mb-2">
                                <a href="/weather?city=Tokyo" class="btn btn-outline-secondary btn-sm w-100">
                                    <i class="fas fa-torii-gate me-1"></i>Tokyo
                                </a>
                            </div>
                            <div class="col-md-6 col-lg-3 mb-2">
                                <a href="/weather?city=Paris" class="btn btn-outline-secondary btn-sm w-100">
                                    <i class="fas fa-eiffel-tower me-1"></i>Paris
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
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
    
    <!-- Custom JavaScript -->
    <script>
        // Auto-focus on search input when page loads
        document.addEventListener('DOMContentLoaded', function() {
            const searchInput = document.querySelector('input[name="city"]');
            if (searchInput) {
                searchInput.focus();
            }
        });
        
        // Form validation and auto-suggestions
        document.querySelector('.search-form').addEventListener('submit', function(e) {
            const cityInput = document.querySelector('input[name="city"]');
            const cityValue = cityInput.value.trim();
            
            if (!cityValue) {
                e.preventDefault();
                alert('Please enter a city name');
                cityInput.focus();
                return;
            }
            
            // Auto-suggestions for common Indian cities
            const suggestions = {
                'nanded': 'Nanded,IN',
                'mumbai': 'Mumbai, India',
                'delhi': 'Delhi, India',
                'pune': 'Pune, India',
                'bangalore': 'Bangalore, India',
                'chennai': 'Chennai, India',
                'kolkata': 'Kolkata, India',
                'hyderabad': 'Hyderabad, India'
            };
            
            const lowerCity = cityValue.toLowerCase();
            if (suggestions[lowerCity] && !cityValue.includes(',')) {
                const useSuggestion = confirm(`Did you mean "${suggestions[lowerCity]}"? Click OK to use the suggested format.`);
                if (useSuggestion) {
                    cityInput.value = suggestions[lowerCity];
                }
            }
        });
        
        // Real-time suggestions as user types
        document.querySelector('input[name="city"]').addEventListener('input', function(e) {
            const cityValue = e.target.value.toLowerCase();
            const suggestions = {
                'nanded': 'Nanded,IN',
                'mumbai': 'Mumbai, India',
                'delhi': 'Delhi, India',
                'pune': 'Pune, India',
                'bangalore': 'Bangalore, India',
                'chennai': 'Chennai, India',
                'kolkata': 'Kolkata, India',
                'hyderabad': 'Hyderabad, India'
            };
            
            // Show suggestion if user types a known city without country code
            if (suggestions[cityValue] && !e.target.value.includes(',')) {
                // You could add a visual suggestion here
                console.log('Suggestion:', suggestions[cityValue]);
            }
        });
    </script>
</body>
</html>
