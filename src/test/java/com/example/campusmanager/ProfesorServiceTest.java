package com.example.campusmanager;

import com.example.campusmanager.dto.ProfesorDTO;
import com.example.campusmanager.entity.Profesor;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ProfesorServiceTest {

    @Mock
    private ProfesorRepository profesorRepository;

    @InjectMocks
    private ProfesorService profesorService;

    private ProfesorDTO profesorDTO;
    private Profesor profesor;

    @BeforeEach
    void setUp() {
        profesorDTO = new ProfesorDTO();
        profesorDTO.nombre = "Carlos Ruiz";
        profesorDTO.especialidad = "Historia";

        profesor = new Profesor();
        profesor.setId(1);
        profesor.setNombre("Carlos Ruiz");
        profesor.setEspecialidad("Historia");
    }

    @Test
    public void getProfesores_DeberiaDevolverListaDeProfesores() {
        // Given
        List<Profesor> profesores = Arrays.asList(profesor);
        when(profesorRepository.findAll()).thenReturn(profesores);

        // When
        List<Profesor> resultado = profesorService.getProfesores();

        // Then
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombre()).isEqualTo("Carlos Ruiz");
        verify(profesorRepository, times(1)).findAll();
    }

    @Test
    public void createProfesor_ConNombreValido_DeberiaGuardarYDevolverProfesor() {
        // Given
        when(profesorRepository.save(any(Profesor.class))).thenReturn(profesor);

        // When
        Profesor resultado = profesorService.createProfesor(profesorDTO);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getNombre()).isEqualTo("Carlos Ruiz");
        assertThat(resultado.getEspecialidad()).isEqualTo("Historia");
        verify(profesorRepository, times(1)).save(any(Profesor.class));
    }

    @Test
    public void createProfesor_ConNombreVacio_DeberiaLanzarExcepcion() {
        // Given
        profesorDTO.nombre = "";

        // When & Then
        assertThatThrownBy(() -> profesorService.createProfesor(profesorDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El nombre es obligatorio.");

        verify(profesorRepository, never()).save(any());
    }

    @Test
    public void createProfesor_ConEspecialidadVacia_DeberiaGuardarSinEspecialidad() {
        // Given
        profesorDTO.especialidad = "";
        Profesor profesorSinEspecialidad = new Profesor();
        profesorSinEspecialidad.setId(2);
        profesorSinEspecialidad.setNombre("María Gómez");
        // especialidad queda null o vacía

        when(profesorRepository.save(any(Profesor.class))).thenAnswer(invocation -> {
            Profesor p = invocation.getArgument(0);
            p.setId(2); // Simular ID asignado
            return p;
        });

        // When
        Profesor resultado = profesorService.createProfesor(profesorDTO);

        // Then
        assertThat(resultado.getNombre()).isEqualTo("Carlos Ruiz");
        assertThat(resultado.getEspecialidad()).isNullOrEmpty(); // o .isEmpty() si es cadena vacía
        verify(profesorRepository, times(1)).save(any(Profesor.class));
    }
}