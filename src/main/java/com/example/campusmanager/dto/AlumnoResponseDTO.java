package com.example.campusmanager.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlumnoResponseDTO {
    private Long id;
    private String nombre;
    private String email;
    private LocalDate fechaNacimiento;
}

