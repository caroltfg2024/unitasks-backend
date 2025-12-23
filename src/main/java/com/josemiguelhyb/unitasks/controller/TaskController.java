package com.josemiguelhyb.unitasks.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import com.josemiguelhyb.unitasks.model.Task;
import com.josemiguelhyb.unitasks.model.TaskStatus;
import com.josemiguelhyb.unitasks.model.User;
import com.josemiguelhyb.unitasks.service.AuthService;
import com.josemiguelhyb.unitasks.service.TaskService;

/**
 * Controlador REST para gestión de tareas.
 * Todas las operaciones están vinculadas al usuario autenticado via JWT.
 */
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final AuthService authService;

    public TaskController(TaskService taskService, AuthService authService) {
        this.taskService = taskService;
        this.authService = authService;
    }

    /**
     * Obtiene todas las tareas DEL USUARIO AUTENTICADO.
     * GET /api/tasks
     * 
     * @param auth Objeto de autenticación con el email del usuario
     * @return 200 OK con lista de tareas del usuario
     */
    @GetMapping
    public ResponseEntity<List<Task>> getMyTasks(Authentication auth) {
        User user = authService.getCurrentUser(auth.getName());
        List<Task> tasks = taskService.getTasksByUserId(user.getId());
        return ResponseEntity.ok(tasks);
    }

    /**
     * Crea una nueva tarea para EL USUARIO AUTENTICADO.
     * POST /api/tasks
     * 
     * @param auth Objeto de autenticación
     * @param task Datos de la tarea
     * @return 201 Created con la tarea creada
     */
    @PostMapping
    public ResponseEntity<Task> createTask(Authentication auth, @Valid @RequestBody Task task) {
        User user = authService.getCurrentUser(auth.getName());
        Task created = taskService.createTask(user.getId(), task);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Cuenta el total de tareas del usuario autenticado.
     * GET /api/tasks/count
     * 
     * @param auth Objeto de autenticación
     * @return 200 OK con el número de tareas
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMyTasks(Authentication auth) {
        User user = authService.getCurrentUser(auth.getName());
        long count = taskService.countTasksByUserId(user.getId());
        return ResponseEntity.ok(count);
    }

    /**
     * Obtiene una tarea por su ID (solo si pertenece al usuario autenticado).
     * GET /api/tasks/{id}
     * 
     * @param id   ID de la tarea
     * @param auth Objeto de autenticación
     * @return 200 OK con la tarea, o 404/403 si no existe o no es del usuario
     */
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id, Authentication auth) {
        User user = authService.getCurrentUser(auth.getName());
        return taskService.getTaskById(id)
                .filter(task -> task.getUser().getId().equals(user.getId()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Actualiza los datos de una tarea (solo si pertenece al usuario autenticado).
     * PUT /api/tasks/{id}
     * 
     * @param id          ID de la tarea
     * @param updatedTask Datos actualizados
     * @param auth        Objeto de autenticación
     * @return 200 OK con la tarea actualizada
     */
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @Valid @RequestBody Task updatedTask,
            Authentication auth) {
        User user = authService.getCurrentUser(auth.getName());
        // Verificar que la tarea pertenece al usuario
        return taskService.getTaskById(id)
                .filter(task -> task.getUser().getId().equals(user.getId()))
                .map(task -> {
                    Task updated = taskService.updateTask(id, updatedTask);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Cambia el estado de una tarea (solo si pertenece al usuario autenticado).
     * PATCH /api/tasks/{id}/status?status=DONE
     * 
     * @param id     ID de la tarea
     * @param status Nuevo estado
     * @param auth   Objeto de autenticación
     * @return 200 OK con la tarea actualizada
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<Task> changeTaskStatus(@PathVariable Long id, @RequestParam TaskStatus status,
            Authentication auth) {
        User user = authService.getCurrentUser(auth.getName());
        return taskService.getTaskById(id)
                .filter(task -> task.getUser().getId().equals(user.getId()))
                .map(task -> {
                    Task updated = taskService.changeTaskStatus(id, status);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Elimina una tarea (solo si pertenece al usuario autenticado).
     * DELETE /api/tasks/{id}
     * 
     * @param id   ID de la tarea
     * @param auth Objeto de autenticación
     * @return 204 No Content si se elimina correctamente
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id, Authentication auth) {
        User user = authService.getCurrentUser(auth.getName());
        return taskService.getTaskById(id)
                .filter(task -> task.getUser().getId().equals(user.getId()))
                .map(task -> {
                    taskService.deleteTask(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
