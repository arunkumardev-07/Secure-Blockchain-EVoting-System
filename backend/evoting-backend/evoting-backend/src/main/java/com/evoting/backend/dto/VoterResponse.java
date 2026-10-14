package com.evoting.backend.dto;

public class VoterResponse {

    private Long id;
    private String fullName;
    private String email;
    private boolean hasVoted;

    public VoterResponse() {
    }

    public VoterResponse(Long id, String fullName, String email, boolean hasVoted) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.hasVoted = hasVoted;
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
}