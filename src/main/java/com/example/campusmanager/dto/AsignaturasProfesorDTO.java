package com.example.campusmanager.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AsignaturasProfesorDTO {
    private Long profesorId;
    private String nombreProfesor;
    private String tituloAsignatura;
    private String curso;
}

