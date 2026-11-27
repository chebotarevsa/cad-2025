package ru.bsuedu.cap.hello;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "ru.bsuedu.cap.hello") // Сканируем пакет на наличие @Component
public class AppConfig {
}