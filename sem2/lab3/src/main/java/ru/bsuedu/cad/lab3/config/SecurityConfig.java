package ru.bsuedu.cad.lab3.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

	@Bean
	public InMemoryUserDetailsManager userDetailsService() {
		UserDetails user = User.withUsername("user")
				.password("{noop}password")
				.roles("USER")
				.build();

		UserDetails moderator = User.withUsername("moderator")
				.password("{noop}password")
				.roles("MODERATOR")
				.build();

		UserDetails admin = User.withUsername("admin")
				.password("{noop}password")
				.roles("ADMIN")
				.build();

		return new InMemoryUserDetailsManager(user, moderator, admin);
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
				.formLogin(Customizer.withDefaults())
				.logout(Customizer.withDefaults());

		return http.build();
	}
}
