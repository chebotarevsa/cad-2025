package ru.raspisa.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.raspisa.dto.StudioDto;
import ru.raspisa.dto.StudioUpdateRequest;
import ru.raspisa.entity.Studio;
import ru.raspisa.entity.Teacher;
import ru.raspisa.repository.StudioRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
public class StudioService {

    private final StudioRepository studioRepository;

    public StudioService(StudioRepository studioRepository) {
        this.studioRepository = studioRepository;
    }

    public List<StudioDto> findAll() {
        return studioRepository.findAll().stream()
                .filter(s -> s.getTeacher() != null)
                .sorted(Comparator.comparing(Studio::getName))
                .map(this::toDto)
                .toList();
    }

    @Transactional
    public StudioDto update(Long id, StudioUpdateRequest req) {
        Studio s = studioRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Студия не найдена: id=" + id));
        s.setDescription(req.description());
        s.setPhotos(req.photos() != null ? new ArrayList<>(req.photos()) : new ArrayList<>());
        s.setTeacherPhoto(req.teacherPhoto());

        Teacher t = s.getTeacher();
        if (t != null && req.teacherName() != null && !req.teacherName().isBlank()) {
            t.setFullName(req.teacherName().trim());
        }
        return toDto(studioRepository.save(s));
    }

    private StudioDto toDto(Studio s) {
        Teacher t = s.getTeacher();
        return new StudioDto(
                s.getId(),
                s.getCode(),
                s.getName(),
                s.getDescription(),
                s.getPhotos() != null ? List.copyOf(s.getPhotos()) : List.of(),
                s.getTeacherPhoto(),
                (t != null) ? t.getFullName() : null
        );
    }
}
