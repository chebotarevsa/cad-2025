package ru.bsuedu.cad.lab;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ParsingTimerAspect {

    @Around("execution(* ru.bsuedu.cad.lab.CSVParser.parse(..))")
    public Object measureParsingTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long elapsed = System.currentTimeMillis() - start;
        System.out.println("Время парсинга CSV: " + elapsed + " мс");
        return result;
    }
}
