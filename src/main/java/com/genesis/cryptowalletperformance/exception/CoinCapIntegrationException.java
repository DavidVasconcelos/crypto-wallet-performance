package com.genesis.cryptowalletperformance.exception;

public class CoinCapIntegrationException extends RuntimeException {
    public CoinCapIntegrationException(String errorMessage) {
        super(errorMessage);
    }
}