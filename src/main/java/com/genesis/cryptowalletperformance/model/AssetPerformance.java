package com.genesis.cryptowalletperformance.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class AssetPerformance {
    private BigDecimal total;
    private String bestAsset;
    private BigDecimal bestPerformance;
    private String worstAsset;
    private BigDecimal worstPerformance;

    @Override
    public String toString() {
        return String.format("total=%.2f,best_asset=%s,best_performance=%.2f,worst_asset=%s,worst_performance=%.2f"
                , total, bestAsset, bestPerformance, worstAsset, worstPerformance);
    }
}
