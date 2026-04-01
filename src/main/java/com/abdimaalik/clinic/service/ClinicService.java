package com.abdimaalik.clinic.service;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.abdimaalik.clinic.domain.Appointment;
import com.abdimaalik.clinic.domain.AppointmentStatus;
import com.abdimaalik.clinic.dto.AppointmentDTO;
import com.abdimaalik.clinic.dto.AppointmentResponseDTO;
import com.abdimaalik.clinic.repository.AppointmentRepository;

@Service
public class ClinicService {

    private final AppointmentRepository appointmentRepository;

    public ClinicService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Cacheable(value = "appointments", key = "#appointmentId")
    public AppointmentResponseDTO getAppointmentById(UUID appointmentId) {
        System.out.println("Fetching appointment from database...");
        return toResponseDTO(getAppointmentOrThrow(appointmentId));
    }

    public AppointmentResponseDTO scheduleAppointment(AppointmentDTO dto) {
        validateAppointment(dto);
        validateNoOverlap(dto);

        Appointment appointment = new Appointment();
        appointment.setId(UUID.randomUUID());
        appointment.setPatientName(dto.getPatientName().trim());
        appointment.setClinicianName(dto.getClinicianName().trim());
        appointment.setStartTime(dto.getStartTime());
        appointment.setEndTime(dto.getEndTime());
        appointment.setFee(dto.getFee());
        appointment.setStatus(AppointmentStatus.CONFIRMED);

        return toResponseDTO(appointmentRepository.save(appointment));
    }

    @CachePut(value = "appointments", key = "#appointmentId")
    public AppointmentResponseDTO cancelAppointment(UUID appointmentId) {
        Appointment appointment = getAppointmentOrThrow(appointmentId);

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new IllegalStateException("Completed appointments cannot be cancelled.");
        }

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new IllegalStateException("Appointment is already cancelled.");
        }

        if (appointment.getStatus() == AppointmentStatus.NO_SHOW) {
            throw new IllegalStateException("No-show appointments cannot be cancelled.");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        return toResponseDTO(appointmentRepository.save(appointment));
    }

    @CachePut(value = "appointments", key = "#appointmentId")
    public AppointmentResponseDTO completeAppointment(UUID appointmentId) {
        Appointment appointment = getAppointmentOrThrow(appointmentId);

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new IllegalStateException("Cancelled appointments cannot be completed.");
        }

        if (appointment.getStatus() == AppointmentStatus.NO_SHOW) {
            throw new IllegalStateException("No-show appointments cannot be completed.");
        }

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new IllegalStateException("Appointment is already completed.");
        }

        appointment.setStatus(AppointmentStatus.COMPLETED);
        return toResponseDTO(appointmentRepository.save(appointment));
    }

    @CachePut(value = "appointments", key = "#appointmentId")
    public AppointmentResponseDTO markNoShow(UUID appointmentId) {
        Appointment appointment = getAppointmentOrThrow(appointmentId);

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new IllegalStateException("Cancelled appointments cannot be marked as no-show.");
        }

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new IllegalStateException("Completed appointments cannot be marked as no-show.");
        }

        if (appointment.getStatus() == AppointmentStatus.NO_SHOW) {
            throw new IllegalStateException("Appointment is already marked as no-show.");
        }

        appointment.setStatus(AppointmentStatus.NO_SHOW);
        return toResponseDTO(appointmentRepository.save(appointment));
    }

    public Page<AppointmentResponseDTO> getAppointments(String clinicianName,
                                                        String patientName,
                                                        AppointmentStatus status,
                                                        Pageable pageable) {
        Page<Appointment> appointments;

        if (clinicianName != null && !clinicianName.isBlank() && status != null) {
            appointments = appointmentRepository.findByClinicianNameContainingIgnoreCaseAndStatus(
                    clinicianName.trim(),
                    status,
                    pageable
            );
        } else if (patientName != null && !patientName.isBlank() && status != null) {
            appointments = appointmentRepository.findByPatientNameContainingIgnoreCaseAndStatus(
                    patientName.trim(),
                    status,
                    pageable
            );
        } else if (clinicianName != null && !clinicianName.isBlank()) {
            appointments = appointmentRepository.findByClinicianNameContainingIgnoreCase(
                    clinicianName.trim(),
                    pageable
            );
        } else if (patientName != null && !patientName.isBlank()) {
            appointments = appointmentRepository.findByPatientNameContainingIgnoreCase(
                    patientName.trim(),
                    pageable
            );
        } else if (status != null) {
            appointments = appointmentRepository.findByStatus(status, pageable);
        } else {
            appointments = appointmentRepository.findAll(pageable);
        }

        return appointments.map(this::toResponseDTO);
    }

    private Appointment getAppointmentOrThrow(UUID appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found: " + appointmentId));
    }

    private void validateAppointment(AppointmentDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Appointment request must not be null.");
        }

        if (dto.getPatientName() == null || dto.getPatientName().isBlank()) {
            throw new IllegalArgumentException("Patient name must not be blank.");
        }

        if (dto.getClinicianName() == null || dto.getClinicianName().isBlank()) {
            throw new IllegalArgumentException("Clinician name must not be blank.");
        }

        if (dto.getStartTime() == null) {
            throw new IllegalArgumentException("Start time must not be null.");
        }

        if (dto.getEndTime() == null) {
            throw new IllegalArgumentException("End time must not be null.");
        }

        if (!dto.getStartTime().isBefore(dto.getEndTime())) {
            throw new IllegalArgumentException("Start time must be before end time.");
        }

        if (dto.getFee() == null) {
            throw new IllegalArgumentException("Fee must not be null.");
        }

        if (dto.getFee().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Fee must be greater than zero.");
        }
    }

    private void validateNoOverlap(AppointmentDTO dto) {
        boolean overlaps =
                appointmentRepository.existsByClinicianNameAndStartTimeLessThanAndEndTimeGreaterThanAndStatusNot(
                        dto.getClinicianName().trim(),
                        dto.getEndTime(),
                        dto.getStartTime(),
                        AppointmentStatus.CANCELLED
                );

        if (overlaps) {
            throw new IllegalArgumentException(
                    "Appointment overlaps with an existing active appointment for this clinician."
            );
        }
    }

    private AppointmentResponseDTO toResponseDTO(Appointment appointment) {
        return new AppointmentResponseDTO(
                appointment.getId(),
                appointment.getPatientName(),
                appointment.getClinicianName(),
                appointment.getStartTime(),
                appointment.getEndTime(),
                appointment.getFee(),
                appointment.getStatus()
        );
    }
}