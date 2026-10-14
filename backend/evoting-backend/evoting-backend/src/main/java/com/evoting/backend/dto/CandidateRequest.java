package com.evoting.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CandidateRequest {

    @NotBlank(message = "Candidate name is required")
    @Size(
            min = 3,
            max = 100,
            message = "Candidate name must be between 3 and 100 characters"
    )
    private String name;

    @Size(
            max = 1000,
            message = "Description cannot exceed 1000 characters"
    )
    private String description;

    public CandidateRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}