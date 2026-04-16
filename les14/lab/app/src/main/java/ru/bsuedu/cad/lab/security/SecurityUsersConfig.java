package ru.bsuedu.cad.lab.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityUsersConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**").hasRole("MANAGER")
                        .requestMatchers("/orders").hasAnyRole("USER", "MANAGER")
                        .requestMatchers("/orders/**").hasRole("MANAGER")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .defaultSuccessUrl("/orders", true)
                ).httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public UserDetailsService users() {

        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder().encode("user"))
                .roles("USER")
                .build();

        UserDetails manager = User.builder()
                .username("manager")
                .password(passwordEncoder().encode("manager"))
                .roles("MANAGER")
                .build();

        return new InMemoryUserDetailsManager(user, manager);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
