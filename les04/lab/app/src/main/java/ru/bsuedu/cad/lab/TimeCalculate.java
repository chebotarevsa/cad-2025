package ru.bsuedu.cad.lab;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class TimeCalculate implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        var startTime = System.currentTimeMillis();
        var result = invocation.proceed();
        var endTime = System.currentTimeMillis();
        var resultTime = endTime - startTime;
        System.out.println("Time parsing: " + resultTime);
        return result;
    }


    
}
