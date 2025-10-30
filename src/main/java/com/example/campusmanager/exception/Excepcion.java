package com.example.campusmanager.exception;

import org.springframework.http.HttpStatus;


public class Excepcion extends RuntimeException {
	
	
	public HttpStatus estado;
	public String tituloError;
	public String message;
	
	
	
	/**
	 * 
	 * @param estado
	 * @param tituloError
	 * @param message
	 * 
	 * Configuraremos los datos para enviarlos al front como error mediante @RestControllerAdvice
	 *  
	 */
	public Excepcion(HttpStatus estado, String tituloError, String message) {
		
		super(message);
		this.estado = estado;
		this.tituloError = tituloError;
		
	}
	
	
	
	
}
