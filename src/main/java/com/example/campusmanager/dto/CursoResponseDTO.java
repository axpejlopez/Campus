package com.example.campusmanager.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CursoResponseDTO {
    private Long id;
    private String nombre;
    private Long profesorId;
    private String profesorNombre;
    private String profesorEspecialidad;
}

