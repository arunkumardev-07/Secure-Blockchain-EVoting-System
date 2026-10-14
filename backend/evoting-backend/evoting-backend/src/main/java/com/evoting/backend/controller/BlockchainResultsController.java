
package com.evoting.backend.controller;

import com.evoting.backend.service.BlockchainResultsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/blockchain/results")
public class BlockchainResultsController {

    private final BlockchainResultsService resultsService;

    public BlockchainResultsController(
            BlockchainResultsService resultsService) {
        this.resultsService = resultsService;
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getResults(
            @RequestParam(defaultValue = "1") long electionId)
            throws Exception {

        return ResponseEntity.ok(
                resultsService.getCandidates(electionId));
    }
}
