package ru.bsuedu.cad.lab;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class TimeLogAspect {


    @Around("within(ru.bsuedu.cad.lab.CSVParser)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis();
        long time = end - start;
        System.out.println("Parsing of csv file in: " + time);
        return result;
    }
}
