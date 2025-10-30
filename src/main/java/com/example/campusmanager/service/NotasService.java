package com.example.campusmanager.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import com.example.campusmanager.domain.Matricula;
import com.example.campusmanager.domain.Notas;
import com.example.campusmanager.dto.NotaAddUpdateDTO;
import com.example.campusmanager.dto.NotaDTO;
import com.example.campusmanager.dto.NotaDeleteDTO;
import com.example.campusmanager.exception.Excepcion;
import com.example.campusmanager.repository.MatriculaRepository;
import com.example.campusmanager.repository.NotasRepository;

@Service
public class NotasService {
	
	@Autowired
	MatriculaRepository matriculaRepository;
	
	@Autowired
	NotasRepository notasRepository;
	
	public List<NotaDTO> getNotasAlumno(Long alumnoId){
		List<NotaDTO> notasDTO = new ArrayList<>();
		List<Matricula> matriculas = matriculaRepository.findAllByAlumno_id(alumnoId);
		
		if (matriculas.isEmpty()){
			throw new Excepcion(HttpStatus.NOT_FOUND,"No Encontrado", "El alumno no se encuentra matriculado en ningún curso");
		}
		
		for(Matricula m: matriculas) {
			NotaDTO notaDTO = new NotaDTO();
			notaDTO.alumnoId = m.getAlumno().getId();
			notaDTO.alumnoNombre = m.getAlumno().getNombre();
			notaDTO.cursoTitulo = m.getCurso().getTitulo();
			
			if (m.getNotas() == null) {
				notaDTO.notaFinal = new BigDecimal("-1");
			}else {
				notaDTO.notaFinal = m.getNotas().getNotaFinal();
			}
			
			notasDTO.add(notaDTO);
			
		}
		return notasDTO;
		
	}
	
	
	public BigDecimal getNotaMediaAlumno(Long alumnoId) {
		
		return notasRepository.findPromedioNotaFinalPorAlumnoId(alumnoId);		
		
	}
	
	
	public NotaDTO newNotaAlumnoCurso(NotaAddUpdateDTO datosNota) {
		
		Optional<Matricula> matricula = matriculaRepository.findAllByAlumnoIdAndCursoId(datosNota.alumnoId, datosNota.cursoId);
										
		
		if (matricula.isEmpty()) {
			//Mensaje de error
			throw new Excepcion(HttpStatus.NOT_FOUND, "No Encontrado", "No se ha Matriculado el Alumno en el Curso");
		}
		
		
		
		if (matricula.get().getNotas() != null) { //No debe haber notas cargadas, debe ser una nota nueva
			
			throw new Excepcion(HttpStatus.NOT_FOUND, "Hay Notas cargadas", "Se encuentran notas cargadas, no se cargará la nota solicitada");
			
		}
		
		Notas notas = new Notas();
		notas.setMatricula(matricula.get());
		notas.setNotaFinal(datosNota.notaFinal);
		notasRepository.save(notas);
		
		NotaDTO notaDTO = new NotaDTO();
		notaDTO.alumnoId = matricula.get().getAlumno().getId();
		notaDTO.alumnoNombre = matricula.get().getAlumno().getNombre();
		notaDTO.cursoTitulo = matricula.get().getCurso().getTitulo();
		notaDTO.notaFinal = notas.getNotaFinal();			
		
		return notaDTO;
	}
	
	public NotaDTO updateNotaAlumnoCurso(NotaAddUpdateDTO datosNota) {
		Optional<Matricula> matricula = matriculaRepository.findAllByAlumnoIdAndCursoId(datosNota.alumnoId, datosNota.cursoId);
		
		if (matricula.isEmpty()) {
			throw new Excepcion(HttpStatus.NOT_FOUND, "No Encontrado", "El alumno no se encuentra matriculado en el curso");
		}
		
		if (matricula.get().getNotas() == null) {
			throw new Excepcion(HttpStatus.NOT_FOUND, "No han sido cargadas notas", "No hay ninguna nota cargada para ese Alumno en el Curso");
		}
		
		matricula.get().getNotas().setNotaFinal(datosNota.notaFinal);
		notasRepository.save(matricula.get().getNotas());
		
		NotaDTO notaDTO = new NotaDTO();
		notaDTO.alumnoId = matricula.get().getAlumno().getId();
		notaDTO.alumnoNombre = matricula.get().getAlumno().getNombre();
		notaDTO.cursoTitulo = matricula.get().getCurso().getTitulo();
		notaDTO.notaFinal = matricula.get().getNotas().getNotaFinal();
		
		return   notaDTO;
		
			
	}
	
	public Boolean deleteNotaAlumnoCurso(NotaDeleteDTO datosAlumnoCurso) {
		
		Optional<Matricula> matricula = matriculaRepository.findAllByAlumnoIdAndCursoId(datosAlumnoCurso.alumnoId, datosAlumnoCurso.cursoId);
		
		if (matricula.isEmpty()) {
			throw new Excepcion(HttpStatus.NOT_FOUND, "No Encontrado", "El alumno no se encuentra matriculado en el curso");
		}
		
		if (matricula.get().getNotas() == null) {
			throw new Excepcion(HttpStatus.NOT_FOUND, "No han sido cargadas notas", "No hay ninguna nota cargada para ese Alumno en el Curso");
		}
		
		
		
		try {
			notasRepository.delete(matricula.get().getNotas());
			return true;
			
		}catch (Exception e) {
			throw new Excepcion(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "La nota  no ha podido ser borrada");
		}
		
		
	}
	
		
		
	public BigDecimal getNotaMediaCurso(Long cursoId) {
		
		return notasRepository.findPromedioNotaFinalPorCursoId(cursoId);		
		
	}	
		
		
	
	
	
	
}
