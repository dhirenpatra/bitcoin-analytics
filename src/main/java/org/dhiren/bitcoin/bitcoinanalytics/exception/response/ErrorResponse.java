package org.dhiren.bitcoin.bitcoinanalytics.exception.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {

    private String message;
    private Instant time;
    private String errorCode;
    private HttpStatus httpStatus;

}
