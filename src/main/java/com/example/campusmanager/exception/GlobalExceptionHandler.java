package com.example.campusmanager.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	
	/**
	 * 
	 * @param excepcion
	 * @return
	 * 
	 * Manejamos nuestras excepciones personalizadas.
	 * 
	 */
	
	@ExceptionHandler(Excepcion.class)
	public ResponseEntity<Map<String, Object>> handlerExcepciones(Excepcion excepcion){
		Map<String, Object> body = new HashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("estado", excepcion.estado.value());
		body.put("error", excepcion.tituloError);
		body.put("mensaje",excepcion.getMessage());
		
		return ResponseEntity.status(excepcion.estado).body(body);		
	}
	

}
