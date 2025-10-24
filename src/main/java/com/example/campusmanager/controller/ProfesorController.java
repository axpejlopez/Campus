package com.example.campusmanager.controller;

import java.net.http.HttpHeaders;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.campusmanager.domain.Profesor;
import com.example.campusmanager.dto.CursosProfesorDTO;
import com.example.campusmanager.dto.ProfesorDTO;
import com.example.campusmanager.service.ProfesorService;

@RestController
@RequestMapping("/profesores")
public class ProfesorController {

	@Autowired
	private ProfesorService profesorService;

	@GetMapping("/getAll")
	public List<Profesor> getProfesores() {
		return profesorService.getProfesores();

	}

	@PostMapping("/createProfesor")
	public Profesor createProfesor(@RequestBody ProfesorDTO profesorDTO) {

		System.out.println(profesorDTO.nombre + " " + profesorDTO.especialidad);

		return profesorService.createProfesor(profesorDTO);
	}

	/**
	 * 
	 * @param nombre
	 * @return
	 * 
	 *         Devuelve los cursos que da un profesor
	 */
	@GetMapping("/cursosProfesor")
	public List<CursosProfesorDTO> cursosProfesor(@RequestBody String nombre) {

		return profesorService.cursosProfesor(nombre);
	}

	/**
	 * Vamos ha hacer que devuelva los cursos por profesor en un fichero csv
	 * 
	 */
	@GetMapping(value = "/cursosProfesor.csv", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> descargaCursosProfesorCSV(@RequestBody String nombre) {
		List<CursosProfesorDTO> listCursosProfesorDTO = profesorService.cursosProfesor(nombre);

		StringBuilder csv = new StringBuilder();
		csv.append("ID;Nombre;Título;Descripcion\n");

		for (CursosProfesorDTO c : listCursosProfesorDTO) {
			csv.append(escapeCsv(c.profesor_id.toString())).append(";").append(escapeCsv(c.nombreProfesor)).append(";")
					.append(escapeCsv(c.tituloCurso)).append(";").append(escapeCsv(c.descripcionCurso)).append("\n");

		}

		return ResponseEntity.ok()
				.header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=profesores.csv")
				.contentType(MediaType.parseMediaType("text/csv")).body(csv.toString());

	}

	// Método auxiliar para escapar comas, comillas y saltos de línea
	private String escapeCsv(String value) {
		if (value == null)
			return "";
		if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
			return "\"" + value.replace("\"", "\"\"") + "\"";
		}
		return value;
	}

}
