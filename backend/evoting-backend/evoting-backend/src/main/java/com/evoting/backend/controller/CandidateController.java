package com.evoting.backend.controller;

import com.evoting.backend.dto.CandidateRequest;
import com.evoting.backend.dto.CandidateResponse;
import com.evoting.backend.entity.Candidate;
import com.evoting.backend.service.CandidateService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/elections/{electionId}/candidates")
public class CandidateController {

    private final CandidateService candidateService;

    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @PostMapping
    public ResponseEntity<CandidateResponse> addCandidate(
            @PathVariable Long electionId,
            @Valid @RequestBody CandidateRequest request) {

        Candidate candidate =
                candidateService.addCandidate(electionId, request);

        CandidateResponse response = convertToResponse(candidate);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<CandidateResponse>> getCandidates(
            @PathVariable Long electionId) {

        List<CandidateResponse> responses =
                candidateService.getCandidatesByElection(electionId)
                        .stream()
                        .map(this::convertToResponse)
                        .toList();

        return ResponseEntity.ok(responses);
    }

    private CandidateResponse convertToResponse(Candidate candidate) {

        return new CandidateResponse(
                candidate.getId(),
                candidate.getName(),
                candidate.getDescription(),
                candidate.getVoteCount(),
                candidate.getElection().getId()
        );
    }
}