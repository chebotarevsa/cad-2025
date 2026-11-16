package ru.raspisa.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.raspisa.dto.UserDto;
import ru.raspisa.entity.User;
import ru.raspisa.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public UserDto login(String login, String rawPassword) {
        User user = userRepository.findByLogin(login);
        if (user == null || !passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            throw new InvalidCredentialsException("Неверный логин или пароль");
        }
        return new UserDto(user.getLogin(), user.getRole().name(), user.getFullName());
    }
}
