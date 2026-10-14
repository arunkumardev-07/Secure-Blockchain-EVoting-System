import { network } from "hardhat";

async function main() {
  const { viem } = await network.create();
  const deployment = await import("../deployment-address.json");
  const voting = await viem.getContractAt(
    "Voting",
    deployment.default.address
  );

  const hash = await voting.write.addCandidate([
    1n,
    102n,
    "Candidate Two"
  ]);

  const receipt = await (await viem.getPublicClient())
    .waitForTransactionReceipt({ hash });

  if (receipt.status !== "success") {
    throw new Error("Candidate transaction failed");
  }

  console.log("Candidate added successfully.");
  console.log("Election ID: 1");
  console.log("Candidate ID: 102");
  console.log("Transaction hash:", hash);
}

main().catch(console.error);
