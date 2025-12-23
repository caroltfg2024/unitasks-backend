package com.josemiguelhyb.unitasks.exception;

/**
 * Excepción lanzada cuando no se encuentra una tarea por su ID.
 */
public class TaskNotFoundException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TaskNotFoundException(String message) {
        super(message);
    }

    public TaskNotFoundException(Long taskId) {
        super("Tarea no encontrada con ID: " + taskId);
    }
}
