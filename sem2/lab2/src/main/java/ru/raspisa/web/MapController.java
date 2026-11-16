package ru.raspisa.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.raspisa.dto.LoginRequest;
import ru.raspisa.dto.UserDto;
import ru.raspisa.entity.MapPlace;
import ru.raspisa.service.AuthService;
import ru.raspisa.service.MapPlaceService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/map")
public class MapController {
    private static final String ADMIN = "map-admin";
    private final MapPlaceService places;
    private final AuthService auth;

    public MapController(MapPlaceService places, AuthService auth) { this.places = places; this.auth = auth; }

    @GetMapping("/session")
    public ResponseEntity<Map<String, Boolean>> session(HttpServletRequest request) {
        return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(Map.of("editor", isAdmin(request)));
    }

    @PostMapping(value = "/session", headers = "X-Map-Request=1")
    public Map<String, Boolean> login(@Valid @RequestBody LoginRequest login, HttpServletRequest request) {
        UserDto user = auth.login(login.login(), login.password());
        if (!"ADMIN".equals(user.role())) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Нужна учётная запись администратора");
        HttpSession session = request.getSession();
        request.changeSessionId();
        session.setAttribute(ADMIN, true);
        return Map.of("editor", true);
    }

    @DeleteMapping(value = "/session", headers = "X-Map-Request=1")
    public void logout(HttpServletRequest request) {
        if (request.getSession(false) != null) request.getSession(false).invalidate();
    }

    @GetMapping("/places")
    public ResponseEntity<List<MapPlace>> all() {
        return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(places.all());
    }

    @GetMapping("/places/{id}/audio")
    public ResponseEntity<ByteArrayResource> audio(@PathVariable long id) {
        MapPlace place = places.find(id);
        byte[] content = places.audio(id);

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(place.audioType))
                .header("X-Content-Type-Options", "nosniff").cacheControl(CacheControl.noCache())
                .contentLength(content.length).body(new ByteArrayResource(content));
    }

    @PostMapping(value = "/places", headers = "X-Map-Request=1")
    public MapPlace create(HttpServletRequest request, @RequestParam String title,
            @RequestParam(defaultValue = "") String description, @RequestParam double latitude,
            @RequestParam double longitude, @RequestParam(required = false) MultipartFile audio,
            @RequestParam(required = false) MultipartFile image,
            @RequestParam(required = false) String icon) throws IOException {
        requireAdmin(request);
        return places.save(null, title, description, latitude, longitude, audio, false, image, false, icon);
    }

    @PutMapping(value = "/places/{id}", headers = "X-Map-Request=1")
    public MapPlace update(HttpServletRequest request, @PathVariable long id, @RequestParam String title,
            @RequestParam(defaultValue = "") String description, @RequestParam double latitude,
            @RequestParam double longitude, @RequestParam(required = false) MultipartFile audio,
            @RequestParam(defaultValue = "false") boolean removeAudio,
            @RequestParam(required = false) MultipartFile image,
            @RequestParam(defaultValue = "false") boolean removeImage,
            @RequestParam(required = false) String icon) throws IOException {
        requireAdmin(request);
        return places.save(id, title, description, latitude, longitude, audio, removeAudio, image, removeImage, icon);
    }

    @GetMapping("/places/{id}/image")
    public ResponseEntity<ByteArrayResource> image(@PathVariable long id) {
        byte[] content = places.image(id);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG)
                .header("X-Content-Type-Options", "nosniff").cacheControl(CacheControl.noCache())
                .contentLength(content.length).body(new ByteArrayResource(content));
    }

    @DeleteMapping(value = "/places/{id}", headers = "X-Map-Request=1")
    public void delete(HttpServletRequest request, @PathVariable long id) {
        requireAdmin(request);
        places.delete(id);
    }

    private boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && Boolean.TRUE.equals(session.getAttribute(ADMIN));
    }

    private void requireAdmin(HttpServletRequest request) {
        if (!isAdmin(request)) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Войдите как администратор карты");
    }
}
