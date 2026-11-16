package ru.raspisa.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.stereotype.Service;
import ru.raspisa.dto.UserDto;
import java.util.List;

@Service
public class SecuritySessionService {
    private final SecurityContextRepository contexts;
    private final CsrfTokenRepository tokens;

    public SecuritySessionService(SecurityContextRepository contexts, CsrfTokenRepository tokens) {
        this.contexts = contexts;
        this.tokens = tokens;
    }

    public void login(UserDto user, HttpServletRequest request, HttpServletResponse response) {
        var session = request.getSession();
        request.changeSessionId();
        var authentication = UsernamePasswordAuthenticationToken.authenticated(user.login(), null,
                List.of(new SimpleGrantedAuthority("ROLE_" + user.role())));
        var context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        session.setAttribute("schoolUser", user);
        session.setAttribute("schoolStaff", List.of("ADMIN", "TEACHER").contains(user.role()));
        session.setAttribute("map-admin", "ADMIN".equals(user.role()));
        tokens.saveToken(null, request, response);
        contexts.saveContext(context, request, response);
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        tokens.saveToken(null, request, response);
        var session = request.getSession(false);
        if (session != null) session.invalidate();
        SecurityContextHolder.clearContext();
    }
}
