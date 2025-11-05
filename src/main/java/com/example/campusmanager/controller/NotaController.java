package com.example.campusmanager.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.List;

import com.example.campusmanager.domain.Nota;
import com.example.campusmanager.service.NotaService;

@RestController
@RequestMapping("/api/notas")
public class NotaController {

    private final NotaService notaService;

    public NotaController(NotaService notaService) {
        this.notaService = notaService;
    }

    // ✅ 1. Obtener todas las notas de un alumno
    @GetMapping("/alumno/{id}")
    public ResponseEntity<List<Nota>> obtenerNotasPorAlumno(@PathVariable Long id) {
        List<Nota> notas = notaService.obtenerNotasPorAlumno(id);
        return ResponseEntity.ok(notas);
    }

    // ✅ 2. Calcular la media de notas de un alumno
    @GetMapping("/alumno/{id}/media")
    public ResponseEntity<BigDecimal> obtenerMediaPorAlumno(@PathVariable Long id) {
        BigDecimal media = notaService.obtenerMediaPorAlumno(id);
        return ResponseEntity.ok(media);
    }

    // ✅ 3. Calcular la media general de todos los alumnos
    @GetMapping("/media-general")
    public ResponseEntity<BigDecimal> obtenerMediaGeneral() {
        BigDecimal mediaGeneral = notaService.obtenerMediaGeneral();
        return ResponseEntity.ok(mediaGeneral);
    }

    // ✅ 4. Añadir una nueva nota
    @PostMapping
    public ResponseEntity<Nota> agregarNota(
            @RequestParam Long alumnoId,
            @RequestParam String asignatura,
            @RequestParam BigDecimal notaFinal) {
        Nota nueva = notaService.agregarNota(alumnoId, asignatura, notaFinal);
        return new ResponseEntity<>(nueva, HttpStatus.CREATED);
    }

    // ✅ 5. Modificar una nota existente
    @PutMapping("/{id}")
    public ResponseEntity<Nota> actualizarNota(
            @PathVariable Long id,
            @RequestParam BigDecimal nuevaNota) {
        Nota actualizada = notaService.actualizarNota(id, nuevaNota);
        return ResponseEntity.ok(actualizada);
    }

    // ✅ 6. Eliminar una nota
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarNota(@PathVariable Long id) {
        notaService.eliminarNota(id);
        return ResponseEntity.noContent().build();
    }
}
