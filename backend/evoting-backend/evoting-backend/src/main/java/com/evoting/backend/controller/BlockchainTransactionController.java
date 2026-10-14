
package com.evoting.backend.controller;

import com.evoting.backend.service.BlockchainTransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/blockchain/transactions")
public class BlockchainTransactionController {

    private final BlockchainTransactionService transactionService;

    public BlockchainTransactionController(
            BlockchainTransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/signer")
    public ResponseEntity<Map<String, String>> getSigner() {

        String privateKey = System.getenv("BLOCKCHAIN_PRIVATE_KEY");

        if (privateKey == null || privateKey.isBlank()) {
            return ResponseEntity.internalServerError().body(
                    Map.of(
                            "error",
                            "BLOCKCHAIN_PRIVATE_KEY is not configured"
                    )
            );
        }

        try {
            org.web3j.crypto.Credentials credentials =
                    org.web3j.crypto.Credentials.create(privateKey);

            return ResponseEntity.ok(
                    Map.of(
                            "configured", "true",
                            "signerAddress", credentials.getAddress()
                    )
            );

        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "The configured test key is invalid")
            );
        }
    }

    @PostMapping("/test-vote")
    public ResponseEntity<Map<String, String>> testVote(
            @RequestParam long electionId,
            @RequestParam long candidateId,
            @RequestParam Long voterId) throws Exception {

        String transactionHash = transactionService.recordVote(
                electionId,
                candidateId,
                voterId
        );

        Map<String, String> response = new LinkedHashMap<>();
        response.put("status", "success");
        response.put("message", "Blockchain transaction confirmed");
        response.put("transactionHash", transactionHash);

        return ResponseEntity.ok(response);
    }
}
