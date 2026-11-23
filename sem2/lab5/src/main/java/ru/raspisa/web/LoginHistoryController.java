package ru.raspisa.web;

import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.raspisa.service.LoginHistoryService;
import java.util.List;

@RestController
@RequestMapping("/api/login-history")
@PreAuthorize("hasRole('ADMIN')")
public class LoginHistoryController {
    private final LoginHistoryService history;
    public LoginHistoryController(LoginHistoryService history) { this.history = history; }
    @GetMapping
    public ResponseEntity<List<LoginHistoryService.Entry>> list() {
        return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(history.list());
    }
}
