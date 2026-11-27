package ru.bsuedu.cad.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity(debug = true)
public class ConfigSecurity {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                                .requestMatchers("/", "/login", "/public/**").permitAll()
                                .requestMatchers("/students/new").hasRole( "ADMIN")
                                .requestMatchers("/students/**").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/api/**").hasRole( "API")
                                .anyRequest().authenticated()
                )
                .formLogin(form -> form
                                .loginPage("/login")
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/students", true)
                                .failureUrl("/login?error")
                                .permitAll()
                )
                .httpBasic(Customizer.withDefaults())
                .logout(logout -> logout
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/login?logout")
                                .permitAll()
                )
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
            User.withUsername("admin")
                .password("{noop}admin")
                .roles("ADMIN")
                ///.authorities("VIEW_PROFILE", "EDIT_PROFILE", "DELETE_USERS")
                .build(),
            User.withUsername("api")
                .password("{noop}api")
                .roles("API")
                ///.authorities("VIEW_PROFILE", "EDIT_PROFILE", "DELETE_USERS")
                .build()
        );
    }

    // @Bean
    // public PasswordEncoder passwordEncoder() {
    //     return new BCryptPasswordEncoder();
    // }

    // @Bean
    // public AuthenticationManager authManager(HttpSecurity http) throws Exception {
    //     AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
    //     authBuilder
    //         .userDetailsService(users())
    //         .passwordEncoder(passwordEncoder());
    //     return authBuilder.build();
    // }
}
