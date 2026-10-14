import { network } from "hardhat";

async function main() {
  const { viem } = await network.create();
  const publicClient = await viem.getPublicClient();
  const address = "0x5fbdb2315678afecb367f032d93f642f64180aa3";
  const voting = await viem.getContractAt("Voting", address);

  const block = await publicClient.getBlock({ blockTag: "latest" });
  const startTime = block.timestamp + 300n;
  const endTime = startTime + 3600n;

  const hash = await voting.write.createElection([
    "Java Transaction Test Two",
    startTime,
    endTime,
  ]);

  const receipt = await publicClient.waitForTransactionReceipt({ hash });
  if (receipt.status !== "success") {
    throw new Error("Election creation transaction failed");
  }

  const candidateHash = await voting.write.addCandidate([
    2n,
    102n,
    "Candidate Two",
  ]);

  const candidateReceipt =
    await publicClient.waitForTransactionReceipt({ hash: candidateHash });

  if (candidateReceipt.status !== "success") {
    throw new Error("Candidate creation transaction failed");
  }

  console.log("New election created successfully.");
  console.log("Election ID: 2");
  console.log("Candidate ID: 102");
  console.log("Start timestamp:", startTime.toString());
  console.log("End timestamp:", endTime.toString());
  console.log("Contract address:", address);
}

main().catch((error) => {
  console.error(error);
  process.exitCode = 1;
});

