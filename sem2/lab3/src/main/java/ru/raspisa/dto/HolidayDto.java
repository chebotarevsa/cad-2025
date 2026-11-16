package ru.raspisa.dto;

import ru.raspisa.entity.HolidayType;

import java.time.LocalDate;

public record HolidayDto(
        Long id,
        HolidayType type,
        String name,
        LocalDate startDate,
        LocalDate endDate,
        boolean yearly
) {
}
