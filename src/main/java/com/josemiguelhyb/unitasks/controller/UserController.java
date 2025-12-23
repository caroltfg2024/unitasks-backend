package com.josemiguelhyb.unitasks.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.josemiguelhyb.unitasks.dto.LeaderboardEntry;
import com.josemiguelhyb.unitasks.model.User;
import com.josemiguelhyb.unitasks.service.UserService;

/**
 * Controlador REST para gestión de usuarios.
 * Expone endpoints HTTP para operaciones CRUD sobre usuarios.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Crea un nuevo usuario.
     * POST /api/users
     * 
     * @param user Datos del usuario (name, lastname, email, password)
     * @return 201 Created con el usuario creado
     */
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User created = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Obtiene todos los usuarios.
     * GET /api/users
     * 
     * @return 200 OK con lista de usuarios
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Obtiene un usuario por su ID.
     * GET /api/users/{id}
     * 
     * @param id ID del usuario
     * @return 200 OK con el usuario, o 404 Not Found si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Busca un usuario por email.
     * GET /api/users/email?email=xxx
     * 
     * @param email Email del usuario
     * @return 200 OK con el usuario, o 404 Not Found si no existe
     */
    @GetMapping("/email")
    public ResponseEntity<User> getUserByEmail(@RequestParam String email) {
        return userService.getUserByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Actualiza los datos de un usuario (name, lastname, email).
     * PUT /api/users/{id}
     * 
     * @param id          ID del usuario
     * @param updatedUser Datos actualizados
     * @return 200 OK con el usuario actualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody User updatedUser) {
        User updated = userService.updateUser(id, updatedUser);
        return ResponseEntity.ok(updated);
    }

    /**
     * Cambia la contraseña de un usuario.
     * PATCH /api/users/{id}/password
     * 
     * @param id          ID del usuario
     * @param oldPassword Contraseña actual
     * @param newPassword Nueva contraseña
     * @return 204 No Content si se actualiza correctamente
     */
    @PatchMapping("/{id}/password")
    public ResponseEntity<Void> changePassword(
            @PathVariable Long id,
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        userService.changePassword(id, oldPassword, newPassword);
        return ResponseEntity.noContent().build();
    }

    /**
     * Activa un usuario.
     * PATCH /api/users/{id}/activate
     * 
     * @param id ID del usuario
     * @return 200 OK con el usuario activado
     */
    @PatchMapping("/{id}/activate")
    public ResponseEntity<User> activateUser(@PathVariable Long id) {
        User activated = userService.activateUser(id);
        return ResponseEntity.ok(activated);
    }

    /**
     * Desactiva un usuario.
     * PATCH /api/users/{id}/deactivate
     * 
     * @param id ID del usuario
     * @return 200 OK con el usuario desactivado
     */
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<User> deactivateUser(@PathVariable Long id) {
        User deactivated = userService.deactivateUser(id);
        return ResponseEntity.ok(deactivated);
    }

    /**
     * Elimina un usuario.
     * DELETE /api/users/{id}
     * 
     * @param id ID del usuario
     * @return 204 No Content si se elimina correctamente
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Cuenta el total de usuarios.
     * GET /api/users/count
     * 
     * @return 200 OK con el número de usuarios
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countUsers() {
        long count = userService.countUsers();
        return ResponseEntity.ok(count);
    }

    /**
     * Obtiene la tabla de clasificación de usuarios ordenada por puntos (tareas
     * completadas).
     * GET /api/users/leaderboard
     * 
     * @return 200 OK con la lista de usuarios con sus estadísticas ordenada por
     *         puntos
     */
    @GetMapping("/leaderboard")
    public ResponseEntity<List<LeaderboardEntry>> getLeaderboard() {
        List<LeaderboardEntry> leaderboard = userService.getLeaderboard();
        return ResponseEntity.ok(leaderboard);
    }
}
