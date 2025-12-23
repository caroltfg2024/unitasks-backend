package com.josemiguelhyb.unitasks.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.josemiguelhyb.unitasks.dto.LeaderboardEntry;
import com.josemiguelhyb.unitasks.exception.EmailAlreadyExistsException;
import com.josemiguelhyb.unitasks.exception.InvalidPasswordException;
import com.josemiguelhyb.unitasks.exception.UserNotFoundException;
import com.josemiguelhyb.unitasks.model.TaskStatus;
import com.josemiguelhyb.unitasks.model.User;
import com.josemiguelhyb.unitasks.repository.TaskRepository;
import com.josemiguelhyb.unitasks.repository.UserRepository;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, TaskRepository taskRepository,
            BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Crea y registra un nuevo usuario en el sistema.
     * Valida que el email sea único, normaliza los datos y encripta la contraseña.
     * 
     * @param user El objeto User con los datos del nuevo usuario (name, lastname,
     *             email, password)
     * @return El usuario creado con su ID asignado automáticamente
     * @throws EmailAlreadyExistsException si el email ya está registrado
     */
    @Transactional
    public User createUser(User user) {
        log.info("Creating user with email: {}", user.getEmail());
        String normalizedEmail = user.getEmail().trim().toLowerCase();

        // Si existe ese User con este email normalizado
        if (userRepository.existsByEmail(normalizedEmail)) {
            log.warn("Intento de registro con email duplicado: {}", normalizedEmail);
            throw new EmailAlreadyExistsException(normalizedEmail);
        }

        // El usuario no existe por tanto creamos el objeto User
        user.setEmail(normalizedEmail);
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encriptamos password con BCrypt
        User savedUser = userRepository.save(user);
        log.info("Usuario registrado exitosamente con ID: {}", savedUser.getId());
        return savedUser;
    }

    /**
     * Obtiene la lista completa de todos los usuarios registrados.
     * 
     * @return Lista de todos los usuarios
     */
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        log.info("Obteniendo todos los usuarios");
        return userRepository.findAll();
    }

    /**
     * Busca un usuario específico por su ID.
     * 
     * @param id El ID del usuario a buscar
     * @return Optional con el usuario si existe, Optional.empty() si no existe
     */
    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id) {
        log.info("Buscando usuario con ID: {}", id);
        return userRepository.findById(id);
    }

    /**
     * Actualiza los datos básicos de un usuario existente (name, lastname, email).
     * No actualiza la contraseña - usar changePassword() para ello.
     * Valida que el nuevo email no esté en uso por otro usuario.
     * 
     * @param id          El ID del usuario a actualizar
     * @param updatedUser Objeto User con los nuevos datos
     * @return El usuario actualizado
     * @throws UserNotFoundException       si el usuario no existe
     * @throws EmailAlreadyExistsException si el email ya está en uso
     */
    @Transactional
    public User updateUser(Long id, User updatedUser) {
        log.info("Actualizando usuario con ID: {}", id);

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        // Actualizamos solo los campos permitidos (no el password aquí, eso va en
        // changePassword)
        existingUser.setName(updatedUser.getName().trim());
        existingUser.setLastname(updatedUser.getLastname().trim());

        // Si el email cambió, validar que no exista otro usuario con ese email
        String normalizedEmail = updatedUser.getEmail().trim().toLowerCase();
        if (!existingUser.getEmail().equals(normalizedEmail)) {
            if (userRepository.existsByEmail(normalizedEmail)) {
                log.warn("Intento de actualizar a email duplicado: {}", normalizedEmail);
                throw new EmailAlreadyExistsException(normalizedEmail);
            }
            existingUser.setEmail(normalizedEmail);
        }

        // NO actualizar password aquí - usar changePassword() por separado para mayor
        // seguridad

        User savedUser = userRepository.save(existingUser);
        log.info("Usuario actualizado exitosamente con ID: {}", savedUser.getId());
        return savedUser;
    }

    /**
     * Cambia la contraseña de un usuario de forma segura.
     * Verifica que la contraseña antigua sea correcta antes de establecer la nueva.
     * La nueva contraseña se encripta automáticamente con BCrypt.
     * 
     * @param id          El ID del usuario
     * @param oldPassword La contraseña actual (para verificación)
     * @param newPassword La nueva contraseña a establecer
     * @throws UserNotFoundException    si el usuario no existe
     * @throws InvalidPasswordException si la contraseña antigua es incorrecta
     */
    @Transactional
    public void changePassword(Long id, String oldPassword, String newPassword) {
        log.info("Intentando cambiar contraseña para usuario con ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        // Verificar que la contraseña antigua coincida
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            log.warn("Intento de cambio de contraseña con contraseña antigua incorrecta para ID: {}", id);
            throw new InvalidPasswordException();
        }

        // Encriptar y guardar la nueva contraseña
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        log.info("Contraseña actualizada exitosamente para usuario con ID: {}", id);
    }

    /**
     * Elimina un usuario del sistema junto con todas sus tareas asociadas.
     * La eliminación es en cascada gracias a la configuración de la relación
     * User-Task.
     * 
     * @param id El ID del usuario a eliminar
     * @throws UserNotFoundException si el usuario no existe
     */
    @Transactional
    public void deleteUser(Long id) {
        log.info("Eliminando usuario con ID: {}", id);

        // Verificar que el usuario existe antes de eliminar
        if (!userRepository.existsById(id)) {
            log.warn("Intento de eliminar usuario inexistente con ID: {}", id);
            throw new UserNotFoundException(id);
        }

        // El usuario existe, eliminar (también eliminará sus tareas por cascade)
        userRepository.deleteById(id);
        log.info("Usuario eliminado exitosamente con ID: {} (incluidas sus tareas)", id);
    }

    /**
     * Busca un usuario por su email de forma segura.
     * Útil para procesos de login y validación de emails únicos.
     * 
     * @param email El email a buscar
     * @return Optional con el usuario si existe, Optional.empty() si no existe
     */
    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(String email) {
        String normalizedEmail = email.trim().toLowerCase();
        log.info("Buscando usuario con email: {}", normalizedEmail);
        return userRepository.findByEmail(normalizedEmail);
    }

    /**
     * Activa un usuario (marca como activo).
     * 
     * @param id El ID del usuario a activar
     * @return El usuario activado
     * @throws UserNotFoundException si el usuario no existe
     */
    @Transactional
    public User activateUser(Long id) {
        log.info("Activando usuario con ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        user.setActive(true);
        User savedUser = userRepository.save(user);
        log.info("Usuario activado exitosamente con ID: {}", id);
        return savedUser;
    }

    /**
     * Desactiva un usuario (marca como inactivo/bloqueado).
     * 
     * @param id El ID del usuario a desactivar
     * @return El usuario desactivado
     * @throws UserNotFoundException si el usuario no existe
     */
    @Transactional
    public User deactivateUser(Long id) {
        log.info("Desactivando usuario con ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        user.setActive(false);
        User savedUser = userRepository.save(user);
        log.info("Usuario desactivado exitosamente con ID: {}", id);
        return savedUser;
    }

    /**
     * Cuenta el total de usuarios registrados en el sistema.
     * Útil para estadísticas y monitoreo.
     * 
     * @return El número total de usuarios
     */
    @Transactional(readOnly = true)
    public long countUsers() {
        log.info("Contando total de usuarios");
        return userRepository.count();
    }

    /**
     * Obtiene la tabla de clasificación de todos los usuarios ACTIVOS ordenada por
     * puntos
     * (tareas completadas).
     * Cada tarea DONE equivale a 1 punto.
     * Solo devuelve usuarios activos.
     * 
     * @return Lista de usuarios activos con sus estadísticas ordenada
     *         descendentemente por
     *         puntos
     */
    @Transactional(readOnly = true)
    public List<LeaderboardEntry> getLeaderboard() {
        log.info("Obteniendo tabla de clasificación de usuarios activos");
        List<User> users = userRepository.findAll();

        return users.stream()
                .filter(User::isActive) // Solo usuarios activos
                .map(user -> {
                    Long userId = user.getId();
                    long totalTasks = taskRepository.countByUserId(userId);
                    long pendingTasks = taskRepository.countByUserIdAndStatus(userId, TaskStatus.PENDING);
                    long inProgressTasks = taskRepository.countByUserIdAndStatus(userId, TaskStatus.IN_PROGRESS);
                    long doneTasks = taskRepository.countByUserIdAndStatus(userId, TaskStatus.DONE);
                    long expiredTasks = taskRepository.countByUserIdAndStatus(userId, TaskStatus.EXPIRED);

                    log.debug("Usuario: {} - Tareas: {} - DONE: {} - Puntos: {}",
                            user.getEmail(), totalTasks, doneTasks, doneTasks);

                    return new LeaderboardEntry(
                            userId,
                            user.getName(),
                            user.getLastname(),
                            user.getEmail(),
                            Long.valueOf(totalTasks),
                            Long.valueOf(pendingTasks),
                            Long.valueOf(inProgressTasks),
                            Long.valueOf(doneTasks),
                            Long.valueOf(expiredTasks));
                })
                .sorted((a, b) -> b.getPoints().compareTo(a.getPoints())) // Orden descendente por puntos
                .collect(Collectors.toList());
    }

}
