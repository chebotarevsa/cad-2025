package ru.bsuedu.cad.demo.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import ru.bsuedu.cad.demo.entity.Student;

@Repository("jpaStudentRepository")
public class StudentJpaRepository implements StudentRepository {

    @PersistenceContext
    private EntityManager entityManager;

    // Сохранение нового студента
    @Override
    public void save(Student student) {
        entityManager.persist(student);
    }

    // Получение студента по id
    @Override
    public Student findById(Long id) {
        return entityManager.find(Student.class, id);
    }

    // Обновление студента
    @Transactional
    @Override
    public void update(Student student) {
        entityManager.merge(student);
    }

    // Удаление студента
    @Transactional
    @Override
    public void delete(Student student) {
        entityManager.remove(
            entityManager.contains(student) ? student : entityManager.merge(student));
    }

    // Поиск студентов по имени с использованием JPQL
    @Override
    public List<Student> findByName(String name) {
        return entityManager.createQuery("FROM Student s WHERE s.name = :name", Student.class)
                            .setParameter("name", name)
                            .getResultList();
    }

}


