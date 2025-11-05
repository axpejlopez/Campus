package com.example.campusmanager.repository;

import com.example.campusmanager.domain.Asignatura;
import com.example.campusmanager.domain.Profesor;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AsignaturaRepository extends JpaRepository<Asignatura, Long> {


@Query("SELECT DISTINCT a.profesor FROM Asignatura a WHERE a.curso.id = :cursoId")
List<Profesor> findProfesoresByCursoId(@Param("cursoId") Long cursoId);



@Query("SELECT a.titulo FROM Asignatura a WHERE a.curso.id = :cursoId AND a.profesor.id = :profesorId")
List<String> findAsignaturasByCursoAndProfesor(@Param("cursoId") Long cursoId, @Param("profesorId") Long profesorId);

}