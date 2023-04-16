package com.genesis.cryptowalletperformance.component;

import com.genesis.cryptowalletperformance.model.Asset;
import com.genesis.cryptowalletperformance.model.CryptoAssetData;
import com.genesis.cryptowalletperformance.model.CryptoPerformance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
@Slf4j
public class PerformanceCalculationComponent {

    public CryptoPerformance calculate(List<Asset> assets, List<CryptoAssetData> cryptoAssets) {
        var cryptoPerformance = CryptoPerformance
                .builder()
                .total(BigDecimal.ZERO)
                .bestPerformance(BigDecimal.ZERO)
                .worstPerformance(BigDecimal.ZERO)
                .build();
        assets.forEach(asset -> {
            var cryptoAsset = getCryptoAsset(cryptoAssets, asset);
            cryptoPerformance.setTotal(cryptoPerformance.getTotal()
                    .add(cryptoAsset.getPriceUsd().multiply(asset.getQuantity()))
                    .setScale(2, RoundingMode.HALF_UP));
            var percent = cryptoAsset.priceUsd.divide(asset.getPrice(), RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP);
            if (percent.compareTo(cryptoPerformance.getBestPerformance()) > 0 ||
                    cryptoPerformance.getBestPerformance().compareTo(BigDecimal.ZERO) == 0) {
                cryptoPerformance.setBestPerformance(percent);
                cryptoPerformance.setBestAsset(asset.getSymbol());
            }
            if (percent.compareTo(cryptoPerformance.getWorstPerformance()) < 0 ||
                    cryptoPerformance.getWorstPerformance().compareTo(BigDecimal.ZERO) == 0) {
                cryptoPerformance.setWorstPerformance(percent);
                cryptoPerformance.setWorstAsset(asset.getSymbol());
            }
        });
        return cryptoPerformance;
    }

    private static CryptoAssetData getCryptoAsset(List<CryptoAssetData> cryptoAssets, Asset asset) {
        return cryptoAssets
                .stream()
                .filter(crypto -> crypto.getSymbol().equals(asset.getSymbol()))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Crypto asset not found"));
    }
}
