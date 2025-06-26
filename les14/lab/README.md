# Отчет о лаботаротоной работе № 7 Spring security.Basic Authentication

## Цель работы

Обеспечение безопасности за счет добавления авторизации пользователя по ролям. Создание формы для авторизации.

## Выполнение работы

Для начала был настроен проект для работы Spring Security добавлением двух библиотек: org.springframework.security:spring-security-web и
org.springframework.security:spring-security-config.

Для конфигурации Spring Security в директории ```config``` был создан конфигурационный класс ```ConfigSecurity.java```

```java

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

```

В конфигурационном классе были созданы два пользователя: user и manager

```java

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

```

Далее была реализована аутентификация и авторизация пользователя с помощью формы

```HTML

<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Вход</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .login-container {
            max-width: 360px;
            margin: 100px auto;
            padding: 20px;
            background-color: white;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }
        .login-container h2 {
            margin-bottom: 20px;
            font-size: 1.5rem;
        }
        .login-container .btn {
            width: 100%;
        }
    </style>
</head>
<body>

<div class="login-container">
    <h2 class="text-center">Вход в систему</h2>

    <form th:action="@{/login}" method="post">
        <div class="form-group">
            <label for="username">Логин:</label>
            <input type="text" id="username" name="username" class="form-control" required />
        </div>

        <div class="form-group">
            <label for="password">Пароль:</label>
            <input type="password" id="password" name="password" class="form-control" required />
        </div>

        <button type="submit" class="btn btn-primary">Войти</button>
    </form>

    <!-- Ошибки -->
    <div th:if="${param.error}">
        <p style="color:red;">Неверный логин или пароль</p>
    </div>

    <div th:if="${param.logout}">
        <p style="color:green;">Вы вышли из системы</p>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.3/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

</body>
</html>

```

При авторизации **пользователя user** в форме доступна часть приложения с просмотром списка товаров

При авторизации **пользователя manager** в форме доступна часть приложения содержащая REST сервис

```java

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

```

Для тестирования работоспособности приложения был выполнен деплой приложения на сервер Apache Tomcat 11.
## Выводы

В ходе выполнения лабораторной работы была добавлена авторизация пользователя по ролям, а также создана форма для авторизации.