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

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * ✅ Pruebas unitarias para la clase NotaService.
 * Incluye tests positivos y negativos para verificar toda la lógica del servicio.
 */
class NotaServiceTest {

    @Mock
    private NotaRepository notaRepository;

    @Mock
    private AlumnoRepository alumnoRepository;

    @InjectMocks
    private NotaService notaService;

    private Alumno alumno;
    private Nota nota1;
    private Nota nota2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        alumno = new Alumno();
        alumno.setId(1L);
        alumno.setNombre("Juan Pérez");
        alumno.setEmail("juan@example.com");
        alumno.setFechaNacimiento(java.time.LocalDate.of(2000, 5, 12));

        nota1 = new Nota(alumno, "Matemáticas", new BigDecimal("8.50"));
        nota1.setId(1L);

        nota2 = new Nota(alumno, "Lengua", new BigDecimal("9.00"));
        nota2.setId(2L);
    }

    // ✅ 1. Test: obtener notas por alumno
    @Test
    void testObtenerNotasPorAlumno() {
        when(alumnoRepository.findById(1L)).thenReturn(Optional.of(alumno));
        when(notaRepository.findAll()).thenReturn(Arrays.asList(nota1, nota2));

        List<Nota> notas = notaService.obtenerNotasPorAlumno(1L);

        assertEquals(2, notas.size());
        assertTrue(notas.stream().allMatch(n -> n.getAlumno().getId().equals(1L)));
        verify(notaRepository, times(1)).findAll();
    }

    // ✅ 2. Test: calcular media de notas de un alumno
    @Test
    void testObtenerMediaPorAlumno() {
        when(alumnoRepository.findById(1L)).thenReturn(Optional.of(alumno));
        when(notaRepository.findAll()).thenReturn(Arrays.asList(nota1, nota2));

        BigDecimal media = notaService.obtenerMediaPorAlumno(1L);

        assertEquals(new BigDecimal("8.75"), media);
    }

    // ✅ 3. Test: agregar nueva nota
    @Test
    void testAgregarNota() {
        when(alumnoRepository.findById(1L)).thenReturn(Optional.of(alumno));
        when(notaRepository.save(any(Nota.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Nota nueva = notaService.agregarNota(1L, "Ciencias", new BigDecimal("7.00"));

        assertNotNull(nueva);
        assertEquals("Ciencias", nueva.getAsignatura());
        assertEquals(new BigDecimal("7.00"), nueva.getNotaFinal());
        verify(notaRepository, times(1)).save(any(Nota.class));
    }

    // ✅ 4. Test: actualizar nota existente
    @Test
    void testActualizarNota() {
        when(notaRepository.findById(1L)).thenReturn(Optional.of(nota1));
        when(notaRepository.save(any(Nota.class))).thenReturn(nota1);

        Nota actualizada = notaService.actualizarNota(1L, new BigDecimal("9.50"));

        assertEquals(new BigDecimal("9.50"), actualizada.getNotaFinal());
    }

    // ✅ 5. Test: eliminar nota
    @Test
    void testEliminarNota() {
        when(notaRepository.existsById(1L)).thenReturn(true);

        notaService.eliminarNota(1L);

        verify(notaRepository, times(1)).deleteById(1L);
    }

    // ❌ NEGATIVO 1: Alumno no encontrado
    @Test
    void testObtenerNotasPorAlumno_NoExisteAlumno() {
        when(alumnoRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> notaService.obtenerNotasPorAlumno(99L));

        assertEquals("Alumno no encontrado con id: 99", ex.getMessage());
    }

    // ❌ NEGATIVO 2: Alumno sin notas
    @Test
    void testObtenerMediaPorAlumno_SinNotas() {
        when(alumnoRepository.findById(1L)).thenReturn(Optional.of(alumno));
        when(notaRepository.findAll()).thenReturn(Collections.emptyList());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> notaService.obtenerMediaPorAlumno(1L));

        assertEquals("El alumno no tiene notas registradas", ex.getMessage());
    }

    // ❌ NEGATIVO 3: Sin notas en el sistema
    @Test
    void testObtenerMediaGeneral_SinNotas() {
        when(notaRepository.findAll()).thenReturn(Collections.emptyList());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> notaService.obtenerMediaGeneral());

        assertEquals("No hay notas registradas en el sistema", ex.getMessage());
    }

    // ❌ NEGATIVO 4: Actualizar nota inexistente
    @Test
    void testActualizarNota_NoExiste() {
        when(notaRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> notaService.actualizarNota(999L, new BigDecimal("10.00")));

        assertEquals("Nota no encontrada con id: 999", ex.getMessage());
    }

    // ❌ NEGATIVO 5: Eliminar nota inexistente
    @Test
    void testEliminarNota_NoExiste() {
        when(notaRepository.existsById(99L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> notaService.eliminarNota(99L));

        assertEquals("No existe una nota con id: 99", ex.getMessage());
    }
}
