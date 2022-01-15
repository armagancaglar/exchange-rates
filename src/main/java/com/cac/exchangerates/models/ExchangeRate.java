package com.cac.exchangerates.models;

import com.cac.exchangerates.dto.ExchangeRateDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="Contacts", indexes = @Index(columnList = "base, date"))
public class ExchangeRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String currency;
    private Date date;
    private Timestamp timestamp;
    private HashMap<String, Double> rates;

    public ExchangeRate(ExchangeRateDto exchangeRateDto) {
        this.timestamp = exchangeRateDto.getTimestamp();
        this.currency = exchangeRateDto.getCurrency();
        this.date = exchangeRateDto.getDate();
        this.rates = exchangeRateDto.getRates();
    }
}
