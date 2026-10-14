import { network } from "hardhat";

async function main() {
  const { viem } = await network.create();
  const publicClient = await viem.getPublicClient();

  const deployment = await import("../deployment-address.json");
  const address = deployment.default.address;
  const voting = await viem.getContractAt("Voting", address);

  const election = await voting.read.elections([3n]);
  const candidates = await voting.read.getCandidates([3n]);
  const block = await publicClient.getBlock({ blockTag: "latest" });

  console.log("Contract:", address);
  console.log("Latest block timestamp:", block.timestamp.toString());
  console.log("Election 3:", election);
  console.log("Candidates:", candidates);
}

main().catch((error) => {
  console.error(error);
  process.exitCode = 1;
});
