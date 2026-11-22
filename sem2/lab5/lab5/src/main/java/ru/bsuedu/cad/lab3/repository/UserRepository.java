package ru.bsuedu.cad.lab3.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.bsuedu.cad.lab3.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByName(String name);
}
