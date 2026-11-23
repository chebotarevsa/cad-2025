package ru.raspisa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.raspisa.entity.LoginHistory;
import java.util.List;

public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long> {
    List<LoginHistory> findTop50ByOrderByLoggedAtDescIdDesc();
}
