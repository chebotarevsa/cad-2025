package ru.raspisa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.raspisa.entity.MapPlace;

public interface MapPlaceRepository extends JpaRepository<MapPlace, Long> {}
