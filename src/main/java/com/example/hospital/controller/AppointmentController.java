package com.example.hospital.controller;

import com.example.hospital.dto.appointment.AppointmentRequest;
import com.example.hospital.dto.appointment.AppointmentResponse;
import com.example.hospital.model.enums.AppointmentStatus;
import com.example.hospital.service.AppointmentService;
import io.github.bucket4j.Bucket;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final Bucket bucket;

    // Create appointment (with rate limiting)
    @PostMapping
    public ResponseEntity<?> bookAppointment(
            @Valid @RequestBody AppointmentRequest request) {

        if (!bucket.tryConsume(1)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("Rate limit exceeded. Please try again later.");
        }

        return ResponseEntity.ok(
                appointmentService.bookAppointment(request));
    }

    // Get appointment by ID
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponse> getAppointmentById(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.getAppointmentById(id));
    }

    // List appointments (pagination + sorting)
    @GetMapping
    public ResponseEntity<Page<AppointmentResponse>> getAllAppointments(
            Pageable pageable) {

        return ResponseEntity.ok(
                appointmentService.getAllAppointments(pageable));
    }

    // Filter appointments by status, doctor, or patient
    @GetMapping("/filter")
    public ResponseEntity<Page<AppointmentResponse>> filterAppointments(
            @RequestParam(required = false) Long doctorId,
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) AppointmentStatus status,
            Pageable pageable) {

        return ResponseEntity.ok(
                appointmentService.filterAppointments(doctorId, patientId, status, pageable));
    }

    // Update appointment
    @PutMapping("/{id}")
    public ResponseEntity<AppointmentResponse> updateAppointment(
            @PathVariable Long id,
            @Valid @RequestBody AppointmentRequest request) {

        return ResponseEntity.ok(
                appointmentService.updateAppointment(id, request));
    }

    // Delete appointment
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.ok("Appointment deleted successfully");
    }

    // Upload document (file upload requirement)
    @PostMapping("/{appointmentId}/upload")
    public ResponseEntity<String> uploadDocument(
            @PathVariable Long appointmentId,
            @RequestParam MultipartFile file) {

        appointmentService.uploadDocument(appointmentId, file);
        return ResponseEntity.ok("File uploaded successfully");
    }
}
