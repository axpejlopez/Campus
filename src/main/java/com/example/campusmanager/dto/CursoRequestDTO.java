package com.example.campusmanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CursoRequestDTO {

    @NotBlank(message = "El nombre del curso es obligatorio.")
    private String nombre;

    @NotNull(message = "El ID del profesor es obligatorio.")
    private Long profesorId;
}
