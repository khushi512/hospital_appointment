package com.example.hospital.dto.analytics;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class DailyAppointmentStatsDto {

    private LocalDate date;
    private Long count;
}