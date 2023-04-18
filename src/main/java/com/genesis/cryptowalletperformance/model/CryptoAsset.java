package com.genesis.cryptowalletperformance.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
public class CryptoAsset {

    @JsonProperty("data")
    public CryptoAssetData data;
    @JsonProperty("timestamp")
    public Long timestamp;

}