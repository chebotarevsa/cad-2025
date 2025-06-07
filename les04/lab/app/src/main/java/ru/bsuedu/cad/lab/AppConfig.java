package ru.bsuedu.cad.lab;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan("ru.bsuedu.cad.lab")
@PropertySource("classpath:application.properties")
@EnableAspectJAutoProxy
public class AppConfig {
}
