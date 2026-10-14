import { network } from "hardhat";

async function main() {
  const { viem } = await network.create();
  const deployment = await import("../deployment-address.json");
  const voting = await viem.getContractAt("Voting", deployment.default.address);

  const election = await voting.read.elections([3n]);
  const candidates = await voting.read.getCandidates([3n]);

  console.log("Contract:", deployment.default.address);
  console.log("Election 3:", election);
  console.log("Candidates:", candidates);
  console.log("Next election ID:", (await voting.read.nextElectionId()).toString());
}

main().catch(console.error);




