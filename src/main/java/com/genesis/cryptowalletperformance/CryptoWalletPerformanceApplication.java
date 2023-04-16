package com.genesis.cryptowalletperformance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CryptoWalletPerformanceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CryptoWalletPerformanceApplication.class, args);
    }

}
