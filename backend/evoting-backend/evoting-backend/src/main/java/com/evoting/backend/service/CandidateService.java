package com.evoting.backend.service;

import com.evoting.backend.dto.CandidateRequest;
import com.evoting.backend.entity.Candidate;
import com.evoting.backend.entity.Election;
import com.evoting.backend.exception.ElectionStateException;
import com.evoting.backend.repository.CandidateRepository;
import com.evoting.backend.repository.ElectionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final ElectionRepository electionRepository;

    public CandidateService(
            CandidateRepository candidateRepository,
            ElectionRepository electionRepository) {

        this.candidateRepository = candidateRepository;
        this.electionRepository = electionRepository;
    }

    public Candidate addCandidate(
            Long electionId,
            CandidateRequest request) {

        Election election = electionRepository.findById(electionId)
                .orElseThrow(() ->
                        new RuntimeException("Election not found"));

        /*
         * Candidates can only be added while the election
         * is still in DRAFT state.
         */
        if (!"DRAFT".equals(election.getStatus())) {
            throw new ElectionStateException(
                    "Candidates can only be added to a DRAFT election"
            );
        }

        Candidate candidate = new Candidate();

        candidate.setName(request.getName());
        candidate.setDescription(request.getDescription());

        /*
         * Vote count is controlled by the server.
         * The client cannot choose the initial vote count.
         */
        candidate.setVoteCount(0);

        candidate.setElection(election);

        return candidateRepository.save(candidate);
    }

    public List<Candidate> getCandidatesByElection(Long electionId) {

        Election election = electionRepository.findById(electionId)
                .orElseThrow(() ->
                        new RuntimeException("Election not found"));

        return candidateRepository.findByElection(election);
    }

    public Candidate getCandidateById(Long id) {

        return candidateRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Candidate not found"));
    }
}
