package com.evoting.backend.controller;

import com.evoting.backend.service.VotingContractService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/blockchain/contract")
public class VotingContractController {

    private final VotingContractService votingContractService;

    public VotingContractController(
            VotingContractService votingContractService) {
        this.votingContractService = votingContractService;
    }

    @GetMapping("/owner")
    public ResponseEntity<Map<String, String>> getOwner()
            throws Exception {

        Map<String, String> response = new LinkedHashMap<>();
        response.put("connected", "true");
        response.put("contractAddress",
                votingContractService.getContractAddress());
        response.put("owner", votingContractService.getOwner());

        return ResponseEntity.ok(response);
    }
}
