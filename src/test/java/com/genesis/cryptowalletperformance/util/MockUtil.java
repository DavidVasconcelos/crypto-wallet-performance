package com.genesis.cryptowalletperformance.util;

import com.genesis.cryptowalletperformance.model.Asset;
import com.genesis.cryptowalletperformance.model.CryptoAsset;
import com.genesis.cryptowalletperformance.model.CryptoAssetData;

import java.math.BigDecimal;
import java.util.List;

public final class MockUtil {

    public static CryptoAsset getBitcoinCryptoAsset() {
        var data = CryptoAssetData.builder()
                .id("bitcoin")
                .rank("1")
                .symbol("BTC")
                .name("Bitcoin")
                .supply(new BigDecimal("19349925.0000000000000000"))
                .maxSupply(new BigDecimal("21000000.0000000000000000"))
                .marketCapUsd(new BigDecimal("584729750308.3069767626494350"))
                .volumeUsd24Hr(new BigDecimal("5642707161.9735756141567815"))
                .priceUsd(new BigDecimal("29270.2948016921740820"))
                .changePercent24Hr(new BigDecimal("2.6003761492275124"))
                .vwap24Hr(new BigDecimal("29796.3378896026877389"))
                .explorer("https://blockchain.info/")
                .build();
        return CryptoAsset.builder().data(data).build();
    }

    public static CryptoAsset getEthereumCryptoAsset() {
        var data = CryptoAssetData.builder()
                .id("ethereum")
                .rank("2")
                .symbol("ETH")
                .name("Ethereum")
                .supply(new BigDecimal("19349925.0000000000000000"))
                .maxSupply(new BigDecimal("21000000.0000000000000000"))
                .marketCapUsd(new BigDecimal("584729750308.3069767626494350"))
                .volumeUsd24Hr(new BigDecimal("5642707161.9735756141567815"))
                .priceUsd(new BigDecimal("2083.9145375355295084"))
                .changePercent24Hr(new BigDecimal("2.6003761492275124"))
                .vwap24Hr(new BigDecimal("29796.3378896026877389"))
                .explorer("https://blockchain.info/")
                .build();
        return CryptoAsset.builder().data(data).build();
    }

    public static CryptoAsset getDogecoinCryptoAsset() {
        var data = CryptoAssetData.builder()
                .id("dogecoin")
                .rank("8")
                .symbol("DOGE")
                .name("Dogecoin")
                .supply(new BigDecimal("19349925.0000000000000000"))
                .maxSupply(new BigDecimal("21000000.0000000000000000"))
                .marketCapUsd(new BigDecimal("584729750308.3069767626494350"))
                .volumeUsd24Hr(new BigDecimal("5642707161.9735756141567815"))
                .priceUsd(new BigDecimal("0.0932214095567902"))
                .changePercent24Hr(new BigDecimal("2.6003761492275124"))
                .vwap24Hr(new BigDecimal("29796.3378896026877389"))
                .explorer("https://blockchain.info/")
                .build();
        return CryptoAsset.builder().data(data).build();
    }

    public static List<CryptoAsset> getListOfCryptoAsset() {
        return List.of(getBitcoinCryptoAsset(), getEthereumCryptoAsset(), getDogecoinCryptoAsset());
    }

    public static List<Asset> getListOfAsset() {
        var bitcoinAsset = Asset.builder()
                .symbol("BTC")
                .quantity(new BigDecimal("0.12345"))
                .price(new BigDecimal("29354.5780"))
                .build();
        var ethereumAsset = Asset.builder()
                .symbol("ETH")
                .quantity(new BigDecimal("4.89532"))
                .price(new BigDecimal("1987.5113"))
                .build();
        var dogecoinAsset = Asset.builder()
                .symbol("DOGE")
                .quantity(new BigDecimal("2.73453"))
                .price(new BigDecimal("0.0907"))
                .build();
        return List.of(bitcoinAsset, ethereumAsset, dogecoinAsset);
    }
}
