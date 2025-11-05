package com.example.campusmanager.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class MatriculaRequestDTO {
    private Long alumnoId;
    private Long cursoId;
    private LocalDate fechaMatricula;
}
