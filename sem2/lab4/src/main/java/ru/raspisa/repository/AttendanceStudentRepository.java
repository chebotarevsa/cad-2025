package ru.raspisa.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.raspisa.entity.AttendanceStudent;
import java.util.List;
public interface AttendanceStudentRepository extends JpaRepository<AttendanceStudent, Long> {
    boolean existsByGroupId(Long groupId);
    List<AttendanceStudent> findByGroupIdOrderByNameAsc(Long groupId);
}
