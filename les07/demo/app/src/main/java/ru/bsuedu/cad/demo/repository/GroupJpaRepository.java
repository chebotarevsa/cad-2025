package ru.bsuedu.cad.demo.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import ru.bsuedu.cad.demo.entity.Group;

@Repository("jpaGroupRepository")
public class GroupJpaRepository implements GroupRepository{

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public void save(Group group) {
        entityManager.persist(group);
    }

    @Override
    public Group findById(Long id) {
        return entityManager.find(Group.class, id);
    }

    @Transactional
    @Override
    public void update(Group group) {
        entityManager.merge(group);
    }

    @Transactional
    @Override
    public void delete(Group group) {
        entityManager.remove(entityManager.contains(group) ? group : entityManager.merge(group));
    }

    @Override
    public List<Group> findByNumber(int number) {
        return entityManager.createQuery("FROM Group g WHERE g.number = :number", Group.class)
                            .setParameter("number", number)
                            .getResultList();
    }

}