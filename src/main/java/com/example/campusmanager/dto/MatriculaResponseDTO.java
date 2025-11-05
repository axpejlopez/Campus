package com.example.campusmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatriculaResponseDTO {
    private Long id;
    private String alumnoNombre;
    private String cursoNombre;
    private LocalDate fechaMatricula;
}

