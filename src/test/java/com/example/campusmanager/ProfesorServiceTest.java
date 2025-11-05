package com.example.campusmanager;

import com.example.campusmanager.domain.Asignatura;
import com.example.campusmanager.domain.Curso;
import com.example.campusmanager.domain.Profesor;
import com.example.campusmanager.dto.AsignaturasProfesorDTO;
import com.example.campusmanager.dto.ProfesorDTO;
import com.example.campusmanager.repository.AsignaturaRepository;
import com.example.campusmanager.repository.ProfesorRepository;
import com.example.campusmanager.service.ProfesorService;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfesorServiceTest {

    @Mock
    private ProfesorRepository profesorRepository;

    @Mock
    private AsignaturaRepository asignaturaRepository;

    @InjectMocks
    private ProfesorService profesorService;

    private Profesor profesor;
    private ProfesorDTO dto;

    @BeforeEach
    void setUp() {
        profesor = new Profesor();
        profesor.setId(1L);
        profesor.setNombre("Luis Pérez");
        profesor.setEspecialidad("Historia");

        dto = new ProfesorDTO();
        dto.setNombre("Luis Pérez");
        dto.setEspecialidad("Historia");
    }

    // ==========================================================
    // 1️⃣ Test: listarProfesores()
    // ==========================================================
    @Test
    void listarProfesores_devuelveLista() {
        when(profesorRepository.findAll()).thenReturn(Arrays.asList(profesor));

        List<Profesor> resultado = profesorService.listarProfesores();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombre()).isEqualTo("Luis Pérez");
        verify(profesorRepository, times(1)).findAll();
    }

    // ==========================================================
    // 2️⃣ Test: crearProfesor() correcto
    // ==========================================================
    @Test
    void crearProfesor_valido_guardadoCorrectamente() {
        when(profesorRepository.save(any(Profesor.class))).thenReturn(profesor);

        Profesor creado = profesorService.crearProfesor(dto);

        assertThat(creado).isNotNull();
        assertThat(creado.getNombre()).isEqualTo("Luis Pérez");
        assertThat(creado.getEspecialidad()).isEqualTo("Historia");
        verify(profesorRepository).save(any(Profesor.class));
    }

    // ==========================================================
    // 3️⃣ Test: crearProfesor() sin nombre → excepción
    // ==========================================================
    @Test
    void crearProfesor_sinNombre_lanzaExcepcion() {
        ProfesorDTO sinNombre = new ProfesorDTO();
        sinNombre.setNombre("");
        sinNombre.setEspecialidad("Historia");

        assertThatThrownBy(() -> profesorService.crearProfesor(sinNombre))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El nombre del profesor es obligatorio.");
    }

    // ==========================================================
    // 4️⃣ Test: asignaturasPorProfesor() correcto
    // ==========================================================
    @Test
    void asignaturasPorProfesor_devuelveListaAsignaturas() {
        Curso curso = new Curso();
        curso.setNombre("2º ESO");

        Asignatura a1 = new Asignatura();
        a1.setTitulo("Historia Moderna");
        a1.setProfesor(profesor);
        a1.setCurso(curso);

        Asignatura a2 = new Asignatura();
        a2.setTitulo("Filosofía Moderna");
        a2.setProfesor(profesor);
        a2.setCurso(curso);

        when(profesorRepository.findByNombre("Luis Pérez")).thenReturn(Optional.of(profesor));
        when(asignaturaRepository.findAll()).thenReturn(Arrays.asList(a1, a2));

        List<AsignaturasProfesorDTO> resultado = profesorService.asignaturasPorProfesor("Luis Pérez");

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getNombreProfesor()).isEqualTo("Luis Pérez");
        assertThat(resultado.get(0).getTituloAsignatura()).isEqualTo("Historia Moderna");
        assertThat(resultado.get(0).getCurso()).isEqualTo("2º ESO");
    }

    // ==========================================================
    // 5️⃣ Test: asignaturasPorProfesor() profesor no existe → excepción
    // ==========================================================
    @Test
    void asignaturasPorProfesor_profesorNoExiste_lanzaExcepcion() {
        when(profesorRepository.findByNombre("Desconocido")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> profesorService.asignaturasPorProfesor("Desconocido"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("No existe un profesor con el nombre: Desconocido");
    }

    // ==========================================================
    // 6️⃣ Test: asignaturasPorProfesor() sin asignaturas → lista vacía
    // ==========================================================
    @Test
    void asignaturasPorProfesor_sinAsignaturas_devuelveVacio() {
        when(profesorRepository.findByNombre("Luis Pérez")).thenReturn(Optional.of(profesor));
        when(asignaturaRepository.findAll()).thenReturn(List.of());

        List<AsignaturasProfesorDTO> resultado = profesorService.asignaturasPorProfesor("Luis Pérez");

        assertThat(resultado).isEmpty();
    }
}

