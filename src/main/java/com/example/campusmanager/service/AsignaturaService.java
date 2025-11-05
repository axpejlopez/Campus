package com.example.campusmanager.service;

import com.example.campusmanager.domain.Asignatura;
import com.example.campusmanager.domain.Curso;
import com.example.campusmanager.domain.Profesor;
import com.example.campusmanager.dto.AsignaturaRequestDTO;
import com.example.campusmanager.dto.AsignaturaResponseDTO;
import com.example.campusmanager.repository.AsignaturaRepository;
import com.example.campusmanager.repository.CursoRepository;
import com.example.campusmanager.repository.ProfesorRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AsignaturaService {
    private final AsignaturaRepository asignaturaRepository;
    private final ProfesorRepository profesorRepository;
    private final CursoRepository cursoRepository;

    public List<Asignatura> listarAsignaturas() {
        return asignaturaRepository.findAll();
    }

    public Asignatura obtenerPorId(Long id) {
        return asignaturaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Asignatura no encontrada."));
    }

    
    public AsignaturaResponseDTO crear(Asignatura asignatura) {
        if (asignatura.getTitulo() == null || asignatura.getTitulo().isBlank()) {
            throw new IllegalArgumentException("El título de la asignatura es obligatorio.");
        }

        // 🔹 Antes de guardar, cargamos las entidades completas
        Profesor profesor = null;
        Curso curso = null;

        if (asignatura.getProfesor() != null && asignatura.getProfesor().getId() != null) {
            profesor =profesorRepository.findById(asignatura.getProfesor().getId())
                    .orElseThrow(() -> new NoSuchElementException("Profesor no encontrado"));
            asignatura.setProfesor(profesor);
        }

        if (asignatura.getCurso() != null && asignatura.getCurso().getId() != null) {
            curso = cursoRepository.findById(asignatura.getCurso().getId())
                    .orElseThrow(() -> new NoSuchElementException("Curso no encontrado"));
            asignatura.setCurso(curso);
        }

        // 🔹 Guardamos la asignatura con las relaciones correctas
        Asignatura nueva = asignaturaRepository.save(asignatura);

        // 🔹 Devolvemos el DTO con la información completa
        return convertirADTO(nueva);
    }

    
    
    public AsignaturaResponseDTO convertirADTO(Asignatura asignatura) {
        return AsignaturaResponseDTO.builder()
                .id(asignatura.getId())
                .titulo(asignatura.getTitulo())
                .profesorNombre(asignatura.getProfesor() != null ? asignatura.getProfesor().getNombre() : null)
                .profesorEspecialidad(asignatura.getProfesor() != null ? asignatura.getProfesor().getEspecialidad() : null)
                .cursoNombre(asignatura.getCurso() != null ? asignatura.getCurso().getNombre() : null)
                .build();
    }
    
    public AsignaturaResponseDTO actualizar(Long id, Asignatura datos) {
        Asignatura existente = asignaturaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Asignatura no encontrada"));

        if (datos.getTitulo() != null && !datos.getTitulo().isBlank()) {
            existente.setTitulo(datos.getTitulo());
        }

        if (datos.getProfesor() != null && datos.getProfesor().getId() != null) {
            Profesor profesor = profesorRepository.findById(datos.getProfesor().getId())
                    .orElseThrow(() -> new NoSuchElementException("Profesor no encontrado"));
            existente.setProfesor(profesor);
        }

        if (datos.getCurso() != null && datos.getCurso().getId() != null) {
            Curso curso = cursoRepository.findById(datos.getCurso().getId())
                    .orElseThrow(() -> new NoSuchElementException("Curso no encontrado"));
            existente.setCurso(curso);
        }

        Asignatura actualizada = asignaturaRepository.save(existente);
        return convertirADTO(actualizada);
    }
    
    public void eliminar(Long id) {
        Asignatura asignatura = asignaturaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Asignatura no encontrada"));
        asignaturaRepository.delete(asignatura);
    }
    
    public Asignatura crear(AsignaturaRequestDTO dto) {
        if (dto.getTitulo() == null || dto.getTitulo().isBlank()) {
            throw new IllegalArgumentException("El título de la asignatura es obligatorio.");
        }

        Profesor profesor = profesorRepository.findById(dto.getProfesorId())
                .orElseThrow(() -> new IllegalArgumentException("Profesor no encontrado."));

        Curso curso = cursoRepository.findById(dto.getCursoId())
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado."));

        Asignatura asignatura = new Asignatura();
        asignatura.setTitulo(dto.getTitulo());
        asignatura.setProfesor(profesor);
        asignatura.setCurso(curso);

        return asignaturaRepository.save(asignatura);
    }

    
    public List<AsignaturaResponseDTO> listarAsignaturasDetalle() {
        return asignaturaRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .toList();
    }




}
