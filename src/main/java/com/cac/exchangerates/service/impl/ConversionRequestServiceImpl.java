package com.cac.exchangerates.service.impl;

import com.cac.exchangerates.dto.ConversionRequestDto;
import com.cac.exchangerates.models.ConversionRequest;
import com.cac.exchangerates.repository.ConversionRequestRepository;
import com.cac.exchangerates.service.ConversionRequestService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConversionRequestServiceImpl implements ConversionRequestService {

    private final ConversionRequestRepository conversionRequestRepository;

    @Autowired
    public ConversionRequestServiceImpl(ConversionRequestRepository conversionRequestRepository) {
        this.conversionRequestRepository = conversionRequestRepository;
    }

    public ConversionRequestDto save(ConversionRequestDto conversionRequestDto) {
        if (null == conversionRequestDto) {
            throw new IllegalArgumentException("ConversionRequest must be provided!");
        }
        ConversionRequest conversionRequest = new ConversionRequest(conversionRequestDto);
        conversionRequest = conversionRequestRepository.save(conversionRequest);
        return new ConversionRequestDto(conversionRequest);
    }

    public List<ConversionRequestDto> getConversionRequestByIdOrDate(String id, LocalDate date, Pageable pageable) {
        if (StringUtils.isBlank(id) && date == null) {
            throw new IllegalArgumentException("Transaction ID or Transaction Date must be provided!");
        }
        return conversionRequestRepository.findByIdOrDate(id, date, pageable).map(ConversionRequestDto::new).stream().collect(Collectors.toList());
    }

}