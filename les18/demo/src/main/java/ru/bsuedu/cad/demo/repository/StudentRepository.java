package ru.bsuedu.cad.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.bsuedu.cad.demo.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {
    
}
