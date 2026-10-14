import { network } from "hardhat";

async function main() {
  const { viem } = await network.create();

  const address = "0x5fbdb2315678afecb367f032d93f642f64180aa3";
  const voting = await viem.getContractAt("Voting", address);

  const election = await voting.read.elections([2n]);
  const candidates = await voting.read.getCandidates([2n]);

  console.log("Contract:", address);
  console.log("Election 2:", election);
  console.log("Candidates:", candidates);
  console.log("Next election ID:", await voting.read.nextElectionId());
}

main().catch((error) => {
  console.error(error);
  process.exitCode = 1;
});
