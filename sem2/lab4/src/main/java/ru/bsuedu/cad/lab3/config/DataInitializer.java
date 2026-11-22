package ru.bsuedu.cad.lab3.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import ru.bsuedu.cad.lab3.model.User;
import ru.bsuedu.cad.lab3.repository.UserRepository;

/** Создаёт три учётные записи из прошлой лабораторной, если таблица users пуста. */
@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initUsers(UserRepository userRepository, PasswordEncoder encoder) {
        return args -> {
            if (userRepository.count() > 0) {
                return;
            }
            String password = encoder.encode("password");
            userRepository.save(new User("user", "user@example.com", password, "USER"));
            userRepository.save(new User("moderator", "moderator@example.com", password, "MODERATOR"));
            userRepository.save(new User("admin", "admin@example.com", password, "ADMIN"));
        };
    }
}
