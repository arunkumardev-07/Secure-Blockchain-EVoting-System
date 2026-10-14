
 // SPDX-License-Identifier: MIT
pragma solidity ^0.8.28;

contract Voting {

    struct Candidate {
        uint256 id;
        string name;
        uint256 voteCount;
    }

    struct Election {
        string title;
        uint256 startTime;
        uint256 endTime;
        bool exists;
    }

    address public owner;
    uint256 public nextElectionId = 1;

    mapping(uint256 => Election) public elections;
    mapping(uint256 => Candidate[]) private candidates;

    // Existing wallet-based voting.
    mapping(uint256 => mapping(address => bool)) public hasVoted;

    // New backend-managed demo voter identifiers.
    mapping(uint256 => mapping(bytes32 => bool))
        public hasVotedNullifier;

    mapping(uint256 => mapping(uint256 => bool))
        private candidateExists;

    event ElectionCreated(
        uint256 indexed electionId,
        string title,
        uint256 startTime,
        uint256 endTime
    );

    event CandidateAdded(
        uint256 indexed electionId,
        uint256 indexed candidateId,
        string name
    );

    event VoteCast(
        uint256 indexed electionId,
        uint256 indexed candidateId
    );

    constructor() {
        owner = msg.sender;
    }

    modifier onlyOwner() {
        require(
            msg.sender == owner,
            "Only owner can perform this action"
        );
        _;
    }

    function createElection(
        string memory title,
        uint256 startTime,
        uint256 endTime
    ) external onlyOwner returns (uint256) {
        require(bytes(title).length > 0, "Title is required");
        require(
            startTime >= block.timestamp,
            "Start time must be in the future"
        );
        require(
            endTime > startTime,
            "End time must be after start time"
        );

        uint256 electionId = nextElectionId++;

        elections[electionId] = Election({
            title: title,
            startTime: startTime,
            endTime: endTime,
            exists: true
        });

        emit ElectionCreated(
            electionId,
            title,
            startTime,
            endTime
        );

        return electionId;
    }

    function addCandidate(
        uint256 electionId,
        uint256 candidateId,
        string memory name
    ) external onlyOwner {
        Election storage election = elections[electionId];

        require(election.exists, "Election does not exist");
        require(
            block.timestamp < election.startTime,
            "Election has already started"
        );
        require(candidateId > 0, "Candidate ID must be positive");
        require(
            !candidateExists[electionId][candidateId],
            "Candidate already exists"
        );
        require(bytes(name).length > 0, "Candidate name is required");

        candidates[electionId].push(
            Candidate({
                id: candidateId,
                name: name,
                voteCount: 0
            })
        );

        candidateExists[electionId][candidateId] = true;

        emit CandidateAdded(electionId, candidateId, name);
    }

    // Original wallet-based voting method.
    function vote(
        uint256 electionId,
        uint256 candidateId
    ) external {
        require(
            !hasVoted[electionId][msg.sender],
            "Address has already voted"
        );

        _recordVote(electionId, candidateId);

        hasVoted[electionId][msg.sender] = true;
    }

    // New method for authenticated Java demo voters.
    function voteWithNullifier(
        uint256 electionId,
        uint256 candidateId,
        bytes32 nullifier
    ) external {
        require(
            nullifier != bytes32(0),
            "Nullifier is required"
        );
        require(
            !hasVotedNullifier[electionId][nullifier],
            "Voter has already voted"
        );

        _recordVote(electionId, candidateId);

        hasVotedNullifier[electionId][nullifier] = true;
    }

    function _recordVote(
        uint256 electionId,
        uint256 candidateId
    ) internal {
        Election storage election = elections[electionId];

        require(election.exists, "Election does not exist");
        require(
            block.timestamp >= election.startTime,
            "Voting has not started"
        );
        require(
            block.timestamp <= election.endTime,
            "Voting has ended"
        );
        require(
            candidateExists[electionId][candidateId],
            "Candidate does not exist"
        );

        Candidate[] storage electionCandidates =
            candidates[electionId];

        for (uint256 i = 0; i < electionCandidates.length; i++) {
            if (electionCandidates[i].id == candidateId) {
                electionCandidates[i].voteCount++;
                break;
            }
        }

        emit VoteCast(electionId, candidateId);
    }

    function getCandidates(
        uint256 electionId
    ) external view returns (Candidate[] memory) {
        require(
            elections[electionId].exists,
            "Election does not exist"
        );

        return candidates[electionId];
    }
}
