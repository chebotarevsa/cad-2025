package ru.bsuedu.cad_2025;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EmployeeHistoryRepository extends JpaRepository<EmployeeHistory, Long> {

    List<EmployeeHistory> findByEmployeeIdOrderByTimestampDesc(Long employeeId);
}
