package com.example.campusmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.campusmanager.domain.Nota;


@Repository
public interface NotaRepository extends JpaRepository<Nota, Long> {
    // Aquí más adelante añadiremos consultas personalizadas
}
