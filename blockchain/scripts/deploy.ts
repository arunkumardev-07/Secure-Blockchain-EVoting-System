import { network } from "hardhat";
import { writeFile } from "node:fs/promises";

const { viem, networkName } = await network.create();

async function main() {
  console.log(`Deploying Voting to ${networkName}...`);

  const voting = await viem.deployContract("Voting");

  const address = voting.address;

  await writeFile(
    "./deployment-address.json",
    JSON.stringify(
      {
        network: networkName,
        chainId: "31337",
        contractName: "Voting",
        address,
      },
      null,
      2
    )
  );

  console.log("Voting contract deployed successfully!");
  console.log("Contract address:", address);
  console.log("Saved address to deployment-address.json");
}

main().catch((error) => {
  console.error(error);
  process.exitCode = 1;
});
