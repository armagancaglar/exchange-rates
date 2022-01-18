package com.cac.exchangerates.constants;

import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Schema(name = "CurrencyEnum", description = "The currency list that can be used on apis")
public enum CurrencyEnum {
    AED("AED"), AFN("AFN"), ALL("ALL"), AMD("AMD"), ANG("ANG"),
    AOA("AOA"), ARS("ARS"), AUD("AUD"), AWG("AWG"), AZN("AZN"),
    BAM("BAM"), BBD("BBD"), BDT("BDT"), BGN("BGN"), BHD("BHD"),
    BIF("BIF"), BMD("BMD"), BND("BND"), BOB("BOB"), BRL("BRL"),
    BSD("BSD"), BTC("BTC"), BTN("BTN"), BWP("BWP"), BYN("BYN"),
    BYR("BYR"), BZD("BZD"), CAD("CAD"), CDF("CDF"), CHF("CHF"),
    CLF("CLF"), CLP("CLP"), CNY("CNY"), COP("COP"), CRC("CRC"),
    CUC("CUC"), CUP("CUP"), CVE("CVE"), CZK("CZK"), DJF("DJF"),
    DKK("DKK"), DOP("DOP"), DZD("DZD"), EGP("EGP"), ERN("ERN"),
    ETB("ETB"), EUR("EUR"), FJD("FJD"), FKP("FKP"), GBP("GBP"),
    GEL("GEL"), GGP("GGP"), GHS("GHS"), GIP("GIP"), GMD("GMD"),
    GNF("GNF"), GTQ("GTQ"), GYD("GYD"), HKD("HKD"), HNL("HNL"),
    HRK("HRK"), HTG("HTG"), HUF("HUF"), IDR("IDR"), ILS("ILS"),
    IMP("IMP"), INR("INR"), IQD("IQD"), IRR("IRR"), ISK("ISK"),
    JEP("JEP"), JMD("JMD"), JOD("JOD"), JPY("JPY"), KES("KES"),
    KGS("KGS"), KHR("KHR"), KMF("KMF"), KPW("KPW"), KRW("KRW"),
    KWD("KWD"), KYD("KYD"), KZT("KZT"), LAK("LAK"), LBP("LBP"),
    LKR("LKR"), LRD("LRD"), LSL("LSL"), LTL("LTL"), LVL("LVL"),
    LYD("LYD"), MAD("MAD"), MDL("MDL"), MGA("MGA"), MKD("MKD"),
    MMK("MMK"), MNT("MNT"), MOP("MOP"), MRO("MRO"), MUR("MUR"),
    MVR("MVR"), MWK("MWK"), MXN("MXN"), MYR("MYR"), MZN("MZN"),
    NAD("NAD"), NGN("NGN"), NIO("NIO"), NOK("NOK"), NPR("NPR"),
    NZD("NZD"), OMR("OMR"), PAB("PAB"), PEN("PEN"), PGK("PGK"),
    PHP("PHP"), PKR("PKR"), PLN("PLN"), PYG("PYG"), QAR("QAR"),
    RON("RON"), RSD("RSD"), RUB("RUB"), RWF("RWF"), SAR("SAR"),
    SBD("SBD"), SCR("SCR"), SDG("SDG"), SEK("SEK"), SGD("SGD"),
    SHP("SHP"), SLL("SLL"), SOS("SOS"), SRD("SRD"), STD("STD"),
    SVC("SVC"), SYP("SYP"), SZL("SZL"), THB("THB"), TJS("TJS"),
    TMT("TMT"), TND("TND"), TOP("TOP"), TRY("TRY"), TTD("TTD"),
    TWD("TWD"), TZS("TZS"), UAH("UAH"), UGX("UGX"), USD("USD"),
    UYU("UYU"), UZS("UZS"), VEF("VEF"), VND("VND"), VUV("VUV"),
    WST("WST"), XAF("XAF"), XAG("XAG"), XAU("XAU"), XCD("XCD"),
    XDR("XDR"), XOF("XOF"), XPF("XPF"), YER("YER"), ZAR("ZAR"),
    ZMK("ZMK"), ZMW("ZMW"), ZWL("ZWL");
    private String code;

    CurrencyEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static HashSet<String> getCurrencyCodeSet(){
        return Stream.of(CurrencyEnum.values()).map(CurrencyEnum::getCode).collect(Collectors.toCollection(HashSet::new));
    }

    public static HashSet<CurrencyEnum> getCurrencySet(){
        return Stream.of(CurrencyEnum.values()).collect(Collectors.toCollection(HashSet::new));
    }

    /**
     * The methods to check if the currencies are exists
     *
     * @param currencyCode
     */
    public static CurrencyEnum validateAndGetCurrencyEnum(String currencyCode) {
        if (StringUtils.isBlank(currencyCode)) {
            throw new IllegalArgumentException("Currency code can not be empty!");
        }
        try {
            return CurrencyEnum.valueOf(currencyCode.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(String.format("%s currency is not supported", currencyCode));
        }
    }
}
