package com.josemiguelhyb.unitasks.exception;

/**
 * Excepción lanzada cuando se intenta registrar o actualizar un usuario con un
 * email que ya existe.
 */
public class EmailAlreadyExistsException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EmailAlreadyExistsException(String email) {
        super("Ya existe un usuario con el email: " + email);
    }
}
