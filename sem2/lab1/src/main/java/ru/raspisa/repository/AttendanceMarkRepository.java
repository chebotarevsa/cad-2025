package ru.raspisa.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.raspisa.entity.AttendanceMark;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
public interface AttendanceMarkRepository extends JpaRepository<AttendanceMark, Long> {
    void deleteByStudentId(Long studentId);
    List<AttendanceMark> findByGroupIdAndLessonDateBetween(Long groupId, LocalDate start, LocalDate end);
    Optional<AttendanceMark> findByStudentIdAndLessonIdAndLessonDate(Long studentId, Long lessonId, LocalDate date);
}
