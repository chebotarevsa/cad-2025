package ru.raspisa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.raspisa.entity.Group;
import ru.raspisa.util.GroupNames;

public interface GroupRepository extends JpaRepository<Group, Long> {

    default Group findByNumber(String number) {
        String normalized = GroupNames.normalize(number);
        return findAll().stream()
                .filter(group -> normalized != null && normalized.equals(GroupNames.normalize(group.getNumber())))
                .findFirst()
                .orElse(null);
    }
}
