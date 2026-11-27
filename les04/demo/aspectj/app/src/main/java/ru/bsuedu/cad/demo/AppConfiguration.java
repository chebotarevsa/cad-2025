package ru.bsuedu.cad.demo;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan(basePackages = "ru.bsuedu.cad.demo")
@EnableAspectJAutoProxy
public class AppConfiguration {

}
