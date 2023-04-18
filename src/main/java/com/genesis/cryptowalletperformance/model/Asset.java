package com.genesis.cryptowalletperformance.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class Asset {
    private String symbol;
    private BigDecimal quantity;
    private BigDecimal price;
}
