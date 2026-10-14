package com.evoting.backend.dto;

public class LoginResponse {

    private Long id;
    private String fullName;
    private String email;
    private boolean hasVoted;
    private String message;

    public LoginResponse() {
    }

    public LoginResponse(Long id, String fullName, String email,
                         boolean hasVoted, String message) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.hasVoted = hasVoted;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public boolean isHasVoted() {
        return hasVoted;
    }

    public String getMessage() {
        return message;
    }
}