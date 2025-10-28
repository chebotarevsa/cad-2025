package ru.bsuedu.cad.lab;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TimingAspect {
    
    @Pointcut("execution(* ru.bsuedu.cad.lab.ResourceFileReader.read(..))")
    public void readMethod() {}

    @Around("readMethod()")
    public Object measureReadTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis();
        System.out.println("Time to method read(): " + (end - start) + " ms");
        return result;
    }
}
