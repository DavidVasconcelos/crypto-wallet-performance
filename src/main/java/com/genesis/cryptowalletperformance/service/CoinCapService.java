package com.genesis.cryptowalletperformance.service;

import com.genesis.cryptowalletperformance.client.CoinCapClient;
import com.genesis.cryptowalletperformance.exception.CoinCapIntegrationException;
import com.genesis.cryptowalletperformance.exception.CryptoCurrencyNotFoundException;
import com.genesis.cryptowalletperformance.model.Asset;
import com.genesis.cryptowalletperformance.model.CryptoAsset;
import com.genesis.cryptowalletperformance.model.Cryptocurrency;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CoinCapService {

    @Autowired
    private CoinCapClient coinCapClient;

    private static int threadIndex = 0;
    private static final ThreadFactory coinCapThreadFactory = r -> new Thread(r, "CoinCap-" + threadIndex++);

    public List<CryptoAsset> getCryptoAssets(List<Asset> assets) {
        var coinCapExecutor = Executors.newFixedThreadPool(assets.size(), coinCapThreadFactory);
        log.info("Now is {}", LocalDateTime.now());
        var coinCapTasks = getTasks(assets);
        var cryptoAssets = new ArrayList<CryptoAsset>();
        var coinCapCF = getCoinCapCompletableFuture(coinCapExecutor, coinCapTasks, cryptoAssets);
        coinCapCF.join();
        coinCapExecutor.shutdown();
        return cryptoAssets;
    }

    private List<Supplier<CryptoAsset>> getTasks(List<Asset> assets) {
        return assets
                .stream()
                .map(this::getCryptoAssetSupplier)
                .collect(Collectors.toList());
    }

    private Supplier<CryptoAsset> getCryptoAssetSupplier(Asset asset) {
        var cryptocurrency = Cryptocurrency.valueOfLabel(asset.getSymbol())
                .orElseThrow(() -> new CryptoCurrencyNotFoundException("Cryptocurrency not found"));
        return () -> {
            log.info("Submitted request {} at {}", asset.getSymbol(), LocalDateTime.now());
            return coinCapClient.getCryptoAsset(cryptocurrency.currency);
        };
    }

    private static CompletableFuture<Boolean> getCoinCapCompletableFuture(ExecutorService coinCapExecutor,
                                                                          List<Supplier<CryptoAsset>> coinCapTasks,
                                                                          ArrayList<CryptoAsset> cryptoAssets) {
        var coinCapCFS = coinCapTasks
                .stream()
                .map(task -> CompletableFuture.supplyAsync(task, coinCapExecutor)
                        .handle((cryptoAsset, exception) -> {
                            if (exception == null) {
                                return cryptoAsset;
                            } else {
                                log.error("An error occurred with CoinCap integration, error message: {}"
                                        ,exception.getMessage());
                                throw new CoinCapIntegrationException("Error while retrieving crypto asset from CoinCap");
                            }
                        })
                )
                .collect(Collectors.toList());
        return CompletableFuture
                .allOf(coinCapCFS.toArray(CompletableFuture[]::new))
                .thenApplyAsync(v ->
                        cryptoAssets.addAll(coinCapCFS
                                .stream()
                                .map(CompletableFuture::join)
                                .collect(Collectors.toList()))
                );
    }
}
