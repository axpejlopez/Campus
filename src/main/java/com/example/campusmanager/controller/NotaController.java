package com.example.campusmanager.controller;

import com.example.campusmanager.domain.Nota;
import com.example.campusmanager.service.NotaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI; // ✅ Import necesario
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notas")
public class NotaController {

    private final NotaService notaService;

    public NotaController(NotaService notaService) {
        this.notaService = notaService;
    }

    // ✅ 1. Obtener notas de un alumno
    @GetMapping("/alumno/{id}")
    public ResponseEntity<?> getNotasPorAlumno(@PathVariable Long id) {
        List<Nota> notas = notaService.obtenerNotasPorAlumno(id);
        return notas.isEmpty()
                ? ResponseEntity.ok(Map.of("mensaje", "El alumno no tiene notas registradas."))
                : ResponseEntity.ok(notas);
    }

    // ✅ 2. Obtener media de un alumno
    @GetMapping("/alumno/{id}/media")
    public ResponseEntity<?> getMediaPorAlumno(@PathVariable Long id) {
        return ResponseEntity.ok(Map.of("media", notaService.obtenerMediaPorAlumno(id)));
    }

    // ✅ 3. Media general de la clase
    @GetMapping("/media-clase")
    public ResponseEntity<?> getMediaClase() {
        return ResponseEntity.ok(Map.of("mediaGeneral", notaService.obtenerMediaClase()));
    }

    // ✅ 4. Crear nueva nota
    @PostMapping
    public ResponseEntity<?> crearNota(@RequestBody Map<String, Object> body) {
        try {
            Long alumnoId = ((Number) body.get("alumnoId")).longValue();
            String asignatura = body.get("asignatura").toString();
            Double notaFinal = Double.parseDouble(body.get("notaFinal").toString());

            // 🟢 CORREGIDO: usar notaService (no "service")
            Nota nueva = notaService.crearNota(alumnoId, asignatura, notaFinal);

            return ResponseEntity.created(URI.create("/api/notas/" + nueva.getId())).body(nueva);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ 5. Actualizar nota existente
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarNota(@PathVariable Long id, @RequestBody Nota nota) {
        return ResponseEntity.ok(notaService.actualizarNota(id, nota));
    }

    // ✅ 6. Eliminar nota
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarNota(@PathVariable Long id) {
        notaService.eliminarNota(id);
        return ResponseEntity.ok(Map.of("mensaje", "Nota eliminada correctamente"));
    }
}


