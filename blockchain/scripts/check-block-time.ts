import { network } from "hardhat";

async function main() {
  const { viem } = await network.create();
  const client = await viem.getPublicClient();
  const block = await client.getBlock({ blockTag: "latest" });

  console.log("Blockchain timestamp:", block.timestamp.toString());
  console.log(
    "Blockchain time:",
    new Date(Number(block.timestamp) * 1000).toLocaleString()
  );
  console.log("Computer time:", new Date().toLocaleString());
}

main().catch((error) => {
  console.error(error);
  process.exitCode = 1;
});
