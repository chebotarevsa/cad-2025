package ru.raspisa.dto;

import java.util.List;

public record StudioUpdateRequest(
        String description,
        List<String> photos,
        String teacherPhoto,
        String teacherName
) {
}
