package com.example.app;

import org.springframework.stereotype.Component;

@Component
public class GreetingPrinter {
    
    public void print(String message) {
        System.out.println(message);
    }
}