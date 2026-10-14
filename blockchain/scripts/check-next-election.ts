import { network } from "hardhat";

async function main() {
  const { viem } = await network.create();
  const deployment = await import("../deployment-address.json");
  const address = deployment.default.address;
  const voting = await viem.getContractAt("Voting", address);

  console.log("Contract address:", address);
  console.log("Next election ID:", (await voting.read.nextElectionId()).toString());
}

main().catch(console.error);
