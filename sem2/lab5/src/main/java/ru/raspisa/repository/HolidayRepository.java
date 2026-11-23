package ru.raspisa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.raspisa.entity.Holiday;

public interface HolidayRepository extends JpaRepository<Holiday, Long> {
}
