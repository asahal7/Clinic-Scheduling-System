package com.abdimaalik.clinic.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.abdimaalik.clinic.domain.AppointmentStatus;
import com.abdimaalik.clinic.dto.AppointmentDTO;
import com.abdimaalik.clinic.dto.AppointmentResponseDTO;
import com.abdimaalik.clinic.service.ClinicService;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final ClinicService clinicService;

    public AppointmentController(ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    @PostMapping
    public AppointmentResponseDTO scheduleAppointment(@RequestBody AppointmentDTO dto) {
        return clinicService.scheduleAppointment(dto);
    }

    @GetMapping("/{id}")
    public AppointmentResponseDTO getAppointmentById(@PathVariable UUID id) {
        return clinicService.getAppointmentById(id);
    }

    @GetMapping
    public Page<AppointmentResponseDTO> getAppointments(
            @RequestParam(required = false) String clinicianName,
            @RequestParam(required = false) String patientName,
            @RequestParam(required = false) AppointmentStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "startTime") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return clinicService.getAppointments(clinicianName, patientName, status, pageable);
    }

    @PatchMapping("/{id}/cancel")
    public AppointmentResponseDTO cancelAppointment(@PathVariable UUID id) {
        return clinicService.cancelAppointment(id);
    }

    @PatchMapping("/{id}/complete")
    public AppointmentResponseDTO completeAppointment(@PathVariable UUID id) {
        return clinicService.completeAppointment(id);
    }

    @PatchMapping("/{id}/no-show")
    public AppointmentResponseDTO markNoShow(@PathVariable UUID id) {
        return clinicService.markNoShow(id);
    }
}