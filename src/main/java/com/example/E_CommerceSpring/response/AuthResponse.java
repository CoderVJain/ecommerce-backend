package com.example.E_CommerceSpring.response;

import com.example.E_CommerceSpring.model.User;

public class AuthResponse {

    private String jwt;
    private String message;
    private User user;
    // A no-argument constructor is also a good practice for many frameworks
    public AuthResponse() {
    }

    public AuthResponse(String jwt, String message) {
        this.jwt = jwt;
        this.message = message;

    }

    public AuthResponse(String jwt, String message,User user) {
        this.jwt = jwt;
        this.message = message;
        this.user = user;
    }

    // Public Getters for the fields
    public String getJwt() {
        return jwt;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }
}