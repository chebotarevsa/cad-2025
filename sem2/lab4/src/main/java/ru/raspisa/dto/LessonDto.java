package ru.raspisa.dto;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record LessonDto(
        Long id,
        DayOfWeek dayOfWeek,
        LocalTime time,
        Integer orderNumber,
        String groupNumber,
        String studioCode,
        String studioName,
        String teacherName,
        boolean special,
        LocalDate date,
        LocalTime endTime,
        String title,
        String description,
        List<String> photos,
        boolean customTime
) {
}
