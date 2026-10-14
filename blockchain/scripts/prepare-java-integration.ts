
import { network } from "hardhat";

async function main() {
    const { viem } = await network.create();
    const publicClient = await viem.getPublicClient();

    const deployment = await import("../deployment-address.json");
    const address = deployment.default.address;
    const voting = await viem.getContractAt("Voting", address);

    const block = await publicClient.getBlock({ blockTag: "latest" });
    const latestTimestamp = block.timestamp;

    // Give the local blockchain a safe buffer before voting starts.
    const startTime = latestTimestamp + 7200n;
    const endTime = startTime + 86400n;

    const electionId = await voting.read.nextElectionId();

    console.log("Contract address:", address);
    console.log("Latest timestamp:", latestTimestamp.toString());
    console.log("New election ID:", electionId.toString());
    console.log("Start timestamp:", startTime.toString());
    console.log("End timestamp:", endTime.toString());

    const createHash = await voting.write.createElection([
        "Tamil Nadu Demo Election - Java Integration",
        startTime,
        endTime
    ]);

    const createReceipt = await publicClient.waitForTransactionReceipt({
        hash: createHash
    });

    if (createReceipt.status !== "success") {
        throw new Error("Blockchain election creation failed");
    }

    const parties = [
        { id: 114n, name: "TVK" },
        { id: 115n, name: "DMK" },
        { id: 116n, name: "AIADMK" },
        { id: 117n, name: "NTK" }
    ];

    for (const party of parties) {
        const candidateHash = await voting.write.addCandidate([
            electionId,
            party.id,
            party.name
        ]);

        const receipt = await publicClient.waitForTransactionReceipt({
            hash: candidateHash
        });

        if (receipt.status !== "success") {
            throw new Error(`Failed to add ${party.name}`);
        }

        console.log(
            `Added ${party.name}, blockchain candidate ID ${party.id}`
        );
    }

    console.log("\nElection setup completed successfully.");
    console.log("Contract address:", address);
    console.log("Blockchain election ID:", electionId.toString());
    console.log("Java election ID: 15");
    console.log("Java candidate 14 -> blockchain candidate 114 (TVK)");
    console.log("Java candidate 15 -> blockchain candidate 115 (DMK)");
    console.log("Java candidate 16 -> blockchain candidate 116 (AIADMK)");
    console.log("Java candidate 17 -> blockchain candidate 117 (NTK)");
}

main().catch((error) => {
    console.error("Election setup failed:", error);
    process.exitCode = 1;
});
