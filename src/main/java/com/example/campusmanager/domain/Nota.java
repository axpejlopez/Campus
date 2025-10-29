package com.example.campusmanager.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(
    name = "notas",
    uniqueConstraints = @UniqueConstraint(columnNames = {"alumno_id", "asignatura"})
)
public class Nota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "alumno_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "notas"})
    private Alumno alumno;


    @NotBlank(message = "La asignatura no puede estar vacía")
    @Column(nullable = false, length = 150)
    private String asignatura;

    @DecimalMin(value = "0.0", message = "La nota mínima es 0.0")
    @DecimalMax(value = "10.0", message = "La nota máxima es 10.0")
    @Column(name = "nota_final", nullable = false)
    private Double notaFinal;


    // --- Getters & Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; } 
    public Alumno getAlumno() { return alumno; }
    public void setAlumno(Alumno alumno) { this.alumno = alumno; }

    public String getAsignatura() { return asignatura; }
    public void setAsignatura(String asignatura) { this.asignatura = asignatura; }

    public Double getNotaFinal() { return notaFinal; }
    public void setNotaFinal(Double notaFinal) { this.notaFinal = notaFinal; }


}

