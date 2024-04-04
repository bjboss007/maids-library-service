package com.maids.librarysystem.config;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingConfig {

    @Before("execution(* com.example.controller.*.*(..))")
    public void logMethodEntry(JoinPoint joinPoint) {
        log.info("Entering method: " + joinPoint.getSignature().getName());
    }

    @AfterReturning(pointcut = "execution(* com.example.controller.*.*(..))", returning = "result")
    public void logMethodExit(JoinPoint joinPoint, Object result) {
        log.info("Exiting method: " + joinPoint.getSignature().getName() + ", Result: " + result);
    }

    @AfterThrowing(pointcut = "execution(* com.example.controller.*.*(..))", throwing = "exception")
    public void logException(JoinPoint joinPoint, Throwable exception) {
        log.error("Exception in method: " + joinPoint.getSignature().getName() + ", Message: " + exception.getMessage());
    }
}
