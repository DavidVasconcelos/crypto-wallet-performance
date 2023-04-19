package com.genesis.cryptowalletperformance.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.genesis.cryptowalletperformance.exception.CryptoCurrencyNotFoundException;
import com.genesis.cryptowalletperformance.model.Asset;
import com.genesis.cryptowalletperformance.model.Cryptocurrency;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.concurrent.CompletionException;

import static com.genesis.cryptowalletperformance.util.MockUtil.getBitcoinCryptoAsset;
import static com.genesis.cryptowalletperformance.util.MockUtil.getDogecoinCryptoAsset;
import static com.genesis.cryptowalletperformance.util.MockUtil.getEthereumCryptoAsset;
import static com.github.tomakehurst.wiremock.client.WireMock.badRequest;
import static com.github.tomakehurst.wiremock.client.WireMock.exactly;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@WireMockTest(httpPort = 9561)
@ActiveProfiles("test")
public class CoinCapServiceTest {

    @Autowired
    private CoinCapService service;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void getAssetSuccessfully() throws JsonProcessingException {
        var cryptoAsset = getBitcoinCryptoAsset();
        stubFor(WireMock.get("/assets/bitcoin?interval=d1&start=1617753600000&end=1617753601000")
                .willReturn(ok()
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(cryptoAsset))));


        var asset = Asset.builder().symbol(Cryptocurrency.BTC.name()).build();
        var assets = List.of(asset);

        var cryptoAssets = service.getCryptoAssets(assets);

        assertEquals(1, cryptoAssets.size());
        assertEquals(Cryptocurrency.BTC.name(), cryptoAssets.get(0).getData().symbol);
        assertEquals(cryptoAsset.getData().getPriceUsd(), cryptoAssets.get(0).getData().getPriceUsd());
        verify(exactly(1), getRequestedFor(urlMatching("/assets/bitcoin.*")));
    }

    @Test
    public void callCapCoinThreeTimesForThreeAssets() throws JsonProcessingException {
        var bitcoinCryptoAsset = getBitcoinCryptoAsset();
        var ethereumCryptoAsset = getEthereumCryptoAsset();
        var dogecoinCryptoAsset = getDogecoinCryptoAsset();
        stubFor(WireMock.get("/assets/bitcoin?interval=d1&start=1617753600000&end=1617753601000")
                .willReturn(ok()
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(bitcoinCryptoAsset))));
        stubFor(WireMock.get("/assets/ethereum?interval=d1&start=1617753600000&end=1617753601000")
                .willReturn(ok()
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(ethereumCryptoAsset))));
        stubFor(WireMock.get("/assets/dogecoin?interval=d1&start=1617753600000&end=1617753601000")
                .willReturn(ok()
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(dogecoinCryptoAsset))));


        var bitcoinAsset = Asset.builder().symbol(Cryptocurrency.BTC.name()).build();
        var ethereumAsset = Asset.builder().symbol(Cryptocurrency.ETH.name()).build();
        var dogecoinAsset = Asset.builder().symbol(Cryptocurrency.DOGE.name()).build();
        var assets = List.of(bitcoinAsset, ethereumAsset, dogecoinAsset);

        var cryptoAssets = service.getCryptoAssets(assets);

        assertEquals(3, cryptoAssets.size());
        assertEquals(Cryptocurrency.BTC.name(), cryptoAssets.get(0).getData().symbol);
        assertEquals(bitcoinCryptoAsset.getData().getPriceUsd(), cryptoAssets.get(0).getData().getPriceUsd());
        assertEquals(Cryptocurrency.ETH.name(), cryptoAssets.get(1).getData().symbol);
        assertEquals(ethereumCryptoAsset.getData().getPriceUsd(), cryptoAssets.get(1).getData().getPriceUsd());
        assertEquals(Cryptocurrency.DOGE.name(), cryptoAssets.get(2).getData().symbol);
        assertEquals(dogecoinCryptoAsset.getData().getPriceUsd(), cryptoAssets.get(2).getData().getPriceUsd());
        verify(exactly(3), getRequestedFor(urlMatching("/assets/.*")));
    }

    @Test
    public void whenCapCoinAPIReturnsAnErrorThrowsException() {
        stubFor(WireMock.get("/assets/bitcoin?interval=d1&start=1617753600000&end=1617753601000")
                .willReturn(badRequest()));

        var asset = Asset.builder().symbol(Cryptocurrency.BTC.name()).build();
        var assets = List.of(asset);

        Exception exception = assertThrows(
                CompletionException.class,
                () -> service.getCryptoAssets(assets));

        assertThat(exception.getMessage(), containsString("Error while retrieving crypto asset from CoinCap"));
    }

    @Test
    public void whenCryptocurrencyDoesNotExistReturnsAnException() {
        var asset = Asset.builder().symbol("test").build();
        var assets = List.of(asset);

        Exception exception = assertThrows(
                CryptoCurrencyNotFoundException.class,
                () -> service.getCryptoAssets(assets));

        assertThat(exception.getMessage(), containsString("Cryptocurrency not found"));
    }
}
