package ru.raspisa.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.raspisa.dto.UserDto;
import ru.raspisa.entity.MapPlace;
import ru.raspisa.service.AuthService;
import ru.raspisa.service.MapPlaceService;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class MapControllerTest {
    private final MapPlaceService places = mock(MapPlaceService.class);
    private final AuthService auth = mock(AuthService.class);
    private MockMvc mvc;

    @BeforeEach void setup() {
        mvc = MockMvcBuilders.standaloneSetup(new MapController(places, auth))
                .setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    @Test void visitorsCanReadButCannotWrite() throws Exception {
        when(places.all()).thenReturn(List.of());
        mvc.perform(get("/api/map/places")).andExpect(status().isOk()).andExpect(content().json("[]"));
        mvc.perform(multipart("/api/map/places").header("X-Map-Request", "1")
                .param("title", "Place").param("latitude", "50.78").param("longitude", "36.48"))
                .andExpect(status().isUnauthorized());
        mvc.perform(delete("/api/map/places/1").header("X-Map-Request", "1"))
                .andExpect(status().isUnauthorized());
        verify(places, never()).delete(anyLong());
    }

    @Test void editorSessionRequiresRealAdminAndCanBeEnded() throws Exception {
        when(auth.login("teacher", "test")).thenReturn(new UserDto("teacher", "TEACHER", "Teacher"));
        mvc.perform(post("/api/map/session").header("X-Map-Request", "1").contentType("application/json")
                .content("{\"login\":\"teacher\",\"password\":\"test\"}"))
                .andExpect(status().isForbidden());
        when(auth.login("admin", "test")).thenReturn(new UserDto("admin", "ADMIN", "Admin"));
        var result = mvc.perform(post("/api/map/session").header("X-Map-Request", "1").contentType("application/json")
                .content("{\"login\":\"admin\",\"password\":\"test\"}"))
                .andExpect(status().isOk()).andReturn();
        var session = (MockHttpSession) result.getRequest().getSession(false);
        mvc.perform(get("/api/map/session").session(session)).andExpect(jsonPath("$.editor").value(true))
                .andExpect(header().string("Cache-Control", "no-store"));
        mvc.perform(delete("/api/map/places/1").session(session).header("X-Map-Request", "1"))
                .andExpect(status().isOk());
        verify(places).delete(1L);
        mvc.perform(delete("/api/map/session").session(session).header("X-Map-Request", "1"))
                .andExpect(status().isOk());
        mvc.perform(get("/api/map/session")).andExpect(jsonPath("$.editor").value(false));
    }

    @Test void crossSiteFormCannotCreatePlaces() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("map-admin", true);
        mvc.perform(multipart("/api/map/places").session(session)
                .param("title", "Place").param("latitude", "50.78").param("longitude", "36.48"))
                .andExpect(status().isNotFound());
        verifyNoInteractions(places);
    }

    @Test void audioSupportsSeeking() throws Exception {
        MapPlace place = new MapPlace();
        place.audioType = "audio/mpeg";
        when(places.find(1L)).thenReturn(place);
        when(places.audio(1L)).thenReturn(new byte[100]);
        mvc.perform(get("/api/map/places/1/audio").header("Range", "bytes=10-19"))
                .andExpect(status().isPartialContent())
                .andExpect(header().string("Content-Range", "bytes 10-19/100"))
                .andExpect(header().string("X-Content-Type-Options", "nosniff"))
                .andExpect(content().bytes(new byte[10]));
    }

    @Test void imageIsPublicAndHasAnExplicitSafeContentType() throws Exception {
        when(places.image(1L)).thenReturn(new byte[]{1, 2, 3});
        mvc.perform(get("/api/map/places/1/image"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("image/png"))
                .andExpect(header().string("X-Content-Type-Options", "nosniff"))
                .andExpect(content().bytes(new byte[]{1, 2, 3}));
    }
}
