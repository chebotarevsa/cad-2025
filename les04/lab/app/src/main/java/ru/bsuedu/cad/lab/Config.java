package ru.bsuedu.cad.lab;

import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

//@Configuration
@ComponentScan("ru.bsuedu.cad.lab")

public class Config {

   @Bean
   public Parser parser() {
      //var csvParser = new CSVParser(); return csvParser;
      var proxy = new ProxyFactory();
      var csvParser = new CSVParser();
      var advice = new TimeAround();
      var pointcut = new TimeAroundPointCut();
      var advisor = new DefaultPointcutAdvisor(pointcut, advice);

      proxy.addAdvisor(advisor);
      proxy.setTarget(csvParser);

      return (Parser)proxy.getProxy();
   }   

}