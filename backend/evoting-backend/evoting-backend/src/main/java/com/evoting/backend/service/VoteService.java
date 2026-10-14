
package com.evoting.backend.service;

import com.evoting.backend.dto.VoteRequest;
import com.evoting.backend.dto.VoteResponse;
import com.evoting.backend.entity.Candidate;
import com.evoting.backend.entity.Election;
import com.evoting.backend.entity.Vote;
import com.evoting.backend.entity.Voter;
import com.evoting.backend.exception.DuplicateVoteException;
import com.evoting.backend.exception.ElectionStateException;
import com.evoting.backend.repository.CandidateRepository;
import com.evoting.backend.repository.ElectionRepository;
import com.evoting.backend.repository.VoteRepository;
import com.evoting.backend.repository.VoterRepository;
import com.evoting.backend.util.HashUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class VoteService {

    private static final long JAVA_ELECTION_ID = 15L;
    private static final long BLOCKCHAIN_ELECTION_ID = 1L;

    private final VoteRepository voteRepository;
    private final VoterRepository voterRepository;
    private final ElectionRepository electionRepository;
    private final CandidateRepository candidateRepository;
    private final BlockchainTransactionService blockchainTransactionService;

    public VoteService(
            VoteRepository voteRepository,
            VoterRepository voterRepository,
            ElectionRepository electionRepository,
            CandidateRepository candidateRepository,
            BlockchainTransactionService blockchainTransactionService) {

        this.voteRepository = voteRepository;
        this.voterRepository = voterRepository;
        this.electionRepository = electionRepository;
        this.candidateRepository = candidateRepository;
        this.blockchainTransactionService = blockchainTransactionService;
    }

    @Transactional
    public VoteResponse castVote(Long voterId, VoteRequest request) {

        Voter voter = voterRepository.findById(voterId)
                .orElseThrow(() ->
                        new RuntimeException("Voter not found"));

        Election election = electionRepository.findById(
                        request.getElectionId())
                .orElseThrow(() ->
                        new RuntimeException("Election not found"));

        if (!"ACTIVE".equals(election.getStatus())) {
            throw new ElectionStateException(
                    "Votes can only be cast in an ACTIVE election");
        }

        LocalDateTime now = LocalDateTime.now();

        if (now.isBefore(election.getStartTime())
                || now.isAfter(election.getEndTime())) {
            throw new ElectionStateException(
                    "Voting is outside the permitted election period");
        }

        Candidate candidate = candidateRepository.findById(
                        request.getCandidateId())
                .orElseThrow(() ->
                        new RuntimeException("Candidate not found"));

        if (!candidate.getElection().getId()
                .equals(election.getId())) {
            throw new ElectionStateException(
                    "Candidate does not belong to this election");
        }

        if (voteRepository.existsByVoterAndElection(voter, election)) {
            throw new DuplicateVoteException(
                    "This voter has already voted in this election");
        }

        if (!election.getId().equals(JAVA_ELECTION_ID)) {
            throw new ElectionStateException(
                    "This election is not configured for blockchain voting");
        }

        long blockchainCandidateId =
                mapBlockchainCandidateId(candidate.getId());

        String transactionHash;

        try {
            transactionHash =
                    blockchainTransactionService.recordVote(
                            BLOCKCHAIN_ELECTION_ID,
                            blockchainCandidateId,
                            voter.getId()
                    );
        } catch (Exception exception) {
            throw new IllegalStateException(
                    "Blockchain vote failed: "
                            + exception.getMessage(),
                    exception
            );
        }

        Vote vote = new Vote();
        vote.setVoter(voter);
        vote.setElection(election);
        vote.setCandidate(candidate);
        vote.setBlockchainTransactionHash(transactionHash);

        String fingerprintInput =
                election.getId() + ":"
                        + candidate.getId() + ":"
                        + voter.getId() + ":"
                        + now;

        vote.setVoteHash(HashUtil.sha256(fingerprintInput));

        Vote savedVote = voteRepository.saveAndFlush(vote);

        return new VoteResponse(
                savedVote.getId(),
                election.getId(),
                "Vote recorded successfully on blockchain",
                savedVote.getCastAt(),
                savedVote.getBlockchainTransactionHash()
        );
    }

    private long mapBlockchainCandidateId(Long javaCandidateId) {

        if (javaCandidateId == null) {
            throw new ElectionStateException(
                    "Candidate ID is required");
        }

        switch (javaCandidateId.intValue()) {
            case 14:
                return 114L; // TVK
            case 15:
                return 115L; // DMK
            case 16:
                return 116L; // AIADMK
            case 17:
                return 117L; // NTK
            default:
                throw new ElectionStateException(
                        "Candidate is not mapped to the blockchain");
        }
    }
}
