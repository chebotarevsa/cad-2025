package ru.bsuedu.cad.demo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.bsuedu.cad.demo.entity.Student;

@Repository
public interface StudentRepository  extends JpaRepository<Student, Long>{
    List<Student> findByName(String name);
    Page<Student> findByNameContaining(String name, Pageable pageable);
}
