package ru.raspisa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.raspisa.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByLogin(String login);
}
