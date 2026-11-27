package com.example.aop;

import org.springframework.stereotype.Service;

@Service
public class MyService {
    public void doWork() {
        System.out.println("📌 Выполнение метода doWork()");
    }

    public String getResult(String name) {
        System.out.println("📌 Выполнение метода getResult() для: " + name);
        return "Hello, " + name;
    }
}