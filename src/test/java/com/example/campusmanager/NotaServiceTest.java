package com.example.campusmanager;
import com.example.campusmanager.domain.Alumno;
import com.example.campusmanager.domain.Nota;
import com.example.campusmanager.repository.AlumnoRepository;
import com.example.campusmanager.repository.NotaRepository;
import com.example.campusmanager.service.NotaService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotaServiceTest {

    @Mock
    private NotaRepository notaRepository;

    @Mock
    private AlumnoRepository alumnoRepository;

    @InjectMocks
    private NotaService notaService;

    private Alumno alumno;
    private Nota nota;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        alumno = new Alumno();
        alumno.setId(1L);
        alumno.setNombre("María Pérez");

        nota = new Nota();
        nota.setId(10L);
        nota.setAlumno(alumno);
        nota.setAsignatura("Matemáticas");
        nota.setNotaFinal(8.5);
    }

    // ✅ Crear nota correctamente
    @Test
    void testCrearNota_OK() {
        when(alumnoRepository.findById(1L)).thenReturn(Optional.of(alumno));
        when(notaRepository.save(any(Nota.class))).thenReturn(nota);

        Nota creada = notaService.crearNota(1L, "Matemáticas", 8.5);

        assertNotNull(creada);
        assertEquals("Matemáticas", creada.getAsignatura());
        verify(notaRepository, times(1)).save(any(Nota.class));
    }

    // ⚠️ Crear nota con alumno inexistente
    @Test
    void testCrearNota_AlumnoNoExiste() {
        when(alumnoRepository.findById(999L)).thenReturn(Optional.empty());

        NoSuchElementException ex = assertThrows(NoSuchElementException.class,
                () -> notaService.crearNota(999L, "Historia", 7.0));

        assertEquals("El alumno con ID 999 no existe.", ex.getMessage());
        verify(notaRepository, never()).save(any());
    }

    // ⚠️ Duplicado al crear nota
    @Test
    void testCrearNota_Duplicada() {
        when(alumnoRepository.findById(1L)).thenReturn(Optional.of(alumno));
        when(notaRepository.save(any())).thenThrow(DataIntegrityViolationException.class);

        assertThrows(DataIntegrityViolationException.class,
                () -> notaService.crearNota(1L, "Historia", 9.0));
    }

    // ✅ Actualizar nota correctamente
    @Test
    void testActualizarNota_OK() {
        when(notaRepository.findById(10L)).thenReturn(Optional.of(nota));
        when(notaRepository.save(any())).thenReturn(nota);

        Nota datos = new Nota();
        datos.setAsignatura("Matemáticas");
        datos.setNotaFinal(9.0);

        Nota actualizada = notaService.actualizarNota(10L, datos);

        assertEquals(9.0, actualizada.getNotaFinal());
        verify(notaRepository).save(any(Nota.class));
    }

    // ⚠️ Actualizar nota inexistente
    @Test
    void testActualizarNota_NoExiste() {
        when(notaRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                () -> notaService.actualizarNota(999L, new Nota()));
    }

    // ⚠️ Actualizar sin cambios
    @Test
    void testActualizarNota_SinCambios() {
        when(notaRepository.findById(10L)).thenReturn(Optional.of(nota));

        Nota sinCambios = new Nota();
        sinCambios.setAsignatura("Matemáticas");
        sinCambios.setNotaFinal(8.5);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> notaService.actualizarNota(10L, sinCambios));

        assertEquals("No se ha modificado ningún dato de la nota.", ex.getMessage());
    }

    // ✅ Eliminar nota correctamente
    @Test
    void testEliminarNota_OK() {
        when(notaRepository.existsById(10L)).thenReturn(true);

        notaService.eliminarNota(10L);

        verify(notaRepository).deleteById(10L);
    }

    // ⚠️ Eliminar nota inexistente
    @Test
    void testEliminarNota_NoExiste() {
        when(notaRepository.existsById(99L)).thenReturn(false);

        assertThrows(NoSuchElementException.class,
                () -> notaService.eliminarNota(99L));
    }
}
