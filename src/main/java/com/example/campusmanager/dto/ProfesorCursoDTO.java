package com.example.campusmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfesorCursoDTO {
    private Long id;
    private String nombre;
    private String especialidad;
    private String asignaturas; // nombres de las asignaturas que imparte dentro del curso
}
