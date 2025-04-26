package ru.bsuedu.cad.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/")
public class Test {

    @GetMapping("")
    public String getUser() {
        return "Text";
    }
}
