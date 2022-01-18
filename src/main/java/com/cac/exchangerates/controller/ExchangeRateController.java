package com.cac.exchangerates.controller;

import com.cac.exchangerates.dto.ConversionResponseDto;
import com.cac.exchangerates.dto.RestResourceResponse;
import com.cac.exchangerates.service.ExchangeRateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Tag(
        name = "Exchange Rate API",
        description = "The API that provides the operations with exchange rates"
)
@RestController
public class ExchangeRateController extends AbstractController{
    Logger logger = LogManager.getLogger();

    private final ExchangeRateService exchangeRateService;

    @Autowired
    public ExchangeRateController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @ApiResponse(
            responseCode = "406",
            description = "Not Acceptable parameters",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = RestResourceResponse.class))
    )
    @ApiResponse(
            responseCode = "200",
            description = "Exchange Rate has been returned successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = RestResourceResponse.class))
    )
    @Operation(
            summary = "Get rate between two currency requests",
            description = "Calculates and returns the rate between two currency"
    )
    @GetMapping("/exchange-rate")
    public RestResourceResponse getExchangeRateBetweenTwoCurrency(
            @Parameter(example = "EUR") @RequestParam("from") String baseCurrency,
            @Parameter(example = "TRY") @RequestParam("to") String targetCurrency) {
        try {
            BigDecimal rate = exchangeRateService.calculateRateBetweenCurrencies(baseCurrency, targetCurrency);
            return createSuccessResponse(rate);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.NOT_ACCEPTABLE.toString(), e.getMessage(), null);
        }
    }

    @ApiResponse(
            responseCode = "406",
            description = "Not Acceptable parameters",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = RestResourceResponse.class))
    )
    @ApiResponse(
            responseCode = "200",
            description = "Conversion result has been returned successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = RestResourceResponse.class))
    )
    @Operation(
            summary = "Get the calculated amount in target currency",
            description = "Calculates and returns the amount of the target currency"
    )
    @GetMapping("/convert")
    public RestResourceResponse convertAmountBetweenCurrencies(
            @Parameter(example = "EUR") @RequestParam("from") String baseCurrency,
            @Parameter(example = "TRY") @RequestParam("to") String targetCurrency,
            @Parameter(example = "100") @RequestParam("amount") BigDecimal amount) {
        try {
            ConversionResponseDto conversion =  exchangeRateService.convertAmountBetweenCurrencies(baseCurrency, targetCurrency, amount);
            return createSuccessResponse(conversion);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.NOT_ACCEPTABLE.toString(), e.getMessage(), null);
        }
    }
}
