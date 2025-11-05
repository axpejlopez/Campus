package com.example.campusmanager.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CursoDTO {
    private Long id;
    private String nombre;
}

