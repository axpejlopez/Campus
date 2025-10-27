package com.example.campusmanager.service;

import com.example.campusmanager.domain.Alumno;
import com.example.campusmanager.domain.Curso;
import com.example.campusmanager.domain.Matricula;
import com.example.campusmanager.domain.Profesor;
import com.example.campusmanager.dto.AlumnoPorCursoDTO;
import com.example.campusmanager.repository.AlumnoRepository;
import com.example.campusmanager.repository.CursoRepository;
import com.example.campusmanager.repository.MatriculaRepository;
import com.example.campusmanager.repository.ProfesorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MatriculaService {

    private final MatriculaRepository repo;
    private final AlumnoRepository alumnoRepo;
    private final CursoRepository cursoRepo;
    
    @Autowired
    private final ProfesorRepository profesorRepo;

    public MatriculaService(MatriculaRepository repo, AlumnoRepository alumnoRepo, CursoRepository cursoRepo, ProfesorRepository profesorRepo) {
        this.repo = repo;
        this.alumnoRepo = alumnoRepo;
        this.cursoRepo = cursoRepo;
        this.profesorRepo = profesorRepo;
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
    public List<AlumnoPorCursoDTO> obtenerAlumnosPorCurso(Long cursoId) {
        
    	
    	// Verificamos que el curso exista
    	    	
    	Curso curso = cursoRepo.findById(cursoId)
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado."));
    	
    	
        List<AlumnoPorCursoDTO> listAlumnoPorCursoDTO = new ArrayList<>();
        Profesor profesor = curso.getProfesor();
        List<Matricula> matriculas = repo.findByCurso_Id(cursoId);
               
        for (Matricula m: matriculas) {
          	AlumnoPorCursoDTO alumnoPorCursoDTO = new AlumnoPorCursoDTO();
        
        	alumnoPorCursoDTO.tituloCurso = curso.getTitulo();
            alumnoPorCursoDTO.descripcionCurso = curso.getDescripcion();
            
            alumnoPorCursoDTO.nombreProfesor = profesor.getNombre();         
        	
        	alumnoPorCursoDTO.alumnoId = m.getAlumno().getId();
        	alumnoPorCursoDTO.nombreAlumno = m.getAlumno().getNombre();
        	alumnoPorCursoDTO.emailAlumno = m.getAlumno().getEmail();
        	
        	listAlumnoPorCursoDTO.add(alumnoPorCursoDTO);
        	
        			          
        	
        }
        return listAlumnoPorCursoDTO;
    }
}
