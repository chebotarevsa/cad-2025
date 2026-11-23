package ru.raspisa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import ru.raspisa.repository.UserRepository;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository users) {
        return login -> {
            var user = users.findByLogin(login);
            if (user == null) throw new UsernameNotFoundException("Пользователь не найден");
            return org.springframework.security.core.userdetails.User.withUsername(user.getLogin())
                    .password(user.getPasswordHash()).roles(user.getRole().name()).build();
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        return new HttpSessionCsrfTokenRepository();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
            SecurityContextRepository contexts, CsrfTokenRepository tokens) throws Exception {
        http
                .securityContext(context -> context.securityContextRepository(contexts))
                .csrf(csrf -> csrf.csrfTokenRepository(tokens)
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(access -> access
                        .requestMatchers("/api/auth/csrf", "/api/auth/login", "/api/auth/logout",
                                "/api/map/session", "/api/push/subscribe").permitAll()
                        .requestMatchers("/api/auth/me").authenticated()
                        .requestMatchers("/api/attendance", "/api/attendance/**")
                                .hasAnyRole("ADMIN", "TEACHER")
                        .requestMatchers("/api/tasks", "/api/tasks/**").hasAnyRole("ADMIN", "METHODIST")
                        .requestMatchers("/api/login-history", "/api/login-history/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/**").permitAll()
                        .requestMatchers("/api/map/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/lessons", "/api/lessons/**").hasAnyRole("ADMIN", "METHODIST")
                        .requestMatchers(HttpMethod.PUT, "/api/lessons/**").hasAnyRole("ADMIN", "METHODIST")
                        .requestMatchers(HttpMethod.DELETE, "/api/lessons/**").hasAnyRole("ADMIN", "METHODIST")
                        .requestMatchers(HttpMethod.DELETE, "/api/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/**").hasRole("ADMIN")
                        .requestMatchers("/dashboard", "/dashboard/**").authenticated()
                        .anyRequest().permitAll())
                .exceptionHandling(errors -> errors
                        .authenticationEntryPoint((request, response, exception) -> {
                            if (!request.getRequestURI().startsWith("/api/")) {
                                response.sendRedirect("/login");
                                return;
                            }
                            response.setStatus(401);
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().write("{\"status\":401,\"error\":\"Необходимо войти\"}");
                        })
                        .accessDeniedHandler((request, response, exception) -> {
                            response.setStatus(403);
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().write("{\"status\":403,\"error\":\"Нет доступа или неверный CSRF-токен\"}");
                        }));
        return http.build();
    }
}
