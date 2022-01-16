package com.cac.exchangerates.models;

import com.cac.exchangerates.constants.CurrencyEnum;
import com.cac.exchangerates.dto.ConsumedRatesDto;
import com.cac.exchangerates.dto.ExchangeRateDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;

import static com.cac.exchangerates.constants.CurrencyEnum.EUR;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="ExchangeRate", indexes = @Index(columnList = "baseCurrency, date, targetCurrency"))
public class ExchangeRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Timestamp timestamp;
    private LocalDate date;
    private CurrencyEnum baseCurrency;
    private CurrencyEnum targetCurrency;
    private BigDecimal rate;

    public ExchangeRate(ConsumedRatesDto consumedRatesDto) {
        this.timestamp = consumedRatesDto.getTimestamp();
        this.baseCurrency = consumedRatesDto.getBase() == null ? EUR : consumedRatesDto.getBase();
        this.date = consumedRatesDto.getDate();
    }

    public ExchangeRate(ExchangeRateDto exchangeRateDto) {
        this.baseCurrency = exchangeRateDto.getBaseCurrency();
        this.targetCurrency = exchangeRateDto.getTargetCurrency();
        this.date = exchangeRateDto.getDate();
        this.timestamp = exchangeRateDto.getTimestamp();
        this.rate = exchangeRateDto.getRate();
    }
}
