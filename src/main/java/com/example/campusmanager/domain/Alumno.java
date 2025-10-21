package com.example.campusmanager.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "alumno") // ✅ CORREGIDO: antes decía "alumnos"
public class Alumno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String email;

    private LocalDate fechaNacimiento;

    // 🔹 Constructor vacío obligatorio para JPA
    public Alumno() {
    }

    // 🔹 Constructor con parámetros (opcional, pero útil)
    public Alumno(String nombre, String email, LocalDate fechaNacimiento) {
        this.nombre = nombre;
        this.email = email;
        this.fechaNacimiento = fechaNacimiento;
    }

    // 🔹 Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
}
