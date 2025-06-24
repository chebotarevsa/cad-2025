package ru.bsuedu.cad.lab;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;


public class ExecutionCSVParser implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        var start = System.currentTimeMillis();
        var result = invocation.proceed();
        var end = System.currentTimeMillis();
        var elapsed = end - start;
        System.out.println("Метод " + invocation.getMethod().getName() + "() класса CSVParser сработал за " + elapsed + " мс");
        return result;
    }
}
