package com.cac.exchangerates.service;

import com.cac.exchangerates.dto.ConversionRequestDto;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface ConversionRequestService {
    ConversionRequestDto save(ConversionRequestDto conversionRequestDto);
    List<ConversionRequestDto> getConversionRequestByIdOrDate(String id, LocalDate date, Pageable pageable);
    void conversionRequestValidation(ConversionRequestDto conversionRequestDto);
}
