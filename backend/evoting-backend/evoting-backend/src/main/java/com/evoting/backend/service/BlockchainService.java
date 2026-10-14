package com.evoting.backend.service;

import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.core.methods.response.EthChainId;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class BlockchainService {

    private final Web3j web3j;

    public BlockchainService(Web3j web3j) {
        this.web3j = web3j;
    }

    public Map<String, String> getBlockchainStatus() throws IOException {

        Web3ClientVersion clientVersion =
                web3j.web3ClientVersion().send();

        EthChainId chainIdResponse =
                web3j.ethChainId().send();

        Map<String, String> status = new LinkedHashMap<>();
        status.put("connected", "true");
        status.put("clientVersion", clientVersion.getWeb3ClientVersion());
        status.put("chainId", chainIdResponse.getChainId().toString());

        return status;
    }
}
