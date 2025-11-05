package com.example.campusmanager.service;

import com.example.campusmanager.domain.Curso;
import com.example.campusmanager.domain.Profesor;
import com.example.campusmanager.dto.CursoResponseDTO;
import com.example.campusmanager.dto.CursoUpdateDTO;
import com.example.campusmanager.dto.ProfesorCursoDTO;
import com.example.campusmanager.repository.AsignaturaRepository;
import com.example.campusmanager.repository.CursoRepository;
import com.example.campusmanager.repository.ProfesorRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class CursoService {

    private final CursoRepository cursoRepository;
    private final ProfesorRepository profesorRepository;
    private final AsignaturaRepository asignaturaRepository;


    public List<Curso> listarTodos() {
        return cursoRepository.findAll();
    }

    public Curso obtenerPorId(Long id) {
        return cursoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Curso no encontrado"));
    }

    public CursoResponseDTO crear(Curso curso, Long profesorId) {
        if (curso.getNombre() == null || curso.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del curso es obligatorio.");
        }

        if (profesorId == null) {
            throw new IllegalArgumentException("Debe asignarse un profesor al curso.");
        }

        Profesor profesor = profesorRepository.findById(profesorId)
                .orElseThrow(() -> new NoSuchElementException("Profesor no encontrado."));

        curso.setProfesor(profesor);
        curso.setId(null);

        Curso guardado = cursoRepository.save(curso);

        return convertirADTO(guardado);
    }




    
    public CursoResponseDTO actualizar(Long id, CursoUpdateDTO dto) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Curso no encontrado"));

        if (dto.getNombre() != null && !dto.getNombre().isBlank()) {
            curso.setNombre(dto.getNombre());
        }

        if (dto.getProfesorId() != null) {
            Profesor profesor = profesorRepository.findById(dto.getProfesorId())
                    .orElseThrow(() -> new NoSuchElementException("Profesor no encontrado"));
            curso.setProfesor(profesor);
        }

        Curso actualizado = cursoRepository.save(curso);
        return convertirADTO(actualizado);
    }


    public void eliminar(Long id) {
        if (!cursoRepository.existsById(id)) {
            throw new NoSuchElementException("Curso no encontrado");
        }
        cursoRepository.deleteById(id);
    }

    public List<Curso> listarPorProfesor(Long profesorId) {
        return cursoRepository.findByProfesor_Id(profesorId);
    }
    
   

    public List<ProfesorCursoDTO> obtenerProfesoresPorCurso(Long cursoId) {
        List<Profesor> profesores = asignaturaRepository.findProfesoresByCursoId(cursoId);

        return profesores.stream()
                .map(profesor -> {
                    List<String> asignaturas = asignaturaRepository.findAsignaturasByCursoAndProfesor(cursoId, profesor.getId());
                    return ProfesorCursoDTO.builder()
                            .id(profesor.getId())
                            .nombre(profesor.getNombre())
                            .especialidad(profesor.getEspecialidad())
                            .asignaturas(String.join(", ", asignaturas))
                            .build();
                })
                .toList();
    }
    
    private CursoResponseDTO convertirADTO(Curso curso) {
        return CursoResponseDTO.builder()
                .id(curso.getId())
                .nombre(curso.getNombre())
                .profesorId(
                        curso.getProfesor() != null ? curso.getProfesor().getId() : null)
                .profesorNombre(
                        curso.getProfesor() != null ? curso.getProfesor().getNombre() : null)
                .profesorEspecialidad(
                        curso.getProfesor() != null ? curso.getProfesor().getEspecialidad() : null)
                .build();
    }


}

