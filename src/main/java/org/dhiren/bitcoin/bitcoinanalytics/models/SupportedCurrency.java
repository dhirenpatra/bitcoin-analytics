package org.dhiren.bitcoin.bitcoinanalytics.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SupportedCurrency {

    private String currency;
    private String country;

}
