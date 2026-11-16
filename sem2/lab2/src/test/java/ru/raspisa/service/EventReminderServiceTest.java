package ru.raspisa.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.raspisa.entity.Lesson;
import ru.raspisa.repository.LessonRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.ArgumentCaptor.forClass;

@ExtendWith(MockitoExtension.class)
class EventReminderServiceTest {

    private static final String ZONE = "Europe/Moscow";

    @Mock LessonRepository lessonRepository;
    @Mock PushService pushService;

    EventReminderService service;

    @BeforeEach
    void setUp() {
        service = new EventReminderService(lessonRepository, pushService, ZONE);
    }

    private LocalDate today() {
        return LocalDate.now(ZoneId.of(ZONE));
    }

    private Lesson event(String title, LocalTime time, String description) {
        Lesson lesson = new Lesson();
        lesson.setSpecial(true);
        lesson.setDate(today());
        lesson.setTitle(title);
        lesson.setTime(time);
        lesson.setDescription(description);
        return lesson;
    }

    @Test
    void pushDisabled_doesNotTouchDatabase() {
        when(pushService.isEnabled()).thenReturn(false);

        service.remindAboutTodayEvents();

        verifyNoInteractions(lessonRepository);
    }

    @Test
    void noEventsToday_sendsNothing() {
        when(pushService.isEnabled()).thenReturn(true);
        when(lessonRepository.findBySpecialTrueAndDate(today())).thenReturn(List.of());

        service.remindAboutTodayEvents();

        verify(pushService, never()).sendToAll(any(), any(), any());
    }

    @Test
    void eventToday_sendsReminderWithTitleAndTime() {
        when(pushService.isEnabled()).thenReturn(true);
        when(lessonRepository.findBySpecialTrueAndDate(today()))
                .thenReturn(List.of(event("День открытых дверей", LocalTime.of(14, 30), null)));

        service.remindAboutTodayEvents();

        var title = forClass(String.class);
        var body = forClass(String.class);
        verify(pushService).sendToAll(title.capture(), body.capture(), any());

        assertThat(title.getValue()).isEqualTo("📣 Сегодня: День открытых дверей");
        assertThat(body.getValue()).isEqualTo("Начало в 14:30");
    }

    @Test
    void description_isStrippedOfHtmlAndAppended() {
        when(pushService.isEnabled()).thenReturn(true);
        when(lessonRepository.findBySpecialTrueAndDate(today()))
                .thenReturn(List.of(event("Концерт", LocalTime.of(18, 0), "<p>Актовый  <b>зал</b></p>")));

        service.remindAboutTodayEvents();

        var body = forClass(String.class);
        verify(pushService).sendToAll(any(), body.capture(), any());

        assertThat(body.getValue()).isEqualTo("Начало в 18:00 – Актовый зал");
    }

    @Test
    void blankTitleAndTime_fallsBackToNeutralText() {
        when(pushService.isEnabled()).thenReturn(true);
        when(lessonRepository.findBySpecialTrueAndDate(today()))
                .thenReturn(List.of(event("  ", null, null)));

        service.remindAboutTodayEvents();

        var title = forClass(String.class);
        var body = forClass(String.class);
        verify(pushService).sendToAll(title.capture(), body.capture(), any());

        assertThat(title.getValue()).isEqualTo("📣 Сегодня: мероприятие");
        assertThat(body.getValue()).isEqualTo("Мероприятие сегодня");
    }

    @Test
    void longDescription_isTruncatedTo120Chars() {
        when(pushService.isEnabled()).thenReturn(true);
        when(lessonRepository.findBySpecialTrueAndDate(today()))
                .thenReturn(List.of(event("Фестиваль", LocalTime.of(10, 0), "и".repeat(300))));

        service.remindAboutTodayEvents();

        var body = forClass(String.class);
        verify(pushService).sendToAll(any(), body.capture(), any());

        assertThat(body.getValue()).hasSize(120).endsWith("…");
    }

    @Test
    void severalEventsToday_sendsOneReminderEach() {
        when(pushService.isEnabled()).thenReturn(true);
        when(lessonRepository.findBySpecialTrueAndDate(today())).thenReturn(List.of(
                event("Концерт", LocalTime.of(12, 0), null),
                event("Выставка", LocalTime.of(15, 0), null)));

        service.remindAboutTodayEvents();

        verify(pushService, times(2)).sendToAll(any(), any(), any());
    }
}
