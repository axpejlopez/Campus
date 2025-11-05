package com.example.campusmanager.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class AlumnoRequestDTO {
    private String nombre;
    private String email;
    private LocalDate fechaNacimiento;
}
