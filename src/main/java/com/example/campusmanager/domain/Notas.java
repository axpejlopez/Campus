package com.example.campusmanager.domain;


import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.campusmanager.repository.MatriculaRepository;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="Notas")
public class Notas {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	private Long id;
	
	
	/**
	 * Hago una relación de Uno a Uno con la Entidad Matricula
	 * 
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "matricula_id",  nullable = false, unique = true)
	// la linea siguiente lo que hace es evitar que vuelva a cargar notas dentro del objeto matricula cuando nos lo soliciten.
	@JsonIgnoreProperties("notas") 
	private Matricula matricula;
	
	@Column(name = "nota_final", precision = 3, scale = 2, nullable = false)
	private BigDecimal notaFinal;
	
	public Notas(Matricula matricula, BigDecimal notaFinal) {
		this.matricula = matricula;
		this.notaFinal = notaFinal;
	}

	public Notas() {
		// TODO Auto-generated constructor stub
	}
	
}
