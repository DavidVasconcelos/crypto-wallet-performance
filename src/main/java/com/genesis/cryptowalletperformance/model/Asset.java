package com.genesis.cryptowalletperformance.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Asset {
    private String symbol;
    private BigDecimal quantity;
    private BigDecimal price;
}
