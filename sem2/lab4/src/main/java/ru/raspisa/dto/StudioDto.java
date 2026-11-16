package ru.raspisa.dto;

import java.util.List;

public record StudioDto(
        Long id,
        String code,
        String name,
        String description,
        List<String> photos,
        String teacherPhoto,
        String teacherName
) {
}
