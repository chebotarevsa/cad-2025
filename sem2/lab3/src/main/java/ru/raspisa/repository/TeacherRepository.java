package ru.raspisa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.raspisa.entity.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
}
