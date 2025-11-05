package com.example.campusmanager.controller;

import com.example.campusmanager.dto.NotaRequestDTO;
import com.example.campusmanager.dto.NotaResponseDTO;
import com.example.campusmanager.service.NotaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notas")
@RequiredArgsConstructor
public class NotaController {

    private final NotaService notaService;

    // 🟢 1. Obtener todas las notas de una matrícula
    @GetMapping("/matricula/{matriculaId}")
    public ResponseEntity<List<NotaResponseDTO>> obtenerNotasPorMatricula(@PathVariable Long matriculaId) {
        List<NotaResponseDTO> notas = notaService.obtenerNotasPorMatricula(matriculaId);
        return ResponseEntity.ok(notas);
    }

    // 🟢 2. Obtener media de notas de una matrícula
    @GetMapping("/matricula/{matriculaId}/media")
    public ResponseEntity<Map<String, Double>> obtenerMediaPorMatricula(@PathVariable Long matriculaId) {
        Double media = notaService.obtenerMediaPorMatricula(matriculaId);
        return ResponseEntity.ok(Map.of("media", media));
    }
    
 

    // 🟢 3. Media general de todas las notas
    @GetMapping("/media-clase")
    public ResponseEntity<Map<String, Double>> obtenerMediaClase() {
        Double mediaGeneral = notaService.obtenerMediaClase();
        return ResponseEntity.ok(Map.of("mediaGeneral", mediaGeneral));
    }

    // 🟢 4. Crear una nueva nota
    @PostMapping
    public ResponseEntity<NotaResponseDTO> crearNota(@Valid @RequestBody NotaRequestDTO dto) {
        NotaResponseDTO nueva = notaService.crearNota(dto);
        return ResponseEntity
                .created(URI.create("/api/notas/" + nueva.getId()))
                .body(nueva);
    }

    // 🟢 5. Actualizar nota existente
    @PutMapping("/{id}")
    public ResponseEntity<NotaResponseDTO> actualizarNota(@PathVariable Long id, @Valid @RequestBody NotaRequestDTO dto) {
        NotaResponseDTO actualizada = notaService.actualizarNota(id, dto);
        return ResponseEntity.ok(actualizada);
    }

    // 🟢 6. Eliminar nota
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminarNota(@PathVariable Long id) {
        notaService.eliminarNota(id);
        return ResponseEntity.ok(Map.of("mensaje", "Nota eliminada correctamente"));
    }
}


