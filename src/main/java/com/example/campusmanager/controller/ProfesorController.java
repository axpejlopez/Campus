package com.example.campusmanager.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.campusmanager.domain.Profesor;
import com.example.campusmanager.dto.CursosProfesorDTO;
import com.example.campusmanager.dto.ProfesorDTO;
import com.example.campusmanager.service.ProfesorService;

@RestController
@RequestMapping("/profesores")
public class ProfesorController {
	
	@Autowired
	private  ProfesorService profesorService;
	
	@GetMapping("/getAll")
	public List<Profesor> getProfesores(){
		return profesorService.getProfesores();
		
	}
	
	@PostMapping("/createProfesor")
	public Profesor createProfesor(@RequestBody ProfesorDTO profesorDTO) {
		
		System.out.println(profesorDTO.nombre + " " + profesorDTO.especialidad);
		
		return profesorService.createProfesor(profesorDTO);
	}
	
	@GetMapping("/cursosProfesor")
	public CursosProfesorDTO cursosProfesor(@RequestBody String nombre) {
		
		return profesorService.cursosProfesor(nombre);
	}
}
