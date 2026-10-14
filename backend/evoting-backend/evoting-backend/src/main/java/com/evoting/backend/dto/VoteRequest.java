package com.evoting.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class VoteRequest {

    @NotNull(message = "Election ID is required")
    @Positive(message = "Election ID must be positive")
    private Long electionId;

    @NotNull(message = "Candidate ID is required")
    @Positive(message = "Candidate ID must be positive")
    private Long candidateId;

    public VoteRequest() {
    }

    public Long getElectionId() {
        return electionId;
    }

    public void setElectionId(Long electionId) {
        this.electionId = electionId;
    }

    public Long getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(Long candidateId) {
        this.candidateId = candidateId;
    }
}
