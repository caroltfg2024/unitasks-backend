package com.josemiguelhyb.unitasks.exception;

/**
 * Excepción lanzada cuando los datos de una tarea son inválidos o incompletos.
 */
public class InvalidTaskDataException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidTaskDataException(String message) {
        super(message);
    }
}
