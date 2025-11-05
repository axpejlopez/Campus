package com.example.campusmanager.controller;

import com.example.campusmanager.domain.Profesor;
import com.example.campusmanager.dto.AsignaturasProfesorDTO;
import com.example.campusmanager.dto.ProfesorDTO;
import com.example.campusmanager.service.ProfesorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profesores")
@RequiredArgsConstructor
public class ProfesorController {

    private final ProfesorService profesorService;

    // ✅ 1. Listar todos los profesores
    @GetMapping
    public ResponseEntity<List<Profesor>> listarProfesores() {
        return ResponseEntity.ok(profesorService.listarProfesores());
    }

    // ✅ 2. Crear un nuevo profesor
    @PostMapping
    public ResponseEntity<Profesor> crearProfesor(@RequestBody ProfesorDTO dto) {
        Profesor nuevo = profesorService.crearProfesor(dto);
        return ResponseEntity.ok(nuevo);
    }

    // ✅ 3. Obtener las asignaturas que imparte un profesor
    @GetMapping("/{nombre}/asignaturas")
    public ResponseEntity<List<AsignaturasProfesorDTO>> obtenerAsignaturasPorProfesor(@PathVariable String nombre) {
        List<AsignaturasProfesorDTO> asignaturas = profesorService.asignaturasPorProfesor(nombre);
        return ResponseEntity.ok(asignaturas);
    }

    // ✅ 4. Exportar las asignaturas del profesor en CSV
    @GetMapping(value = "/{nombre}/asignaturas.csv", produces = "text/csv")
    public ResponseEntity<String> exportarAsignaturasCSV(@PathVariable String nombre) {
        List<AsignaturasProfesorDTO> asignaturas = profesorService.asignaturasPorProfesor(nombre);

        StringBuilder csv = new StringBuilder();
        csv.append("ID;NombreProfesor;Asignatura;Curso\n");

        for (AsignaturasProfesorDTO dto : asignaturas) {
            csv.append(dto.getProfesorId()).append(";")
               .append(dto.getNombreProfesor()).append(";")
               .append(dto.getTituloAsignatura()).append(";")
               .append(dto.getCurso()).append("\n");
        }

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=asignaturas_profesor.csv")
                .body(csv.toString());
    }
}
