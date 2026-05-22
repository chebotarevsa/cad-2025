package ru.bsuedu.cad.lab;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ParsingTimeAspect {
    @Around("execution(* ru.bsuedu.cad.lab.CSVParser.parse(..))")
    public Object measureParseTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long finish = System.currentTimeMillis();

        System.out.println("CSV parsing time: " + (finish - start) + " ms");
        return result;
    }
}
