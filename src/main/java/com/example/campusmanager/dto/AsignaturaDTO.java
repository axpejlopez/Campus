package com.example.campusmanager.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AsignaturaDTO {
    private Long id;
    private String titulo;
    private String curso;
    private String profesor;
}

