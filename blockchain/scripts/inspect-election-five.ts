
import { Contract, JsonRpcProvider } from "ethers";
import deployment from "../deployment-address.json";

async function main() {
    const contractAddress = deployment.address;
    const provider = new JsonRpcProvider("http://127.0.0.1:8545");

    const abi = [
        "function elections(uint256) view returns (string title, uint256 startTime, uint256 endTime, bool exists)",
        "function getCandidates(uint256) view returns (tuple(uint256 id, string name, uint256 voteCount)[])"
    ];

    const voting = new Contract(contractAddress, abi, provider);
    const electionId = 1;

    console.log("Contract address:", contractAddress);
    console.log("Connected chain ID:", (await provider.getNetwork()).chainId.toString());

    const code = await provider.getCode(contractAddress);
    if (code === "0x") {
        throw new Error(
            "No contract exists at this address on the running local blockchain. Do not restart or redeploy yet."
        );
    }

    const election = await voting.elections(electionId);
    const candidates = await voting.getCandidates(electionId);
    const block = await provider.getBlock("latest");

    console.log("\nElection ID:", electionId);
    console.log("Title:", election.title);
    console.log("Exists:", election.exists);
    console.log("Start timestamp:", election.startTime.toString());
    console.log("End timestamp:", election.endTime.toString());

    if (block) {
        console.log("Blockchain time:", new Date(block.timestamp * 1000).toLocaleString());
        console.log("Voting started:", BigInt(block.timestamp) >= election.startTime);
        console.log("Voting ended:", BigInt(block.timestamp) > election.endTime);
    }

    console.log("\nCandidates:");
    for (const candidate of candidates) {
        console.log({
            id: candidate.id.toString(),
            name: candidate.name,
            voteCount: candidate.voteCount.toString()
        });
    }
}

main().catch((error) => {
    console.error(error);
    process.exitCode = 1;
});
