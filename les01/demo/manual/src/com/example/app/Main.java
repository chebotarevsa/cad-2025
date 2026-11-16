package com.example.app;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        // 1. Создаем Spring-контекст (контейнер)
        // Указываем пакет, где Spring должен искать компоненты (com.example.app)
        var context = new AnnotationConfigApplicationContext("com.example.app");

        // 2. Достаем готовые объекты из Spring
        GreetingService greetingService = context.getBean(GreetingService.class);
        GreetingPrinter greetingPrinter = context.getBean(GreetingPrinter.class);

        // 3. Выполняем логику
        String greetingMessage = greetingService.getGreeting();
        greetingPrinter.print(greetingMessage);

        // 4. Закрываем контекст
        context.close();
    }
}