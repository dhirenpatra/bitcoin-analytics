package org.dhiren.bitcoin.bitcoinanalytics.beans;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class ApplicationBeans {

    @Bean
    public RestClient restClient() {
        return RestClient.builder().baseUrl("https://api.coindesk.com").build();
    }

}
