package com.example.campusmanager.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.aspectj.util.LangUtil.ProcessController.Thrown;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.campusmanager.dto.NotaAddUpdateDTO;
import com.example.campusmanager.dto.NotaDTO;
import com.example.campusmanager.dto.NotaDeleteDTO;
import com.example.campusmanager.exception.Excepcion;
import com.example.campusmanager.service.NotasService;

@RestController
@RequestMapping("/api/notas")
public class NotasController {
	
	@Autowired
	private NotasService notasService;
	
	/**
	 * 
	 * @param alumnoId
	 * @return
	 * 
	 * Nos pide el Id del alumno, y nos devuelve una lista con las asignaturas y sus notas
	 * 
	 */
	@GetMapping("/notasAlumno")
	public ResponseEntity<?> getNotasAlumno(@RequestParam("alumnoId") Long alumnoId){
		
		
		// Los errores se han capturado en el Service y se han tratado con GlobalExceptionHandler.	
		return ResponseEntity.ok(notasService.getNotasAlumno(alumnoId));
		
	}
	
	/**
	 * 
	 * @param alumnoId
	 * @return
	 * 
	 * Nos pide el id del alumno y nos devuelve la nota media de las asignaturas en las que se encuentra matriculado
	 * 
	 * 
	 * 
	 */
	
	@GetMapping("/notaMediaAlumno")
	public ResponseEntity<?> getNotaMediaAlumno(@RequestParam("alumnoId") Long alumnoId){
		
		BigDecimal notaMedia  = notasService.getNotaMediaAlumno(alumnoId);
		
		if (notaMedia  == null) {
			throw new Excepcion(HttpStatus.NOT_FOUND, "No Encontrado", "No hay notas con la que calcular la Media del alumno: " + alumnoId); 		
		}
		
		return ResponseEntity.ok(notaMedia);
	}
	
		
	@PostMapping("/newNotaAlumnoCurso")
	public ResponseEntity<?> newNotaAlumnoCurso(@RequestBody NotaAddUpdateDTO datosNota){
		
		if(datosNota.notaFinal.intValue() < 0 || datosNota.notaFinal.intValue() > 10 ) {
			throw new Excepcion(HttpStatus.NOT_FOUND, "Nota no valida", "La nota no se encuentra entre 0 y 10.");
		}
		
		NotaDTO notaDTO = notasService.newNotaAlumnoCurso(datosNota); 
		
		
		
		
		if (notaDTO == null) {
			throw new Excepcion(HttpStatus.NOT_FOUND, "No Encontrado", "El alumno no se encuentra matriculado en el curso.");
		}
		
		return ResponseEntity.ok(notaDTO);
		
		
	}
	
	
	
	
	@PutMapping("/updateNotaAlumnoCurso")
	public ResponseEntity<?> updateNotaAlumnoCurso(@RequestBody NotaAddUpdateDTO datosNota){
		
		if(datosNota.notaFinal.intValue() < 0 || datosNota.notaFinal.intValue() > 10 ) {
			throw new Excepcion(HttpStatus.NOT_FOUND, "Nota no valida", "La nota no se encuentra entre 0 y 10.");
		}
		
		NotaDTO notaDTO = notasService.updateNotaAlumnoCurso(datosNota);
		
		return ResponseEntity.ok(notaDTO);
		
	}
	
	
	@DeleteMapping("/deleteNotaAlumnoCurso")
	public ResponseEntity<?> deleteNotaAlumnoCurso(@RequestBody NotaDeleteDTO datosAlumnoCurso){
		
		if(notasService.deleteNotaAlumnoCurso(datosAlumnoCurso)) {
			return ResponseEntity.ok("Nota borrada correctamente.");
		}
		
		return ResponseEntity.badRequest().body("No ha podido ser eliminado");
		
		
		
	}
	
	
	@GetMapping("/getNotaMediaCurso")
	public ResponseEntity<?> getNotaMediaCurso(@RequestParam("cursoId") Long cursoId){
		
		BigDecimal notaMedia  = notasService.getNotaMediaCurso(cursoId);
		
		if (notaMedia  == null) {
			throw new Excepcion(HttpStatus.NOT_FOUND, "No Encontrado", "No hay notas con la que calcular la Media del alumno: " + alumnoId); 		
		}
		
		return ResponseEntity.ok("La nota media del curso es: " + notaMedia);
		
	}
		
	
	
	

}
