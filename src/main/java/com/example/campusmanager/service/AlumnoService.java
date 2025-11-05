package com.example.campusmanager.service;

import com.example.campusmanager.domain.Alumno;
import com.example.campusmanager.dto.AlumnoRequestDTO;
import com.example.campusmanager.dto.AlumnoResponseDTO;
import com.example.campusmanager.repository.AlumnoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlumnoService {

    private final AlumnoRepository repo;

    public AlumnoService(AlumnoRepository repo) {
        this.repo = repo;
    }

    // 🟢 Listar todos los alumnos
    public List<AlumnoResponseDTO> listarTodos() {
        return repo.findAll().stream()
                .map(a -> new AlumnoResponseDTO(
                        a.getId(),
                        a.getNombre(),
                        a.getEmail(),
                        a.getFechaNacimiento()
                ))
                .collect(Collectors.toList());
    }

    // 🟢 Obtener un alumno por su ID
    public AlumnoResponseDTO obtenerPorId(Long id) {
        Alumno alumno = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Alumno no encontrado."));
        return new AlumnoResponseDTO(
                alumno.getId(),
                alumno.getNombre(),
                alumno.getEmail(),
                alumno.getFechaNacimiento()
        );
    }

    // 🟢 Crear un nuevo alumno
    public AlumnoResponseDTO crear(AlumnoRequestDTO dto) {
        Alumno alumno = new Alumno();
        alumno.setNombre(dto.getNombre());
        alumno.setEmail(dto.getEmail());
        alumno.setFechaNacimiento(dto.getFechaNacimiento());
        Alumno guardado = repo.save(alumno);
        return new AlumnoResponseDTO(
                guardado.getId(),
                guardado.getNombre(),
                guardado.getEmail(),
                guardado.getFechaNacimiento()
        );
    }

    // 🟢 Actualizar un alumno existente
    public AlumnoResponseDTO actualizar(Long id, AlumnoRequestDTO dto) {
        Alumno alumno = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Alumno no encontrado."));
        alumno.setNombre(dto.getNombre());
        alumno.setEmail(dto.getEmail());
        alumno.setFechaNacimiento(dto.getFechaNacimiento());
        Alumno actualizado = repo.save(alumno);
        return new AlumnoResponseDTO(
                actualizado.getId(),
                actualizado.getNombre(),
                actualizado.getEmail(),
                actualizado.getFechaNacimiento()
        );
    }

    // 🟢 Eliminar un alumno
    public void eliminar(Long id) {
        if (!repo.existsById(id)) {
            throw new IllegalArgumentException("Alumno no encontrado.");
        }
        repo.deleteById(id);
    }
}

