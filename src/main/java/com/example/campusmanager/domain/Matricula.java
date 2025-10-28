package com.example.campusmanager.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "matricula")
public class Matricula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación con Alumno
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alumno_id")
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Alumno alumno;

    // Relación con Curso
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id")
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Curso curso;

    @Column(name = "fecha_matricula", nullable = false)
    private LocalDate fechaMatricula;
    
    //Esto nos permite ver la notaFinal que se ha sacado en esta matricula, ademas aplico cascada
    @OneToOne(mappedBy = "matricula", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    //Evito que muestre matricula en el objeto Notas
    @JsonIgnoreProperties("matricula")
    private Notas notas;
    

    public Matricula() {}

    public Matricula(Alumno alumno, Curso curso, LocalDate fechaMatricula) {
        this.alumno = alumno;
        this.curso = curso;
        this.fechaMatricula = fechaMatricula;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public Alumno getAlumno() { return alumno; }
    public Curso getCurso() { return curso; }
    public LocalDate getFechaMatricula() { return fechaMatricula; }

    public void setId(Long id) { this.id = id; }
    public void setAlumno(Alumno alumno) { this.alumno = alumno; }
    public void setCurso(Curso curso) { this.curso = curso; }
    public void setFechaMatricula(LocalDate fechaMatricula) { this.fechaMatricula = fechaMatricula; }
}

