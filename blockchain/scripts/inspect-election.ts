import { network } from "hardhat";

async function main() {
  const { viem } = await network.create();
  const publicClient = await viem.getPublicClient();

  const address = "0x9a676e781a523b5d0c0e43731313a708cb607508";
  const voting = await viem.getContractAt("Voting", address);

  const election = await voting.read.elections([2n]);
  const candidates = await voting.read.getCandidates([2n]);
  const block = await publicClient.getBlock({ blockTag: "latest" });

  console.log("Latest block timestamp:", block.timestamp.toString());
  console.log("Election title:", election[0]);
  console.log("Election start:", election[1].toString());
  console.log("Election end:", election[2].toString());
  console.log("Election exists:", election[3]);
  console.log("Candidates:", candidates);
}

main().catch((error) => {
  console.error(error);
  process.exitCode = 1;
});
