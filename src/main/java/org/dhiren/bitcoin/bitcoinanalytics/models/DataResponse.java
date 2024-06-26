package org.dhiren.bitcoin.bitcoinanalytics.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DataResponse {

    @JsonProperty("bpi")
    private Map<LocalDate, BigDecimal> bpi;

    @JsonProperty("disclaimer")
    private String disclaimer;

}
