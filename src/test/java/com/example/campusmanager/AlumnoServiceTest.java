package com.example.campusmanager;

import com.example.campusmanager.domain.Alumno;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

public class AlumnoServiceTest {

    @Mock
    private AlumnoRepository alumnoRepository;

    @InjectMocks
    private AlumnoService alumnoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListarTodos() {
        // 🔹 Creamos datos simulados
        Alumno alumno1 = new Alumno("María Gómez", "maria@example.com", LocalDate.of(2000, 5, 12));
        Alumno alumno2 = new Alumno("Carlos Ruiz", "carlos@example.com", LocalDate.of(1999, 8, 30));

        // 🔹 Simulamos el comportamiento del repositorio
        when(alumnoRepository.findAll()).thenReturn(Arrays.asList(alumno1, alumno2));

        // 🔹 Llamamos al servicio real
        List<Alumno> resultado = alumnoService.listarTodos();

        // 🔹 Comprobamos los resultados
        assertEquals(2, resultado.size());
        assertEquals("María Gómez", resultado.get(0).getNombre());
        assertEquals("Carlos Ruiz", resultado.get(1).getNombre());
    }

    @Test
    void testCrearAlumno() {
        // 🔹 Creamos un alumno de prueba
        Alumno alumno = new Alumno("Lucía Pérez", "lucia@example.com", LocalDate.of(2001, 3, 22));

        // 🔹 Simulamos el comportamiento del repositorio
        when(alumnoRepository.save(alumno)).thenReturn(alumno);

        // 🔹 Llamamos al método del servicio
        Alumno resultado = alumnoService.crearAlumno(alumno);

        // 🔹 Comprobamos los resultados
        assertEquals("Lucía Pérez", resultado.getNombre());
        assertEquals("lucia@example.com", resultado.getEmail());
        assertEquals(LocalDate.of(2001, 3, 22), resultado.getFechaNacimiento());

        // 🔹 Verificamos que se haya llamado a save() una vez
        verify(alumnoRepository, times(1)).save(alumno);
    }
}
