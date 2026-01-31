package com.example.hospital.dto.doctor;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DoctorResponse {

    private Long doctorId;
    private String name;
    private String specialization;
}