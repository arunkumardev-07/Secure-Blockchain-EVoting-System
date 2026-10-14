import fs from "node:fs";

const rpcUrl = "http://127.0.0.1:8545";
const deployment = JSON.parse(
  fs.readFileSync("deployment-address.json", "utf8")
);
const artifact = JSON.parse(
  fs.readFileSync("artifacts/contracts/Voting.sol/Voting.json", "utf8")
);

async function rpc(method, params) {
  const response = await fetch(rpcUrl, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      jsonrpc: "2.0",
      method,
      params,
      id: 1
    })
  });

  const result = await response.json();
  if (result.error) throw new Error(result.error.message);
  return result.result;
}

const accounts = await rpc("eth_accounts", []);
const sender = accounts[0];

const selector = "0x" + Buffer.from(
  "hasVoted(uint256,address)"
).toString("hex");

const crypto = await import("ethers");
const iface = new crypto.Interface(artifact.abi);
const data = iface.encodeFunctionData(
  "hasVoted",
  [3, sender]
);

const result = await rpc("eth_call", [
  {
    to: deployment.address,
    data
  },
  "latest"
]);

console.log("Contract:", deployment.address);
console.log("Sender address:", sender);
console.log(
  "Already voted in Election 3:",
  iface.decodeFunctionResult("hasVoted", result)[0]
);
