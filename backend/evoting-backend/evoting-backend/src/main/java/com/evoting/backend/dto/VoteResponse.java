package com.evoting.backend.dto;

import java.time.LocalDateTime;

public class VoteResponse {

    private Long voteId;
    private Long electionId;
    private String message;
    private LocalDateTime castAt;
    private String blockchainTransactionHash;

    public VoteResponse() {
    }

    public VoteResponse(
            Long voteId,
            Long electionId,
            String message,
            LocalDateTime castAt,
            String blockchainTransactionHash) {

        this.voteId = voteId;
        this.electionId = electionId;
        this.message = message;
        this.castAt = castAt;
        this.blockchainTransactionHash = blockchainTransactionHash;
    }

    public Long getVoteId() {
        return voteId;
    }

    public Long getElectionId() {
        return electionId;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getCastAt() {
        return castAt;
    }

    public String getBlockchainTransactionHash() {
        return blockchainTransactionHash;
    }
}
