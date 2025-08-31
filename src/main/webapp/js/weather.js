// Weather WebApp JavaScript Functions
// This file contains all the JavaScript functionality for the weather application

// Global variable to store daily forecast data
let dailyForecastData = [];

// Function to initialize daily forecast data
function initializeDailyForecastData(data) {
    console.log('weather.js: initializeDailyForecastData called with:', data);
    dailyForecastData = data;
    console.log('weather.js: Daily forecast data initialized:', dailyForecastData);
    console.log('weather.js: Number of forecast days:', dailyForecastData.length);

    // Verify the data is properly set
    if (dailyForecastData && dailyForecastData.length > 0) {
        console.log('weather.js: First day data:', dailyForecastData[0]);
    } else {
        console.warn('weather.js: No forecast data received');
    }
}

// Function to show day details in modal
function showDayDetails(dayIndex) {
    console.log('showDayDetails called with index:', dayIndex);
    console.log('dailyForecastData length:', dailyForecastData.length);
    console.log('dailyForecastData:', dailyForecastData);

    if (!dailyForecastData || dailyForecastData.length === 0) {
        console.error('No daily forecast data available');
        alert('No forecast data available');
        return;
    }

    const day = dailyForecastData[dayIndex];
    console.log('Selected day:', day);
    if (!day) {
        console.error('No day data found for index:', dayIndex);
        alert('No day data found for index: ' + dayIndex);
        return;
    }

    // Check if modal exists
    const modal = document.getElementById('dayDetailsModal');
    if (!modal) {
        console.error('Modal element not found');
        alert('Modal not found!');
        return;
    }

    // Update modal title
    const modalTitle = document.getElementById('modalDayTitle');
    if (modalTitle) {
        modalTitle.textContent = day.date + ' Forecast';
    }

    // Update weather icon
    const weatherIcon = document.getElementById('modalWeatherIcon');
    if (weatherIcon) {
        weatherIcon.src = 'https://openweathermap.org/img/wn/' + day.weatherIcon + '@2x.png';
        weatherIcon.alt = day.weatherCondition;
    }

    // Update temperatures with null checks
    const maxTemp = day.maxTemperature || 0;
    const minTemp = day.minTemperature || 0;

    const modalMaxTemp = document.getElementById('modalMaxTemp');
    const modalMinTemp = document.getElementById('modalMinTemp');
    if (modalMaxTemp) modalMaxTemp.textContent = Math.round(maxTemp);
    if (modalMinTemp) modalMinTemp.textContent = Math.round(minTemp);

    // Update weather condition
    const modalWeatherCondition = document.getElementById('modalWeatherCondition');
    if (modalWeatherCondition) {
        modalWeatherCondition.textContent = day.weatherCondition || 'Unknown';
    }

    // Update detailed information with null checks
    const humidity = day.humidity || 0;
    const windSpeed = day.windSpeed || 0;
    const precipitation = day.precipitation || 0;

    const modalHumidity = document.getElementById('modalHumidity');
    const modalWindSpeed = document.getElementById('modalWindSpeed');
    const modalPrecipitation = document.getElementById('modalPrecipitation');
    const modalTempRange = document.getElementById('modalTempRange');

    if (modalHumidity) modalHumidity.textContent = humidity + '%';
    if (modalWindSpeed) modalWindSpeed.textContent = windSpeed.toFixed(1) + ' m/s';
    if (modalPrecipitation) modalPrecipitation.textContent = precipitation + ' mm';
    if (modalTempRange) {
        modalTempRange.textContent = Math.round(maxTemp) + '° / ' + Math.round(minTemp) + '°';
    }

    // Update weather summary
    const modalWeatherSummary = document.getElementById('modalWeatherSummary');
    if (modalWeatherSummary) {
        const tempRange = Math.round(maxTemp - minTemp);
        const summary = `Expect ${day.weatherCondition || 'unknown conditions'} with temperatures ranging from ${Math.round(minTemp)}° to ${Math.round(maxTemp)}° (${tempRange}° range). `;
        const windInfo = `Wind speed will be around ${windSpeed.toFixed(1)} m/s. `;
        const humidityInfo = `Humidity levels at ${humidity}%. `;
        const precipInfo = precipitation > 0 ? `There's a chance of precipitation with ${precipitation}mm expected.` : 'No precipitation expected.';

        modalWeatherSummary.textContent = summary + windInfo + humidityInfo + precipInfo;
    }
    
    // Update background based on the selected day's weather
    if (day.weatherCondition) {
        setWeatherBackground(day.weatherCondition);
    }

    // Show the modal
    try {
        const bootstrapModal = new bootstrap.Modal(modal);
        bootstrapModal.show();
        console.log('Modal shown successfully');
    } catch (error) {
        console.error('Error showing modal:', error);
        alert('Error showing modal: ' + error.message);
    }
}

// Function to set weather-specific background
function setWeatherBackground(weatherCondition) {
    const body = document.body;

    // Remove existing weather classes
    body.classList.remove('sunny', 'cloudy', 'rainy', 'snowy', 'night');

    // Convert weather condition to lowercase for easier matching
    const condition = weatherCondition.toLowerCase();

    // Set background based on weather condition
    if (condition.includes('clear') || condition.includes('sunny')) {
        body.classList.add('sunny');
    } else if (condition.includes('cloud') || condition.includes('overcast')) {
        body.classList.add('cloudy');
    } else if (condition.includes('rain') || condition.includes('drizzle') || condition.includes('shower')) {
        body.classList.add('rainy');
    } else if (condition.includes('snow') || condition.includes('sleet')) {
        body.classList.add('snowy');
    } else if (condition.includes('night') || condition.includes('clear night')) {
        body.classList.add('night');
    }

    console.log('Set weather background for condition:', weatherCondition);
}

// Auto-refresh weather data every 30 minutes
setTimeout(function () {
    window.location.reload();
}, 30 * 60 * 1000);

// Add smooth scrolling for better UX
document.addEventListener('DOMContentLoaded', function () {
    console.log('DOM loaded, initializing weather app...');

    // Set weather background based on current weather condition
    const weatherConditionElement = document.querySelector('.text-capitalize');
    if (weatherConditionElement) {
        const weatherCondition = weatherConditionElement.textContent;
        setWeatherBackground(weatherCondition);
    }

    // Smooth scroll to top when clicking "New Search"
    const newSearchButton = document.querySelector('button[onclick*="New Search"]');
    if (newSearchButton) {
        newSearchButton.addEventListener('click', function (e) {
            e.preventDefault();
            window.scrollTo({ top: 0, behavior: 'smooth' });
            setTimeout(function () {
                window.location.href = '/';
            }, 500);
        });
    }

    // Add hover effects to forecast cards
    document.querySelectorAll('.forecast-day-card').forEach(card => {
        card.addEventListener('mouseenter', function () {
            this.style.transform = 'translateY(-5px)';
            this.style.boxShadow = '0 8px 25px rgba(0,0,0,0.15)';
        });

        card.addEventListener('mouseleave', function () {
            this.style.transform = 'translateY(0)';
            this.style.boxShadow = '0 2px 10px rgba(0,0,0,0.1)';
        });
    });

    console.log('Weather app initialization complete');
});
