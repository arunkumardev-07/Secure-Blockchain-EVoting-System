import { network } from "hardhat";

async function main() {
  const { viem } = await network.create();
  const publicClient = await viem.getPublicClient();
  const address = "0x9a676e781a523b5d0c0e43731313a708cb607508";
  const voting = await viem.getContractAt("Voting", address);

  const nextId = await voting.read.nextElectionId();

  if (nextId > 7n) {
    throw new Error(
      `Blockchain next election ID is ${nextId}; cannot create election ID 7 safely.`
    );
  }

  if (nextId < 7n) {
    throw new Error(
      `Blockchain next election ID is ${nextId}; create earlier IDs first.`
    );
  }

  const block = await publicClient.getBlock({ blockTag: "latest" });
  const startTime = block.timestamp + 120n;
  const endTime = startTime + 7200n;

  const hash = await voting.write.createElection([
    "Java Blockchain Integration Test Three",
    startTime,
    endTime,
  ]);

  await publicClient.waitForTransactionReceipt({ hash });

  const candidateHash = await voting.write.addCandidate([
    7n,
    8n,
    "Candidate Three",
  ]);

  await publicClient.waitForTransactionReceipt({ hash: candidateHash });

  console.log("Blockchain election and candidate created.");
  console.log("Election ID: 7");
  console.log("Candidate ID: 8");
  console.log("Start timestamp:", startTime.toString());
  console.log("End timestamp:", endTime.toString());
  console.log("Contract address:", address);
}

main().catch((error) => {
  console.error(error);
  process.exitCode = 1;
});
