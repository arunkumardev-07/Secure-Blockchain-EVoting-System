import { JsonRpcProvider } from "ethers";

async function main() {
    const provider = new JsonRpcProvider(
        "http://127.0.0.1:8545"
    );

    // Start time from the current blockchain election.
    const electionStartTimestamp = 1791981809;

    const latestBlock = await provider.getBlock("latest");

    if (!latestBlock) {
        throw new Error("Could not read the latest block.");
    }

    const currentTimestamp = latestBlock.timestamp;

    console.log("Current blockchain timestamp:", currentTimestamp);
    console.log("Election start timestamp:", electionStartTimestamp);

    if (currentTimestamp >= electionStartTimestamp) {
        console.log("The election start time has already arrived.");
        return;
    }

    const secondsToAdvance =
        electionStartTimestamp - currentTimestamp + 5;

    await provider.send("evm_increaseTime", [
        secondsToAdvance
    ]);

    await provider.send("evm_mine", []);

    const updatedBlock = await provider.getBlock("latest");

    console.log(
        "Updated blockchain timestamp:",
        updatedBlock?.timestamp
    );

    console.log("Blockchain time advanced successfully.");
}

main().catch((error) => {
    console.error(error);
    process.exitCode = 1;
});