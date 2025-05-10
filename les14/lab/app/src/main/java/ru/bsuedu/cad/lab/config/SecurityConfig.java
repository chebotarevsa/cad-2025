package ru.bsuedu.cad.lab.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(new AntPathRequestMatcher("/orders/create")).hasRole("MANAGER")
                .requestMatchers(new AntPathRequestMatcher("/orders/edit/**")).hasRole("MANAGER")
                .requestMatchers(new AntPathRequestMatcher("/orders/delete/**")).hasRole("MANAGER")
                .requestMatchers(new AntPathRequestMatcher("/orders/**")).hasAnyRole("USER", "MANAGER")
                .requestMatchers(new AntPathRequestMatcher("/api/**")).hasRole("MANAGER")
                .anyRequest().permitAll()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/orders", true)
                .permitAll()
            )
            .httpBasic(withDefaults())
            .logout(logout -> logout.permitAll());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        var manager = User.withUsername("manager")
                .password(passwordEncoder().encode("manager123"))
                .roles("MANAGER")
                .build();

        var user = User.withUsername("user")
                .password(passwordEncoder().encode("user123"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user, manager);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
