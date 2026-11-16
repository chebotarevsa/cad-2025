package ru.raspisa.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.raspisa.dto.TeacherDto;
import ru.raspisa.repository.TeacherRepository;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    private final TeacherRepository teacherRepository;

    public TeacherController(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @GetMapping
    public List<TeacherDto> all() {
        return teacherRepository.findAll().stream()
                .map(t -> new TeacherDto(t.getId(), t.getFullName()))
                .sorted(Comparator.comparing(TeacherDto::fullName))
                .toList();
    }
}
