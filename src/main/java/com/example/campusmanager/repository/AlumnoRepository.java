package com.example.campusmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.campusmanager.domain.Alumno;

@Repository
public interface AlumnoRepository extends JpaRepository<Alumno, Long> {
}
