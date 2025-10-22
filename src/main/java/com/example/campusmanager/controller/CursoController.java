package com.example.campusmanager.controller;




import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.campusmanager.domain.Curso;
import com.example.campusmanager.service.CursoService;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    private final CursoService service;

    public CursoController(CursoService service) {
        this.service = service;
    }

    // GET /api/cursos -> lista todos
    @GetMapping
    public List<Curso> getAll() {
        return service.listarTodos();
    }

    // POST /api/cursos -> crea nuevo curso
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Curso c) {
        try {
            Curso creado = service.crear(c);
            return ResponseEntity
                    .created(URI.create("/api/cursos/" + creado.getId()))
                    .body(creado);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("Introduzca los datos del Curso correctamente", ex.getMessage()));
        }
    }
}

