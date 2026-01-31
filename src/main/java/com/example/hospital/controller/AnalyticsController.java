package com.example.hospital.controller;

import com.example.hospital.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    // Status-wise appointment count
    @GetMapping("/status-count")
    public ResponseEntity<List<Object[]>> statusWiseCount() {
        return ResponseEntity.ok(
                analyticsService.getStatusWiseCount()
        );
    }
    // Doctor-wise appointment count
    @GetMapping("/doctor-count")
    public ResponseEntity<List<Object[]>> doctorWiseCount() {
        return ResponseEntity.ok(
                analyticsService.getDoctorWiseCount()
        );
    }
    // Daily appointment count
    @GetMapping("/daily")
    public ResponseEntity<List<Object[]>> dailyAppointments() {
        return ResponseEntity.ok(
                analyticsService.getDailyAppointments()
        );
    }
}