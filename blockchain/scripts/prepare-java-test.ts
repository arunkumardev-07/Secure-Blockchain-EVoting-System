import { network } from "hardhat";

const { viem } = await network.create();

async function main() {
  const publicClient = await viem.getPublicClient();
  const [owner] = await viem.getWalletClients();

  if (!owner) {
    throw new Error("Hardhat owner account not found");
  }

  const voting = await viem.deployContract("Voting");

  const block = await publicClient.getBlock({ blockTag: "latest" });
  const startTime = block.timestamp + 60n;
  const endTime = block.timestamp + 3600n;

  let hash = await voting.write.createElection([
    "Java Blockchain Integration Test",
    startTime,
    endTime,
  ]);

  await publicClient.waitForTransactionReceipt({ hash });

  hash = await voting.write.addCandidate([
    1n,
    101n,
    "Candidate One",
  ]);

  await publicClient.waitForTransactionReceipt({ hash });

  console.log("Test election created.");
  console.log("Election ID: 1");
  console.log("Candidate ID: 101");
  console.log("Contract address:", voting.address);
  console.log("Voting starts at:", startTime.toString());
  console.log("Voting ends at:", endTime.toString());
}

main().catch((error) => {
  console.error(error);
  process.exitCode = 1;
});
