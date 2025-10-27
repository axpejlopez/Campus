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
 // ✅ Actualizar un alumno existente
    public String actualizarAlumno(Long id, Alumno alumnoNuevo) {
        Alumno alumnoExistente = alumnoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado"));

        boolean modificado = false;

        if (alumnoNuevo.getNombre() != null && !alumnoNuevo.getNombre().equals(alumnoExistente.getNombre())) {
            alumnoExistente.setNombre(alumnoNuevo.getNombre());
            modificado = true;
        }
        if (alumnoNuevo.getEmail() != null && !alumnoNuevo.getEmail().equals(alumnoExistente.getEmail())) {
            alumnoExistente.setEmail(alumnoNuevo.getEmail());
            modificado = true;
        }
        if (alumnoNuevo.getFechaNacimiento() != null &&
                !alumnoNuevo.getFechaNacimiento().equals(alumnoExistente.getFechaNacimiento())) {
            alumnoExistente.setFechaNacimiento(alumnoNuevo.getFechaNacimiento());
            modificado = true;
        }

        if (!modificado) {
            return "❌ No se realizaron cambios. El alumno no ha sido modificado.";
        }

        alumnoRepository.save(alumnoExistente);
        return "✅ Alumno actualizado correctamente.";
    }

}
