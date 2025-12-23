package com.josemiguelhyb.unitasks.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.josemiguelhyb.unitasks.model.Task;
import com.josemiguelhyb.unitasks.model.TaskStatus;

public interface TaskRepository extends JpaRepository<Task, Long> {

    // Por heredar de JpaRepository<Task, Long> ya tienes implementados:
    // - save(entity): inserta/actualiza
    // - findById(id), findAll(), findAll(Pageable), count()
    // - delete(entity), deleteById(id), deleteAll()
    // - existsById(id)
    // Además soporta paginación, ordenación y consultas derivadas por nombre.
    // Solo declaras firmas adicionales y Spring Data genera las queries (JPQL/SQL)

    // Obtener todas las tareas de un usuario
    List<Task> findByUserId(Long userId);

    // Obtener tareas de un usuario filtradas por estado
    List<Task> findByUserIdAndStatus(Long userId, TaskStatus status);

    // Contar tareas de un usuario
    long countByUserId(Long userId);

    // Contar tareas de un usuario por estado
    long countByUserIdAndStatus(Long userId, TaskStatus status);
}
