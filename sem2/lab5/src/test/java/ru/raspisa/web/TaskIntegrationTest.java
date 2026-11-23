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
import ru.raspisa.repository.LoginHistoryRepository;
import ru.raspisa.repository.TaskHistoryRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:lab4-tasks;DB_CLOSE_DELAY=-1",
        "spring.datasource.username=sa", "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop", "spring.jpa.show-sql=false",
        "push.reminder.cron=-", "logging.level.root=ERROR"
})
class TaskIntegrationTest {
    @Autowired WebApplicationContext context;
    @Autowired TaskHistoryRepository history;
    @Autowired LoginHistoryRepository logins;
    MockMvc mvc;

    @BeforeEach
    void setup() { mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build(); }

    MockHttpSession login(String login, String password) throws Exception {
        var result = mvc.perform(post("/api/auth/login").with(csrf()).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"" + login + "\",\"password\":\"" + password + "\"}"))
                .andExpect(status().isOk()).andReturn();
        return (MockHttpSession) result.getRequest().getSession(false);
    }

    String task(String title, String status) {
        return "{\"title\":\"" + title + "\",\"description\":\"Подготовить материалы\",\"priority\":\"HIGH\","
                + "\"category\":\"Мастер-класс\",\"status\":\"" + status + "\"}";
    }

    long create(MockHttpSession session) throws Exception {
        var result = mvc.perform(post("/api/tasks").session(session).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON).content(task("Подготовка зала", "TODO")))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.owner").value("methodist"))
                .andExpect(jsonPath("$.createdAt").isNotEmpty()).andReturn();
        return ((Number) JsonPath.read(result.getResponse().getContentAsString(), "$.id")).longValue();
    }

    @Test
    void taskAndSubtaskChangesArePersistedWithHistory() throws Exception {
        var session = login("methodist", "Methodist!2026");
        long id = create(session);
        mvc.perform(put("/api/tasks/" + id).session(session).with(csrf()).contentType(MediaType.APPLICATION_JSON)
                .content(task("Подготовка зала", "IN_PROGRESS"))).andExpect(status().isOk());
        var added = mvc.perform(post("/api/tasks/" + id + "/subtasks").session(session).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON).content("{\"description\":\"Проверить проектор\",\"completed\":false}"))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.subtasks.length()").value(1)).andReturn();
        long subId = ((Number) JsonPath.read(added.getResponse().getContentAsString(), "$.subtasks[0].id")).longValue();
        mvc.perform(put("/api/tasks/" + id + "/subtasks/" + subId).session(session).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON).content("{\"description\":\"Проверить проектор\",\"completed\":true}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.subtasks[0].completed").value(true));
        mvc.perform(get("/api/tasks").session(session)).andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id == " + id + ")].status").value("IN_PROGRESS"));
        mvc.perform(get("/api/tasks/" + id + "/history").session(session)).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4)).andExpect(jsonPath("$[0].actor").value("methodist"));
        mvc.perform(delete("/api/tasks/" + id + "/subtasks/" + subId).session(session).with(csrf()))
                .andExpect(status().isOk()).andExpect(jsonPath("$.subtasks.length()").value(0));
        mvc.perform(delete("/api/tasks/" + id).session(session).with(csrf())).andExpect(status().isNoContent());
        mvc.perform(get("/api/tasks/" + id + "/history").session(session)).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].action").value("DELETE")).andExpect(jsonPath("$.length()").value(6));
        mvc.perform(put("/api/tasks/" + id).session(session).with(csrf()).contentType(MediaType.APPLICATION_JSON)
                .content(task("Возврат", "TODO"))).andExpect(status().isNotFound());
    }

    @Test
    void guestStudentAndTeacherCannotReadTasksOrAudit() throws Exception {
        mvc.perform(get("/api/tasks")).andExpect(status().isUnauthorized());
        for (String[] account : new String[][] {{"student", "Student!2026"}, {"teacher", "Teacher!2026"}}) {
            var session = login(account[0], account[1]);
            mvc.perform(get("/api/tasks").session(session)).andExpect(status().isForbidden());
            mvc.perform(get("/api/login-history").session(session)).andExpect(status().isForbidden());
            mvc.perform(post("/api/tasks").session(session).with(csrf()).contentType(MediaType.APPLICATION_JSON)
                    .content(task("Запрещено", "TODO"))).andExpect(status().isForbidden());
        }
        var methodist = login("methodist", "Methodist!2026");
        mvc.perform(get("/api/login-history").session(methodist)).andExpect(status().isForbidden());
        var admin = login("admin", "Admin!2026");
        mvc.perform(get("/api/login-history").session(admin)).andExpect(status().isOk());
        mvc.perform(get("/api/tasks").session(admin)).andExpect(status().isOk());
    }

    @Test
    void failedLoginDoesNotCreateSuccessHistory() throws Exception {
        long before = logins.count();
        mvc.perform(post("/api/auth/login").with(csrf()).contentType(MediaType.APPLICATION_JSON)
                .content("{\"login\":\"admin\",\"password\":\"wrong\"}")).andExpect(status().isUnauthorized());
        assertEquals(before, logins.count());
        login("admin", "Admin!2026");
        assertEquals(before + 1, logins.count());
        assertEquals("127.0.0.1", logins.findTop50ByOrderByLoggedAtDescIdDesc().get(0).ipAddress);
    }

    @Test
    void invalidChangesAndCrossTaskSubtaskIdsDoNotChangeHistory() throws Exception {
        var session = login("methodist", "Methodist!2026");
        long first = create(session);
        long second = create(session);
        var result = mvc.perform(post("/api/tasks/" + first + "/subtasks").session(session).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON).content("{\"description\":\"Материалы\",\"completed\":false}"))
                .andExpect(status().isCreated()).andReturn();
        long subId = ((Number) JsonPath.read(result.getResponse().getContentAsString(), "$.subtasks[0].id")).longValue();
        long before = history.count();
        mvc.perform(delete("/api/tasks/" + second + "/subtasks/" + subId).session(session).with(csrf()))
                .andExpect(status().isNotFound());
        mvc.perform(put("/api/tasks/" + first).session(session).with(csrf()).contentType(MediaType.APPLICATION_JSON)
                .content(task(" ", "DONE"))).andExpect(status().isBadRequest());
        mvc.perform(put("/api/tasks/" + first).session(session).contentType(MediaType.APPLICATION_JSON)
                .content(task("Без CSRF", "DONE"))).andExpect(status().isForbidden());
        assertEquals(before, history.count());
    }
}
