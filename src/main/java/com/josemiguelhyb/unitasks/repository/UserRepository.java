package com.josemiguelhyb.unitasks.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.josemiguelhyb.unitasks.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	// Por heredar de JpaRepository<User, Long> ya tienes implementados:
	// - save(entity): inserta/actualiza
	// - findById(id), findAll(), findAll(Pageable), count()
	// - delete(entity), deleteById(id), deleteAll()
	// - existsById(id)
	// Además soporta paginación, ordenación y consultas derivadas por nombre.
	// Solo declaras las firmas adicionales y Spring Data genera las queries
	// (JPQL/SQL) automáticamente.
	// Ejemplo: findByEmail o existsByEmail se traducen a SELECT ... WHERE email = ?
	// sin escribir SQL.

	Optional<User> findByEmail(String email);	

	boolean existsByEmail(String email);
}
