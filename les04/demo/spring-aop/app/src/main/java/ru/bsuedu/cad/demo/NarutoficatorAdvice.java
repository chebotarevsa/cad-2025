package ru.bsuedu.cad.demo;

import java.lang.reflect.Method;

import org.springframework.aop.AfterReturningAdvice;

public class NarutoficatorAdvice implements AfterReturningAdvice{

    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        System.out.println(", とってばよ！");
    }

    
}
