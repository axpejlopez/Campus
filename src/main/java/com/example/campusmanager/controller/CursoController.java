package com.example.campusmanager.controller;

import com.example.campusmanager.domain.Curso;
import com.example.campusmanager.dto.CursoRequestDTO;
import com.example.campusmanager.dto.CursoResponseDTO;
import com.example.campusmanager.dto.CursoUpdateDTO;
import com.example.campusmanager.dto.ProfesorCursoDTO;
import com.example.campusmanager.service.CursoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cursos")
@RequiredArgsConstructor
public class CursoController {

    private final CursoService cursoService;

    // 1️⃣ Listar todos
    @GetMapping
    public ResponseEntity<List<Curso>> listarTodos() {
        return ResponseEntity.ok(cursoService.listarTodos());
    }

    // 2️⃣ Obtener por ID
    @GetMapping("/{id}")
    public ResponseEntity<Curso> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(cursoService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<CursoResponseDTO> crear(@RequestBody @Valid CursoRequestDTO dto) {
        Curso curso = new Curso();
        curso.setNombre(dto.getNombre());

        CursoResponseDTO creado = cursoService.crear(curso, dto.getProfesorId());
        return ResponseEntity.ok(creado);
    }


 

    // 5️⃣ Eliminar curso
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        cursoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{id}/profesores")
    public ResponseEntity<List<ProfesorCursoDTO>> obtenerProfesoresDeCurso(@PathVariable Long id) {
        return ResponseEntity.ok(cursoService.obtenerProfesoresPorCurso(id));
    }

    
    @PutMapping("/{id}")
    public ResponseEntity<CursoResponseDTO> actualizar(
            @PathVariable Long id,
            @RequestBody CursoUpdateDTO dto) {
        CursoResponseDTO actualizado = cursoService.actualizar(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    
}


  
