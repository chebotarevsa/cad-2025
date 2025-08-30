package ru.bsuedu.cad.lab.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
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
@ComponentScan(basePackages = "ru.bsuedu.cad.lab")
@EnableWebSecurity(debug = true)
public class ConfigSecurity {
    
    @Bean
    @Order(1)
    public SecurityFilterChain filterChainForm(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/view/**", "/login", "/logout")
                .authorizeHttpRequests(authz -> authz
                                .requestMatchers("/", "/public/").permitAll()
                                .requestMatchers("/view/v1/order/create").hasRole( "MANAGER")
                                .requestMatchers("/view/v1/order/").hasAnyRole("USER", "MANAGER")  
                                .anyRequest().authenticated()
                )
                .formLogin(form -> form
                                .loginPage("/login")
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/view/v1/order", true)
                                .failureUrl("/login?error")
                                .permitAll()
                )
                .logout(logout -> logout
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/login?logout")
                                .permitAll()
                );

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain filterChainBasic(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**")
                .authorizeHttpRequests(authz -> authz
                                .requestMatchers(HttpMethod.POST,"/api/v1/order/").hasRole( "MANAGER")
                                .requestMatchers(HttpMethod.DELETE,"/api/v1/order/").hasRole( "MANAGER")
                                .requestMatchers(HttpMethod.PATCH,"/api/v1/order/").hasRole( "MANAGER")
                                .requestMatchers(HttpMethod.GET, "/api/v1/order/").hasAnyRole("USER", "MANAGER")  
                                .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable());

        return http.build();
    }


    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(
            User.withUsername("user")
                .password("{noop}1234")
                .roles("USER")
                //.authorities("VIEW_PROFILE")
                .build(),
            User.withUsername("manager")
                .password("{noop}manager1234")
                .roles("MANAGER")
                ///.authorities("VIEW_PROFILE", "EDIT_PROFILE", "DELETE_USERS")
                .build()
        );
    }
}