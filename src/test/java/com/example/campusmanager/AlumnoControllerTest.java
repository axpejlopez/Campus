package com.example.campusmanager;

import com.example.campusmanager.controller.AlumnoController;
import com.example.campusmanager.dto.AlumnoRequestDTO;
import com.example.campusmanager.dto.AlumnoResponseDTO;
import com.example.campusmanager.service.AlumnoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AlumnoControllerTest {

    @Mock
    private AlumnoService alumnoService;

    @InjectMocks
    private AlumnoController alumnoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListarAlumnos() {
        // Datos simulados
        AlumnoResponseDTO alumno1 = new AlumnoResponseDTO(1L, "María Gómez", "maria@example.com", LocalDate.of(2000, 5, 12));
        AlumnoResponseDTO alumno2 = new AlumnoResponseDTO(2L, "Carlos Ruiz", "carlos@example.com", LocalDate.of(1999, 8, 30));

        when(alumnoService.listarTodos()).thenReturn(Arrays.asList(alumno1, alumno2));

        // Llamada al método real del controller
        List<AlumnoResponseDTO> resultado = alumnoController.listar();

        // Comprobaciones
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("María Gómez", resultado.get(0).getNombre());
        verify(alumnoService, times(1)).listarTodos();
    }

    @Test
    void testCrearAlumno() {
        // Petición de entrada simulada
        AlumnoRequestDTO request = new AlumnoRequestDTO();
        request.setNombre("Lucía Pérez");
        request.setEmail("lucia@example.com");
        request.setFechaNacimiento(LocalDate.of(2001, 3, 22));

        // Respuesta simulada del servicio
        AlumnoResponseDTO response = new AlumnoResponseDTO(10L, "Lucía Pérez", "lucia@example.com", LocalDate.of(2001, 3, 22));

        when(alumnoService.crear(any(AlumnoRequestDTO.class))).thenReturn(response);

        // Llamada al método del controller
        Object resultado = alumnoController.crear(request).getBody();

        assertTrue(resultado instanceof AlumnoResponseDTO);
        AlumnoResponseDTO dto = (AlumnoResponseDTO) resultado;

        assertEquals("Lucía Pérez", dto.getNombre());
        assertEquals("lucia@example.com", dto.getEmail());

        verify(alumnoService, times(1)).crear(any(AlumnoRequestDTO.class));
    }
}
