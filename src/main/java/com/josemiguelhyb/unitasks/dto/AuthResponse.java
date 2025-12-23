package com.josemiguelhyb.unitasks.dto;

public class AuthResponse {

    private String token;
    private String email;
    private String name;
    private String lastname;

    // Constructores
    public AuthResponse() {
    }

    public AuthResponse(String token, String email, String name, String lastname) {
        this.token = token;
        this.email = email;
        this.name = name;
        this.lastname = lastname;
    }

    // Getters y Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}
