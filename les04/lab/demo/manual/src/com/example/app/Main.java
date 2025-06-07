package com.example.app;

public class Main {
    public static void main(String[] args) {
        GreetingService greetingService = new GreetingService();
        GreetingPrinter greetingPrinter = new GreetingPrinter();
        
        String greetingMessage = greetingService.getGreeting();
        greetingPrinter.print(greetingMessage);
    }
}
