package ru.bsuedu.cad.lab;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class PerfomanceAdvice implements MethodInterceptor{

    @Override
    @Nullable
    public Object invoke(@Nonnull MethodInvocation invocation) throws Throwable {
        var start = System.currentTimeMillis();
        var result = invocation.proceed();
        var finish = System.currentTimeMillis();
        var delta = finish - start;
        System.out.println("Execution time: " + delta);
        return result;
    }
    
}
