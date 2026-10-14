package com.evoting.backend.controller;

import com.evoting.backend.service.BlockchainService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/blockchain")
public class BlockchainController {

    private final BlockchainService blockchainService;

    public BlockchainController(BlockchainService blockchainService) {
        this.blockchainService = blockchainService;
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, String>> getStatus()
            throws IOException {
        return ResponseEntity.ok(
                blockchainService.getBlockchainStatus()
        );
    }
}
