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
                        <a class="nav-link" href="/">
                            <i class="fas fa-home me-1"></i>Home
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" href="/health">
                            <i class="fas fa-heartbeat me-1"></i>Health
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <!-- Main Content -->
    <main class="container my-5">
        <div class="row justify-content-center">
            <div class="col-lg-8 col-md-10">
                <!-- Health Status Card -->
                <div class="card shadow-lg border-0">
                    <div class="card-body text-center p-5">
                        <!-- Health Icon -->
                        <div class="health-icon mb-4">
                            <i class="fas fa-heartbeat fa-4x text-success"></i>
                        </div>
                        
                        <!-- Health Status -->
                        <h1 class="display-4 fw-bold text-success mb-3">
                            Application Health Check
                        </h1>
                        
                        <!-- Status Details -->
                        <div class="row mt-5">
                            <div class="col-md-6 mb-4">
                                <div class="health-status-item">
                                    <i class="fas fa-server fa-2x text-primary mb-3"></i>
                                    <h5>Service Status</h5>
                                    <p class="text-success fw-bold">${status}</p>
                                </div>
                            </div>
                            <div class="col-md-6 mb-4">
                                <div class="health-status-item">
                                    <i class="fas fa-clock fa-2x text-info mb-3"></i>
                                    <h5>Last Check</h5>
                                    <p class="text-muted">
                                        <fmt:formatDate value="${timestamp}" pattern="MMM dd, yyyy 'at' HH:mm:ss"/>
                                    </p>
                                </div>
                            </div>
                        </div>
                        
                        <!-- Service Information -->
                        <div class="mt-4">
                            <h5 class="text-primary">Service Information</h5>
                            <p class="text-muted">${service}</p>
                        </div>
                        
                        <!-- Back to Home Button -->
                        <div class="mt-5">
                            <a href="/" class="btn btn-primary btn-lg">
                                <i class="fas fa-home me-2"></i>Back to Home
                            </a>
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
</body>
</html>
