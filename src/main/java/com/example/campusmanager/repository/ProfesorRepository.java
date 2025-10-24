package com.example.campusmanager.repository;




import org.springframework.stereotype.Repository;

import com.example.campusmanager.domain.Profesor;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;




@Repository
public interface ProfesorRepository extends JpaRepository<Profesor,Integer>{

	List<Profesor> findAll();

	Object findByNombreAndEspecialidad(String nombre, String especialidad);

	Profesor save(Profesor profesor);

	Optional<Profesor> findByNombre(String nombre);
	
	
	
	

}
