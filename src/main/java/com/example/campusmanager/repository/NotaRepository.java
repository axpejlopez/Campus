package com.example.campusmanager.repository;

import com.example.campusmanager.domain.Nota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NotaRepository extends JpaRepository<Nota, Long> {

    // Notas de un alumno
    @Query("SELECT n FROM Nota n WHERE n.alumno.id = :alumnoId")
    List<Nota> findByAlumnoId(@Param("alumnoId") Long alumnoId);

    // Media de un alumno
    @Query("SELECT AVG(n.notaFinal) FROM Nota n WHERE n.alumno.id = :alumnoId")
    Optional<Double> calcularMediaPorAlumno(@Param("alumnoId") Long alumnoId);

    // Media total del curso
    @Query("SELECT AVG(n.notaFinal) FROM Nota n")
    Optional<Double> calcularMediaTotal();
}
