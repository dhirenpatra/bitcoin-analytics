package org.dhiren.bitcoin.bitcoinanalytics.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BitCoinStats extends RepresentationModel<BitCoinStats> {

    private BigDecimal minValue;
    private BigDecimal maxValue;
    private LocalDate startDate;
    private LocalDate endDate;
    private String currency;

}
