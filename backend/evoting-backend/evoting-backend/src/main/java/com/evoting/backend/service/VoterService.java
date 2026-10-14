package com.evoting.backend.service;

import com.evoting.backend.dto.LoginRequest;
import com.evoting.backend.dto.LoginResponse;
import com.evoting.backend.entity.Voter;
import com.evoting.backend.repository.VoterRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class VoterService {

    private final VoterRepository voterRepository;
    private final PasswordEncoder passwordEncoder;

    public VoterService(VoterRepository voterRepository,
                        PasswordEncoder passwordEncoder) {
        this.voterRepository = voterRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Voter registerVoter(Voter voter) {

        if (voterRepository.existsByEmail(voter.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        // Hash the password before saving
        String hashedPassword = passwordEncoder.encode(voter.getPassword());

        voter.setPassword(hashedPassword);

        return voterRepository.save(voter);
    }

    public LoginResponse login(LoginRequest loginRequest) {

        Voter voter = voterRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("Invalid email or password"));

        boolean passwordMatches = passwordEncoder.matches(
                loginRequest.getPassword(),
                voter.getPassword()
        );

        if (!passwordMatches) {
            throw new RuntimeException("Invalid email or password");
        }

        return new LoginResponse(
                voter.getId(),
                voter.getFullName(),
                voter.getEmail(),
                voter.isHasVoted(),
                "Login successful"
        );
    }
}