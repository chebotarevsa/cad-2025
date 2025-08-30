package ru.bsuedu.cad.lab;

import org.springframework.context.annotation.Configuration;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@Configuration
@ComponentScan(basePackages = "ru.bsuedu.cad.lab")
public class SpringConfig {

	@Bean
	public Parser parser() {
		var proxy = new ProxyFactory();
		var csvParser = new CSVParser();
		var advice = new TimeCounterAdvice();
		var pointcut = new TimeCounterPointCut();
		Advisor advisor = new DefaultPointcutAdvisor(pointcut, advice);
		proxy.addAdvisor(advisor);
		proxy.setTarget(csvParser);

		return (Parser) proxy.getProxy();
	}
}
