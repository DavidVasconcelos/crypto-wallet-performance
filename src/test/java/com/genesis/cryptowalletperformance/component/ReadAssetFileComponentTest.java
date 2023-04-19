package com.genesis.cryptowalletperformance.component;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class ReadAssetFileComponentTest {

    @Autowired
    private ReadAssetFileComponent component;

    @Value("two-assets.csv")
    private ClassPathResource twoAssetsClassPathResource;

    @Value("three-assets.csv")
    private ClassPathResource threeAssetsClassPathResource;

    @Test
    public void readFileWithTwoAssetsSuccessfully() throws IOException {
        var assets = component.parseAssetFile(twoAssetsClassPathResource.getFile());

        assertEquals(2, assets.size());
    }

    @Test
    public void readFileWithThreeAssetsSuccessfully() throws IOException {
        var assets = component.parseAssetFile(threeAssetsClassPathResource.getFile());

        assertEquals(3, assets.size());
    }
}
