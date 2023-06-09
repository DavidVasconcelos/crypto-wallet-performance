package com.genesis.cryptowalletperformance.component;

import com.genesis.cryptowalletperformance.exception.CryptoAssetNotFoundException;
import com.genesis.cryptowalletperformance.model.Asset;
import com.genesis.cryptowalletperformance.model.CryptoAssetData;
import com.genesis.cryptowalletperformance.model.AssetPerformance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class AssetPerformanceCalculationComponent {

    public AssetPerformance calculate(List<Asset> assets, Map<String, CryptoAssetData> cryptoAssets) {
        var assetPerformance = initAssetPerformance();
        assets.forEach(asset -> {
            var cryptoAsset = Optional.ofNullable(cryptoAssets.get(asset.getSymbol()))
                    .orElseThrow(() -> new CryptoAssetNotFoundException("Crypto asset not found"));
            calculateTotalAssetPerformance(assetPerformance, asset, cryptoAsset);
            var percentPerformance = calculatePercentPerformance(asset, cryptoAsset);
            if (percentPerformance.compareTo(assetPerformance.getBestPerformance()) > 0 ||
                    assetPerformance.getBestPerformance().compareTo(BigDecimal.ZERO) == 0) {
                assetPerformance.setBestPerformance(percentPerformance);
                assetPerformance.setBestAsset(asset.getSymbol());
            }
            if (percentPerformance.compareTo(assetPerformance.getWorstPerformance()) < 0 ||
                    assetPerformance.getWorstPerformance().compareTo(BigDecimal.ZERO) == 0) {
                assetPerformance.setWorstPerformance(percentPerformance);
                assetPerformance.setWorstAsset(asset.getSymbol());
            }
        });
        return assetPerformance;
    }

    private AssetPerformance initAssetPerformance() {
        return AssetPerformance
                .builder()
                .total(BigDecimal.ZERO)
                .bestPerformance(BigDecimal.ZERO)
                .worstPerformance(BigDecimal.ZERO)
                .build();
    }
    private void calculateTotalAssetPerformance(AssetPerformance assetPerformance, Asset asset, CryptoAssetData cryptoAsset) {
        assetPerformance.setTotal(assetPerformance.getTotal()
                .add(cryptoAsset.getPriceUsd().multiply(asset.getQuantity()))
                .setScale(2, RoundingMode.HALF_UP));
    }

    private BigDecimal calculatePercentPerformance(Asset asset, CryptoAssetData cryptoAsset) {
        return cryptoAsset.priceUsd.divide(asset.getPrice(), RoundingMode.HALF_UP)
                .setScale(2, RoundingMode.HALF_UP);
    }
}
