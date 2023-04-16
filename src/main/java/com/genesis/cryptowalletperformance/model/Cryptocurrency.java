package com.genesis.cryptowalletperformance.model;

import java.util.Arrays;
import java.util.Optional;

public enum Cryptocurrency {
    BTC("bitcoin"),
    ETH("ethereum");

    public final String currency;

    Cryptocurrency(String currency) {
        this.currency = currency;
    }

    public static Optional<Cryptocurrency> valueOfLabel(String currency) {
        return Arrays.stream(values()).filter(value -> value.name().equals(currency)).findFirst();
    }
}
