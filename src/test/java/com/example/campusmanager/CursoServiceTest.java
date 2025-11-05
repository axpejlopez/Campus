package com.example.campusmanager;

import com.example.campusmanager.domain.Curso;
import com.example.campusmanager.domain.Profesor;
import com.example.campusmanager.dto.CursoResponseDTO;
import com.example.campusmanager.repository.CursoRepository;
import com.example.campusmanager.repository.ProfesorRepository;
import com.example.campusmanager.service.CursoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CursoServiceTest {

    @Mock
    private CursoRepository cursoRepository;

    @Mock
    private ProfesorRepository profesorRepository;

    @InjectMocks
    private CursoService cursoService;

    private Profesor profesor;
    private Curso curso;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        profesor = new Profesor();
        profesor.setId(2L);
        profesor.setNombre("Luis Pérez");
        profesor.setEspecialidad("Historia");

        curso = new Curso();
        curso.setId(8L);
        curso.setNombre("1º BACH");
    }

    @Test
    void testCrearCurso_ConProfesor_OK() {
        when(profesorRepository.findById(2L)).thenReturn(Optional.of(profesor));
        when(cursoRepository.save(any(Curso.class))).thenAnswer(invocation -> {
            Curso c = invocation.getArgument(0);
            c.setId(8L);
            return c;
        });

        CursoResponseDTO creado = cursoService.crear(curso, 2L);

        assertNotNull(creado);
        assertEquals("1º BACH", creado.getNombre());
        assertEquals("Luis Pérez", creado.getProfesorNombre());
        verify(cursoRepository, times(1)).save(any(Curso.class));
    }

    @Test
    void testCrearCurso_SinProfesor_LanzaExcepcion() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> cursoService.crear(curso, null)
        );

        assertEquals("Debe asignarse un profesor al curso.", ex.getMessage());
        verify(cursoRepository, never()).save(any(Curso.class));
    }

    @Test
    void testCrearCurso_ProfesorNoExiste_LanzaExcepcion() {
        when(profesorRepository.findById(99L)).thenReturn(Optional.empty());

        NoSuchElementException ex = assertThrows(
                NoSuchElementException.class,
                () -> cursoService.crear(curso, 99L)
        );

        assertEquals("Profesor no encontrado.", ex.getMessage());
    }

    @Test
    void testCrearCurso_SinNombre_LanzaExcepcion() {
        Curso sinNombre = new Curso();
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> cursoService.crear(sinNombre, 2L)
        );

        assertEquals("El nombre del curso es obligatorio.", ex.getMessage());
    }
}



