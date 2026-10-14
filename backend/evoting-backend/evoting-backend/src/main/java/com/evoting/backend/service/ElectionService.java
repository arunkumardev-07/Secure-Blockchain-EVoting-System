
package com.evoting.backend.service;

import com.evoting.backend.entity.Election;
import com.evoting.backend.repository.ElectionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ElectionService {

    private final ElectionRepository electionRepository;

    public ElectionService(ElectionRepository electionRepository) {
        this.electionRepository = electionRepository;
    }

    public Election createElection(Election election) {
        election.setStatus("DRAFT");
        return electionRepository.save(election);
    }

    public List<Election> getAllElections() {
        return electionRepository.findAll();
    }

    public Election getElectionById(Long id) {
        return electionRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Election not found"));
    }

    public Election startElection(Long id) {
        Election election = getElectionById(id);

        if (!"DRAFT".equals(election.getStatus())) {
            throw new RuntimeException(
                    "Only a DRAFT election can be started");
        }

        if (election.getStartTime().isAfter(LocalDateTime.now())) {
            throw new RuntimeException(
                    "Election start time has not arrived");
        }

        election.setStatus("ACTIVE");
        return electionRepository.save(election);
    }

    public Election endElection(Long id) {
        Election election = getElectionById(id);

        if (!"ACTIVE".equals(election.getStatus())) {
            throw new RuntimeException(
                    "Only an ACTIVE election can be ended");
        }

        election.setStatus("ENDED");
        return electionRepository.save(election);
    }

    // DEMO ONLY: extend an active election by up to 120 minutes.
    public Election extendElectionTime(Long id, long extraMinutes) {
        Election election = getElectionById(id);

        if (!"ACTIVE".equals(election.getStatus())) {
            throw new RuntimeException(
                    "Only an ACTIVE election can be extended");
        }

        if (extraMinutes < 1 || extraMinutes > 120) {
            throw new IllegalArgumentException(
                    "Extension must be between 1 and 120 minutes");
        }

        election.setEndTime(
                election.getEndTime().plusMinutes(extraMinutes));

        return electionRepository.save(election);
    }
}
