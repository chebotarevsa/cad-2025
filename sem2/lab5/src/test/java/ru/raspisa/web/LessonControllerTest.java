package ru.raspisa.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.raspisa.dto.LessonDto;
import ru.raspisa.service.LessonService;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LessonControllerTest {

    private final LessonService lessonService = Mockito.mock(LessonService.class);
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(new LessonController(lessonService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    private LessonDto sample(long id, String group, String code, String studio, String teacher) {
        return new LessonDto(id, DayOfWeek.MONDAY, LocalTime.of(9, 0), 1, group, code, studio, teacher,
                false, null, null, null, null, List.of(), false);
    }

    @Test
    void getByGroup_returnsJson() throws Exception {
        when(lessonService.findByGroup("ВР-1")).thenReturn(List.of(sample(1, "ВР-1", "ФВ", "Фото", "Иванов")));

        mvc.perform(get("/api/lessons").param("group", "ВР-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].groupNumber").value("ВР-1"))
                .andExpect(jsonPath("$[0].studioName").value("Фото"))
                .andExpect(jsonPath("$[0].teacherName").value("Иванов"));
    }

    @Test
    void getById_notFound_returns404() throws Exception {
        when(lessonService.findById(99L)).thenThrow(new NoSuchElementException("Занятие не найдено: id=99"));

        mvc.perform(get("/api/lessons/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void create_invalidBody_returns400() throws Exception {
        String body = "{\"time\":\"09:00\",\"orderNumber\":1,\"studioCode\":\"\"}";

        mvc.perform(post("/api/lessons").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Ошибка валидации"));
    }

    @Test
    void create_validBody_returns201() throws Exception {
        when(lessonService.create(any())).thenReturn(sample(10, "ВР-1", "ФВ", "Фото", "Иванов"));
        String body = "{\"dayOfWeek\":\"MONDAY\",\"time\":\"09:00\",\"orderNumber\":1,\"groupNumber\":\"ВР-1\",\"studioCode\":\"ФВ\"}";

        mvc.perform(post("/api/lessons").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10));
    }
}
