package ru.raspisa.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.raspisa.entity.LoginHistory;
import ru.raspisa.repository.LoginHistoryRepository;
import ru.raspisa.repository.UserRepository;
import java.time.Instant;
import java.util.List;

@Service
@Transactional
public class LoginHistoryService {
    private final LoginHistoryRepository history;
    private final UserRepository users;
    public LoginHistoryService(LoginHistoryRepository history, UserRepository users) {
        this.history = history;
        this.users = users;
    }

    public void record(String login, String ipAddress) {
        var entry = new LoginHistory();
        entry.user = users.findByLogin(login);
        entry.ipAddress = ipAddress == null ? "unknown" : ipAddress;
        history.save(entry);
    }

    @Transactional(readOnly = true)
    public List<Entry> list() {
        return history.findTop50ByOrderByLoggedAtDescIdDesc().stream()
                .map(h -> new Entry(h.id, h.loggedAt, h.user.getLogin(), h.ipAddress)).toList();
    }

    public record Entry(Long id, Instant loggedAt, String login, String ipAddress) {}
}
