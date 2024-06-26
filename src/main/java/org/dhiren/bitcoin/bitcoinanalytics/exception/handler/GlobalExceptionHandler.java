package org.dhiren.bitcoin.bitcoinanalytics.exception.handler;

import org.dhiren.bitcoin.bitcoinanalytics.exception.response.ErrorResponse;
import org.dhiren.bitcoin.bitcoinanalytics.exception.type.ExternalServiceException;
import org.dhiren.bitcoin.bitcoinanalytics.exception.type.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(InvalidFormatException.class)
    public ErrorResponse handleInvalidFormatException(InvalidFormatException exception) {

        logger.error("Exception Occured {} ", exception.getMessage());

        return  ErrorResponse.builder()
                .errorCode("400")
                .time(Instant.now())
                .httpStatus(HttpStatus.BAD_REQUEST)
                .message(exception.getMessage())
                .build();
    }

    @ExceptionHandler(ExternalServiceException.class)
    public ErrorResponse handleExternalServiceException(ExternalServiceException exception, WebRequest request) {

        logger.error("Exception Occured {} ", exception.getMessage());

        return  ErrorResponse.builder()
                .errorCode("400")
                .time(Instant.now())
                .httpStatus(HttpStatus.SERVICE_UNAVAILABLE)
                .message(exception.getMessage())
                .build();

    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException
            (MissingServletRequestParameterException exception) {

        logger.error("Exception Occured {} ", exception.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode("400")
                .time(Instant.now())
                .httpStatus(HttpStatus.BAD_REQUEST)
                .message(exception.getMessage())
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException
            (MethodArgumentTypeMismatchException exception) {

        logger.error("Exception Occured {} ", exception.getPropertyName() + " For "+exception.getMostSpecificCause().getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode("400")
                .time(Instant.now())
                .httpStatus(HttpStatus.BAD_REQUEST)
                .message(exception.getPropertyName() + " For "+exception.getMostSpecificCause().getMessage())
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

}
