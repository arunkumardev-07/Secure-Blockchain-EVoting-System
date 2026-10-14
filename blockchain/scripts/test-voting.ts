import { network } from "hardhat";

const { viem } = await network.create();

async function main() {
  const publicClient = await viem.getPublicClient();
  const [owner, voter1] = await viem.getWalletClients();

  if (!owner || !voter1) {
    throw new Error("Local test accounts are unavailable");
  }

  const voting = await viem.deployContract("Voting");

  const block = await publicClient.getBlock({ blockTag: "latest" });
  const startTime = block.timestamp + 60n;
  const endTime = block.timestamp + 3600n;

  let hash = await voting.write.createElection([
    "Duplicate Vote Test",
    startTime,
    endTime,
  ]);
  await publicClient.waitForTransactionReceipt({ hash });

  hash = await voting.write.addCandidate([1n, 101n, "Arun Kumar"]);
  await publicClient.waitForTransactionReceipt({ hash });

  await publicClient.request({
    method: "evm_increaseTime",
    params: [61],
  } as any);

  await publicClient.request({
    method: "evm_mine",
    params: [],
  } as any);

  const voterContract = await viem.getContractAt(
    "Voting",
    voting.address,
    { client: { wallet: voter1 } }
  );

  hash = await voterContract.write.vote([1n, 101n]);
  await publicClient.waitForTransactionReceipt({ hash });
  console.log("First vote succeeded.");

  try {
    await voterContract.write.vote([1n, 101n]);
    throw new Error("TEST FAILED: duplicate vote was unexpectedly accepted.");
  } catch (error) {
    if (
      error instanceof Error &&
      error.message.startsWith("TEST FAILED:")
    ) {
      throw error;
    }

    console.log("Second vote rejected as expected.");
  }

  const candidates = await voting.read.getCandidates([1n]);
  console.log("Final vote count:", candidates[0]?.voteCount);
  console.log("Duplicate-vote test completed!");
}

main().catch((error) => {
  console.error(error);
  process.exitCode = 1;
});
