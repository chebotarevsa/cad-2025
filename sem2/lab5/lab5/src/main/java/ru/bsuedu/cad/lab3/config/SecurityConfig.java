package ru.bsuedu.cad.lab3.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import ru.bsuedu.cad.lab3.repository.UserRepository;
import ru.bsuedu.cad.lab3.security.LoginSuccessHandler;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /** Пользователи теперь берутся из таблицы users, а не из памяти. */
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> userRepository.findByName(username)
                .map(u -> org.springframework.security.core.userdetails.User
                        .withUsername(u.getName())
                        .password(u.getPassword())
                        .roles(u.getRole())
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   LoginSuccessHandler loginSuccessHandler) throws Exception {
        http
                // REST-клиент не использует сессию и CSRF-токен, поэтому для /api/** CSRF отключён
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))
                .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
                .formLogin(form -> form.successHandler(loginSuccessHandler))
                // HTTP Basic: клиент передаёт заголовок Authorization: Basic base64(логин:пароль)
                .httpBasic(Customizer.withDefaults())
                .logout(Customizer.withDefaults());

        return http.build();
    }
}
