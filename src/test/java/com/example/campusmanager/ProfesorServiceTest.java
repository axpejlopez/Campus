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
    private CursoRepository cursoRepository; // aunque no se usa directamente en cursosProfesor(), lo dejamos por si acaso

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

    // ========================================================
    // 1. Test: getProfesores()
    // ========================================================
    @Test
    void getProfesores_ReturnsAllProfesores() {
        // Given
        List<Profesor> lista = Arrays.asList(profesor);
        when(profesorRepository.findAll()).thenReturn(lista);

        // When
        List<Profesor> resultado = profesorService.getProfesores();

        // Then
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombre()).isEqualTo("Ana García");
        verify(profesorRepository).findAll();
    }

    // ========================================================
    // 2. Test: createProfesor() - con datos válidos
    // ========================================================
    @Test
    void createProfesor_ValidDTO_ReturnsSavedProfesor() {
        // Given
        when(profesorRepository.save(any(Profesor.class))).thenReturn(profesor);

        // When
        Profesor resultado = profesorService.createProfesor(profesorDTO);

        // Then
        assertThat(resultado.getNombre()).isEqualTo("Ana García");
        assertThat(resultado.getEspecialidad()).isEqualTo("Matemáticas");
        verify(profesorRepository).save(any(Profesor.class));
    }

    // ========================================================
    // 3. Test: createProfesor() - nombre vacío → excepción
    // ========================================================
    @Test
    void createProfesor_EmptyName_ThrowsIllegalArgumentException() {
        // Given
        ProfesorDTO dtoInvalido = new ProfesorDTO();
        dtoInvalido.nombre = ""; // vacío
        dtoInvalido.especialidad = "Física";

        // When + Then
        assertThatThrownBy(() -> profesorService.createProfesor(dtoInvalido))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El nombre es obligatorio.");
    }

    // ========================================================
    // 4. Test: cursosProfesor() - profesor con cursos
    // ========================================================
    @Test
    void cursosProfesor_ProfesorConCursos_ReturnsListaDeCursos() {
        // Given
        Curso curso1 = new Curso();
        curso1.setTitulo("Álgebra");
        curso1.setDescripcion("Curso básico de álgebra");

        Curso curso2 = new Curso();
        curso2.setTitulo("Cálculo");
        curso2.setDescripcion("Cálculo avanzado");

        profesor.setCursos(Arrays.asList(curso1, curso2));

        when(profesorRepository.findByNombre("Ana García"))
                .thenReturn(Optional.of(profesor));

        // When
        List<CursosProfesorDTO> resultado = profesorService.cursosProfesor("Ana García");

        // Then
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).nombreProfesor).isEqualTo("Ana García");
        assertThat(resultado.get(0).tituloCurso).isEqualTo("Álgebra");
        assertThat(resultado.get(1).tituloCurso).isEqualTo("Cálculo");
        assertThat(resultado.get(0).profesor_id).isEqualTo(1L);
    }

    // ========================================================
    // 5. Test: cursosProfesor() - profesor SIN cursos
    // ========================================================
    @Test
    void cursosProfesor_ProfesorSinCursos_ReturnsListaConAsteriscos() {
        // Given
        profesor.setCursos(new ArrayList<>()); // lista vacía

        when(profesorRepository.findByNombre("Ana García"))
                .thenReturn(Optional.of(profesor));

        // When
        List<CursosProfesorDTO> resultado = profesorService.cursosProfesor("Ana García");

        // Then
        assertThat(resultado).hasSize(1);
        CursosProfesorDTO dto = resultado.get(0);
        assertThat(dto.nombreProfesor).isEqualTo("Ana García");
        assertThat(dto.tituloCurso).isEqualTo("*****");
        assertThat(dto.descripcionCurso).isEqualTo("*****");
        assertThat(dto.profesor_id).isEqualTo(1L);
    }

    // ========================================================
    // 6. Test: cursosProfesor() - profesor no existe → excepción
    // ========================================================
    @Test
    void cursosProfesor_ProfesorNoEncontrado_ThrowsRuntimeException() {
        // Given
        when(profesorRepository.findByNombre("Desconocido"))
                .thenReturn(Optional.empty());

        // When + Then
        assertThatThrownBy(() -> profesorService.cursosProfesor("Desconocido"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("No existe un profesor con ese nombre: Desconocido");
    }
}