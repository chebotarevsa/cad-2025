package ru.bsuedu.cad.demo;

import org.springframework.stereotype.Component;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
@Component
public class NarutoficatorAspect {

    @AfterReturning("execution(* ru.bsuedu.cad.demo.Person.*(..))")
    public void narutoSay() {
        System.out.println(", とってばよ！");
    }


    @Around("execution(* ru.bsuedu.cad.demo.Person.*(..))")
    public Object narutoLog(ProceedingJoinPoint joinPoint) throws Throwable {

        System.out.println("-- Start ナルト mode");

        Object result = joinPoint.proceed();

        System.out.println("-- Finish ナルト mod");
        return result;
    
    }
    
}
