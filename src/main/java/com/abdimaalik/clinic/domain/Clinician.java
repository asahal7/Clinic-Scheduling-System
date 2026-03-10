package com.abdimaalik.clinic.domain;

import java.util.UUID;

public class Clinician {

    private UUID id;
    private String name;
    private String specialty;

    public Clinician() {}

    public Clinician(UUID id, String name, String specialty) {
        this.id = id;
        this.name = name;
        this.specialty = specialty;
    }

    public UUID getId() { return id; }

    public String getName() { return name; }

    public String getSpecialty() { return specialty; }

    public void setId(UUID id) { this.id = id; }

    public void setName(String name) { this.name = name; }

    public void setSpecialty(String specialty) { this.specialty = specialty; }
}
