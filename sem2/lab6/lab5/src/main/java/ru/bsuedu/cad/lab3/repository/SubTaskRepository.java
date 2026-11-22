package ru.bsuedu.cad.lab3.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.bsuedu.cad.lab3.model.SubTask;

public interface SubTaskRepository extends JpaRepository<SubTask, Long> {
}
