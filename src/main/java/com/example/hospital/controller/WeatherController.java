package com.example.hospital.controller;

import com.example.hospital.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * External API Integration Controller (PRD Requirement)
 * Provides weather information for hospital location
 */
@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    /**
     * Get weather for hospital's default location
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getHospitalWeather() {
        return ResponseEntity.ok(weatherService.getHospitalLocationWeather());
    }

    /**
     * Get weather for specific coordinates
     * Useful when hospital has multiple branches
     */
    @GetMapping("/location")
    public ResponseEntity<Map<String, Object>> getWeatherByLocation(
            @RequestParam double latitude,
            @RequestParam double longitude) {
        return ResponseEntity.ok(weatherService.getCurrentWeather(latitude, longitude));
    }
}
