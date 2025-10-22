package com.example.campusmanager.service;

import com.example.campusmanager.domain.Alumno;
import com.example.campusmanager.repository.AlumnoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlumnoService {

    private final AlumnoRepository alumnoRepository;

    // Constructor: Spring inyecta el repositorio automáticamente
    public AlumnoService(AlumnoRepository alumnoRepository) {
        this.alumnoRepository = alumnoRepository;
    }

    // Obtener todos los alumnos
    public List<Alumno> listarTodos() {
        return alumnoRepository.findAll();
       
    }

    // Crear un nuevo alumno
    public Alumno crearAlumno(Alumno alumno) {
        // Aquí podríamos añadir validaciones si es necesario
        return alumnoRepository.save(alumno);
    }
}
