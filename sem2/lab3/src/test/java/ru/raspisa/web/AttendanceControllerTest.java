package ru.raspisa.web;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.raspisa.service.AttendanceService;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AttendanceControllerTest {
    @Test void journalRequiresSessionAndIsNeverCached() throws Exception {
        AttendanceService service = mock(AttendanceService.class);
        when(service.journal(1L, "2026-09")).thenReturn(new AttendanceService.Journal(List.of(), List.of(), List.of()));
        var mvc = MockMvcBuilders.standaloneSetup(new AttendanceController(service)).build();
        mvc.perform(get("/api/attendance?groupId=1&month=2026-09")).andExpect(status().isUnauthorized());
        mvc.perform(put("/api/attendance/marks").header("X-Attendance-Request", "1").contentType("application/json")
                .content("{\"studentId\":1,\"lessonId\":1,\"date\":\"2026-09-07\",\"value\":\"П\"}"))
                .andExpect(status().isUnauthorized());
        verifyNoInteractions(service);
        MockHttpSession session = new MockHttpSession(); session.setAttribute("schoolStaff", true);
        mvc.perform(get("/api/attendance?groupId=1&month=2026-09").session(session))
                .andExpect(status().isOk()).andExpect(header().string("Cache-Control", "no-store"));
    }
}
