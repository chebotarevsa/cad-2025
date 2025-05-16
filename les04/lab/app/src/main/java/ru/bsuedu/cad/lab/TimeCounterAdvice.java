package ru.bsuedu.cad.lab;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class TimeCounterAdvice implements MethodInterceptor {

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Long startTime = System.currentTimeMillis();
		Object res = invocation.proceed();
		Long endTime = System.currentTimeMillis();
		Long duration = endTime - startTime;
		System.out.println(duration + "мс тратится на парсинг CSV файла");
		return res;
	}

}