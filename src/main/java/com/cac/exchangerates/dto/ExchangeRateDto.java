package com.cac.exchangerates.dto;

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
    private String fromCurrencyCode;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate date;
    private String toCurrencyCode;
    private BigDecimal rate;

    public ExchangeRateDto(ExchangeRate exchangeRate) {
        this.fromCurrencyCode = exchangeRate.getFromCurrencyCode();
        this.toCurrencyCode = exchangeRate.getToCurrencyCode();
        this.date = exchangeRate.getDate();
        this.timestamp = exchangeRate.getTimestamp();
        this.rate = exchangeRate.getRate();
    }

}
