package org.dhiren.bitcoin.bitcoinanalytics.contollers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import org.dhiren.bitcoin.bitcoinanalytics.contollers.hateos.BitCoinStatsModelAssembler;
import org.dhiren.bitcoin.bitcoinanalytics.exception.response.ErrorResponse;
import org.dhiren.bitcoin.bitcoinanalytics.exception.type.InvalidFormatException;
import org.dhiren.bitcoin.bitcoinanalytics.models.BitCoinStats;
import org.dhiren.bitcoin.bitcoinanalytics.service.BitcoinService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.EntityModel;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/bitcoin")
@Tag(name = "BitCoin", description = "Bitcoin management APIs")
@Validated
public class BitcoinController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final BitcoinService bitcoinService;

    private final BitCoinStatsModelAssembler bitCoinStatsModelAssembler;

    @Autowired
    public BitcoinController(BitcoinService bitcoinService, BitCoinStatsModelAssembler bitCoinStatsModelAssembler) {
        this.bitcoinService = bitcoinService;
        this.bitCoinStatsModelAssembler = bitCoinStatsModelAssembler;
    }

    @Operation(
            summary = "Retrieve a Bit Coin Min and Max value from date range",
            description = "Retrieve a Bit Coin Min and Max value from date range. The response is Bitcoin object with " +
                    "start date, end date, min value , max value and currency."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = BitCoinStats.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema(implementation = ErrorResponse.class)) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping("/stats")
    public EntityModel<BitCoinStats> getBitcoinStats(@RequestParam(name = "start_date")
                                       @DateTimeFormat(pattern = "dd-MM-yyyy")
                                       @NotNull(message = "Start Date Must be present") LocalDate startDate,
                                                     @RequestParam(name = "end_date")
                                        @DateTimeFormat(pattern = "dd-MM-yyyy")
                                        @NotNull(message = "End Date Must be present") LocalDate endDate) {

        logger.info("Invoked...");
        isValid(startDate,endDate);

        return bitCoinStatsModelAssembler.toModel(bitcoinService.getOpenValues(startDate, endDate));
    }

    @GetMapping("/convert")
    public BitCoinStats convertCurrency(
            @RequestParam BigDecimal maxAmount,
            @RequestParam BigDecimal minAmount,
            @RequestParam String baseCurrency,
            @RequestParam String targetCurrency) {

        String target = bitcoinService.mapSupportedCountries().get(targetCurrency);

        System.err.println(target);

        if(target == null) {
            throw new InvalidFormatException("Target Currency Not Available");
        }

        BigDecimal rate = bitcoinService.calculateConversionRate(baseCurrency, target);

        return BitCoinStats.builder()
                .maxValue(maxAmount.multiply(rate))
                .minValue(minAmount.multiply(rate))
                .currency(target)
                .build();

        //return bitcoinService.convertToCurrency(maxAmount, minAmount, baseCurrency, targetCurrency);

        // BigDecimal convertedAmount = conversionService.convertCurrency(amount, baseCurrency, targetCurrency);
        // return BitCoinStats.builder().maxValue(new BigDecimal("123.435")).build();
    }

    @GetMapping("/supported-currencies")
    public Map<String, String> supportedCurrencies() {
        return bitcoinService.mapSupportedCountries();
    }

    private void isValid(LocalDate startDate, LocalDate endDate) {
        if( ! startDate.isEqual(endDate))
            if(startDate.isAfter(endDate))
                throw new InvalidFormatException("Start Date can't be after end date");
    }
}
