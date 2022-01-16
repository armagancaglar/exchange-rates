package com.cac.exchangerates.service;

import com.cac.exchangerates.dto.ConversionRequestDto;
import com.cac.exchangerates.models.ConversionRequest;
import com.cac.exchangerates.repository.ConversionRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConversionRequestService {

    private final ConversionRequestRepository conversionRequestRepository;

    @Autowired
    public ConversionRequestService(ConversionRequestRepository conversionRequestRepository){
        this.conversionRequestRepository = conversionRequestRepository;
    }

    public ConversionRequestDto save(ConversionRequestDto conversionRequestDto){
        ConversionRequest conversionRequest = new ConversionRequest(conversionRequestDto);
        conversionRequest = conversionRequestRepository.save(conversionRequest);
        return new ConversionRequestDto(conversionRequest);
    }

    public List<ConversionRequestDto> getConversionRequestByIdOrDate(String id, LocalDate date, Pageable pageable) {
        return conversionRequestRepository.findByIdOrDate(id, date, pageable).map(ConversionRequestDto::new).stream().collect(Collectors.toList());
    }

}
