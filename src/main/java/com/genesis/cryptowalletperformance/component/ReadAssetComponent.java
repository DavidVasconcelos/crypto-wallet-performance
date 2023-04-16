package com.genesis.cryptowalletperformance.component;

import com.genesis.cryptowalletperformance.model.Asset;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ReadAssetComponent {

    private static final String COMMA_DELIMITER = ",";
    private static final Integer HEADERS_LINE = 1;

    public List<Asset> parse(File file) {
        List<Asset> assets = new ArrayList<>();
        try {
            CSVReader reader = new CSVReaderBuilder(new FileReader(file)).
                    withSkipLines(HEADERS_LINE).
                    build();
            assets = reader.readAll().stream().map(data -> {
                Asset csvObject = new Asset();
                csvObject.setSymbol(data[0]);
                csvObject.setQuantity(new BigDecimal(data[1]));
                csvObject.setPrice(new BigDecimal(data[2]));
                return csvObject;
            }).collect(Collectors.toList());
        } catch (IOException | CsvException exception) {
            log.error(exception.getMessage());
        }
        return assets;
    }
}



