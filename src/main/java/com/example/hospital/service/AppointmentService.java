package com.example.hospital.service;

import com.example.hospital.dto.appointment.AppointmentRequest;
import com.example.hospital.dto.appointment.AppointmentResponse;
import com.example.hospital.model.Appointment;
import com.example.hospital.model.Doctor;
import com.example.hospital.model.User;
import com.example.hospital.model.enums.AppointmentStatus;
import com.example.hospital.repository.AppointmentRepo;
import com.example.hospital.repository.DoctorRepo;
import com.example.hospital.repository.UserRepo;
import com.example.hospital.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AppointmentService {

        private final AppointmentRepo appointmentRepo;
        private final UserRepo userRepo;
        private final DoctorRepo doctorRepo;

        public AppointmentResponse bookAppointment(AppointmentRequest request) {

                User patient = userRepo.findById(request.getPatientId())
                                .orElseThrow(() -> new RuntimeException("Patient not found"));

                Doctor doctor = doctorRepo.findById(request.getDoctorId())
                                .orElseThrow(() -> new RuntimeException("Doctor not found"));

                Appointment appointment = Appointment.builder()
                                .patient(patient)
                                .doctor(doctor)
                                .appointmentDate(request.getAppointmentDate())
                                .appointmentTime(request.getAppointmentTime())
                                .status(AppointmentStatus.BOOKED)
                                .build();

                appointmentRepo.save(appointment);
                return new AppointmentResponse("Appointment booked successfully");
        }

        public AppointmentResponse getAppointmentById(Long id) {
                Appointment a = appointmentRepo.findById(id)
                                .orElseThrow(() -> new RuntimeException("Appointment not found"));
                return new AppointmentResponse(
                                "ID: " + a.getId() + ", Patient: " + a.getPatient().getName() +
                                                ", Doctor: " + a.getDoctor().getUser().getName() +
                                                ", Date: " + a.getAppointmentDate() +
                                                ", Status: " + a.getStatus());
        }

        public Page<AppointmentResponse> getAllAppointments(Pageable pageable) {
                return appointmentRepo.findAll(pageable)
                                .map(a -> new AppointmentResponse(
                                                "ID: " + a.getId() + ", Status: " + a.getStatus()));
        }

        public Page<AppointmentResponse> filterAppointments(
                        Long doctorId, Long patientId, AppointmentStatus status, Pageable pageable) {

                if (status != null) {
                        return appointmentRepo.findByStatus(status, pageable)
                                        .map(a -> new AppointmentResponse(
                                                        "ID: " + a.getId() + ", Status: " + a.getStatus()));
                }
                if (doctorId != null) {
                        return appointmentRepo.findByDoctorId(doctorId, pageable)
                                        .map(a -> new AppointmentResponse(
                                                        "ID: " + a.getId() + ", Doctor ID: " + a.getDoctor().getId()));
                }
                if (patientId != null) {
                        return appointmentRepo.findByPatientId(patientId, pageable)
                                        .map(a -> new AppointmentResponse("ID: " + a.getId() + ", Patient ID: "
                                                        + a.getPatient().getId()));
                }
                return getAllAppointments(pageable);
        }

        public AppointmentResponse updateAppointment(Long id, AppointmentRequest request) {

                Appointment appointment = appointmentRepo.findById(id)
                                .orElseThrow(() -> new RuntimeException("Appointment not found"));

                Doctor doctor = doctorRepo.findById(request.getDoctorId())
                                .orElseThrow(() -> new RuntimeException("Doctor not found"));

                appointment.setDoctor(doctor);
                appointment.setAppointmentDate(request.getAppointmentDate());
                appointment.setAppointmentTime(request.getAppointmentTime());

                appointmentRepo.save(appointment);
                return new AppointmentResponse("Appointment updated successfully");
        }

        public void deleteAppointment(Long id) {
                Appointment appointment = appointmentRepo.findById(id)
                                .orElseThrow(() -> new RuntimeException("Appointment not found"));
                appointmentRepo.delete(appointment);
        }

        // File upload (PRD mandatory)
        public void uploadDocument(Long appointmentId, MultipartFile file) {

                Appointment appointment = appointmentRepo.findById(appointmentId)
                                .orElseThrow(() -> new RuntimeException("Appointment not found"));

                String path = FileUtil.saveFile(file);
                appointment.setDocumentPath(path);

                appointmentRepo.save(appointment);
        }
}
