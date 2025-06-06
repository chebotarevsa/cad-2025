package ru.bsuedu.cad.lab.service;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PerformanceAspect {
    @Around("execution(* ru.bsuedu.cad.lab.service.CSVParser.parse(..))")
    public Object measureParseTime(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.nanoTime();
        Object result = pjp.proceed();
        long end = System.nanoTime();
        System.out.println("[AOP] CSV parsing took " + (end - start)/1_000_000 + " ms");
        return result;
    }
}
