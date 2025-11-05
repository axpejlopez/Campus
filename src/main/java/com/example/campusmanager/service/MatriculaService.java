package com.example.campusmanager.service;

import com.example.campusmanager.domain.Alumno;
import com.example.campusmanager.domain.Curso;
import com.example.campusmanager.domain.Matricula;
import com.example.campusmanager.repository.AlumnoRepository;
import com.example.campusmanager.repository.CursoRepository;
import com.example.campusmanager.repository.MatriculaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

// 🔹 Librerías para generar PDF
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import java.io.ByteArrayOutputStream;
import java.util.stream.Stream;

@Service
public class MatriculaService {

    private final MatriculaRepository repo;
    private final AlumnoRepository alumnoRepo;
    private final CursoRepository cursoRepo;

    public MatriculaService(MatriculaRepository repo, AlumnoRepository alumnoRepo, CursoRepository cursoRepo) {
        this.repo = repo;
        this.alumnoRepo = alumnoRepo;
        this.cursoRepo = cursoRepo;
    }

    public List<Matricula> listarTodas() {
        return repo.findAll();
    }

    public Matricula crear(Long alumnoId, Long cursoId, LocalDate fechaMatricula) {
        if (alumnoId == null || cursoId == null || fechaMatricula == null) {
            throw new IllegalArgumentException("Alumno, curso y fecha son obligatorios.");
        }

        if (repo.existsByAlumno_IdAndCurso_Id(alumnoId, cursoId)) {
            throw new IllegalArgumentException("El alumno ya está matriculado en ese curso.");
        }

        Alumno alumno = alumnoRepo.findById(alumnoId)
                .orElseThrow(() -> new IllegalArgumentException("Alumno no encontrado."));
        Curso curso = cursoRepo.findById(cursoId)
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado."));

        Matricula matricula = new Matricula(alumno, curso, fechaMatricula);
        return repo.save(matricula);
    }

    // 🔹 Método existente: obtener alumnos de un curso
    public List<Alumno> obtenerAlumnosPorCurso(Long cursoId) {
        Curso curso = cursoRepo.findById(cursoId)
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado."));
        return repo.findByCurso_Id(curso.getId())
                .stream()
                .map(Matricula::getAlumno)
                .collect(Collectors.toList());
    }

    // 🔹 NUEVO MÉTODO: Generar PDF con los alumnos de un curso
    public byte[] generarPdfAlumnosPorCurso(Long cursoId) {
        Curso curso = cursoRepo.findById(cursoId)
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado."));

        List<Alumno> alumnos = obtenerAlumnosPorCurso(cursoId);

        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("Alumnos matriculados en el curso: " + curso.getTitulo(), titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{2, 4, 5});

            Font headFont = new Font(Font.HELVETICA, 12, Font.BOLD);
            Stream.of("ID", "Nombre", "Email").forEach(col -> {
                PdfPCell cell = new PdfPCell(new Phrase(col, headFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            });

            for (Alumno alumno : alumnos) {
                table.addCell(alumno.getId().toString());
                table.addCell(alumno.getNombre());
                table.addCell(alumno.getEmail());
            }

            document.add(table);
            document.close();

        } catch (Exception e) {
            throw new RuntimeException("Error al generar el PDF: " + e.getMessage(), e);
        }

        return out.toByteArray();
    }
}
