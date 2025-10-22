package com.example.campusmanager.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
@Entity
@Table(name="profesor")
public class Profesor {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "nombre", nullable = false)
	@Size(min = 3, max = 100)
	private String nombre;
	
	@Column(name = "especialidad", nullable = true)
	@Size(min = 3, max = 100)
	private String especialidad;
	
	

}
