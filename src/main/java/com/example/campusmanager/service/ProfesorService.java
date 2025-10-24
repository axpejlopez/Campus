package com.example.campusmanager.service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.campusmanager.domain.Curso;
import com.example.campusmanager.domain.Profesor;
import com.example.campusmanager.dto.CursosProfesorDTO;
import com.example.campusmanager.dto.ProfesorDTO;
import com.example.campusmanager.repository.CursoRepository;
import com.example.campusmanager.repository.ProfesorRepository;

@Service
public class ProfesorService {
	
	@Autowired
	private ProfesorRepository profesorRepository;
	
	@Autowired
	private CursoRepository cursoRepository;
	
	
	public List<Profesor> getProfesores(){
		return profesorRepository.findAll();
	}
	
	
	public Profesor createProfesor(ProfesorDTO profesorDTO){
		
		if (profesorDTO.nombre.isEmpty()) {
			throw new IllegalArgumentException("El nombre es obligatorio.");
		}
		
		Profesor profesor = new Profesor();
		
		profesor.setNombre(profesorDTO.nombre);
		
		if (!profesorDTO.especialidad.isEmpty()) {
			profesor.setEspecialidad(profesorDTO.especialidad);
		}
		System.out.println(profesorDTO);
		
		return profesorRepository.save(profesor);
		
	}
	
	
	/**
	 * 
	 * @param nombre
	 * @return
	 * 
	 * Devuelve la lista de cursos que da un profesor.
	 */
	public CursosProfesorDTO cursosProfesor(String nombre){
		
		
		Profesor profesor = profesorRepository.findByNombre(nombre)
				.orElseThrow(()->new RuntimeException("No existe un profesor con ese nombre: " + nombre));
		
		CursosProfesorDTO cursoProfesorDTO = new CursosProfesorDTO();
		cursoProfesorDTO.nombreProfesor = nombre;
		
		List<Curso> listCursos = profesor.getCursos();
		
		cursoProfesorDTO.cursos = new HashMap<>();
		
		for(Curso c : listCursos ) {
			cursoProfesorDTO.cursos.put(c.getTitulo(),c.getDescripcion());
		}
		
		return cursoProfesorDTO;
					
		
	}
	
	
}
