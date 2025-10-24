package com.example.campusmanager.controller;

import com.example.campusmanager.domain.Alumno;
import com.example.campusmanager.domain.Matricula;
import com.example.campusmanager.service.MatriculaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/matriculas")
public class MatriculaController {

    private final MatriculaService service;

    public MatriculaController(MatriculaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Matricula> getAll() {
        return service.listarTodas();
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Map<String, Object> body) {
        try {
            Long alumnoId = ((Number) body.get("alumnoId")).longValue();
            Long cursoId = ((Number) body.get("cursoId")).longValue();
            LocalDate fecha = LocalDate.parse(body.get("fechaMatricula").toString());

            Matricula creada = service.crear(alumnoId, cursoId, fecha);
            return ResponseEntity.created(URI.create("/api/matriculas/" + creada.getId())).body(creada);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("La matricula no es la correcta", e.getMessage()));
        }
    }

    @PostMapping("/alumno/{alumnoId}/curso/{cursoId}")
    public ResponseEntity<?> matricularAlumnoEnCurso(
            @PathVariable Long alumnoId,
            @PathVariable Long cursoId) {
        try {
            Matricula matricula = service.crear(alumnoId, cursoId, LocalDate.now());
            return ResponseEntity
                    .created(URI.create("/api/matriculas/" + matricula.getId()))
                    .body(matricula);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 🔹 NUEVO ENDPOINT: obtener alumnos por curso
    @GetMapping("/curso/{cursoId}/alumnos")
    public ResponseEntity<?> obtenerAlumnosPorCurso(@PathVariable Long cursoId) {
        try {
            List<Alumno> alumnos = service.obtenerAlumnosPorCurso(cursoId);
            return ResponseEntity.ok(alumnos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    // 🔹 NUEVO ENDPOINT: generar PDF de alumnos por curso
    @GetMapping("/curso/{cursoId}/pdf")
    public ResponseEntity<byte[]> generarPdfPorCurso(@PathVariable Long cursoId) {
        try {
            byte[] pdfBytes = service.generarPdfAlumnosPorCurso(cursoId);

            return ResponseEntity.ok()
                    .header("Content-Type", "application/pdf")
                    .header("Content-Disposition", "attachment; filename=alumnos_curso_" + cursoId + ".pdf")
                    .body(pdfBytes);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(null);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }
}
