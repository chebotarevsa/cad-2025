package ru.bsuedu.cad.demo.repository;

import java.util.List;

import ru.bsuedu.cad.demo.entity.Group;

public interface GroupRepository  {
    void save(Group student);
    Group findById(Long id);
    void update(Group group);
    void delete(Group group);
    List<Group> findByNumber(int number);
}
