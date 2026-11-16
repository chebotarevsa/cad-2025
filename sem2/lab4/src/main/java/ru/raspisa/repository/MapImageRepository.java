package ru.raspisa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.raspisa.entity.MapImage;

public interface MapImageRepository extends JpaRepository<MapImage, Long> {}
