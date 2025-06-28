package ru.bsuedu.cad.demo;

import org.springframework.stereotype.Component;

@Component
public class Person {

    public void say() {
        System.out.print("Rust лучше C#");
    }

    public void scream() {
        System.out.print("Swift лучше C#");
    }
}
