package com.example.app;

import org.springframework.stereotype.Service;

@Service
public class GreetingService {
    
    public String getGreeting() {
        return "Hello from GreetingService!";
    }
}