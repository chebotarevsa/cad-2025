package ru.bsu.cad.lab.time;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PerformanceAspect {
    @Around("execution(* ru.bsu.cad.lab.parser.ProductCSVParser.parse(..))")
    public Object measureTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed(); // Выполняет оригинальный метод
        long duration = System.currentTimeMillis() - start;
        System.out.println("[AOP] CSV parsing took: " + duration + "ms");
        return result;
    }
}