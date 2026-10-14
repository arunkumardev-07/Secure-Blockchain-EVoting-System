package com.evoting.backend.repository;

import com.evoting.backend.entity.Candidate;
import com.evoting.backend.entity.Election;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {

    List<Candidate> findByElection(Election election);
}