package ru.raspisa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.raspisa.entity.Studio;

public interface StudioRepository extends JpaRepository<Studio, Long> {

    Studio findByCode(String code);
}
