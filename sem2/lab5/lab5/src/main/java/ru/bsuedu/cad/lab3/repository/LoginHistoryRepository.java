package ru.bsuedu.cad.lab3.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.bsuedu.cad.lab3.model.LoginHistory;

public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long> {
}
