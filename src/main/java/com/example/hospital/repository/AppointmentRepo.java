package com.example.hospital.repository;

import com.example.hospital.model.Appointment;
import com.example.hospital.model.enums.AppointmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AppointmentRepo extends JpaRepository<Appointment, Long> {

    // ------------------------------------------------
    // Pagination + Filtering
    // ------------------------------------------------

    // Patient-wise appointments (paginated & sortable)
    Page<Appointment> findByPatientId(
            Long patientId,
            Pageable pageable
    );

    // Doctor-wise appointments (paginated & sortable)
    Page<Appointment> findByDoctorId(
            Long doctorId,
            Pageable pageable
    );

    // Status-based filtering with pagination
    Page<Appointment> findByStatus(
            AppointmentStatus status,
            Pageable pageable
    );

    // ------------------------------------------------
    // Analytics / Complex Queries
    // ------------------------------------------------

    // Status-wise appointment count
    @Query("""
        SELECT a.status, COUNT(a.id)
        FROM Appointment a
        GROUP BY a.status
    """)
    List<Object[]> countAppointmentsByStatus();

    // Doctor-wise appointment count
    @Query("""
        SELECT a.doctor.id, COUNT(a.id)
        FROM Appointment a
        GROUP BY a.doctor.id
    """)
    List<Object[]> countAppointmentsByDoctor();

    // Daily appointment count
    @Query("""
        SELECT a.appointmentDate, COUNT(a.id)
        FROM Appointment a
        GROUP BY a.appointmentDate
        ORDER BY a.appointmentDate
    """)
    List<Object[]> countAppointmentsPerDay();
}