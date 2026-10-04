package ru.bsuedu.cad.lab5.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.bsuedu.cad.lab5.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
