package com.genesis.cryptowalletperformance.exception;

public class CryptoAssetNotFoundException extends RuntimeException {
    public CryptoAssetNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}