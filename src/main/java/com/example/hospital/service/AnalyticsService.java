package com.example.hospital.service;

import com.example.hospital.repository.AppointmentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final AppointmentRepo appointmentRepo;

    public List<Object[]> getStatusWiseCount() {
        return appointmentRepo.countAppointmentsByStatus();
    }

    public List<Object[]> getDoctorWiseCount() {
        return appointmentRepo.countAppointmentsByDoctor();
    }

    public List<Object[]> getDailyAppointments() {
        return appointmentRepo.countAppointmentsPerDay();
    }
}