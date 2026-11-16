package ru.raspisa.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.raspisa.dto.UserDto;
import ru.raspisa.entity.User;
import ru.raspisa.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
    }

    @Transactional(readOnly = true)
    public UserDto login(String login, String rawPassword) {
        try {
            authenticationManager.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(login, rawPassword));
        } catch (AuthenticationException exception) {
            throw new InvalidCredentialsException("Неверный логин или пароль");
        }
        User user = userRepository.findByLogin(login);
        return new UserDto(user.getLogin(), user.getRole().name(), user.getFullName());
    }
}
