package com.cac.exchangerates.controller;

import com.cac.exchangerates.dto.ConversionRequestDto;
import com.cac.exchangerates.dto.RestResourceResponse;
import com.cac.exchangerates.service.ConversionRequestService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@OpenAPIDefinition(
        info = @Info(
                title = "Exchange Rates API",
                version = "1.0"
        )
)
@Tag(
        name = "Conversion Request API",
        description = "The API that provides the conversion request list"
)
@RestController
public class ConversionRequestController extends AbstractController{
    private final ConversionRequestService conversionRequestService;

    @Autowired
    public ConversionRequestController(ConversionRequestService conversionRequestService) {
        this.conversionRequestService = conversionRequestService;
    }

    @ApiResponse(
            responseCode = "406",
            description = "Not Acceptable parameters",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = RestResourceResponse.class))
    )
    @ApiResponse(
            responseCode = "200",
            description = "Conversion Requests",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = RestResourceResponse.class))
    )
    @Operation(
            summary = "Get conversion requests",
            description = "Retrieves and return exchange rate conversion requests"
    )
    @GetMapping("/conversions")
    public RestResourceResponse getConversionRequestList(
            @Parameter(example = "3cb30028-78b1-4ef8-a28f-6ced01be7490") @RequestParam(value = "id", required = false) String id,
            @Parameter(example = "2022-01-18") @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @Parameter(example = "0") @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @Parameter(example = "10") @RequestParam(name = "size", defaultValue = "10", required = false) int size) {
        try {
            List<ConversionRequestDto> conversionList = conversionRequestService.getConversionRequestByIdOrDate(id, date, PageRequest.of(page, size));
            return createSuccessResponse(conversionList);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.NOT_ACCEPTABLE.toString(), e.getMessage(),  null);
        }
    }
}
