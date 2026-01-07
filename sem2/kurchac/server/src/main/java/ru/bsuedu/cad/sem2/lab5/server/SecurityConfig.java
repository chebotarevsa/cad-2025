package ru.bsuedu.cad.sem2.lab5.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Кодировщик паролей
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()  // Отключаем CSRF, если приложение работает через API
            .authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN")  // Доступ для администраторов
                .antMatchers("/trainer/**").hasRole("TRAINER")  // Доступ для тренеров
                .antMatchers("/client/**").hasRole("CLIENT")  // Доступ для клиентов
                .antMatchers("/api/**").authenticated()  // Доступ для всех авторизованных пользователей
                .anyRequest().permitAll()  // Разрешаем доступ ко всем остальным страницам без авторизации
            .and()
            .httpBasic();  // Основная аутентификация с помощью HTTP Basic
        return http.build();
    }
}
