package com.evoting.backend.dto;

public class CandidateResponse {

    private Long id;
    private String name;
    private String description;
    private int voteCount;
    private Long electionId;

    public CandidateResponse() {
    }

    public CandidateResponse(Long id,
                             String name,
                             String description,
                             int voteCount,
                             Long electionId) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.voteCount = voteCount;
        this.electionId = electionId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public Long getElectionId() {
        return electionId;
    }
}