package ru.bsuedu.cad.lab;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class TimeAround implements MethodInterceptor{

    @Override
    @Nullable
    public Object invoke(@Nonnull MethodInvocation invocation) throws Throwable {
        var startTime = System.currentTimeMillis();
        var result = invocation.proceed();
        var endTime = System.currentTimeMillis();
        System.out.println("Time processed: " + (endTime - startTime) + " milisecond");
        return result;
    }
    
}