package ru.raspisa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.raspisa.entity.Lesson;
import ru.raspisa.entity.Group;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {

    List<Lesson> findByDayOfWeek(DayOfWeek dayOfWeek);

    List<Lesson> findByGroup(Group group);

    List<Lesson> findByStudio_Teacher_Id(Long teacherId);

    List<Lesson> findBySpecialTrue();



    List<Lesson> findBySpecialTrueAndDate(LocalDate date);
}
