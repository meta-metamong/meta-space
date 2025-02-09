package com.metamong.mt.global.logging;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
    
    @Around("execution(public * com.metamong.mt.domain..service..*.*(..)) || execution(public * com.metamong.mt.domain..repository..*.*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Signature signature = joinPoint.getSignature();
        if (log.isDebugEnabled()) {
            log.debug("==================================");
            log.debug("Method name: {}.{}", joinPoint.getTarget().getClass(), signature.getName());
            log.debug("Arguments: {}", Arrays.toString(args));
            log.debug("{} Start!", signature.getName());
            log.debug("==================================");
        }
        Object returnValue = joinPoint.proceed(args);
        if (log.isDebugEnabled()) {
            log.debug("##################################");
            log.debug("Method end: {}.{}", joinPoint.getTarget().getClass(), signature.getName());
            log.debug("Returns: {}", returnValue);
            log.debug("##################################");
        }
        return returnValue;
    }
}
