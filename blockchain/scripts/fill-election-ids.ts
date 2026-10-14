import { network } from "hardhat";

async function main() {
  const { viem } = await network.create();
  const publicClient = await viem.getPublicClient();

  const address = "0x9a676e781a523b5d0c0e43731313a708cb607508";
  const voting = await viem.getContractAt("Voting", address);

  const nextId = await voting.read.nextElectionId();

  if (nextId !== 3n) {
    throw new Error(
      `Expected next election ID 3, found ${nextId}. Stop safely.`
    );
  }

  const latestBlock = await publicClient.getBlock({ blockTag: "latest" });
  const startTime = latestBlock.timestamp + 3600n;
  const endTime = startTime + 7200n;

  console.log("Latest blockchain timestamp:", latestBlock.timestamp.toString());
  console.log("New election start:", startTime.toString());

  for (let id = 3n; id <= 7n; id++) {
    const hash = await voting.write.createElection([
      `Placeholder Election ${id}`,
      startTime,
      endTime,
    ]);

    await publicClient.waitForTransactionReceipt({ hash });
    console.log(`Created placeholder election ${id}`);
  }

  const candidateHash = await voting.write.addCandidate([
    7n,
    8n,
    "Candidate Three",
  ]);

  await publicClient.waitForTransactionReceipt({ hash: candidateHash });

  console.log("Blockchain election 7 and Candidate 8 created.");
}

main().catch((error) => {
  console.error(error);
  process.exitCode = 1;
});
