package ru.bsuedu.cad.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ThymleafHelloController {
    
    @GetMapping("/thymleaf/hello")
    public String hello(Model model) {
        model.addAttribute("name", "Твой пользователь");
        return "hello"; // Вернет /WEB-INF/templates/index.html
    }
}
