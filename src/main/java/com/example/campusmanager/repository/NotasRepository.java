package com.example.campusmanager.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.campusmanager.domain.Notas;

@Repository
public interface NotasRepository extends JpaRepository<Notas, Long> {
	
	// Promedio de notas de un alumno específico
	@Query("SELECT AVG(n.notaFinal) FROM Notas n WHERE n.matricula.alumno.id = :alumnoId")
	BigDecimal findPromedioNotaFinalPorAlumnoId(@Param("alumnoId") Long alumnoId);

	
	// Promedio de notas de un alumno específico
	@Query("SELECT AVG(n.notaFinal) FROM Notas n WHERE n.matricula.curso.id = :cursoId")
	BigDecimal findPromedioNotaFinalPorCursoId(@Param("cursoId") Long cursoId);

}
