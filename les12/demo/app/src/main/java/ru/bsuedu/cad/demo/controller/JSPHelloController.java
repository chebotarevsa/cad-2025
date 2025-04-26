package ru.bsuedu.cad.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/jsp/hello")
public class JSPHelloController {

    @GetMapping("")
    public String hello(Model model) {
        model.addAttribute("name", "Мир");
        return "hello"; // Отображает /WEB-INF/views/hello.jsp
    }
}
