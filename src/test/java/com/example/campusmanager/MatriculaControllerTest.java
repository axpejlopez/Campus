package com.example.campusmanager;

import com.example.campusmanager.controller.MatriculaController;
import com.example.campusmanager.domain.Matricula;
import com.example.campusmanager.service.MatriculaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatriculaControllerTest {

    @Mock
    private MatriculaService matriculaService;

    @InjectMocks
    private MatriculaController matriculaController;

    private Matricula matricula;

    @BeforeEach
    void setUp() {
        matricula = new Matricula();
        matricula.setId(10L);
        matricula.setFechaMatricula(LocalDate.now());
    }

    // ✅ Test de creación de matrícula (OK)
   
	@Test
    void testMatricularAlumnoEnCurso_OK() {
        when(matriculaService.crear(1L, 2L, LocalDate.now())).thenReturn(matricula);

        ResponseEntity<?> response = matriculaController.matricularAlumnoEnCurso(1L, 2L);

        assertEquals(201, response.getStatusCode().value());

        assertTrue(response.getBody() instanceof Matricula);
        verify(matriculaService, times(1)).crear(eq(1L), eq(2L), any(LocalDate.class));
    }

    // ❌ Test cuando el alumno ya está matriculado
    @Test
    void testMatricularAlumnoEnCurso_KO() {
        when(matriculaService.crear(1L, 2L, LocalDate.now()))
                .thenThrow(new IllegalArgumentException("El alumno ya está matriculado en ese curso."));

        ResponseEntity<?> response = matriculaController.matricularAlumnoEnCurso(1L, 2L);

        assertEquals(400, response.getStatusCode().value());
        assertTrue(((Map<?, ?>) response.getBody()).containsKey("error"));
        assertTrue(((Map<?, ?>) response.getBody()).get("error").toString().contains("matriculado"));
        verify(matriculaService, times(1)).crear(eq(1L), eq(2L), any(LocalDate.class));
    }
}

