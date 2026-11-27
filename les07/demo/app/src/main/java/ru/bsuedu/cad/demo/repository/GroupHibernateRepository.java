package ru.bsuedu.cad.demo.repository;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import ru.bsuedu.cad.demo.entity.Group;

@Repository("hibernateGroupRepository")
public class GroupHibernateRepository  implements GroupRepository{

    private SessionFactory sessionFactory;

    public GroupHibernateRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(Group group) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(group);
            tx.commit();
        }
    }

    @Override
    public Group findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Group.class, id);
        }
    }

    @Override
    public void delete(Group group) {
        try (Session session = sessionFactory.openSession()) {
            //Transaction tx = session.beginTransaction();
            session.remove(group);
            //tx.commit();
        }
    }

    @Override
    public void update(Group student) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(student);
            tx.commit();
        }
    }

    @Override
    public List<Group> findByNumber(int number) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Group g WHERE g.number = :number", Group.class)
                          .setParameter("number", number)
                          .getResultList();
        }
    }

}