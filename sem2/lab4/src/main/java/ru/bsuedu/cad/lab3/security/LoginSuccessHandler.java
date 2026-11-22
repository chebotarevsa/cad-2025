package ru.bsuedu.cad.lab3.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.bsuedu.cad.lab3.model.LoginHistory;
import ru.bsuedu.cad.lab3.repository.LoginHistoryRepository;
import ru.bsuedu.cad.lab3.repository.UserRepository;

/** При успешном входе записывает строку в login_history, затем делает обычный редирект. */
@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final LoginHistoryRepository loginHistoryRepository;

    public LoginSuccessHandler(UserRepository userRepository,
                               LoginHistoryRepository loginHistoryRepository) {
        this.userRepository = userRepository;
        this.loginHistoryRepository = loginHistoryRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {
        userRepository.findByName(authentication.getName()).ifPresent(user ->
                loginHistoryRepository.save(new LoginHistory(user, request.getRemoteAddr())));
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
