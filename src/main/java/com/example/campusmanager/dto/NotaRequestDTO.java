package com.example.campusmanager.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class NotaRequestDTO {

    @NotNull(message = "La matrícula es obligatoria")
    private Long matriculaId;

    @NotNull(message = "La asignatura es obligatoria")
    private Long asignaturaId;

    @DecimalMin(value = "0.0", message = "La nota mínima es 0.0")
    @DecimalMax(value = "10.0", message = "La nota máxima es 10.0")
    private Double notaFinal;
}


