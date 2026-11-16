package ru.raspisa.config;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.raspisa.dto.LessonRequest;
import ru.raspisa.entity.Lesson;
import ru.raspisa.repository.LessonRepository;

@Component("eventAccess")
public class EventAccess {
    private final LessonRepository lessons;

    public EventAccess(LessonRepository lessons) {
        this.lessons = lessons;
    }

    private boolean hasRole(String role) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_" + role));
    }

    public boolean canCreate(LessonRequest request) {
        return hasRole("ADMIN") || hasRole("METHODIST") && Boolean.TRUE.equals(request.special());
    }

    public boolean canUpdate(Long id, LessonRequest request) {
        return hasRole("ADMIN") || hasRole("METHODIST") && Boolean.TRUE.equals(request.special())
                && lessons.findById(id).map(Lesson::isSpecial).orElse(false);
    }

    public boolean canDelete(Long id) {
        return hasRole("ADMIN") || hasRole("METHODIST")
                && lessons.findById(id).map(Lesson::isSpecial).orElse(false);
    }
}
