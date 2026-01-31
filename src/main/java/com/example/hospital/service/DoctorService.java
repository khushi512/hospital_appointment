package com.example.hospital.service;

import com.example.hospital.dto.doctor.DoctorRequest;
import com.example.hospital.dto.doctor.DoctorResponse;
import com.example.hospital.model.Doctor;
import com.example.hospital.model.User;
import com.example.hospital.model.enums.Role;
import com.example.hospital.repository.DoctorRepo;
import com.example.hospital.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {

        private final DoctorRepo doctorRepo;
        private final UserRepo userRepo;
        private final PasswordEncoder passwordEncoder;

        @CacheEvict(value = "doctors", allEntries = true)
        public DoctorResponse createDoctor(DoctorRequest request) {

                User user = User.builder()
                                .name(request.getName())
                                .email(request.getEmail())
                                .password(passwordEncoder.encode(request.getPassword()))
                                .role(Role.DOCTOR)
                                .build();

                userRepo.save(user);

                Doctor doctor = Doctor.builder()
                                .user(user)
                                .specialization(request.getSpecialization())
                                .experienceYears(request.getExperienceYears())
                                .consultationFee(request.getConsultationFee())
                                .build();

                doctorRepo.save(doctor);

                return new DoctorResponse(
                                doctor.getId(),
                                user.getName(),
                                doctor.getSpecialization());
        }

        @Cacheable(value = "doctors")
        public List<DoctorResponse> getAllDoctors() {
                return doctorRepo.findAll()
                                .stream()
                                .map(d -> new DoctorResponse(
                                                d.getId(),
                                                d.getUser().getName(),
                                                d.getSpecialization()))
                                .toList();
        }
}