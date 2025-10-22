package com.example.campusmanager.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.campusmanager.dto.ProfesorDTO;
import com.example.campusmanager.entity.Profesor;
import com.example.campusmanager.repository.ProfesorRepository;

@Service
public class ProfesorService {
	
	@Autowired
	private ProfesorRepository profesorRepository;

	
	
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
	
	
}
