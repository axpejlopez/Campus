package com.example.campusmanager;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.campusmanager.domain.Curso;
import com.example.campusmanager.domain.Profesor;
import com.example.campusmanager.dto.CursosProfesorDTO;
import com.example.campusmanager.dto.ProfesorDTO;
import com.example.campusmanager.repository.CursoRepository;
import com.example.campusmanager.repository.ProfesorRepository;
import com.example.campusmanager.service.ProfesorService;

@ExtendWith(MockitoExtension.class)
class ProfesorServiceTest {

    @Mock
    private ProfesorRepository profesorRepository;

    @Mock
    private CursoRepository cursoRepository;

    @InjectMocks
    private ProfesorService profesorService;

    private Profesor profesor;
    private ProfesorDTO profesorDTO;

    @BeforeEach
    void setUp() {
        profesor = new Profesor();
        profesor.setId(1L);
        profesor.setNombre("Ana García");
        profesor.setEspecialidad("Matemáticas");

        profesorDTO = new ProfesorDTO();
        profesorDTO.nombre = "Ana García";
        profesorDTO.especialidad = "Matemáticas";
    }

    // ----------------------------
    // Test: getProfesores()
    // ----------------------------
    @Test
    void getProfesores_ReturnsListOfProfesores() {
        // Given
        List<Profesor> profesores = Arrays.asList(profesor);
        when(profesorRepository.findAll()).thenReturn(profesores);

        // When
        List<Profesor> result = profesorService.getProfesores();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNombre()).isEqualTo("Ana García");
        verify(profesorRepository).findAll();
    }

    // ----------------------------
    // Test: createProfesor() - éxito
    // ----------------------------
    @Test
    void createProfesor_ValidDTO_ReturnsSavedProfesor() {
        // Given
        when(profesorRepository.save(any(Profesor.class))).thenReturn(profesor);

        // When
        Profesor result = profesorService.createProfesor(profesorDTO);

        // Then
        assertThat(result.getNombre()).isEqualTo("Ana García");
        assertThat(result.getEspecialidad()).isEqualTo("Matemáticas");
        verify(profesorRepository).save(any(Profesor.class));
    }

    // ----------------------------
    // Test: createProfesor() - nombre vacío → excepción
    // ----------------------------
    @Test
    void createProfesor_EmptyName_ThrowsIllegalArgumentException() {
        // Given
        ProfesorDTO dto = new ProfesorDTO();
        dto.nombre = ""; // vacío
        dto.especialidad = "Física";

        // When + Then
        assertThatThrownBy(() -> profesorService.createProfesor(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El nombre es obligatorio.");
    }

    // ----------------------------
    // Test: cursosProfesor() - éxito
    // ----------------------------
    @Test
    void cursosProfesor_ProfesorExists_ReturnsCursosProfesorDTO() {
        // Given
        Curso curso1 = new Curso();
        curso1.setTitulo("Álgebra");
        curso1.setDescripcion("Curso de álgebra básica");

        Curso curso2 = new Curso();
        curso2.setTitulo("Cálculo");
        curso2.setDescripcion("Curso de cálculo avanzado");

        profesor.setCursos(Arrays.asList(curso1, curso2));

        when(profesorRepository.findByNombre("Ana García"))
                .thenReturn(Optional.of(profesor));

        // When
        CursosProfesorDTO result = profesorService.cursosProfesor("Ana García");

        // Then
        assertThat(result.nombreProfesor).isEqualTo("Ana García");
        assertThat(result.cursos).hasSize(2);
        assertThat(result.cursos).containsEntry("Álgebra", "Curso de álgebra básica");
        assertThat(result.cursos).containsEntry("Cálculo", "Curso de cálculo avanzado");
    }

    // ----------------------------
    // Test: cursosProfesor() - profesor no existe → excepción
    // ----------------------------
    @Test
    void cursosProfesor_ProfesorNotFound_ThrowsRuntimeException() {
        // Given
        when(profesorRepository.findByNombre("Desconocido"))
                .thenReturn(Optional.empty());

        // When + Then
        assertThatThrownBy(() -> profesorService.cursosProfesor("Desconocido"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("No existe un profesor con ese nombre: Desconocido");
    }
}