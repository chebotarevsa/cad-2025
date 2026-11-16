package ru.raspisa.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.raspisa.dto.LessonDto;
import ru.raspisa.dto.LessonRequest;
import ru.raspisa.entity.Group;
import ru.raspisa.entity.Lesson;
import ru.raspisa.entity.Studio;
import ru.raspisa.entity.Teacher;
import ru.raspisa.repository.GroupRepository;
import ru.raspisa.repository.LessonRepository;
import ru.raspisa.repository.StudioRepository;
import ru.raspisa.util.GroupNames;

import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
public class LessonService {

    private final LessonRepository lessonRepository;
    private final GroupRepository groupRepository;
    private final StudioRepository studioRepository;
    private final PushService pushService;

    public LessonService(LessonRepository lessonRepository,
                         GroupRepository groupRepository,
                         StudioRepository studioRepository,
                         PushService pushService) {
        this.lessonRepository = lessonRepository;
        this.groupRepository = groupRepository;
        this.studioRepository = studioRepository;
        this.pushService = pushService;
    }

    private static final Comparator<String> GROUP_NULLS_LAST = GroupNames.COMPARATOR;

    public List<LessonDto> findAll() {
        return lessonRepository.findAll().stream()
                .map(this::toDto)
                .sorted(Comparator.comparing(LessonDto::dayOfWeek)
                        .thenComparing(LessonDto::time)
                        .thenComparing(LessonDto::groupNumber, GROUP_NULLS_LAST))
                .toList();
    }

    public List<LessonDto> findByDay(DayOfWeek day) {
        return lessonRepository.findByDayOfWeek(day).stream()
                .map(this::toDto)
                .sorted(Comparator.comparing(LessonDto::time)
                        .thenComparing(LessonDto::groupNumber, GROUP_NULLS_LAST))
                .toList();
    }

    public List<LessonDto> findByGroup(String groupNumber) {
        Group group = groupRepository.findByNumber(GroupNames.normalize(groupNumber));
        Map<Long, Lesson> merged = new LinkedHashMap<>();
        if (group != null) {
            lessonRepository.findByGroup(group).forEach(l -> merged.put(l.getId(), l));
        }
        lessonRepository.findBySpecialTrue().forEach(l -> merged.put(l.getId(), l));
        return merged.values().stream()
                .map(this::toDto)
                .sorted(Comparator.comparing(LessonDto::dayOfWeek)
                        .thenComparing(LessonDto::time))
                .toList();
    }

    public List<LessonDto> findByTeacher(Long teacherId) {
        return lessonRepository.findByStudio_Teacher_Id(teacherId).stream()
                .map(this::toDto)
                .sorted(Comparator.comparing(LessonDto::dayOfWeek)
                        .thenComparing(LessonDto::time)
                        .thenComparing(LessonDto::groupNumber, GROUP_NULLS_LAST))
                .toList();
    }

    public LessonDto findById(Long id) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Занятие не найдено: id=" + id));
        return toDto(lesson);
    }

    @Transactional
    public LessonDto create(LessonRequest req) {
        Lesson lesson = new Lesson();
        applyRequest(lesson, req);
        Lesson saved = lessonRepository.save(lesson);

        if (saved.isSpecial()) {
            String title = (saved.getTitle() != null && !saved.getTitle().isBlank())
                    ? saved.getTitle() : "Новое мероприятие";
            pushService.sendToAll("📣 " + title, notificationBody(saved), "/");
        }
        return toDto(saved);
    }

    private static final DateTimeFormatter PUSH_DATE = DateTimeFormatter.ofPattern("d MMMM", new Locale("ru"));
    private static final DateTimeFormatter PUSH_TIME = DateTimeFormatter.ofPattern("HH:mm");


    private static String notificationBody(Lesson lesson) {
        StringBuilder sb = new StringBuilder();
        if (lesson.getDate() != null) {
            sb.append(lesson.getDate().format(PUSH_DATE));
            if (lesson.getTime() != null) {
                sb.append(" в ").append(lesson.getTime().format(PUSH_TIME));
            }
        }

        String description = lesson.getDescription();
        if (description != null && !description.isBlank()) {
            String text = description.replaceAll("<[^>]*>", " ").replaceAll("\\s+", " ").trim();
            if (sb.length() > 0) sb.append(" — ");
            sb.append(text);
        } else if (sb.length() == 0) {
            sb.append("Новое мероприятие");
        }

        String result = sb.toString();
        return result.length() > 120 ? result.substring(0, 117) + "…" : result;
    }

    @Transactional
    public LessonDto update(Long id, LessonRequest req) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Занятие не найдено: id=" + id));
        applyRequest(lesson, req);
        return toDto(lessonRepository.save(lesson));
    }

    @Transactional
    public void delete(Long id) {
        if (!lessonRepository.existsById(id)) {
            throw new NoSuchElementException("Занятие не найдено: id=" + id);
        }
        lessonRepository.deleteById(id);
    }

    private void applyRequest(Lesson lesson, LessonRequest req) {

        boolean customTime = req.customTime() != null ? req.customTime() : lesson.isCustomTime();
        if (customTime && (req.time() == null || req.endTime() == null || !req.endTime().isAfter(req.time()))) {
            throw new IllegalArgumentException("Окончание занятия должно быть позже начала");
        }

        Group group = null;
        if (req.groupNumber() != null) {
            group = groupRepository.findByNumber(GroupNames.normalize(req.groupNumber()));
            if (group == null) {
                throw new IllegalArgumentException("Группа не найдена: " + req.groupNumber());
            }
        }
        Studio studio = studioRepository.findByCode(req.studioCode());
        if (studio == null) {
            throw new IllegalArgumentException("Студия не найдена: " + req.studioCode());
        }
        lesson.setDayOfWeek(req.dayOfWeek());
        lesson.setTime(req.time());
        lesson.setOrderNumber(req.orderNumber());
        lesson.setSpecial(req.special() != null && req.special());
        lesson.setDate(req.date());
        lesson.setEndTime(req.endTime());
        lesson.setCustomTime(customTime);
        lesson.setTitle(req.title());
        lesson.setDescription(req.description());
        lesson.setPhotos(req.photos() != null ? new ArrayList<>(req.photos()) : new ArrayList<>());
        lesson.setGroup(group);
        lesson.setStudio(studio);
    }

    private LessonDto toDto(Lesson l) {
        Studio studio = l.getStudio();
        Teacher teacher = (studio != null) ? studio.getTeacher() : null;
        return new LessonDto(
                l.getId(),
                l.getDayOfWeek(),
                l.getTime(),
                l.getOrderNumber(),
                (l.getGroup() != null) ? l.getGroup().getNumber() : null,
                (studio != null) ? studio.getCode() : null,
                (studio != null) ? studio.getName() : null,
                (teacher != null) ? teacher.getFullName() : null,
                l.isSpecial(),
                l.getDate(),
                l.getEndTime(),
                l.getTitle(),
                l.getDescription(),
                l.getPhotos() != null ? List.copyOf(l.getPhotos()) : List.of(),
                l.isCustomTime()
        );
    }
}
