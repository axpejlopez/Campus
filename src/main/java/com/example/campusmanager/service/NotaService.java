package com.example.campusmanager.service;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import com.example.campusmanager.domain.Alumno;
import com.example.campusmanager.domain.Nota;
import com.example.campusmanager.repository.NotaRepository;
import com.example.campusmanager.repository.AlumnoRepository;

@Service
public class NotaService {

    private final NotaRepository notaRepository;
    private final AlumnoRepository alumnoRepository;

    public NotaService(NotaRepository notaRepository, AlumnoRepository alumnoRepository) {
        this.notaRepository = notaRepository;
        this.alumnoRepository = alumnoRepository;
    }

    // ✅ 1. Obtener todas las notas de un alumno
    public List<Nota> obtenerNotasPorAlumno(Long alumnoId) {
        Alumno alumno = alumnoRepository.findById(alumnoId)
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado con id: " + alumnoId));

        return notaRepository.findAll().stream()
                .filter(n -> n.getAlumno().getId().equals(alumnoId))
                .toList();
    }

    // ✅ 2. Calcular la media de notas de un alumno
    public BigDecimal obtenerMediaPorAlumno(Long alumnoId) {
        List<Nota> notas = obtenerNotasPorAlumno(alumnoId);
        if (notas.isEmpty()) {
            throw new RuntimeException("El alumno no tiene notas registradas");
        }

        BigDecimal suma = notas.stream()
                .map(Nota::getNotaFinal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return suma.divide(BigDecimal.valueOf(notas.size()), 2, RoundingMode.HALF_UP);
    }

    // ✅ 3. Calcular la media general de todos los alumnos
    public BigDecimal obtenerMediaGeneral() {
        List<Nota> todasLasNotas = notaRepository.findAll();
        if (todasLasNotas.isEmpty()) {
            throw new RuntimeException("No hay notas registradas en el sistema");
        }

        BigDecimal suma = todasLasNotas.stream()
                .map(Nota::getNotaFinal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return suma.divide(BigDecimal.valueOf(todasLasNotas.size()), 2, RoundingMode.HALF_UP);
    }

    // ✅ 4. Añadir una nueva nota
    public Nota agregarNota(Long alumnoId, String asignatura, BigDecimal notaFinal) {
        Alumno alumno = alumnoRepository.findById(alumnoId)
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado con id: " + alumnoId));

        Nota nuevaNota = new Nota(alumno, asignatura, notaFinal);
        return notaRepository.save(nuevaNota);
    }

    // ✅ 5. Modificar una nota existente
    public Nota actualizarNota(Long id, BigDecimal nuevaNota) {
        Nota nota = notaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nota no encontrada con id: " + id));

        nota.setNotaFinal(nuevaNota);
        return notaRepository.save(nota);
    }

    // ✅ 6. Eliminar una nota
    public void eliminarNota(Long id) {
        if (!notaRepository.existsById(id)) {
            throw new RuntimeException("No existe una nota con id: " + id);
        }
        notaRepository.deleteById(id);
    }
}
