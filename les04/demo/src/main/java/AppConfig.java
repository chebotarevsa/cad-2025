package com.example.aop;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan(basePackages = "com.example.aop")
@EnableAspectJAutoProxy // Включаем поддержку AOP
public class AppConfig {
}