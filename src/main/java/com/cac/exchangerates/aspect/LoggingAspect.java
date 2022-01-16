package com.cac.exchangerates.aspect;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    Logger logger = LogManager.getLogger();

    @AfterThrowing(value="execution(* com.cac.exchangerates.service.impl.ConversionRequestServiceImpl.*(..)) ||" +
            "execution(* com.cac.exchangerates.service.impl.ExchangeRateConsumerServiceImpl.*(..)) ||" +
            "execution(* com.cac.exchangerates.service.impl.ExchangeRateServiceImpl.*(..))", throwing="ex")
    public void afterThrowingAdvice(JoinPoint joinPoint, Exception ex) {
        System.out.println("After Throwing exception in method:"+joinPoint.getSignature());
        System.out.println("Exception is:"+ex.getMessage());
        logger.error(ex.getMessage(), ex);
    }
}
