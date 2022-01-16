package com.cac.exchangerates.converter;

import com.cac.exchangerates.constants.CurrencyEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CurrencyEnumConverter implements Converter<String, CurrencyEnum> {
    @Override
    public CurrencyEnum convert(String source) {
        return CurrencyEnum.valueOf(source.toUpperCase());
    }
}
