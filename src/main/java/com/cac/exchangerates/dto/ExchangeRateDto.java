package com.cac.exchangerates.dto;

import com.cac.exchangerates.constants.CurrencyEnum;
import com.cac.exchangerates.models.ExchangeRate;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExchangeRateDto {
    private Timestamp timestamp;
    private CurrencyEnum baseCurrency;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate date;
    private CurrencyEnum targetCurrency;
    private BigDecimal rate;

    public ExchangeRateDto(ExchangeRate exchangeRate) {
        this.baseCurrency = exchangeRate.getBaseCurrency();
        this.targetCurrency = exchangeRate.getTargetCurrency();
        this.date = exchangeRate.getDate();
        this.timestamp = exchangeRate.getTimestamp();
        this.rate = exchangeRate.getRate();
    }

}
