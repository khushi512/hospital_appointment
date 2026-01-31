package com.example.hospital.controller;

import com.example.hospital.dto.doctor.DoctorRequest;
import com.example.hospital.dto.doctor.DoctorResponse;
import com.example.hospital.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping
    public ResponseEntity<DoctorResponse> createDoctor(
            @Valid @RequestBody DoctorRequest request) {

        return ResponseEntity.ok(
                doctorService.createDoctor(request)
        );
    }

    @GetMapping
    public ResponseEntity<List<DoctorResponse>> getAllDoctors() {
        return ResponseEntity.ok(
                doctorService.getAllDoctors()
        );
    }
}