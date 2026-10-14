
package com.evoting.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Collections;

@Service
public class BlockchainTransactionService {

    private final Web3j web3j;

    @Value("${blockchain.contract-address}")
    private String contractAddress;

    @Value("${blockchain.chain-id:31337}")
    private long chainId;

    public BlockchainTransactionService(Web3j web3j) {
        this.web3j = web3j;
    }

    public String recordVote(
            long electionId,
            long candidateId,
            Long voterId) throws Exception {

        if (electionId <= 0 || candidateId <= 0
                || voterId == null || voterId <= 0) {
            throw new IllegalArgumentException(
                    "Election, candidate, and voter IDs must be positive");
        }

        if (contractAddress == null
                || !contractAddress.matches("^0x[0-9a-fA-F]{40}$")) {
            throw new IllegalStateException(
                    "Invalid blockchain contract address configuration");
        }

        String privateKey = System.getenv("BLOCKCHAIN_PRIVATE_KEY");

        if (privateKey == null || privateKey.isBlank()) {
            throw new IllegalStateException(
                    "BLOCKCHAIN_PRIVATE_KEY is not configured");
        }

        Credentials credentials = Credentials.create(privateKey);

        // Create an election-specific hash. Do not place email or name on-chain.
        byte[] nullifier = createNullifier(electionId, voterId);

        System.out.println(
                "Blockchain sender address: " + credentials.getAddress());
        System.out.println(
                "Contract: " + contractAddress
                        + ", election: " + electionId
                        + ", candidate: " + candidateId);

        Function function = new Function(
                "voteWithNullifier",
                Arrays.asList(
                        new Uint256(BigInteger.valueOf(electionId)),
                        new Uint256(BigInteger.valueOf(candidateId)),
                        new Bytes32(nullifier)
                ),
                Collections.emptyList()
        );

        String encodedFunction = FunctionEncoder.encode(function);

        RawTransactionManager transactionManager =
                new RawTransactionManager(web3j, credentials, chainId);

        EthSendTransaction response = transactionManager.sendTransaction(
                DefaultGasProvider.GAS_PRICE,
                DefaultGasProvider.GAS_LIMIT,
                contractAddress,
                encodedFunction,
                BigInteger.ZERO
        );

        if (response.hasError()) {
            throw new IllegalStateException(
                    "Blockchain transaction rejected: "
                            + response.getError().getMessage());
        }

        String transactionHash = response.getTransactionHash();

        if (transactionHash == null || transactionHash.isBlank()) {
            throw new IllegalStateException(
                    "Blockchain returned no transaction hash");
        }

        TransactionReceipt receipt = web3j
                .ethGetTransactionReceipt(transactionHash)
                .send()
                .getTransactionReceipt()
                .orElseThrow(() -> new IllegalStateException(
                        "Transaction submitted, but receipt is unavailable"));

        if (!receipt.isStatusOK()) {
            throw new IllegalStateException(
                    "Blockchain transaction was mined but failed");
        }

        System.out.println(
                "Blockchain transaction confirmed: "
                        + receipt.getTransactionHash());

        return receipt.getTransactionHash();
    }

    private byte[] createNullifier(long electionId, Long voterId)
            throws Exception {

        String input = "secure-evoting-demo:"
                + electionId + ":" + voterId;

        return MessageDigest.getInstance("SHA-256")
                .digest(input.getBytes(StandardCharsets.UTF_8));
    }
}
