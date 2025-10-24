package com.example.campusmanager;

import com.example.campusmanager.domain.Alumno;
import com.example.campusmanager.domain.Curso;
import com.example.campusmanager.domain.Matricula;
import com.example.campusmanager.repository.AlumnoRepository;
import com.example.campusmanager.repository.CursoRepository;
import com.example.campusmanager.repository.MatriculaRepository;
import com.example.campusmanager.service.MatriculaService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatriculaServiceTest {

    @Mock private MatriculaRepository repo;
    @Mock private AlumnoRepository alumnoRepo;
    @Mock private CursoRepository cursoRepo;

    @InjectMocks private MatriculaService service;

    private Alumno alumno;
    private Curso curso;

    @BeforeEach
    void setUp() {
        alumno = new Alumno();
        alumno.setId(1L);
        curso = new Curso();
        curso.setId(2L);
    }

    @Test
    void crear_ok() {
        when(alumnoRepo.findById(1L)).thenReturn(Optional.of(alumno));
        when(cursoRepo.findById(2L)).thenReturn(Optional.of(curso));
        when(repo.existsByAlumno_IdAndCurso_Id(1L, 2L)).thenReturn(false);
        when(repo.save(any(Matricula.class))).thenAnswer(inv -> inv.getArgument(0));

        Matricula m = service.crear(1L, 2L, LocalDate.now());
        assertEquals(alumno, m.getAlumno());
        assertEquals(curso, m.getCurso());
        verify(repo).save(any());
    }

    @Test
    void crear_duplicada_ko() {
        when(repo.existsByAlumno_IdAndCurso_Id(1L, 2L)).thenReturn(true);
        var ex = assertThrows(IllegalArgumentException.class, () -> service.crear(1L, 2L, LocalDate.now()));
        assertEquals("El alumno ya está matriculado en ese curso.", ex.getMessage());
    }

    // 🔹 Nuevo test: obtener alumnos por curso
    @Test
    void obtenerAlumnosPorCurso_ok() {
        Alumno alumno1 = new Alumno();
        alumno1.setId(1L);
        alumno1.setNombre("María");

        Alumno alumno2 = new Alumno();
        alumno2.setId(2L);
        alumno2.setNombre("Carlos");

        Curso curso = new Curso();
        curso.setId(10L);

        Matricula m1 = new Matricula(alumno1, curso, LocalDate.now());
        Matricula m2 = new Matricula(alumno2, curso, LocalDate.now());

        when(cursoRepo.findById(10L)).thenReturn(Optional.of(curso));
        when(repo.findByCurso_Id(10L)).thenReturn(List.of(m1, m2));

        List<Alumno> resultado = service.obtenerAlumnosPorCurso(10L);

        assertEquals(2, resultado.size());
        assertEquals("María", resultado.get(0).getNombre());
        assertEquals("Carlos", resultado.get(1).getNombre());
    }

    @Test
    void obtenerAlumnosPorCurso_cursoNoEncontrado() {
        when(cursoRepo.findById(99L)).thenReturn(Optional.empty());

        var ex = assertThrows(IllegalArgumentException.class, () -> service.obtenerAlumnosPorCurso(99L));
        assertEquals("Curso no encontrado.", ex.getMessage());
    }
}
