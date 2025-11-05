package com.example.campusmanager.controller;

import com.example.campusmanager.domain.Asignatura;
import com.example.campusmanager.dto.AsignaturaResponseDTO;
import com.example.campusmanager.service.AsignaturaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/asignaturas")
@RequiredArgsConstructor
public class AsignaturaController {

    private final AsignaturaService asignaturaService;

    @GetMapping
    public ResponseEntity<List<Asignatura>> listarAsignaturas() {
        return ResponseEntity.ok(asignaturaService.listarAsignaturas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Asignatura> obtenerAsignatura(@PathVariable Long id) {
        return ResponseEntity.ok(asignaturaService.obtenerPorId(id));
    }
    
    @PostMapping
    public ResponseEntity<AsignaturaResponseDTO> crearAsignatura(@RequestBody Asignatura asignatura) {
        AsignaturaResponseDTO nueva = asignaturaService.crear(asignatura);
        return ResponseEntity.ok(nueva);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<AsignaturaResponseDTO> actualizarAsignatura(
            @PathVariable Long id,
            @RequestBody Asignatura asignatura) {
        AsignaturaResponseDTO actualizada = asignaturaService.actualizar(id, asignatura);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminarAsignatura(@PathVariable Long id) {
        asignaturaService.eliminar(id);
        return ResponseEntity.ok(Map.of("mensaje", "Asignatura eliminada correctamente"));
    }
    
    @GetMapping("/detalle")
    public ResponseEntity<List<AsignaturaResponseDTO>> listarAsignaturasDetalle() {
        return ResponseEntity.ok(asignaturaService.listarAsignaturasDetalle());
    }


}
