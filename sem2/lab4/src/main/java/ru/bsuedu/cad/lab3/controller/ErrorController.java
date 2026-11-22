package ru.bsuedu.cad.lab3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping("/error")
    public String handleError(Model model) {
        model.addAttribute("errorMessage", "У вас нет прав для выполнения этого действия!");
        return "error";
    }
}