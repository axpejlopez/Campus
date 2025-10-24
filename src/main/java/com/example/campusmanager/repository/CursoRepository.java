package com.example.campusmanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.campusmanager.domain.Curso;



@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
    boolean existsByTitulo(String titulo);
    
}
