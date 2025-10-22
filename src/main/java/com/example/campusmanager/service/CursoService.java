package com.example.campusmanager.service;

import org.springframework.stereotype.Service;

import com.example.campusmanager.domain.Curso;
import com.example.campusmanager.repository.CursoRepository;

import java.util.List;

@Service
public class CursoService {

    private final CursoRepository repo;

    public CursoService(CursoRepository repo) {
        this.repo = repo;
    }

    public List<Curso> listarTodos() {
        // devolvemos lista (aunque esté vacía) para simplificar el contrato REST
        return repo.findAll();
    }

    public Curso crear(Curso c) {
        if (c.getTitulo() == null || c.getTitulo().isBlank()) {
            throw new IllegalArgumentException("El título es obligatorio.");
        }
        if (c.getDescripcion() == null || c.getDescripcion().isBlank()) {
            throw new IllegalArgumentException("La descripción es obligatoria.");
        }
        if (repo.existsByTitulo(c.getTitulo())) {
            throw new IllegalArgumentException("Ya existe un curso con ese título.");
        }
        return repo.save(c);
    }
}
