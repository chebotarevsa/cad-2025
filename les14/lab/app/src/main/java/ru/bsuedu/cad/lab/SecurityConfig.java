package ru.bsuedu.cad.lab;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder().encode("user123"))
                .roles("USER")
                .build();

        UserDetails manager = User.builder()
                .username("manager")
                .password(passwordEncoder().encode("manager123"))
                .roles("MANAGER")
                .build();

        return new InMemoryUserDetailsManager(user, manager);
    }

    /**
     * REST API: /api/** — Basic Authentication, CSRF отключён.
     * USER может только читать, MANAGER — всё.
     */
    @Bean
    @Order(1)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/api/**")
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.GET, "/api/orders", "/api/orders/*")
                    .hasAnyRole("USER", "MANAGER")
                .anyRequest().hasRole("MANAGER")
            )
            .httpBasic(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable());
        return http.build();
    }

    /**
     * Web UI: /orders/** + /login + /logout — Form Login.
     * USER может только просматривать список, MANAGER — всё.
     */
    @Bean
    @Order(2)
    public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/orders/**", "/login", "/logout")
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.GET, "/orders").hasAnyRole("USER", "MANAGER")
                .anyRequest().hasRole("MANAGER")
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/orders", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );
        return http.build();
    }
}
