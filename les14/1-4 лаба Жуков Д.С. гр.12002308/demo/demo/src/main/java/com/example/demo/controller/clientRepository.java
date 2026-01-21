package com.example.demo.controller;

import com.example.demo.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface clientRepository extends JpaRepository<Client, Long> {

    // Поиск по фамилии (без учета регистра, частичное совпадение)
    @Query("SELECT c FROM Client c WHERE LOWER(c.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))")
    List<Client> findByLastNameContainingIgnoreCase(@Param("lastName") String lastName);

    // Поиск по имени (без учета регистра, частичное совпадение)
    @Query("SELECT c FROM Client c WHERE LOWER(c.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))")
    List<Client> findByFirstNameContainingIgnoreCase(@Param("firstName") String firstName);

    // Поиск по телефону (частичное совпадение)
    @Query("SELECT c FROM Client c WHERE c.phone LIKE CONCAT('%', :phone, '%')")
    List<Client> findByPhoneContaining(@Param("phone") String phone);

    // Комбинированный поиск по фамилии и имени
    @Query("SELECT c FROM Client c WHERE LOWER(c.lastName) LIKE LOWER(CONCAT('%', :lastName, '%')) " +
            "AND LOWER(c.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))")
    List<Client> findByLastNameAndFirstNameContainingIgnoreCase(
            @Param("lastName") String lastName,
            @Param("firstName") String firstName);
}
