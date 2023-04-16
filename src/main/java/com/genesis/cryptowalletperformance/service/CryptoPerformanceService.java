package com.genesis.cryptowalletperformance.service;

import com.genesis.cryptowalletperformance.component.PerformanceCalculationComponent;
import com.genesis.cryptowalletperformance.component.ReadAssetComponent;
import com.genesis.cryptowalletperformance.model.Asset;
import com.genesis.cryptowalletperformance.model.CryptoAsset;
import com.genesis.cryptowalletperformance.model.CryptoAssetData;
import com.genesis.cryptowalletperformance.model.CryptoPerformance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CryptoPerformanceService {

    @Autowired
    private ReadAssetComponent readAssetComponent;

    @Autowired
    private CoinCapService coinCapService;

    @Autowired
    private PerformanceCalculationComponent performanceCalculationComponent;

    @Value("assets.csv")
    ClassPathResource classPathResource;

    @EventListener
    public void calculateCryptoPerformance(ApplicationStartedEvent event) throws IOException {
        var assets = readAssetComponent.parse(classPathResource.getFile());
        var cryptoPerformance = retrievePerformance(assets);
        System.out.println(cryptoPerformance);
    }

    private CryptoPerformance retrievePerformance(List<Asset> assets) {
        var cryptoAssets = coinCapService.getCryptoAssets(assets);
        var cryptoAssetData = cryptoAssets.stream().map(CryptoAsset::getData).collect(Collectors.toList());
        return performanceCalculationComponent.calculate(assets,cryptoAssetData);
    }
}
