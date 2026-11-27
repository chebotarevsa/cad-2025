package ru.bsuedu.cad.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ru.bsuedu.cad.demo.entity.Group;

@Repository
public interface GroupRepository  extends CrudRepository<Group, Long> {
    List<Group> findByNumber(int number);

    List<Group> searchByNumber(int number);

    List<Group> getByNumber(int number);


    @Query("SELECT g FROM Group g WHERE g.description  LIKE %:text%")
    List<Group> searchByDescription(@Param("text") String text );
}
