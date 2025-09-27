package com.example.E_CommerceSpring.response;

public class AuthResponse {

    private String jwt;
    private String message;

    // A no-argument constructor is also a good practice for many frameworks
    public AuthResponse() {
    }

    public AuthResponse(String jwt, String message) {
        this.jwt = jwt;
        this.message = message;
    }

    // Public Getters for the fields
    public String getJwt() {
        return jwt;
    }

    public String getMessage() {
        return message;
    }
}