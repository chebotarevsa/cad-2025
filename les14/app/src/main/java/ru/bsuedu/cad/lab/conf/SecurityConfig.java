package ru.bsuedu.cad.lab.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig {
    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(
            User.withUsername("user")
                .password("{noop}0451")
                .roles("USER")
                .build(),
            User.withUsername("manager")
                .password("{noop}manager")
                .roles("MANAGER")
                .build()
        );
    }

    @Order(2)
    @Bean
    public SecurityFilterChain filterChainForm(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/view/order/**")
                .authorizeHttpRequests(authz -> authz
                                .requestMatchers("/", "/login").permitAll()
                                .requestMatchers("/view/order/new", "/view/order/edit/**", "/view/order/delete/**").hasRole( "MANAGER")
                                .requestMatchers("/view/order/**").hasAnyRole("USER", "MANAGER")
                                .anyRequest().authenticated()
                )
                .formLogin(form -> form
                                .loginPage("/login")
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/view/order", true)
                                .failureUrl("/login?error")
                                .permitAll()
                )
                .logout(logout -> logout
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/login?logout")
                                .permitAll()
                )
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Order(1)
    @Bean
    public SecurityFilterChain filterChainBasic(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/order/**")
                .authorizeHttpRequests(authz -> authz
                                .requestMatchers(HttpMethod.POST, "/api/order/**").hasRole( "MANAGER")
                                .requestMatchers(HttpMethod.DELETE, "/api/order/**").hasRole( "MANAGER")
                                .requestMatchers(HttpMethod.PATCH, "/api/order/**").hasRole( "MANAGER")
                                .requestMatchers(HttpMethod.GET,"/api/order/**").hasAnyRole("USER", "MANAGER")
                                .anyRequest().authenticated()
                ) 
                .httpBasic(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}
