import { network } from "hardhat";

async function main() {
  const { viem } = await network.create();

  const address = "0x9a676e781a523b5d0c0e43731313a708cb607508";
  const voting = await viem.getContractAt("Voting", address);

  const election = await voting.read.elections([7n]);
  const candidates = await voting.read.getCandidates([7n]);

  console.log("Blockchain Election 7:", election);
  console.log("Blockchain Candidates:", candidates);
  console.log("Contract address:", address);
}

main().catch((error) => {
  console.error(error);
  process.exitCode = 1;
});
