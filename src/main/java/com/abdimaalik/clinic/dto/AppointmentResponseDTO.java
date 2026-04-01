package com.abdimaalik.clinic.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.abdimaalik.clinic.domain.AppointmentStatus;

public class AppointmentResponseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;
    private String patientName;
    private String clinicianName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal fee;
    private AppointmentStatus status;

    public AppointmentResponseDTO() {
    }

    public AppointmentResponseDTO(UUID id, String patientName, String clinicianName,
                                  LocalDateTime startTime, LocalDateTime endTime,
                                  BigDecimal fee, AppointmentStatus status) {
        this.id = id;
        this.patientName = patientName;
        this.clinicianName = clinicianName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.fee = fee;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getClinicianName() {
        return clinicianName;
    }

    public void setClinicianName(String clinicianName) {
        this.clinicianName = clinicianName;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }
}