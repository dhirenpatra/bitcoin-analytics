package org.dhiren.bitcoin.bitcoinanalytics.models;

import lombok.Data;

@Data
public class DataRequest {

    private String startdate;
    private String enddate;
    private String currencyType;

}
