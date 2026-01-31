package com.example.hospital.dto.analytics;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StatusCountDto {

    private String status;
    private Long count;
}