package ru.bsuedu.cad.lab3.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import ru.bsuedu.cad.lab3.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

    /** Задачи сразу с автором и подзадачами (один запрос вместо N+1). */
    @EntityGraph(attributePaths = {"owner", "subTasks"})
    List<Task> findAllByOrderByCreatedAtDesc();
}
