package com.example.campusmanager;

import com.example.campusmanager.domain.Asignatura;
import com.example.campusmanager.domain.Curso;
import com.example.campusmanager.domain.Profesor;
import com.example.campusmanager.dto.AsignaturaRequestDTO;
import com.example.campusmanager.repository.AsignaturaRepository;
import com.example.campusmanager.repository.CursoRepository;
import com.example.campusmanager.repository.ProfesorRepository;
import com.example.campusmanager.service.AsignaturaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

class AsignaturaServiceTest {

    @Mock
    private AsignaturaRepository asignaturaRepository;

    @Mock
    private CursoRepository cursoRepository;

    @Mock
    private ProfesorRepository profesorRepository;

    @InjectMocks
    private AsignaturaService asignaturaService;

    private Asignatura asignatura;
    private Curso curso;
    private Profesor profesor;

    @BeforeEach
    void setUp() {
        curso = new Curso();
        curso.setId(1L);
        curso.setNombre("3º ESO");

        profesor = new Profesor();
        profesor.setId(2L);
        profesor.setNombre("Luis Pérez");
        profesor.setEspecialidad("Historia");

        asignatura = new Asignatura();
        asignatura.setId(10L);
        asignatura.setTitulo("Historia Contemporánea");
        asignatura.setCurso(curso);
        asignatura.setProfesor(profesor);
    }

    // ============================================================
    // 1️⃣ listarAsignaturas()
    // ============================================================
    @Test
    void listarAsignaturas_devuelveListaCorrecta() {
        when(asignaturaRepository.findAll()).thenReturn(Arrays.asList(asignatura));

        List<Asignatura> resultado = asignaturaService.listarAsignaturas();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getTitulo()).isEqualTo("Historia Contemporánea");
        verify(asignaturaRepository).findAll();
    }

    // ============================================================
    // 2️⃣ obtenerPorId() existente
    // ============================================================
    @Test
    void obtenerPorId_devuelveAsignaturaSiExiste() {
        when(asignaturaRepository.findById(10L)).thenReturn(Optional.of(asignatura));

        Asignatura resultado = asignaturaService.obtenerPorId(10L);

        assertThat(resultado.getTitulo()).isEqualTo("Historia Contemporánea");
        assertThat(resultado.getProfesor().getNombre()).isEqualTo("Luis Pérez");
    }

    // ============================================================
    // 3️⃣ obtenerPorId() inexistente
    // ============================================================
    @Test
    void obtenerPorId_lanzaExcepcionSiNoExiste() {
        when(asignaturaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> asignaturaService.obtenerPorId(99L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Asignatura no encontrada.");
    }

    // ============================================================
    // 4️⃣ crear() correcto
    // ============================================================
    @Test
    void crear_valido_devuelveAsignaturaGuardada() {
        AsignaturaRequestDTO dto = new AsignaturaRequestDTO();
        dto.setTitulo("Filosofía Clásica");
        dto.setCursoId(1L);
        dto.setProfesorId(2L);

        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));
        when(profesorRepository.findById(2L)).thenReturn(Optional.of(profesor));
        when(asignaturaRepository.save(any(Asignatura.class))).thenReturn(asignatura);

        Asignatura creada = asignaturaService.crear(dto);

        assertThat(creada).isNotNull();
        assertThat(creada.getTitulo()).isEqualTo("Historia Contemporánea");
        assertThat(creada.getProfesor().getNombre()).isEqualTo("Luis Pérez");
        verify(asignaturaRepository).save(any(Asignatura.class));
    }

    // ============================================================
    // 5️⃣ crear() curso no existe
    // ============================================================
    @Test
    void crear_cursoNoExiste_lanzaExcepcion() {
        AsignaturaRequestDTO dto = new AsignaturaRequestDTO();
        dto.setTitulo("Filosofía Clásica");
        dto.setCursoId(1L);
        dto.setProfesorId(2L);

        when(cursoRepository.findById(1L)).thenReturn(Optional.empty());
        when(profesorRepository.findById(2L)).thenReturn(Optional.of(profesor));

        assertThatThrownBy(() -> asignaturaService.crear(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Curso no encontrado.");
    }

    // ============================================================
    // 6️⃣ crear() profesor no existe
    // ============================================================
    @Test
    void crear_profesorNoExiste_lanzaExcepcion() {
        AsignaturaRequestDTO dto = new AsignaturaRequestDTO();
        dto.setTitulo("Filosofía Clásica");
        dto.setCursoId(1L);
        dto.setProfesorId(2L);

        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));
        when(profesorRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> asignaturaService.crear(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Profesor no encontrado.");
    }
}

