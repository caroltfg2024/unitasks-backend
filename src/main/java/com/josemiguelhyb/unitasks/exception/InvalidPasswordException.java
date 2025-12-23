package com.josemiguelhyb.unitasks.exception;

/**
 * Excepción lanzada cuando la contraseña actual proporcionada no coincide.
 */
public class InvalidPasswordException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidPasswordException() {
        super("La contraseña actual no es correcta");
    }

    public InvalidPasswordException(String message) {
        super(message);
    }
}
