package com.abdimaalik.clinic.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abdimaalik.clinic.domain.Appointment;
import com.abdimaalik.clinic.service.ClinicService;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final ClinicService clinicService;

    public AppointmentController(ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    @PostMapping
    public Appointment createAppointment(@RequestBody Appointment appointment) {
        return clinicService.createAppointment(appointment);
    }

    @GetMapping
    public List<Appointment> getAppointments() {
        return clinicService.getAppointments();
    }

    @DeleteMapping("/{id}")
    public void cancelAppointment(@PathVariable UUID id) {
        clinicService.cancelAppointment(id);
    }

}

