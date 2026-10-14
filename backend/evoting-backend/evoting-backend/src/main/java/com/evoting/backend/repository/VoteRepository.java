package com.evoting.backend.repository;

import com.evoting.backend.entity.Election;
import com.evoting.backend.entity.Vote;
import com.evoting.backend.entity.Voter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    boolean existsByVoterAndElection(Voter voter, Election election);

    List<Vote> findByElection(Election election);

    List<Vote> findByVoter(Voter voter);
}
