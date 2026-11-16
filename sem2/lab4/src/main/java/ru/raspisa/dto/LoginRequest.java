package ru.raspisa.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Введите логин") String login,
        @NotBlank(message = "Введите пароль") String password
) {
}
