package ru.raspisa.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.raspisa.dto.LessonDto;
import ru.raspisa.dto.LessonRequest;
import ru.raspisa.entity.Group;
import ru.raspisa.entity.Lesson;
import ru.raspisa.entity.Studio;
import ru.raspisa.entity.Teacher;
import ru.raspisa.repository.GroupRepository;
import ru.raspisa.repository.LessonRepository;
import ru.raspisa.repository.StudioRepository;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LessonServiceTest {

    @Mock LessonRepository lessonRepository;
    @Mock GroupRepository groupRepository;
    @Mock StudioRepository studioRepository;
    @Mock PushService pushService;

    @InjectMocks LessonService service;

    private Lesson lesson(Long id, DayOfWeek day, LocalTime time, String groupNumber,
                          String code, String studioName, String teacherName) {
        Teacher teacher = new Teacher();
        teacher.setFullName(teacherName);
        Studio studio = new Studio();
        studio.setCode(code);
        studio.setName(studioName);
        studio.setTeacher(teacher);
        Lesson lesson = new Lesson();
        lesson.setId(id);
        lesson.setDayOfWeek(day);
        lesson.setTime(time);
        lesson.setOrderNumber(1);
        lesson.setStudio(studio);
        if (groupNumber != null) {
            Group group = new Group();
            group.setNumber(groupNumber);
            lesson.setGroup(group);
        }
        return lesson;
    }

    private LessonRequest request(String groupNumber, String studioCode) {
        return new LessonRequest(DayOfWeek.MONDAY, LocalTime.of(9, 0), 1, groupNumber, studioCode,
                false, null, null, null, null, null, null);
    }

    @Test
    void findById_mapsTeacherViaStudio() {
        when(lessonRepository.findById(1L))
                .thenReturn(Optional.of(lesson(1L, DayOfWeek.MONDAY, LocalTime.of(9, 0), "ВР-1", "ФВ", "Фото", "Иванов")));

        LessonDto dto = service.findById(1L);

        assertThat(dto.teacherName()).isEqualTo("Иванов");
        assertThat(dto.studioName()).isEqualTo("Фото");
        assertThat(dto.studioCode()).isEqualTo("ФВ");
        assertThat(dto.groupNumber()).isEqualTo("ВР-1");
    }

    @Test
    void customTimeIsSavedAndReturnedByGroupAndTeacher() {
        Group group = new Group();
        group.setNumber("2");
        when(groupRepository.findByNumber("2")).thenReturn(group);
        when(studioRepository.findByCode("VR")).thenReturn(new Studio());
        when(lessonRepository.save(any())).thenAnswer(invocation -> {
            Lesson lesson = invocation.getArgument(0);
            lesson.setId(20L);
            return lesson;
        });
        var request = new LessonRequest(DayOfWeek.MONDAY, LocalTime.of(13, 15), 1, "2", "VR",
                false, null, LocalTime.of(14, 35), null, null, null, true);
        LessonDto saved = service.create(request);
        assertThat(saved.customTime()).isTrue();
        assertThat(saved.time()).isEqualTo(LocalTime.of(13, 15));
        assertThat(saved.endTime()).isEqualTo(LocalTime.of(14, 35));
        Lesson stored = lesson(20L, DayOfWeek.MONDAY, saved.time(), "2", "VR", "Studio", "Teacher");
        stored.setEndTime(saved.endTime());
        stored.setCustomTime(true);
        when(lessonRepository.findByGroup(group)).thenReturn(List.of(stored));
        when(lessonRepository.findByStudio_Teacher_Id(1L)).thenReturn(List.of(stored));
        assertThat(service.findByGroup("2").get(0).endTime()).isEqualTo(saved.endTime());
        assertThat(service.findByTeacher(1L).get(0).customTime()).isTrue();
    }

    @Test
    void customTimeRejectsMissingOrReversedEnd() {
        for (LocalTime end : new LocalTime[]{null, LocalTime.of(13, 15), LocalTime.of(12, 0)}) {
            var request = new LessonRequest(DayOfWeek.MONDAY, LocalTime.of(13, 15), 1, "2", "VR",
                    false, null, end, null, null, null, true);
            assertThatThrownBy(() -> service.create(request)).isInstanceOf(IllegalArgumentException.class);
        }
        verify(lessonRepository, never()).save(any());
    }

    @Test
    void findById_notFound_throws() {
        when(lessonRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(99L))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void findByGroup_mergesSpecialEventsAndSortsByDayThenTime() {
        Lesson mon = lesson(1L, DayOfWeek.MONDAY, LocalTime.of(10, 0), "ВР-1", "ФВ", "Фото", "И");
        Lesson tue = lesson(2L, DayOfWeek.TUESDAY, LocalTime.of(9, 0), "ВР-1", "ДЗ", "Диз", "П");
        Lesson special = lesson(3L, DayOfWeek.MONDAY, LocalTime.of(8, 0), null, "ВР", "VR", "С");
        special.setSpecial(true);

        Group group = mon.getGroup();
        when(groupRepository.findByNumber("ВР-1")).thenReturn(group);
        when(lessonRepository.findByGroup(group)).thenReturn(List.of(mon, tue));
        when(lessonRepository.findBySpecialTrue()).thenReturn(List.of(special));

        List<LessonDto> result = service.findByGroup("ВР-1");


        assertThat(result).extracting(LessonDto::id).containsExactly(3L, 1L, 2L);
    }

    @Test
    void create_resolvesGroupAndStudio_savesAndMaps() {
        Teacher teacher = new Teacher();
        teacher.setFullName("Иванов");
        Studio studio = new Studio();
        studio.setCode("ФВ");
        studio.setName("Фото");
        studio.setTeacher(teacher);
        Group group = new Group();
        group.setNumber("ВР-1");

        when(groupRepository.findByNumber("ВР-1")).thenReturn(group);
        when(studioRepository.findByCode("ФВ")).thenReturn(studio);
        when(lessonRepository.save(any(Lesson.class))).thenAnswer(inv -> {
            Lesson saved = inv.getArgument(0);
            saved.setId(10L);
            return saved;
        });

        LessonDto dto = service.create(request("вр-1", "ФВ"));

        assertThat(dto.id()).isEqualTo(10L);
        assertThat(dto.studioCode()).isEqualTo("ФВ");
        assertThat(dto.teacherName()).isEqualTo("Иванов");
        assertThat(dto.groupNumber()).isEqualTo("ВР-1");
    }

    @Test
    void create_groupNotFound_throwsAndDoesNotSave() {
        when(groupRepository.findByNumber("99")).thenReturn(null);

        assertThatThrownBy(() -> service.create(request("99", "ФВ")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Группа");

        verify(lessonRepository, never()).save(any());
    }

    @Test
    void create_studioNotFound_throws() {
        when(groupRepository.findByNumber("3")).thenReturn(new Group());
        when(studioRepository.findByCode("XX")).thenReturn(null);

        assertThatThrownBy(() -> service.create(request("3", "XX")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Студия");
    }

    @Test
    void delete_notExists_throwsAndDoesNotDelete() {
        when(lessonRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> service.delete(99L))
                .isInstanceOf(NoSuchElementException.class);

        verify(lessonRepository, never()).deleteById(any());
    }

    @Test
    void delete_exists_deletes() {
        when(lessonRepository.existsById(5L)).thenReturn(true);

        service.delete(5L);

        verify(lessonRepository).deleteById(5L);
    }
}
