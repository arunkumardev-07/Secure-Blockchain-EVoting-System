
package com.evoting.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class BlockchainResultsService {

    private final Web3j web3j;

    @Value("${blockchain.contract-address}")
    private String contractAddress;

    public BlockchainResultsService(Web3j web3j) {
        this.web3j = web3j;
    }

    public List<Map<String, Object>> getCandidates(long electionId)
            throws Exception {

        if (electionId <= 0) {
            throw new IllegalArgumentException(
                    "Election ID must be positive");
        }

        Function function = new Function(
                "getCandidates",
                Collections.singletonList(
                        new Uint256(BigInteger.valueOf(electionId))),
                Collections.singletonList(
                        new TypeReference<DynamicArray<CandidateData>>() {})
        );

        String encoded = FunctionEncoder.encode(function);

        var response = web3j.ethCall(
                Transaction.createEthCallTransaction(
                        null, contractAddress, encoded),
                DefaultBlockParameterName.LATEST
        ).send();

        if (response.isReverted()) {
            throw new IllegalStateException(
                    "The blockchain rejected the results request");
        }

        List<Type> decoded = FunctionReturnDecoder.decode(
                response.getValue(),
                function.getOutputParameters());

        if (decoded.isEmpty()) {
            return Collections.emptyList();
        }

        @SuppressWarnings("unchecked")
        DynamicArray<CandidateData> candidateArray =
                (DynamicArray<CandidateData>) decoded.get(0);

        List<Map<String, Object>> results = new ArrayList<>();

        for (CandidateData candidate : candidateArray.getValue()) {
            Map<String, Object> item = new LinkedHashMap<>();

            item.put("candidateId",
                    candidate.id.getValue().longValue());
            item.put("name", candidate.name.getValue());
            item.put("voteCount",
                    candidate.voteCount.getValue().longValue());

            results.add(item);
        }

        return results;
    }

    public static class CandidateData extends DynamicStruct {

        public final Uint256 id;
        public final Utf8String name;
        public final Uint256 voteCount;

        public CandidateData(
                Uint256 id,
                Utf8String name,
                Uint256 voteCount) {

            super(id, name, voteCount);

            this.id = id;
            this.name = name;
            this.voteCount = voteCount;
        }

        public CandidateData(List<Type> values) {
            this(
                    (Uint256) values.get(0),
                    (Utf8String) values.get(1),
                    (Uint256) values.get(2));
        }
    }
}
