package ru.bsuedu.cad.demo;

import java.lang.reflect.Method;

import org.springframework.aop.support.NameMatchMethodPointcut;

public class NarutoficatorPointCut extends NameMatchMethodPointcut {

    @Override 
	public boolean matches(Method method, Class<?> targetClass) {
		return "scream".equals(method.getName());
	}
}
