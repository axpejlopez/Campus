package com.example.campusmanager.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotaResponseDTO {
    private Long id;
    private String alumno;
    private String curso;
    private String asignatura;
    private String profesor;
    private Double notaFinal;
}

