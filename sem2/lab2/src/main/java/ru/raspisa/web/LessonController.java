package ru.raspisa.web;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.raspisa.dto.LessonDto;
import ru.raspisa.dto.LessonRequest;
import ru.raspisa.service.LessonService;

import java.time.DayOfWeek;
import java.util.List;

@RestController
@RequestMapping("/api/lessons")
public class LessonController {

    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @GetMapping
    public List<LessonDto> list(@RequestParam(required = false) DayOfWeek day,
                                @RequestParam(required = false) String group,
                                @RequestParam(required = false) Long teacher) {
        if (teacher != null) return lessonService.findByTeacher(teacher);
        if (group != null) return lessonService.findByGroup(group);
        if (day != null) return lessonService.findByDay(day);
        return lessonService.findAll();
    }

    @GetMapping("/{id}")
    public LessonDto byId(@PathVariable Long id) {
        return lessonService.findById(id);
    }

    @PostMapping
    public ResponseEntity<LessonDto> create(@Valid @RequestBody LessonRequest request) {
        LessonDto created = lessonService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public LessonDto update(@PathVariable Long id, @Valid @RequestBody LessonRequest request) {
        return lessonService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        lessonService.delete(id);
    }
}
