package ru.bsuedu.cad.lab;


import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "ru.bsuedu.cad.lab")


public class TableConfiguration {
    
   

    @Bean
    public Parser Parser(){
        // var CSVParser = new CSVParser(); return CSVParser;
        var proxy = new ProxyFactory();
        var CSVParser = new CSVParser();
        var advice = new TimeCalculate();
        var pointcut = new TimeCalculatePointCut();
        var advisor = new DefaultPointcutAdvisor(pointcut, advice);

        proxy.addAdvisor(advisor);
        proxy.setTarget(CSVParser);

        return (Parser)proxy.getProxy();
    }

    
   
}
