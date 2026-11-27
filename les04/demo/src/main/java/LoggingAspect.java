package com.example.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    // 1. Выполняется ДО вызова любого метода в MyService
    @Before("execution(* com.example.aop.MyService.*(..))")
    public void logBeforeMethod() {
        System.out.println("🔍 [BEFORE] Логирование: метод вызывается...");
    }

    // 2. Выполняется ПОСЛЕ успешного завершения метода (с возвращаемым значением)
    @AfterReturning(pointcut = "execution(* com.example.aop.MyService.getResult(..))", returning = "result")
    public void logAfterReturning(Object result) {
        System.out.println("✅ [AFTER RETURNING] Метод вернул: " + result);
    }

    // 3. Полный контроль: ДО и ПОСЛЕ метода
    @Around("execution(* com.example.aop.MyService.doWork(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("🔁 [AROUND] Начало работы метода...");
        long start = System.currentTimeMillis();
        
        Object result = joinPoint.proceed(); // Вызов самого метода
        
        long end = System.currentTimeMillis();
        System.out.println("🔁 [AROUND] Метод завершился. Время выполнения: " + (end - start) + " мс.");
        return result;
    }
}