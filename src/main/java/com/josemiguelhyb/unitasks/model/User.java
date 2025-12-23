package com.josemiguelhyb.unitasks.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "El nombre no puede estar vacío")
	@Size(min = 2, max = 50)
	@Column(nullable = false, length = 50)
	private String name;

	@NotBlank(message = "El apellido no puede estar vacío")
	@Size(min = 2, max = 50)
	@Column(nullable = false, length = 50)
	private String lastname;

	@NotBlank(message = "El email no puede estar vacío")
	@Email(message = "Formato de email inválido")
	@Column(nullable = false, length = 150, unique = true)
	private String email;

	// monimo 8 caracteres y hasheado con Bcrypt
	@NotBlank(message = "El password no puede estar vacío")
	@Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
	@Column(nullable = false)
	private String password;

	// para ver si esta activo o bloqueado
	@Column(nullable = false)
	private boolean active = true;

	// Un usuario va a tener una lista de tareas
	// @JsonIgnore para evitar recursión infinita en la serialización JSON
	@JsonIgnore
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Task> tasks = new ArrayList<>();

	// Constructor sin argumentos es obligatorio para JPA
	public User() {
	}

	// Constructor con argumentos
	public User(String name, String lastname, String email, String password, boolean active) {
		this.name = name;
		this.lastname = lastname;
		this.email = email;
		this.password = password;
		this.active = active;
	}

	// Getters y Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	// toString
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", lastname=" + lastname + ", email=" + email
				+ ", active=" + active + "]";
	}
}
