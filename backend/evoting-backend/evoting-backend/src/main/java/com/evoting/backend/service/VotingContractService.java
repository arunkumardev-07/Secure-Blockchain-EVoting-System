package com.evoting.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;

import java.util.Collections;
import java.util.List;

@Service
public class VotingContractService {

    private final Web3j web3j;

    @Value("${blockchain.contract-address}")
    private String contractAddress;

    public VotingContractService(Web3j web3j) {
        this.web3j = web3j;
    }

    public String getOwner() throws Exception {

        Function function = new Function(
                "owner",
                Collections.emptyList(),
                List.of(new TypeReference<Address>() {})
        );

        String encodedFunction = FunctionEncoder.encode(function);

        var response = web3j.ethCall(
                Transaction.createEthCallTransaction(
                        null,
                        contractAddress,
                        encodedFunction
                ),
                DefaultBlockParameterName.LATEST
        ).send();

        if (response.hasError()) {
            throw new IllegalStateException(
                    "Blockchain call failed: "
                            + response.getError().getMessage()
            );
        }

        List<Type> values = FunctionReturnDecoder.decode(
                response.getValue(),
                function.getOutputParameters()
        );

        if (values.isEmpty()) {
            throw new IllegalStateException(
                    "The contract returned no owner address"
            );
        }

        return values.get(0).getValue().toString();
    }

    public String getContractAddress() {
        return contractAddress;
    }
}
