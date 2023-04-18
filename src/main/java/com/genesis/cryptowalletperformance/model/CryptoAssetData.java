package com.genesis.cryptowalletperformance.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
public class CryptoAssetData {

    @JsonProperty("id")
    public String id;
    @JsonProperty("rank")
    public String rank;
    @JsonProperty("symbol")
    public String symbol;
    @JsonProperty("name")
    public String name;
    @JsonProperty("supply")
    public BigDecimal supply;
    @JsonProperty("maxSupply")
    public BigDecimal maxSupply;
    @JsonProperty("marketCapUsd")
    public BigDecimal marketCapUsd;
    @JsonProperty("volumeUsd24Hr")
    public BigDecimal volumeUsd24Hr;
    @JsonProperty("priceUsd")
    public BigDecimal priceUsd;
    @JsonProperty("changePercent24Hr")
    public BigDecimal changePercent24Hr;
    @JsonProperty("vwap24Hr")
    public BigDecimal vwap24Hr;
    @JsonProperty("explorer")
    public String explorer;
}