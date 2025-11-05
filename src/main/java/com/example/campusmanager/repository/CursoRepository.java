package com.example.campusmanager.repository;

import com.example.campusmanager.domain.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CursoRepository extends JpaRepository<Curso, Long> {
    List<Curso> findByProfesor_Id(Long profesorId);
}


