package ru.raspisa.web;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.raspisa.dto.LoginRequest;
import ru.raspisa.dto.UserDto;
import ru.raspisa.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public UserDto login(@Valid @RequestBody LoginRequest request, jakarta.servlet.http.HttpServletRequest http) {
        UserDto user = authService.login(request.login(), request.password());
        var session = http.getSession();
        http.changeSessionId();
        session.setAttribute("schoolStaff", "ADMIN".equals(user.role()) || "TEACHER".equals(user.role()));
        return user;
    }

    @PostMapping("/logout")
    public void logout(jakarta.servlet.http.HttpServletRequest request) {
        var session = request.getSession(false);
        if (session != null) session.invalidate();
    }
}
