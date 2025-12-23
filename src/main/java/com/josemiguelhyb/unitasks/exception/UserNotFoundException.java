package com.josemiguelhyb.unitasks.exception;

/**
 * Excepción lanzada cuando no se encuentra un usuario por su ID o email.
 */
public class UserNotFoundException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(Long userId) {
        super("Usuario no encontrado con ID: " + userId);
    }
}
