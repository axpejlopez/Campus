package com.example.campusmanager;

import com.example.campusmanager.domain.Alumno;
import com.example.campusmanager.dto.AlumnoRequestDTO;
import com.example.campusmanager.dto.AlumnoResponseDTO;
import com.example.campusmanager.repository.AlumnoRepository;
import com.example.campusmanager.service.AlumnoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AlumnoServiceTest {

    @Mock
    private AlumnoRepository alumnoRepository;

    @InjectMocks
    private AlumnoService alumnoService;

    private Alumno alumno;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        alumno = new Alumno();
        alumno.setId(1L);
        alumno.setNombre("María Gómez");
        alumno.setEmail("maria@example.com");
        alumno.setFechaNacimiento(LocalDate.of(2000, 5, 12));
    }

    @Test
    void testListarTodos() {
        when(alumnoRepository.findAll()).thenReturn(Arrays.asList(alumno));

        List<AlumnoResponseDTO> resultado = alumnoService.listarTodos();

        assertEquals(1, resultado.size());
        assertEquals("María Gómez", resultado.get(0).getNombre());
        verify(alumnoRepository, times(1)).findAll();
    }

    @Test
    void testObtenerPorId() {
        when(alumnoRepository.findById(1L)).thenReturn(Optional.of(alumno));

        AlumnoResponseDTO resultado = alumnoService.obtenerPorId(1L);

        assertEquals("María Gómez", resultado.getNombre());
        assertEquals("maria@example.com", resultado.getEmail());
        verify(alumnoRepository, times(1)).findById(1L);
    }

    @Test
    void testCrearAlumno() {
        AlumnoRequestDTO dto = new AlumnoRequestDTO();
        dto.setNombre("Carlos Ruiz");
        dto.setEmail("carlos@example.com");
        dto.setFechaNacimiento(LocalDate.of(1999, 8, 30));

        when(alumnoRepository.save(any(Alumno.class))).thenReturn(alumno);

        AlumnoResponseDTO creado = alumnoService.crear(dto);

        assertNotNull(creado);
        verify(alumnoRepository, times(1)).save(any(Alumno.class));
    }
}
