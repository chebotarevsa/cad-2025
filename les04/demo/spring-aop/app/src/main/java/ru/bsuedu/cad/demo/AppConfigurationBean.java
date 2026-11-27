package ru.bsuedu.cad.demo;

import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "ru.bsuedu.cad.demo")
public class AppConfigurationBean implements BeanFactoryAware {

   private BeanFactory beanFactory;

   @Bean
   public Person person() {
      return new Person();
   }

   @Bean
   public Advisor advisor() {
      var advice = new NarutoficatorAdvice();
      var pointcut = new NarutoficatorPointCut();
      Advisor advisor = new DefaultPointcutAdvisor(pointcut, advice);
      return advisor;
   }

   @Bean
   public Person naruto() {
      var proxy = new ProxyFactoryBean();
      proxy.setInterceptorNames("advisor");
      proxy.setBeanFactory(beanFactory);
      proxy.setTarget(person());
      return (Person) proxy.getObject();
   }

   @Override
   public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
      this.beanFactory = beanFactory;
   }
}
