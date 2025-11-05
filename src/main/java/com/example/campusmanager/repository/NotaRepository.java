package com.example.campusmanager.repository;

import com.example.campusmanager.domain.Nota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NotaRepository extends JpaRepository<Nota, Long> {

    List<Nota> findByMatricula_Id(Long matriculaId);
    
    boolean existsByMatricula_IdAndAsignatura_Id(Long matriculaId, Long asignaturaId);

    @Query("SELECT AVG(n.notaFinal) FROM Nota n WHERE n.matricula.id = :matriculaId")
    Optional<Double> calcularMediaPorMatricula(Long matriculaId);

    @Query("SELECT AVG(n.notaFinal) FROM Nota n")
    Optional<Double> calcularMediaTotal();
}

