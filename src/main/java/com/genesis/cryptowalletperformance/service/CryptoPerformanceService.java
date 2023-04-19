package com.genesis.cryptowalletperformance.service;

import com.genesis.cryptowalletperformance.component.AssetPerformanceCalculationComponent;
import com.genesis.cryptowalletperformance.component.ReadAssetFileComponent;
import com.genesis.cryptowalletperformance.model.Asset;
import com.genesis.cryptowalletperformance.model.AssetPerformance;
import com.genesis.cryptowalletperformance.model.CryptoAsset;
import com.genesis.cryptowalletperformance.model.CryptoAssetData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CryptoPerformanceService {

    @Autowired
    private ReadAssetFileComponent readAssetFileComponent;

    @Autowired
    private CoinCapService coinCapService;

    @Autowired
    private AssetPerformanceCalculationComponent assetPerformanceCalculationComponent;

    @Autowired
    private Environment environment;

    @Value("assets.csv")
    private ClassPathResource classPathResource;

    @EventListener
    public void calculateCryptoPerformance(ApplicationStartedEvent event) throws IOException {
        var activeProfiles = this.environment.getActiveProfiles();
        if (activeProfiles.length == 0) {
            var assets = readAssetFileComponent.parseAssetFile(classPathResource.getFile());
            var cryptoPerformance = retrievePerformance(assets);
            System.out.println(cryptoPerformance);
        }
    }

    private AssetPerformance retrievePerformance(List<Asset> assets) {
        var cryptoAssets = coinCapService.getCryptoAssets(assets);
        var listOfCryptoAssetData = cryptoAssets.stream().map(CryptoAsset::getData).collect(Collectors.toList());
        var cryptoAssetDataMap = listOfCryptoAssetData.stream()
                .collect(Collectors.toMap(CryptoAssetData::getSymbol, CryptoAssetData -> CryptoAssetData));
        return assetPerformanceCalculationComponent.calculate(assets, cryptoAssetDataMap);
    }
}
