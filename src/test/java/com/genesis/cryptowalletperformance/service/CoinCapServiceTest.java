package com.genesis.cryptowalletperformance.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.genesis.cryptowalletperformance.client.CoinCapClient;
import com.genesis.cryptowalletperformance.model.Asset;
import com.genesis.cryptowalletperformance.model.CryptoAsset;
import com.genesis.cryptowalletperformance.model.CryptoAssetData;
import com.genesis.cryptowalletperformance.model.Cryptocurrency;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@WireMockTest(httpPort = 9561)
@ActiveProfiles("test")
public class CoinCapServiceTest {

    @Autowired
    private CoinCapService service;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void getAssetSuccessfully() throws JsonProcessingException {
        var data = CryptoAssetData.builder().priceUsd(BigDecimal.ONE).build();
        var cryptoAsset = CryptoAsset.builder().data(data).build();
        stubFor(WireMock.get("/assets/bitcoin?interval=d1&start=1617753600000&end=1617753601000")
                .willReturn(ok()
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(cryptoAsset))));


        var asset = Asset.builder().symbol(Cryptocurrency.BTC.name()).build();
        var assets = List.of(asset);


        var cryptoAssets = service.getCryptoAssets(assets);

        assertEquals(1, cryptoAssets.size());
    }


}
