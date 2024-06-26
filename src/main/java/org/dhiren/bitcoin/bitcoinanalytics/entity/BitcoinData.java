package org.dhiren.bitcoin.bitcoinanalytics.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BitcoinData {

    private LocalDate startDate;
    private BigDecimal openValue;
    private BigDecimal closeValue;

}
