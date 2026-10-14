package com.evoting.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

@Configuration
public class BlockchainConfig {

    private static final String BLOCKCHAIN_URL =
            "http://127.0.0.1:8545";

    @Bean
    public Web3j web3j() {
        return Web3j.build(new HttpService(BLOCKCHAIN_URL));
    }
}
