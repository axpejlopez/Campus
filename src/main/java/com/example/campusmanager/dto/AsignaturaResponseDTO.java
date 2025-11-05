package com.example.campusmanager.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AsignaturaResponseDTO {
    private Long id;
    private String titulo;
    private String profesorNombre;
    private String profesorEspecialidad;
    private String cursoNombre;
}
