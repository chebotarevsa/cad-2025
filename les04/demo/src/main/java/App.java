package com.example.aop;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(AppConfig.class);
        MyService myService = context.getBean(MyService.class);
        
        System.out.println("--- Вызов doWork() ---");
        myService.doWork();
        
        System.out.println("\n--- Вызов getResult() ---");
        myService.getResult("Student");
        
        context.close();
    }
}