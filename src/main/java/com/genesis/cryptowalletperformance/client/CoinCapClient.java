package com.genesis.cryptowalletperformance.client;

import com.genesis.cryptowalletperformance.model.CryptoAsset;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "coin-cap", url = "https://api.coincap.io/v2/")
public interface CoinCapClient {

    @GetMapping("/assets/{id}?interval=d1&start=1617753600000&end=1617753601000")
    CryptoAsset getCryptoAsset(@PathVariable("id") String id);
}
