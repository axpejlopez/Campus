package com.example.campusmanager.service;

import com.example.campusmanager.domain.Alumno;
import com.example.campusmanager.domain.Nota;
import com.example.campusmanager.repository.AlumnoRepository;
import com.example.campusmanager.repository.NotaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class NotaService {

    private final NotaRepository notaRepository;
    private final AlumnoRepository alumnoRepository;

    public NotaService(NotaRepository notaRepository, AlumnoRepository alumnoRepository) {
        this.notaRepository = notaRepository;
        this.alumnoRepository = alumnoRepository;
    }

    // ✅ Obtener todas las notas de un alumno
    public List<Nota> obtenerNotasPorAlumno(Long alumnoId) {
        return notaRepository.findByAlumnoId(alumnoId);
    }

    // ✅ Calcular la media de un alumno
    public Double obtenerMediaPorAlumno(Long alumnoId) {
        return notaRepository.calcularMediaPorAlumno(alumnoId).orElse(0.0);
    }

    // ✅ Calcular la media general de la clase
    public Double obtenerMediaClase() {
        return notaRepository.calcularMediaTotal().orElse(0.0);
    }

    // ✅ Crear una nueva nota (ya enlazada a un alumno existente)
    public Nota crearNota(Long alumnoId, String asignatura, Double notaFinal) {
        Alumno alumno = alumnoRepository.findById(alumnoId)
                .orElseThrow(() -> new NoSuchElementException("El alumno con ID " + alumnoId + " no existe."));

        Nota nota = new Nota();
        nota.setAlumno(alumno);
        nota.setAsignatura(asignatura);
        nota.setNotaFinal(notaFinal);

        return notaRepository.save(nota);
    }

    public Nota actualizarNota(Long id, Nota datos) {
        Nota existente = notaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Nota no encontrada"));

        boolean modificado = false;

        if (datos.getNotaFinal() != null && !datos.getNotaFinal().equals(existente.getNotaFinal())) {
            existente.setNotaFinal(datos.getNotaFinal());
            modificado = true;
        }
        if (datos.getAsignatura() != null && !datos.getAsignatura().equals(existente.getAsignatura())) {
            existente.setAsignatura(datos.getAsignatura());
            modificado = true;
        }

        if (!modificado) {
            throw new IllegalArgumentException("No se ha modificado ningún dato de la nota.");
        }

        try {
            return notaRepository.save(existente);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Ya existe una nota registrada para ese alumno en esa asignatura.");
        }
    }


    // ✅ Eliminar nota por ID
    public void eliminarNota(Long id) {
        if (!notaRepository.existsById(id)) {
            throw new NoSuchElementException("Nota no encontrada");
        }
        notaRepository.deleteById(id);
    }
}



