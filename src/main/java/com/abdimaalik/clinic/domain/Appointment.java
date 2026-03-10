package com.abdimaalik.clinic.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class Appointment {

    private UUID id;
    private UUID patientId;
    private UUID clinicianId;
    private LocalDateTime appointmentTime;

    public Appointment() {}

    public Appointment(UUID id, UUID patientId, UUID clinicianId, LocalDateTime appointmentTime) {
        this.id = id;
        this.patientId = patientId;
        this.clinicianId = clinicianId;
        this.appointmentTime = appointmentTime;
    }

    public UUID getId() { return id; }

    public UUID getPatientId() { return patientId; }

    public UUID getClinicianId() { return clinicianId; }

    public LocalDateTime getAppointmentTime() { return appointmentTime; }

    public void setId(UUID id) { this.id = id; }

    public void setPatientId(UUID patientId) { this.patientId = patientId; }

    public void setClinicianId(UUID clinicianId) { this.clinicianId = clinicianId; }

    public void setAppointmentTime(LocalDateTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }
}
