package com.cac.exchangerates.dto;

import com.cac.exchangerates.models.ExchangeRate;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExchangeRateDto {
    private boolean success;
    private Timestamp timestamp;
    private String currency;
    private Date date;
    private HashMap<String, Double> rates;

    public ExchangeRateDto(ExchangeRate exchangeRate) {
        this.timestamp = exchangeRate.getTimestamp();
        this.currency = exchangeRate.getCurrency();
        this.date = exchangeRate.getDate();
        this.rates = exchangeRate.getRates();
    }
}
