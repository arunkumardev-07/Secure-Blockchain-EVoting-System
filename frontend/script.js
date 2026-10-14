
const API_BASE = "http://localhost:8081/api";
const ELECTION_ID = 15;
const BLOCKCHAIN_ELECTION_ID = 1;

let loggedInVoterId = null;
let candidates = [];
let voteSubmitted = false;

function showMessage(elementId, message) {
    document.getElementById(elementId).textContent = message;
}

async function readResponse(response) {
    const text = await response.text();
    let data = {};

    if (text) {
        try {
            data = JSON.parse(text);
        } catch {
            data = { message: text };
        }
    }

    if (!response.ok) {
        throw new Error(data.message || `Request failed: ${response.status}`);
    }

    return data;
}

// LOGIN
document.getElementById("loginForm").addEventListener("submit", async (event) => {
    event.preventDefault();

    const email = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value;
    const loginButton = event.submitter;

    if (loginButton) loginButton.disabled = true;
    showMessage("loginMessage", "Signing in...");

    try {
        const response = await fetch(`${API_BASE}/voters/login`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email, password })
        });

        const data = await readResponse(response);
        const voterId = data.id ?? data.voterId;

        if (!voterId) {
            throw new Error("The login response did not contain a voter ID.");
        }

        loggedInVoterId = voterId;
        voteSubmitted = false;

        document.getElementById("loginPage").hidden = true;
        document.getElementById("votingPage").hidden = false;
        document.getElementById("receiptPanel").hidden = true;

        showMessage("voteMessage", "");
        await loadCandidates();
        await loadResults();
    } catch (error) {
        showMessage("loginMessage", error.message);
    } finally {
        if (loginButton) loginButton.disabled = false;
    }
});

// PARTY SYMBOLS: illustrative symbols for this academic demonstration.
// The TVK symbol is shown as a whistle illustration.
// Return the local SVG path for each party's illustrative symbol.
function getPartySymbolPath(name) {
    const symbols = {
        TVK: "assets/tvk-whistle.svg",
        DMK: "assets/dmk-sun.svg",
        AIADMK: "assets/aiadmk-leaves.svg",
        NTK: "assets/ntk-microphone.svg"
    };

    return symbols[name] || "assets/tvk-whistle.svg";
}

function createPartySymbolImage(name) {
    const image = document.createElement("img");
    image.src = getPartySymbolPath(name);
    image.alt = `${name} illustrative symbol`;
    image.className = "party-symbol-image";
    image.width = 64;
    image.height = 64;
    image.loading = "lazy";
    return image;
}

// LOAD PARTIES
async function loadCandidates() {
    const list = document.getElementById("candidateList");
    list.replaceChildren();

    const loading = document.createElement("p");
    loading.textContent = "Loading parties...";
    list.appendChild(loading);

    try {
        const response = await fetch(
            `${API_BASE}/elections/${ELECTION_ID}/candidates`
        );

        candidates = await readResponse(response);
        list.replaceChildren();

        if (!Array.isArray(candidates) || candidates.length === 0) {
            list.textContent = "No candidates are available for this election.";
            return;
        }

        candidates.forEach((candidate, index) => {
            const row = document.createElement("article");
            row.className = "party-row";

            const number = document.createElement("div");
            number.className = "party-number";
            number.textContent = `${index + 1}.`;

            const details = document.createElement("div");
            details.className = "party-details";

            const name = document.createElement("div");
            name.className = "party-name";
            name.textContent = candidate.name;

            const description = document.createElement("div");
            description.className = "party-votes";
            description.textContent =
                candidate.description || "Illustrative party symbol";

            details.append(name, description);

            const symbol = document.createElement("div");
            symbol.className = "party-symbol";
            symbol.setAttribute("aria-label", `${candidate.name} illustrative symbol`);
            symbol.appendChild(createPartySymbolImage(candidate.name));

            const voteButton = document.createElement("button");
            voteButton.type = "button";
            voteButton.className = "vote-button";
            voteButton.textContent = "Vote";
            voteButton.disabled = voteSubmitted;

            voteButton.addEventListener("click", () => {
                castVote(candidate, voteButton);
            });

            row.append(number, details, symbol, voteButton);
            list.appendChild(row);
        });
    } catch (error) {
        list.replaceChildren();
        const errorText = document.createElement("p");
        errorText.textContent = `Unable to load parties: ${error.message}`;
        list.appendChild(errorText);
    }
}

// CAST VOTE
async function castVote(candidate, button) {
    if (!loggedInVoterId) {
        showMessage("voteMessage", "Please log in first.");
        return;
    }

    if (voteSubmitted) {
        showMessage("voteMessage", "You have already voted in this session.");
        return;
    }

    if (!window.confirm(
        `Are you sure you want to vote for ${candidate.name}? You cannot change your vote after submission.`
    )) {
        return;
    }

    button.disabled = true;
    button.textContent = "Submitting...";

    try {
        const response = await fetch(
            `${API_BASE}/votes?voterId=${encodeURIComponent(loggedInVoterId)}`,
            {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({
                    electionId: ELECTION_ID,
                    candidateId: candidate.id
                })
            }
        );

        const result = await readResponse(response);
        voteSubmitted = true;

        const transactionHash =
            result.blockchainTransactionHash ||
            result.transactionHash ||
            result.txHash;

        showMessage("voteMessage", "Your vote was submitted successfully.");

        document.getElementById("receiptText").textContent = transactionHash
            ? `Blockchain transaction: ${transactionHash}`
            : "The server accepted your vote. No transaction hash was returned.";

        document.getElementById("receiptPanel").hidden = false;

        document.querySelectorAll(".vote-button").forEach(voteButton => {
            voteButton.disabled = true;
            voteButton.textContent = "Voting closed";
        });

        await loadCandidates();
        await loadResults();
    } catch (error) {
        showMessage("voteMessage", `Vote failed: ${error.message}`);
        button.disabled = false;
        button.textContent = "Vote";
    }
}

// LOAD REAL BLOCKCHAIN RESULTS
async function loadResults() {
    const resultsContainer = document.getElementById("candidateResults");
    const refreshButton = document.getElementById("refreshResultsButton");

    refreshButton.disabled = true;
    showMessage("resultsMessage", "Loading blockchain results...");

    try {
        const response = await fetch(
            `${API_BASE}/blockchain/results?electionId=${BLOCKCHAIN_ELECTION_ID}`
        );

        const results = await readResponse(response);

        if (!Array.isArray(results)) {
            throw new Error("Unexpected results API response.");
        }

        const totalVotes = results.reduce(
            (sum, candidate) => sum + Number(candidate.voteCount || 0),
            0
        );

        document.getElementById("totalVotes").textContent = totalVotes;

        const maxVotes = results.length
            ? Math.max(...results.map(candidate => Number(candidate.voteCount || 0)))
            : 0;

        const leaders = results.filter(
            candidate => Number(candidate.voteCount || 0) === maxVotes
        );

        if (totalVotes === 0) {
            document.getElementById("leaderTitle").textContent = "No votes recorded yet";
            document.getElementById("leaderDescription").textContent =
                "The blockchain currently has no recorded votes.";
        } else if (leaders.length > 1) {
            document.getElementById("leaderTitle").textContent = "Provisional tie";
            document.getElementById("leaderDescription").textContent =
                "More than one candidate currently has the highest vote count.";
        } else {
            document.getElementById("leaderTitle").textContent =
                `Provisional leader: ${leaders[0].name}`;
            document.getElementById("leaderDescription").textContent =
                `${leaders[0].name} currently has the most recorded votes.`;
        }

        resultsContainer.replaceChildren();

        results.forEach(candidate => {
            const votes = Number(candidate.voteCount || 0);
            const percentage = totalVotes > 0 ? (votes / totalVotes) * 100 : 0;

            const card = document.createElement("article");
            card.className = "candidate-result";

            const top = document.createElement("div");
            top.className = "candidate-result-top";

            const symbol = document.createElement("div");
            symbol.className = "party-symbol";
            symbol.appendChild(createPartySymbolImage(candidate.name));

            const details = document.createElement("div");
            details.className = "candidate-result-details";

            const name = document.createElement("h3");
            name.textContent = candidate.name;

            const percentageText = document.createElement("p");
            percentageText.textContent = `${percentage.toFixed(1)}% of recorded votes`;

            details.append(name, percentageText);

            const voteCount = document.createElement("div");
            voteCount.className = "result-vote-count";

            const number = document.createElement("strong");
            number.textContent = votes;

            const label = document.createElement("span");
            label.textContent = votes === 1 ? "vote" : "votes";

            voteCount.append(number, label);
            top.append(symbol, details, voteCount);

            const track = document.createElement("div");
            track.className = "progress-track";

            const fill = document.createElement("div");
            fill.className = "progress-fill";
            fill.style.width = `${percentage}%`;

            track.appendChild(fill);
            card.append(top, track);
            resultsContainer.appendChild(card);
        });

        showMessage("resultsMessage", "Results updated from the blockchain successfully.");
    } catch (error) {
        showMessage("resultsMessage", `Unable to load results: ${error.message}`);
    } finally {
        refreshButton.disabled = false;
    }
}

document.getElementById("refreshResultsButton").addEventListener("click", loadResults);

// LOGOUT
document.getElementById("logoutButton").addEventListener("click", () => {
    loggedInVoterId = null;
    candidates = [];
    voteSubmitted = false;

    document.getElementById("votingPage").hidden = true;
    document.getElementById("loginPage").hidden = false;
    document.getElementById("loginForm").reset();

    showMessage("loginMessage", "");
    showMessage("voteMessage", "");
    showMessage("resultsMessage", "");
    document.getElementById("candidateList").replaceChildren();
    document.getElementById("candidateResults").replaceChildren();
    document.getElementById("receiptPanel").hidden = true;
});


