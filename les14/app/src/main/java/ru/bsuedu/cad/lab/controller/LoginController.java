package ru.bsuedu.cad.lab.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {

    @GetMapping("")
    public String login(Model model) {
        return "login";  // Отправляем на страницу логина для отображения
    }

    @PostMapping("")
    public String loginSubmit(String username, String password, Model model) {
        // Логика аутентификации
        if (username.equals("admin") && password.equals("admin")) {
            // Пример успешной аутентификации
            return "redirect:/home";  // Перенаправляем на главную страницу
        }
        // В случае неудачной аутентификации, возвращаем обратно на страницу логина
        model.addAttribute("error", "Неверное имя пользователя или пароль");
        return "login";
    }
}
