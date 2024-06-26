package org.dhiren.bitcoin.bitcoinanalytics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class BitcoinAnalyticsApplication {

    public static void main(String[] args) {
        SpringApplication.run(BitcoinAnalyticsApplication.class, args);
    }

}
