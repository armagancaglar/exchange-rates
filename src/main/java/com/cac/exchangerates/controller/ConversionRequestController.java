package com.cac.exchangerates.controller;

import com.cac.exchangerates.dto.ConversionRequestDto;
import com.cac.exchangerates.models.ConversionRequest;
import com.cac.exchangerates.service.ConversionRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
public class ConversionRequestController {
    private final ConversionRequestService conversionRequestService;

    @Autowired
    public ConversionRequestController(ConversionRequestService conversionRequestService) {
        this.conversionRequestService = conversionRequestService;
    }

    @GetMapping("/conversions")
    public ResponseEntity<List<ConversionRequestDto>> getConversionRequestList(@RequestParam(value = "id", required = false) String id,
                                                                              @RequestParam(value = "date", required = false)
                                                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                                                              @RequestParam(name = "page", defaultValue = "0", required = false) int page, @RequestParam(name = "size", defaultValue = "10", required = false) int size) {
        List<ConversionRequestDto> conversionList = conversionRequestService.getConversionRequestByIdOrDate(id, date, PageRequest.of(page, size));
        return new ResponseEntity<List<ConversionRequestDto>>(conversionList, HttpStatus.OK);
    }
}
