package com.evoting.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "candidates")
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private int voteCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "election_id", nullable = false)
    private Election election;

    public Candidate() {
    }

    public Candidate(String name, String description, Election election) {
        this.name = name;
        this.description = description;
        this.election = election;
        this.voteCount = 0;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public Election getElection() {
        return election;
    }

    public void setElection(Election election) {
        this.election = election;
    }
}