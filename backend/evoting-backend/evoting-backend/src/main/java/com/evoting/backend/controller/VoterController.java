package com.evoting.backend.controller;

import com.evoting.backend.dto.LoginRequest;
import com.evoting.backend.dto.LoginResponse;
import com.evoting.backend.dto.VoterResponse;
import com.evoting.backend.entity.Voter;
import com.evoting.backend.service.VoterService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/voters")
public class VoterController {

    private final VoterService voterService;

    public VoterController(VoterService voterService) {
        this.voterService = voterService;
    }

    @PostMapping("/register")
    public ResponseEntity<VoterResponse> registerVoter(
            @Valid @RequestBody Voter voter) {

        Voter registeredVoter = voterService.registerVoter(voter);

        VoterResponse response = new VoterResponse(
                registeredVoter.getId(),
                registeredVoter.getFullName(),
                registeredVoter.getEmail(),
                registeredVoter.isHasVoted()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest loginRequest) {

        LoginResponse response = voterService.login(loginRequest);

        return ResponseEntity.ok(response);
    }
}