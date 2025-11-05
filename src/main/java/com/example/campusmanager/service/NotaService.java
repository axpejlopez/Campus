package com.example.campusmanager.service;

import com.example.campusmanager.domain.Asignatura;
import com.example.campusmanager.domain.Matricula;
import com.example.campusmanager.domain.Nota;
import com.example.campusmanager.dto.NotaRequestDTO;
import com.example.campusmanager.dto.NotaResponseDTO;
import com.example.campusmanager.repository.AsignaturaRepository;
import com.example.campusmanager.repository.MatriculaRepository;
import com.example.campusmanager.repository.NotaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotaService {

    private final NotaRepository notaRepository;
    private final MatriculaRepository matriculaRepository;
    private final AsignaturaRepository asignaturaRepository;

    // 🟢 Obtener todas las notas de una matrícula
    public List<NotaResponseDTO> obtenerNotasPorMatricula(Long matriculaId) {
        return notaRepository.findByMatricula_Id(matriculaId)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // 🟢 Media por matrícula
    public Double obtenerMediaPorMatricula(Long matriculaId) {
        return notaRepository.findByMatricula_Id(matriculaId)
                .stream()
                .mapToDouble(Nota::getNotaFinal)
                .average()
                .orElse(0.0);
    }

    // 🟢 Media global
    public Double obtenerMediaClase() {
        return notaRepository.findAll()
                .stream()
                .mapToDouble(Nota::getNotaFinal)
                .average()
                .orElse(0.0);
    }

    // 🟢 Crear nueva nota
    public NotaResponseDTO crearNota(NotaRequestDTO dto) {
        Matricula matricula = matriculaRepository.findById(dto.getMatriculaId())
                .orElseThrow(() -> new IllegalArgumentException("La matrícula no existe."));

        Asignatura asignatura = asignaturaRepository.findById(dto.getAsignaturaId())
                .orElseThrow(() -> new IllegalArgumentException("La asignatura no existe."));

        // 🔒 Evita duplicados: una matrícula no puede tener dos notas para la misma asignatura
        boolean existe = notaRepository.existsByMatricula_IdAndAsignatura_Id(
                dto.getMatriculaId(), dto.getAsignaturaId());
        if (existe) {
            throw new IllegalArgumentException("Ya existe una nota para esta asignatura en esta matrícula.");
        }

        Nota nueva = Nota.builder()
                .matricula(matricula)
                .asignatura(asignatura)
                .notaFinal(dto.getNotaFinal())
                .build();

        return convertirADTO(notaRepository.save(nueva));
    }

    // 🟢 Actualizar nota
    public NotaResponseDTO actualizarNota(Long id, NotaRequestDTO dto) {
        Nota existente = notaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nota no encontrada."));

        existente.setNotaFinal(dto.getNotaFinal());
        return convertirADTO(notaRepository.save(existente));
    }

    // 🟢 Eliminar nota
    public void eliminarNota(Long id) {
        if (!notaRepository.existsById(id)) {
            throw new IllegalArgumentException("Nota no encontrada.");
        }
        notaRepository.deleteById(id);
    }

    // 🔁 Conversor entidad → DTO
    private NotaResponseDTO convertirADTO(Nota nota) {
        return NotaResponseDTO.builder()
                .id(nota.getId())
                .alumno(nota.getMatricula().getAlumno().getNombre())
                .curso(nota.getMatricula().getCurso().getNombre()) // asegúrate de usar 'titulo' o 'nombre' según tu entidad
                .asignatura(nota.getAsignatura().getTitulo())
                .profesor(nota.getAsignatura().getProfesor().getNombre())
                .notaFinal(nota.getNotaFinal())
                .build();
    }
}
