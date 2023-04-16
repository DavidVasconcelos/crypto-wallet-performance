package com.genesis.cryptowalletperformance.service;

import com.genesis.cryptowalletperformance.client.CoinCapClient;
import com.genesis.cryptowalletperformance.model.Asset;
import com.genesis.cryptowalletperformance.model.CryptoAsset;
import com.genesis.cryptowalletperformance.model.Cryptocurrency;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CoinCapService {

    @Autowired
    private CoinCapClient coinCapClient;

    public List<CryptoAsset> getCryptoAssets(List<Asset> assets) {
        var cryptoAssets = new ArrayList<CryptoAsset>();
        assets.forEach(asset -> {
            var cryptocurrency = Cryptocurrency.valueOfLabel(asset.getSymbol())
                    .orElseThrow(() -> new RuntimeException("Cryptocurrency not found"));
            var cryptoAsset = coinCapClient.getCryptoAsset(cryptocurrency.currency);
            cryptoAssets.add(cryptoAsset);
        });
        return cryptoAssets;
    }
}
