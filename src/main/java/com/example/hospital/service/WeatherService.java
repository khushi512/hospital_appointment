package com.example.hospital.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * External Weather API Integration (PRD Requirement)
 * Uses Open-Meteo API (free, no API key required)
 */
@Service
@RequiredArgsConstructor
public class WeatherService {

    private static final String WEATHER_API_URL = "https://api.open-meteo.com/v1/forecast?latitude={lat}&longitude={lon}&current_weather=true";

    private final RestTemplate restTemplate;

    /**
     * Get current weather for hospital location
     * Useful for patients planning their appointments
     */
    public Map<String, Object> getCurrentWeather(double latitude, double longitude) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.getForObject(
                    WEATHER_API_URL,
                    Map.class,
                    latitude,
                    longitude);
            return response;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch weather data: " + e.getMessage());
        }
    }

    /**
     * Get weather for default hospital location (Mumbai, India)
     */
    public Map<String, Object> getHospitalLocationWeather() {
        // Default: Mumbai coordinates
        return getCurrentWeather(19.0760, 72.8777);
    }
}
