package ru.bsuedu.cad.lab;

import java.lang.reflect.Method;

import org.springframework.aop.support.NameMatchMethodPointcut;

public class TimeCalculatePointCut extends NameMatchMethodPointcut{
    @Override 
    public boolean matches(Method method, Class<?> targetClass) {
    return "parse".equals(method.getName());
    }
}
