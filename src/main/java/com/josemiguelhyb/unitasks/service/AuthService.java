package com.josemiguelhyb.unitasks.service;

import com.josemiguelhyb.unitasks.dto.AuthResponse;
import com.josemiguelhyb.unitasks.dto.LoginRequest;
import com.josemiguelhyb.unitasks.dto.RegisterRequest;
import com.josemiguelhyb.unitasks.model.User;
import com.josemiguelhyb.unitasks.repository.UserRepository;
import com.josemiguelhyb.unitasks.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Registra un nuevo usuario en el sistema
     */
    public AuthResponse register(RegisterRequest request) {
        // Verificar si el email ya existe
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        // Crear nuevo usuario
        User user = new User();
        user.setName(request.getName());
        user.setLastname(request.getLastname());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setActive(true);

        // Guardar en base de datos
        userRepository.save(user);

        // Generar token JWT
        String token = jwtUtil.generateToken(user.getEmail());

        // Devolver respuesta con token
        return new AuthResponse(token, user.getEmail(), user.getName(), user.getLastname());
    }

    /**
     * Inicia sesión de un usuario existente
     */
    public AuthResponse login(LoginRequest request) {
        // Buscar usuario por email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        // Verificar contraseña
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        // Verificar si el usuario está activo
        if (!user.isActive()) {
            throw new RuntimeException("La cuenta está desactivada");
        }

        // Generar token JWT
        String token = jwtUtil.generateToken(user.getEmail());

        // Devolver respuesta con token
        return new AuthResponse(token, user.getEmail(), user.getName(), user.getLastname());
    }

    /**
     * Obtiene el usuario actual a partir del email del token
     */
    public User getCurrentUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}
