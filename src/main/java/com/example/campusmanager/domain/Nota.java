package com.example.campusmanager.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

/**
 * Entidad que representa la tabla "notas" en la base de datos.
 */
@Entity
@Table(name = "notas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Nota {

    /**
     * Identificador único de la nota (clave primaria).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Relación con la entidad Alumno.
     * Cada nota pertenece a un único alumno.
     */
    @ManyToOne
    @JoinColumn(name = "alumno_id", nullable = false)
    private Alumno alumno;

    /**
     * Nombre de la asignatura (máximo 100 caracteres).
     */
    @Column(nullable = false, length = 100)
    private String asignatura;

    /**
     * Nota final del alumno en la asignatura.
     * BigDecimal se usa para representar valores decimales con precisión.
     */
    @Column(name = "nota_final", nullable = false, precision = 4, scale = 2)
    private BigDecimal notaFinal;

    /**
     * 🔹 Constructor personalizado para crear una nueva nota
     * sin pasar el ID (ya que se genera automáticamente).
     */
    public Nota(Alumno alumno, String asignatura, BigDecimal notaFinal) {
        this.alumno = alumno;
        this.asignatura = asignatura;
        this.notaFinal = notaFinal;
    }
}

