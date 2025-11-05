package com.example.campusmanager.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AsignaturaRequestDTO {

    private String titulo;
    private Long cursoId;
    private Long profesorId;
}
