package com.cac.exchangerates.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConversionResponseDto {
    private String transactionId;
    private BigDecimal amount;

    public ConversionResponseDto(ConversionRequestDto conversionRequestDto) {
        this.transactionId = conversionRequestDto.getId();
        this.amount = conversionRequestDto.getPrice();
    }
}
