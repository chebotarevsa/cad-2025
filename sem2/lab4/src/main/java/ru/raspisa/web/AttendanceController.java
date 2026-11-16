package ru.raspisa.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.raspisa.entity.AttendanceStudent;
import ru.raspisa.service.AttendanceService;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {
    private final AttendanceService service;
    public AttendanceController(AttendanceService service) { this.service = service; }
    private void requireLogin(HttpServletRequest request) {
        var session = request.getSession(false);
        if (session == null || !Boolean.TRUE.equals(session.getAttribute("schoolStaff")))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Войдите заново, чтобы открыть журнал");
    }
    @GetMapping
    public ResponseEntity<AttendanceService.Journal> journal(HttpServletRequest request, @RequestParam long groupId, @RequestParam String month) {
        requireLogin(request);
        return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(service.journal(groupId, month));
    }
    public record Names(List<String> names) {}
    public record StudentUpdate(String name, boolean archived) {}
    public record Mark(long studentId, long lessonId, LocalDate date, String value) {}
    @PostMapping(value = "/students", headers = "X-Attendance-Request=1")
    public List<AttendanceStudent> add(HttpServletRequest request, @RequestParam long groupId, @RequestBody Names body) {
        requireLogin(request); return service.addStudents(groupId, body.names());
    }
    @PutMapping(value = "/students/{id}", headers = "X-Attendance-Request=1")
    public AttendanceStudent update(HttpServletRequest request, @PathVariable long id, @RequestBody StudentUpdate body) {
        requireLogin(request); return service.updateStudent(id, body.name(), body.archived());
    }
    @DeleteMapping(value = "/students/{id}", headers = "X-Attendance-Request=1")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(HttpServletRequest request, @PathVariable long id) {
        requireLogin(request); service.deleteStudent(id);
    }
    @PutMapping(value = "/marks", headers = "X-Attendance-Request=1")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void mark(HttpServletRequest request, @RequestBody Mark body) {
        requireLogin(request); service.mark(body.studentId(), body.lessonId(), body.date(), body.value());
    }
}
