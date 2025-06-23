package ru.bsuedu.cad.demo;

import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "ru.bsuedu.cad.demo")
public class AppConfiguration {

    @Bean
    public Person person(){

        var proxy = new ProxyFactory();
        var person = new Person();
        var advice = new NarutoficatorAdvice();
        var pointcut = new NarutoficatorPointCut();
        Advisor advisor = new DefaultPointcutAdvisor(pointcut, advice);
        //proxy.addAdvice(advice);
        proxy.addAdvisor(advisor);
        proxy.setTarget(person);

        return (Person)proxy.getProxy();
    }
}
