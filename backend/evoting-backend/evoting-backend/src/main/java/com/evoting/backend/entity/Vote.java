package com.evoting.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "votes",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_vote_voter_election",
            columnNames = {"voter_id", "election_id"}
        )
    }
)
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "voter_id", nullable = false)
    private Voter voter;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "election_id", nullable = false)
    private Election election;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;

    @Column(nullable = false, updatable = false)
    private LocalDateTime castAt;

    @Column(length = 64)
    private String voteHash;

    @Column(length = 100)
    private String blockchainTransactionHash;

    public Vote() {
    }

    @PrePersist
    protected void onCreate() {
        if (castAt == null) {
            castAt = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public Voter getVoter() {
        return voter;
    }

    public void setVoter(Voter voter) {
        this.voter = voter;
    }

    public Election getElection() {
        return election;
    }

    public void setElection(Election election) {
        this.election = election;
    }

    public Candidate getCandidate() {
        return candidate;
    }

    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
    }

    public LocalDateTime getCastAt() {
        return castAt;
    }

    public String getVoteHash() {
        return voteHash;
    }

    public void setVoteHash(String voteHash) {
        this.voteHash = voteHash;
    }

    public String getBlockchainTransactionHash() {
        return blockchainTransactionHash;
    }

    public void setBlockchainTransactionHash(
            String blockchainTransactionHash) {
        this.blockchainTransactionHash = blockchainTransactionHash;
    }
}
