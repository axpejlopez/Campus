package com.example.campusmanager;

import com.example.campusmanager.controller.AlumnoController;
import com.example.campusmanager.domain.Alumno;
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

public class AlumnoControllerTest {

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
        // 🔹 Datos simulados
        Alumno alumno1 = new Alumno("María Gómez", "maria@example.com", LocalDate.of(2000, 5, 12));
        Alumno alumno2 = new Alumno("Carlos Ruiz", "carlos@example.com", LocalDate.of(1999, 8, 30));

        when(alumnoService.listarTodos()).thenReturn(Arrays.asList(alumno1, alumno2));

        // 🔹 Llamada al controlador
        List<Alumno> resultado = alumnoController.listarTodos();

        // 🔹 Verificaciones
        assertEquals(2, resultado.size());
        assertEquals("María Gómez", resultado.get(0).getNombre());
        assertEquals("Carlos Ruiz", resultado.get(1).getNombre());
    }

    @Test
    void testCrearAlumno() {
        // 🔹 Alumno de prueba
        Alumno alumno = new Alumno("Lucía Pérez", "lucia@example.com", LocalDate.of(2001, 3, 22));

        when(alumnoService.crearAlumno(alumno)).thenReturn(alumno);

        // 🔹 Llamada al controlador
        Alumno resultado = alumnoController.crearAlumno(alumno);

        // 🔹 Verificaciones
        assertEquals("Lucía Pérez", resultado.getNombre());
        assertEquals("lucia@example.com", resultado.getEmail());

        // 🔹 Verifica que se llamó una vez al servicio
        verify(alumnoService, times(1)).crearAlumno(alumno);
    }
}
