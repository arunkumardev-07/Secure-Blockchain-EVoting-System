
package com.evoting.backend.controller;

import com.evoting.backend.entity.Election;
import com.evoting.backend.service.ElectionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/elections")
public class ElectionController {

    private final ElectionService electionService;

    public ElectionController(ElectionService electionService) {
        this.electionService = electionService;
    }

    @PostMapping
    public ResponseEntity<Election> createElection(
            @RequestBody Election election) {

        return ResponseEntity.ok(
                electionService.createElection(election));
    }

    @GetMapping
    public ResponseEntity<List<Election>> getAllElections() {
        return ResponseEntity.ok(
                electionService.getAllElections());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Election> getElectionById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                electionService.getElectionById(id));
    }

    @PutMapping("/{id}/start")
    public ResponseEntity<Election> startElection(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                electionService.startElection(id));
    }

    @PutMapping("/{id}/end")
    public ResponseEntity<Election> endElection(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                electionService.endElection(id));
    }

    // DEMO ONLY: extend the end time of an active election.
    @PutMapping("/{id}/extend")
    public ResponseEntity<Election> extendElectionTime(
            @PathVariable Long id,
            @RequestParam long minutes) {

        return ResponseEntity.ok(
                electionService.extendElectionTime(id, minutes));
    }
}
