package ru.bsuedu.cad.lab.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.Customizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.http.HttpMethod;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.authorizeHttpRequests(authz -> authz
						.requestMatchers("/login", "/css/**").permitAll()

						.requestMatchers(HttpMethod.GET, "/orders/**").hasAnyRole("USER", "MANAGER")

						.requestMatchers("/orders/**").hasRole("MANAGER")

						.requestMatchers("/api/**").hasRole("MANAGER")

						.anyRequest().authenticated())
				.formLogin(form -> form
						.loginPage("/login")
						.defaultSuccessUrl("/orders", true)
						.permitAll())
				.httpBasic(Customizer.withDefaults())
				.logout(logout -> logout
						.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
						.logoutSuccessUrl("/login?logout")
						.permitAll())
				.csrf(csrf -> csrf.disable());

		return http.build();
	}

	@Bean
	public UserDetailsService users() {
		return new InMemoryUserDetailsManager(
				User.withUsername("user")
						.password("{noop}1234")
						.roles("USER")
						.build(),
				User.withUsername("manager")
						.password("{noop}4321")
						.roles("MANAGER")
						.build());
	}
}