package com.genesis.cryptowalletperformance.component;

import com.genesis.cryptowalletperformance.exception.CryptoAssetNotFoundException;
import com.genesis.cryptowalletperformance.model.CryptoAsset;
import com.genesis.cryptowalletperformance.model.CryptoAssetData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

import static com.genesis.cryptowalletperformance.util.MockUtil.getBitcoinCryptoAsset;
import static com.genesis.cryptowalletperformance.util.MockUtil.getEthereumCryptoAsset;
import static com.genesis.cryptowalletperformance.util.MockUtil.getListOfAsset;
import static com.genesis.cryptowalletperformance.util.MockUtil.getListOfCryptoAsset;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
public class AssetPerformanceCalculationComponentTest {

    @Autowired
    private AssetPerformanceCalculationComponent component;

    @Test
    public void getCryptoPerformanceSuccessfully() {
        var listOfAsset = getListOfAsset();
        var listOfCryptoAssetData = getListOfCryptoAsset()
                .stream()
                .map(CryptoAsset::getData).collect(Collectors.toList());
        var cryptoAssetDataMap = listOfCryptoAssetData.stream()
                .collect(Collectors.toMap(CryptoAssetData::getSymbol, CryptoAssetData -> CryptoAssetData));

        var assetPerformance = component.calculate(listOfAsset, cryptoAssetDataMap);

        var expectedTotal = new BigDecimal("13815.1013238984").setScale(2, RoundingMode.HALF_UP);
        var expectedBestAsset = "ETH";
        var expectedBestPerformance = new BigDecimal("1.04850449783").setScale(2, RoundingMode.HALF_UP);
        var expectedWorstAsset = "BTC";
        var expectedWorstPerformance = new BigDecimal("0.9971287886").setScale(2, RoundingMode.HALF_UP);

        assertEquals(expectedTotal, assetPerformance.getTotal());
        assertEquals(expectedBestAsset, assetPerformance.getBestAsset());
        assertEquals(expectedBestPerformance, assetPerformance.getBestPerformance());
        assertEquals(expectedWorstAsset, assetPerformance.getWorstAsset());
        assertEquals(expectedWorstPerformance, assetPerformance.getWorstPerformance());
    }

    @Test
    public void whenTheListsOfAssetsAndCryptosDontMatchThrowsException() {
        var listOfAsset = getListOfAsset();
        var bitcoinCryptoAsset = List.of(getBitcoinCryptoAsset(), getEthereumCryptoAsset());
        var listOfCryptoAssetData = bitcoinCryptoAsset
                .stream()
                .map(CryptoAsset::getData).collect(Collectors.toList());
        var cryptoAssetDataMap = listOfCryptoAssetData.stream()
                .collect(Collectors.toMap(CryptoAssetData::getSymbol, CryptoAssetData -> CryptoAssetData));


        Exception exception = assertThrows(
                CryptoAssetNotFoundException.class,
                () -> component.calculate(listOfAsset, cryptoAssetDataMap));

        assertThat(exception.getMessage(), containsString("Crypto asset not found"));
    }
}
