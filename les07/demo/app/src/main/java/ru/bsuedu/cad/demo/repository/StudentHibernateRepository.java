package ru.bsuedu.cad.demo.repository;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import ru.bsuedu.cad.demo.entity.Student;

@Repository("hibernateStudentRepository")
public class StudentHibernateRepository implements StudentRepository  {

    private SessionFactory sessionFactory;

    public StudentHibernateRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void save(Student student) {
        Session session = sessionFactory.getCurrentSession();
            //Transaction tx = session.beginTransaction();
            session.persist(student);

    }

    public Student findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Student.class, id);
        }
    }

    public void update(Student student) {
        try (Session session = sessionFactory.openSession()) {
            //Transaction tx = session.beginTransaction();
            session.merge(student);
            //tx.commit();
        }
    }

    public void delete(Student student) {
        try (Session session = sessionFactory.openSession()) {
            //Transaction tx = session.beginTransaction();
            session.remove(student);
            //tx.commit();
        }
    }

    public List<Student> findByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Student s WHERE s.name = :name", Student.class)
                          .setParameter("name", name)
                          .getResultList();
        }
    }
}
