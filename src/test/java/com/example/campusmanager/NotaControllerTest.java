package com.example.campusmanager;

import com.example.campusmanager.controller.NotaController;
import com.example.campusmanager.domain.Nota;
import com.example.campusmanager.service.NotaService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotaControllerTest {

    @Mock
    private NotaService notaService;

    @InjectMocks
    private NotaController notaController;

    private Nota nota1;
    private Nota nota2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        nota1 = new Nota();
        nota1.setId(1L);
        nota1.setAsignatura("Matemáticas");
        nota1.setNotaFinal(8.0);

        nota2 = new Nota();
        nota2.setId(2L);
        nota2.setAsignatura("Lengua");
        nota2.setNotaFinal(7.0);
    }

    // ✅ GET: obtener notas por alumno
    @Test
    void testGetNotasPorAlumno_OK() {
        when(notaService.obtenerNotasPorAlumno(1L)).thenReturn(Arrays.asList(nota1, nota2));

        ResponseEntity<?> response = notaController.getNotasPorAlumno(1L);

        assertEquals(200, response.getStatusCode().value());
        List<?> body = (List<?>) response.getBody();
        assertEquals(2, body.size());
        verify(notaService, times(1)).obtenerNotasPorAlumno(1L);
    }

    // ⚠️ GET: sin notas
    @Test
    void testGetNotasPorAlumno_SinNotas() {
        when(notaService.obtenerNotasPorAlumno(1L)).thenReturn(List.of());

        ResponseEntity<?> response = notaController.getNotasPorAlumno(1L);

        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertTrue(body.containsKey("mensaje"));
        assertEquals("El alumno no tiene notas registradas.", body.get("mensaje"));
    }

    // ✅ GET: media de un alumno
    @Test
    void testGetMediaPorAlumno_OK() {
        when(notaService.obtenerMediaPorAlumno(1L)).thenReturn(8.5);

        ResponseEntity<?> response = notaController.getMediaPorAlumno(1L);

        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals(8.5, body.get("media"));
        verify(notaService, times(1)).obtenerMediaPorAlumno(1L);
    }

    // ✅ POST: crear nota correctamente
    @Test
    void testCrearNota_OK() {
        Map<String, Object> body = new HashMap<>();
        body.put("alumnoId", 1);
        body.put("asignatura", "Historia");
        body.put("notaFinal", 9.0);

        Nota creada = new Nota();
        creada.setId(5L);
        creada.setAsignatura("Historia");
        creada.setNotaFinal(9.0);

        when(notaService.crearNota(1L, "Historia", 9.0)).thenReturn(creada);

        ResponseEntity<?> response = notaController.crearNota(body);

        assertEquals(201, response.getStatusCode().value());
        Nota notaBody = (Nota) response.getBody();
        assertEquals("Historia", notaBody.getAsignatura());
        assertEquals(9.0, notaBody.getNotaFinal());
    }

    // ⚠️ POST: error al crear nota
    @Test
    void testCrearNota_Error() {
        Map<String, Object> body = Map.of(
                "alumnoId", 1,
                "asignatura", "Historia",
                "notaFinal", 9.5
        );

        when(notaService.crearNota(anyLong(), anyString(), anyDouble()))
                .thenThrow(new IllegalArgumentException("Error al crear nota"));

        ResponseEntity<?> response = notaController.crearNota(body);

        assertEquals(400, response.getStatusCode().value());
        Map<?, ?> bodyResp = (Map<?, ?>) response.getBody();
        assertTrue(bodyResp.containsKey("error"));
    }

    // ✅ PUT: actualizar nota
    @Test
    void testActualizarNota_OK() {
        when(notaService.actualizarNota(eq(1L), any(Nota.class))).thenReturn(nota2);

        ResponseEntity<?> response = notaController.actualizarNota(1L, nota2);

        assertEquals(200, response.getStatusCode().value());
        Nota actualizada = (Nota) response.getBody();
        assertEquals("Lengua", actualizada.getAsignatura());
    }

    // ✅ DELETE: eliminar nota
    @Test
    void testEliminarNota_OK() {
        doNothing().when(notaService).eliminarNota(1L);

        ResponseEntity<?> response = notaController.eliminarNota(1L);

        assertEquals(200, response.getStatusCode().value());
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals("Nota eliminada correctamente", body.get("mensaje"));
    }
}


