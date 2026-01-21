package com.example.demo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        // Клиент - может просматривать свои заказы и историю обслуживания
        UserDetails abiturient = User.withDefaultPasswordEncoder()
                .username("abiturient")
                .password("password")
                .roles("ABITURIENT")
                .build();

        // Приемщик - принимает автомобили, создает заказы-наряды
        UserDetails sotrudnik = User.withDefaultPasswordEncoder()
                .username("sotrudnik")
                .password("password")
                .roles("SOTRUDNIK")
                .build();

        // Администратор - полный доступ ко всем функциям системы
        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("password")
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(abiturient, sotrudnik, admin);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/", "/login", "/css/**").permitAll()
                        .requestMatchers("/clients").hasAnyRole("SOTRUDNIK","ADMIN","ABITURIENT")
                        .requestMatchers("/orders").hasAnyRole("SOTRUDNIK","ADMIN")
                        .requestMatchers("/home").hasAnyRole("SOTRUDNIK","ADMIN","ABITURIENT")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .successHandler(successHandler())
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );
        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        SimpleUrlAuthenticationSuccessHandler handler = new SimpleUrlAuthenticationSuccessHandler();
        handler.setDefaultTargetUrl("/clients");
        handler.setAlwaysUseDefaultTargetUrl(true);
        return handler;
    }
}
