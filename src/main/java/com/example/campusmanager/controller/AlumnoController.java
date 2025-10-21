package com.example.campusmanager.controller;

import com.example.campusmanager.domain.Alumno;
import com.example.campusmanager.service.AlumnoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alumnos")
public class AlumnoController {

    private final AlumnoService alumnoService;

    public AlumnoController(AlumnoService alumnoService) {
        this.alumnoService = alumnoService;
    }

    // Obtener todos los alumnos
    @GetMapping("/obtenerTodos")
    public List<Alumno> listarTodos() {
        return alumnoService.listarTodos();
    }

    // Crear un nuevo alumno
    @PostMapping ("/crear")
    public Alumno crearAlumno(@RequestBody Alumno alumno) {
        return alumnoService.crearAlumno(alumno);
    }
}
