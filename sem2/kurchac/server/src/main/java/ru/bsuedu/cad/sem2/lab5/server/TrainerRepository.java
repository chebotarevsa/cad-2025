package ru.bsuedu.cad.sem2.lab5.server;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TrainerRepository extends JpaRepository<Trainer, Long> {

    // Заглушка для поиска тренера по имени
    Optional<Trainer> findByName(String name);

    // Заглушка для поиска тренера по пользователю (User)
    Optional<Trainer> findByUser(User user);
}
