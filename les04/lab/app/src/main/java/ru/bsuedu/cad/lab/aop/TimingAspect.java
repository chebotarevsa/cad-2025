package ru.bsuedu.cad.lab.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TimingAspect {

    @Around("execution(* ru.bsuedu.cad.lab.reader.ResourceFileReader.read(..))")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.nanoTime();

        Object result = joinPoint.proceed();

        long end = System.nanoTime();
        long duration = (end - start) / 1_000_000; // в миллисекундах

        System.out.println("Время выполнения метода read(): " + duration + " мс");

        return result;
    }
}
