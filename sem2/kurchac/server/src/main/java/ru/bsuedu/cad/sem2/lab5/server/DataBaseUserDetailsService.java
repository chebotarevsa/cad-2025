package ru.bsuedu.cad.sem2.lab5.server;

import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DataBaseUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    // Конструктор с необходимыми репозиториями
    public DataBaseUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Находим пользователя по имени
        User user = userRepository.findByName(username).orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

        // Определяем роль пользователя
        String role = determineRole(user);
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);

        // Возвращаем пользователя с соответствующими правами
        return new org.springframework.security.core.userdetails.User(
            user.getName(),
            user.getPassword(),
            Collections.singleton(authority)
        );
    }

    private String determineRole(User user) {
        // Заглушка для определения роли
        if (user.getRole() != null) {
            return user.getRole().toString();  // Используем роль пользователя из сущности
        }
        return "ADMIN"; // По умолчанию - роль ADMIN
    }
}
