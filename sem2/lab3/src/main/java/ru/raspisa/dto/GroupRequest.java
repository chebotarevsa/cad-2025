package ru.raspisa.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import static ru.raspisa.util.GroupNames.PATTERN;

public record GroupRequest(
        @NotBlank(message = "название группы обязательно")
        @Size(max = 32, message = "название группы должно быть не длиннее 32 символов")
        @Pattern(regexp = PATTERN, message = "используйте буквы, цифры и дефис")
        String number,
        @NotBlank String shift
) {}
