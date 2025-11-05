package com.example.campusmanager.service;

import com.example.campusmanager.domain.Asignatura;
import com.example.campusmanager.domain.Profesor;
import com.example.campusmanager.dto.AsignaturasProfesorDTO;
import com.example.campusmanager.dto.ProfesorDTO;
import com.example.campusmanager.repository.AsignaturaRepository;
import com.example.campusmanager.repository.ProfesorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfesorService {

    private final ProfesorRepository profesorRepository;
    private final AsignaturaRepository asignaturaRepository;

    public List<Profesor> listarProfesores() {
        return profesorRepository.findAll();
    }

    public Profesor crearProfesor(ProfesorDTO dto) {
        if (dto.getNombre() == null || dto.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del profesor es obligatorio.");
        }

        Profesor profesor = new Profesor();
        profesor.setNombre(dto.getNombre());
        profesor.setEspecialidad(dto.getEspecialidad());

        return profesorRepository.save(profesor);
    }

    public List<AsignaturasProfesorDTO> asignaturasPorProfesor(String nombreProfesor) {
        Profesor profesor = profesorRepository.findByNombre(nombreProfesor)
                .orElseThrow(() -> new IllegalArgumentException("No existe un profesor con el nombre: " + nombreProfesor));

        return asignaturaRepository.findAll().stream()
                .filter(a -> a.getProfesor().equals(profesor))
                .map(a -> AsignaturasProfesorDTO.builder()
                        .profesorId(profesor.getId())
                        .nombreProfesor(profesor.getNombre())
                        .tituloAsignatura(a.getTitulo())
                        .curso(a.getCurso().getNombre())
                        .build())
                .collect(Collectors.toList());
    }
}


