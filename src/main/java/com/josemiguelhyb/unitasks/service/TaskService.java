package com.josemiguelhyb.unitasks.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.josemiguelhyb.unitasks.exception.InvalidTaskDataException;
import com.josemiguelhyb.unitasks.exception.TaskNotFoundException;
import com.josemiguelhyb.unitasks.exception.UserNotFoundException;
import com.josemiguelhyb.unitasks.model.Task;
import com.josemiguelhyb.unitasks.model.TaskPriority;
import com.josemiguelhyb.unitasks.model.TaskStatus;
import com.josemiguelhyb.unitasks.model.User;
import com.josemiguelhyb.unitasks.repository.TaskRepository;
import com.josemiguelhyb.unitasks.repository.UserRepository;

@Service
public class TaskService {
    private static final Logger log = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    /**
     * Crea una nueva tarea para un usuario específico.
     * Valida existencia del usuario, normaliza datos y asigna valores por defecto.
     * 
     * @param userId El ID del usuario dueño de la tarea
     * @param task   El objeto Task con los datos de la nueva tarea (title,
     *               description, status, priority, dueDate)
     * @return La tarea creada con su ID asignado automáticamente
     * @throws UserNotFoundException    si el usuario no existe
     * @throws InvalidTaskDataException si el título está vacío
     */
    @Transactional
    public Task createTask(Long userId, Task task) {
        // Antes de persistir, el ID de la tarea es null; registra datos útiles
        log.info("Creando tarea para userId={}, title='{}'", userId, task.getTitle());

        // Validar existencia del usuario
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        // Normalizar y defaults
        if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
            throw new InvalidTaskDataException("El título de la tarea es obligatorio");
        }
        task.setTitle(task.getTitle().trim());

        if (task.getStatus() == null) {
            task.setStatus(TaskStatus.PENDING);
        }
        if (task.getPriority() == null) {
            task.setPriority(TaskPriority.MEDIUM);
        }

        // Asignar el dueño gestionado por JPA
        task.setUser(owner);

        // Persistir
        Task saved = taskRepository.save(task);
        log.info("Tarea creada exitosamente id={}, userId={}", saved.getId(), userId);
        return saved;
    }

    /**
     * Obtiene la lista completa de todas las tareas del sistema.
     * 
     * @return Lista de todas las tareas
     */
    @Transactional(readOnly = true)
    public List<Task> getAllTasks() {
        log.info("Obteniendo todas las tareas");
        return taskRepository.findAll();
    }

    /**
     * Busca una tarea específica por su ID.
     * 
     * @param id El ID de la tarea a buscar
     * @return Optional con la tarea si existe, Optional.empty() si no existe
     */

    @Transactional(readOnly = true)
    public Optional<Task> getTaskById(Long id) {
        log.info("Buscando tarea con ID: {}", id);
        return taskRepository.findById(id);
    }

    /**
     * Actualiza los datos de una tarea existente (title, description, status,
     * priority, dueDate).
     * No cambia el usuario dueño.
     * 
     * @param taskId      El ID de la tarea a actualizar
     * @param updatedTask Objeto Task con los nuevos datos
     * @return La tarea actualizada
     * @throws TaskNotFoundException si la tarea no existe
     */
    @Transactional
    public Task updateTask(Long taskId, Task updatedTask) {
        log.info("Actualizando tarea con ID: {}", taskId);

        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));

        // Actualizar campos permitidos
        if (updatedTask.getTitle() != null) {
            String trimmedTitle = updatedTask.getTitle().trim();
            if (trimmedTitle.isEmpty()) {
                throw new InvalidTaskDataException("El título de la tarea no puede estar vacío");
            }
            existingTask.setTitle(trimmedTitle);
        }
        if (updatedTask.getDescription() != null) {
            existingTask.setDescription(updatedTask.getDescription());
        }
        if (updatedTask.getStatus() != null) {
            existingTask.setStatus(updatedTask.getStatus());
        }
        if (updatedTask.getPriority() != null) {
            existingTask.setPriority(updatedTask.getPriority());
        }
        if (updatedTask.getDueDate() != null) {
            existingTask.setDueDate(updatedTask.getDueDate());
        }

        // NO cambiar el usuario dueño en una actualización normal

        Task saved = taskRepository.save(existingTask);
        log.info("Tarea actualizada exitosamente con ID: {}", saved.getId());
        return saved;
    }

    /**
     * Elimina una tarea del sistema.
     * 
     * @param taskId El ID de la tarea a eliminar
     * @throws TaskNotFoundException si la tarea no existe
     */
    @Transactional
    public void deleteTask(Long taskId) {
        log.info("Eliminando tarea con ID: {}", taskId);

        if (!taskRepository.existsById(taskId)) {
            log.warn("Intento de eliminar tarea inexistente con ID: {}", taskId);
            throw new TaskNotFoundException(taskId);
        }

        taskRepository.deleteById(taskId);
        log.info("Tarea eliminada exitosamente con ID: {}", taskId);
    }

    /**
     * Obtiene todas las tareas de un usuario específico.
     * 
     * @param userId El ID del usuario
     * @return Lista de tareas del usuario
     */
    @Transactional(readOnly = true)
    public List<Task> getTasksByUserId(Long userId) {
        log.info("Obteniendo tareas del usuario con ID: {}", userId);
        return taskRepository.findByUserId(userId);
    }

    /**
     * Obtiene tareas de un usuario filtradas por estado.
     * 
     * @param userId El ID del usuario
     * @param status El estado de las tareas a buscar
     * @return Lista de tareas del usuario con el estado especificado
     */
    @Transactional(readOnly = true)
    public List<Task> getTasksByUserIdAndStatus(Long userId, TaskStatus status) {
        log.info("Obteniendo tareas del usuario {} con estado {}", userId, status);
        return taskRepository.findByUserIdAndStatus(userId, status);
    }

    /**
     * Cambia el estado de una tarea específica.
     * 
     * @param taskId    El ID de la tarea
     * @param newStatus El nuevo estado a asignar
     * @return La tarea actualizada
     * @throws TaskNotFoundException si la tarea no existe
     */
    @Transactional
    public Task changeTaskStatus(Long taskId, TaskStatus newStatus) {
        log.info("Cambiando estado de tarea {} a {}", taskId, newStatus);

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));

        if (newStatus == null) {
            throw new InvalidTaskDataException("El estado de la tarea es obligatorio");
        }

        task.setStatus(newStatus);
        Task saved = taskRepository.save(task);
        log.info("Estado de tarea {} actualizado a {}", taskId, newStatus);
        return saved;
    }

    /**
     * Cuenta el total de tareas en el sistema.
     * 
     * @return El número total de tareas
     */
    @Transactional(readOnly = true)
    public long countTasks() {
        log.info("Contando total de tareas");
        return taskRepository.count();
    }

    /**
     * Cuenta las tareas de un usuario específico.
     * 
     * @param userId El ID del usuario
     * @return El número de tareas del usuario
     */
    @Transactional(readOnly = true)
    public long countTasksByUserId(Long userId) {
        log.info("Contando tareas del usuario {}", userId);
        return taskRepository.countByUserId(userId);
    }
}
