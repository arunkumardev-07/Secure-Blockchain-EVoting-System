package com.evoting.backend.controller;

import com.evoting.backend.dto.VoteRequest;
import com.evoting.backend.dto.VoteResponse;
import com.evoting.backend.service.VoteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/votes")
public class VoteController {

    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping
    public ResponseEntity<VoteResponse> castVote(
            @RequestParam Long voterId,
            @Valid @RequestBody VoteRequest request) {

        VoteResponse response =
                voteService.castVote(voterId, request);

        return ResponseEntity.ok(response);
    }
}
