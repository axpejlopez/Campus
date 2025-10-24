package com.example.campusmanager.service;

import com.example.campusmanager.domain.Alumno;
import com.example.campusmanager.domain.Curso;
import com.example.campusmanager.domain.Matricula;
import com.example.campusmanager.repository.AlumnoRepository;
import com.example.campusmanager.repository.CursoRepository;
import com.example.campusmanager.repository.MatriculaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MatriculaService {

    private final MatriculaRepository repo;
    private final AlumnoRepository alumnoRepo;
    private final CursoRepository cursoRepo;

    public MatriculaService(MatriculaRepository repo, AlumnoRepository alumnoRepo, CursoRepository cursoRepo) {
        this.repo = repo;
        this.alumnoRepo = alumnoRepo;
        this.cursoRepo = cursoRepo;
    }

    public List<Matricula> listarTodas() {
        return repo.findAll();
    }

    public Matricula crear(Long alumnoId, Long cursoId, LocalDate fechaMatricula) {
        if (alumnoId == null || cursoId == null || fechaMatricula == null) {
            throw new IllegalArgumentException("Alumno, curso y fecha son obligatorios.");
        }

        if (repo.existsByAlumno_IdAndCurso_Id(alumnoId, cursoId)) {
            throw new IllegalArgumentException("El alumno ya está matriculado en ese curso.");
        }

        Alumno alumno = alumnoRepo.findById(alumnoId)
                .orElseThrow(() -> new IllegalArgumentException("Alumno no encontrado."));
        Curso curso = cursoRepo.findById(cursoId)
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado."));

        Matricula matricula = new Matricula(alumno, curso, fechaMatricula);
        return repo.save(matricula);
    }

    // 🔹 NUEVO MÉTODO: obtener alumnos de un curso
    public List<Alumno> obtenerAlumnosPorCurso(Long cursoId) {
        // Verificamos que el curso exista
        Curso curso = cursoRepo.findById(cursoId)
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado."));

        // Buscamos las matrículas y extraemos los alumnos
        return repo.findByCurso_Id(curso.getId())
                   .stream()
                   .map(Matricula::getAlumno)
                   .collect(Collectors.toList());
    }
}
