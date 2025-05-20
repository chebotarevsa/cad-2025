package ru.bsuedu.cad.lab;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@ComponentScan(basePackages = "ru.bsuedu.cad.lab")
public class Config {

    @Bean
    public Parser parser(){
        var proxy = new ProxyFactory();
        var parser = new CSVParser();
        var perfomance = new PerfomanceAdvice();
        proxy.addAdvice(perfomance);
        proxy.setTarget(parser);
        return (Parser)proxy.getProxy();
    }
}
