package ru.bsuedu.cad.sem2.lab5.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Создаем пользователей с различными ролями
        createUserIfNotExists("admin", "admin@example.com", "password", Role.ADMIN);
        createUserIfNotExists("trainer", "trainer@example.com", "password", Role.TRAINER);
        createUserIfNotExists("client", "client@example.com", "password", Role.CLIENT);
    }

    private void createUserIfNotExists(String name, String email, String password, Role role) {
        // Проверка на существование пользователя
        if (userRepository.findByName(name).isEmpty()) {
            // Создание нового пользователя с заданной ролью
            User user = new User(name, email, passwordEncoder.encode(password), role);
            userRepository.save(user);
        }
    }
}
