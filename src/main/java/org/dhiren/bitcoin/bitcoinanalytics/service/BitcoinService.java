package org.dhiren.bitcoin.bitcoinanalytics.service;

import org.dhiren.bitcoin.bitcoinanalytics.exception.type.DataNotFoundException;
import org.dhiren.bitcoin.bitcoinanalytics.exception.type.ExternalServiceException;
import org.dhiren.bitcoin.bitcoinanalytics.models.BitCoinStats;
import org.dhiren.bitcoin.bitcoinanalytics.models.DataResponse;
import org.dhiren.bitcoin.bitcoinanalytics.models.SupportedCurrency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class BitcoinService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final RestClient restClient;

    @Autowired
    public BitcoinService(RestClient restClient) {
        this.restClient = restClient;
    }

    public BitCoinStats getOpenValues(LocalDate startDate, LocalDate endDate) {

        DataResponse body ;

        try {
            body = restClient.get()
                    .uri("/v1/bpi/historical/open.json")
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                        throw new ExternalServiceException(response.getStatusCode().toString() + response.getHeaders());
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                        throw new ExternalServiceException(response.getStatusCode().toString() + response.getHeaders());
                    })
                    .body(DataResponse.class);
            if (body == null) {
                throw new DataNotFoundException("No Bitcoin stats found.");
            }
        } catch (Exception e) {
            throw new ExternalServiceException("An unexpected error occurred: " + e.getMessage());
        }

        logger.info("Data {} Fetched Successfully ", body.getBpi().size());

        return calculateBitCoinStats(body, startDate.minusDays(1), endDate.plusDays(1));
    }

    private BitCoinStats calculateBitCoinStats(DataResponse closeValues,
                                              LocalDate startDate, LocalDate endDate) {

        Predicate<Map.Entry<LocalDate, BigDecimal>> datesFilter =
                (entry) -> entry.getKey().isBefore(endDate) && entry.getKey().isAfter(startDate);

        List<BigDecimal> list = closeValues.getBpi().entrySet()
                .stream()
                .filter(datesFilter)
                .map(Map.Entry::getValue)
                .toList();

        BigDecimal MIN_VALUE = BigDecimal.ZERO;
        BigDecimal MAX_VALUE = BigDecimal.ZERO;

        if(list.size() == 1) {
            MIN_VALUE = MAX_VALUE = list.get(0);
        } else if (list.size() > 1) {
            MIN_VALUE = MAX_VALUE = list.get(0);
            for(BigDecimal num : list) {
                MAX_VALUE = num.max(MAX_VALUE);
                MIN_VALUE = num.min(MIN_VALUE);
            }
        }

        return BitCoinStats.builder()
                .startDate(startDate)
                .endDate(endDate)
                .maxValue(MAX_VALUE)
                .minValue(MIN_VALUE)
                .currency("USD")
                .build();
    }

    public BigDecimal calculateConversionRate(String base, String target) {
        // TODO : Hard-coding this for now need to implement login for finding conversion logic.
        return new BigDecimal("81.65");
    }

    public List<SupportedCurrency> supportedCurrencies() {
        try {
            List<SupportedCurrency> currencies = restClient.get()
                    .uri("v1/bpi/supported-currencies.json")
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                        throw new ExternalServiceException(response.getStatusCode().toString() + response.getHeaders());
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                        throw new ExternalServiceException(response.getStatusCode().toString() + response.getHeaders());
                    })
                    .body(new ParameterizedTypeReference<>() {});
            if (currencies == null || currencies.isEmpty()) {
                throw new DataNotFoundException("No Bitcoin stats found.");
            }
            return currencies;
        } catch (Exception e) {
            throw new ExternalServiceException("An unexpected error occurred: " + e.getMessage());
        }
    }

    @Cacheable(value = "bitCoinStatsMap")
    public Map<String, String> mapSupportedCountries() {

        return supportedCurrencies().stream().collect(Collectors.toMap(
                SupportedCurrency::getCurrency,
                SupportedCurrency::getCountry
        ));
    }
}
