package ru.raspisa.dto;

import jakarta.validation.constraints.NotNull;
import ru.raspisa.entity.HolidayType;

import java.time.LocalDate;

public record HolidayRequest(
        @NotNull(message = "тип обязателен") HolidayType type,
        String name,
        @NotNull(message = "дата начала обязательна") LocalDate startDate,
        @NotNull(message = "дата окончания обязательна") LocalDate endDate,
        Boolean yearly
) {
}
