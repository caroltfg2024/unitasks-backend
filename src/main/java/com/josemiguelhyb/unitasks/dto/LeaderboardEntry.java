package com.josemiguelhyb.unitasks.dto;

/**
 * DTO para representar una entrada en la tabla de clasificación de usuarios.
 * Contiene estadísticas de tareas por usuario.
 */
public class LeaderboardEntry {

    private Long userId;
    private String name;
    private String lastname;
    private String email;
    private Long totalTasks;
    private Long pendingTasks;
    private Long inProgressTasks;
    private Long doneTasks;
    private Long expiredTasks;
    private Long points; // puntos = tareas completadas (DONE)

    // Constructores
    public LeaderboardEntry() {
    }

    public LeaderboardEntry(Long userId, String name, String lastname, String email,
            Long totalTasks, Long pendingTasks, Long inProgressTasks,
            Long doneTasks, Long expiredTasks) {
        this.userId = userId;
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.totalTasks = totalTasks;
        this.pendingTasks = pendingTasks;
        this.inProgressTasks = inProgressTasks;
        this.doneTasks = doneTasks;
        this.expiredTasks = expiredTasks;
        this.points = doneTasks; // 1 punto por tarea completada
    }

    // Getters y Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getTotalTasks() {
        return totalTasks;
    }

    public void setTotalTasks(Long totalTasks) {
        this.totalTasks = totalTasks;
    }

    public Long getPendingTasks() {
        return pendingTasks;
    }

    public void setPendingTasks(Long pendingTasks) {
        this.pendingTasks = pendingTasks;
    }

    public Long getInProgressTasks() {
        return inProgressTasks;
    }

    public void setInProgressTasks(Long inProgressTasks) {
        this.inProgressTasks = inProgressTasks;
    }

    public Long getDoneTasks() {
        return doneTasks;
    }

    public void setDoneTasks(Long doneTasks) {
        this.doneTasks = doneTasks;
        this.points = doneTasks; // Actualizar puntos automáticamente
    }

    public Long getExpiredTasks() {
        return expiredTasks;
    }

    public void setExpiredTasks(Long expiredTasks) {
        this.expiredTasks = expiredTasks;
    }

    public Long getPoints() {
        return points;
    }

    public void setPoints(Long points) {
        this.points = points;
    }
}
