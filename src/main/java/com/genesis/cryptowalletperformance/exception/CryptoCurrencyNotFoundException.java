package com.genesis.cryptowalletperformance.exception;

public class CryptoCurrencyNotFoundException extends RuntimeException {
    public CryptoCurrencyNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}