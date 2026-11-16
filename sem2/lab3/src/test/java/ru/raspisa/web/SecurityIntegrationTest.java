package ru.raspisa.web;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:lab3-security;DB_CLOSE_DELAY=-1",
        "spring.jpa.hibernate.ddl-auto=create-drop", "spring.jpa.show-sql=false",
        "push.reminder.cron=-", "logging.level.root=ERROR"
})
class SecurityIntegrationTest {
    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    private MockHttpSession login(String login, String password) throws Exception {
        var result = mvc.perform(post("/api/auth/login").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"" + login + "\",\"password\":\"" + password + "\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.login").value(login)).andReturn();
        var session = (MockHttpSession) result.getRequest().getSession(false);
        assertNotNull(session.getAttribute("SPRING_SECURITY_CONTEXT"));
        return session;
    }

    private String lesson(boolean event) {
        return "{\"dayOfWeek\":\"MONDAY\",\"time\":\"09:00\",\"orderNumber\":1,"
                + "\"groupNumber\":\"1\",\"studioCode\":\"ФВ\",\"special\":" + event
                + ",\"date\":\"2026-11-16\",\"title\":\"Учебное мероприятие\"}";
    }

    private long create(MockHttpSession session, boolean event) throws Exception {
        var result = mvc.perform(post("/api/lessons").session(session).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON).content(lesson(event)))
                .andExpect(status().isCreated()).andReturn();
        return ((Number) JsonPath.read(result.getResponse().getContentAsString(), "$.id")).longValue();
    }

    @Test
    void guestCanReadButCannotManage() throws Exception {
        mvc.perform(get("/api/ping")).andExpect(status().isOk());
        mvc.perform(get("/api/attendance").param("groupId", "1").param("month", "2026-11"))
                .andExpect(status().isUnauthorized());
        mvc.perform(post("/api/lessons").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(lesson(false)))
                .andExpect(status().isUnauthorized());
        mvc.perform(get("/dashboard")).andExpect(status().is3xxRedirection());
    }

    @Test
    void wrongPasswordIsRejected() throws Exception {
        mvc.perform(post("/api/auth/login").with(csrf()).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"admin\",\"password\":\"wrong\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void studentCanReadEventsButCannotOpenJournalOrWrite() throws Exception {
        var session = login("student", "Student!2026");
        mvc.perform(get("/api/auth/me").session(session)).andExpect(jsonPath("$.role").value("STUDENT"));
        mvc.perform(get("/api/lessons").session(session)).andExpect(status().isOk());
        mvc.perform(get("/api/attendance").session(session).param("groupId", "1").param("month", "2026-11"))
                .andExpect(status().isForbidden());
        mvc.perform(post("/api/lessons").session(session).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(lesson(true)))
                .andExpect(status().isForbidden());
    }

    @Test
    void teacherCanUseJournalButCannotManageEvents() throws Exception {
        var session = login("teacher", "Teacher!2026");
        mvc.perform(get("/api/attendance").session(session).param("groupId", "1").param("month", "2026-11"))
                .andExpect(status().isOk());
        mvc.perform(post("/api/lessons").session(session).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(lesson(true)))
                .andExpect(status().isForbidden());
    }

    @Test
    void methodistCanManageOnlyEvents() throws Exception {
        var admin = login("admin", "Admin!2026");
        long ordinaryId = create(admin, false);
        var methodist = login("methodist", "Methodist!2026");
        mvc.perform(post("/api/lessons").session(methodist).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(lesson(false)))
                .andExpect(status().isForbidden());
        mvc.perform(put("/api/lessons/" + ordinaryId).session(methodist).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(lesson(true)))
                .andExpect(status().isForbidden());
        mvc.perform(delete("/api/lessons/" + ordinaryId).session(methodist).with(csrf())).andExpect(status().isForbidden());
        long eventId = create(methodist, true);
        mvc.perform(put("/api/lessons/" + eventId).session(methodist).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(lesson(false)))
                .andExpect(status().isForbidden());
        mvc.perform(put("/api/lessons/" + eventId).session(methodist).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(lesson(true)))
                .andExpect(status().isOk());
        mvc.perform(delete("/api/lessons/" + eventId).session(methodist).with(csrf())).andExpect(status().isNoContent());
        mvc.perform(get("/api/attendance").session(methodist).param("groupId", "1").param("month", "2026-11"))
                .andExpect(status().isForbidden());
        mvc.perform(post("/api/groups").session(methodist).with(csrf()).contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminCanManageAndLogoutAndCsrfIsRequired() throws Exception {
        var session = login("admin", "Admin!2026");
        long id = create(session, false);
        mvc.perform(post("/api/lessons").session(session).contentType(MediaType.APPLICATION_JSON).content(lesson(false)))
                .andExpect(status().isForbidden());
        mvc.perform(delete("/api/lessons/" + id).session(session).with(csrf())).andExpect(status().isNoContent());
        mvc.perform(post("/api/auth/logout").session(session).with(csrf())).andExpect(status().isOk());
        mvc.perform(get("/api/auth/me")).andExpect(status().isUnauthorized());
    }
}
