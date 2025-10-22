package com.example.campusmanager.repository;




import com.example.campusmanager.entity.Profesor;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;




@Repository
public interface ProfesorRepository extends JpaRepository<Profesor,Integer>{

	List<Profesor> findAll();

	Object findByNombreAndEspecialidad(String nombre, String especialidad);

	Profesor save(Profesor profesor);
	
	
	
	

}
