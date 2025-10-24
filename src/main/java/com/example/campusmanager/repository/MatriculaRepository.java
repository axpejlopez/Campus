package com.example.campusmanager.repository;

import com.example.campusmanager.domain.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, Long> {

    boolean existsByAlumno_IdAndCurso_Id(Long alumnoId, Long cursoId);

    // 🔹 NUEVO MÉTODO: obtener todas las matrículas de un curso concreto
    List<Matricula> findByCurso_Id(Long cursoId);
}
