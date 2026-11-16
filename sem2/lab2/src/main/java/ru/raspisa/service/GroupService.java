package ru.raspisa.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.raspisa.dto.GroupDto;
import ru.raspisa.dto.GroupRequest;
import ru.raspisa.entity.Group;
import ru.raspisa.entity.Shift;
import ru.raspisa.repository.GroupRepository;
import ru.raspisa.util.GroupNames;

import java.util.Comparator;
import java.util.List;

@Service
public class GroupService {

    private final GroupRepository repo;
    private final ru.raspisa.repository.AttendanceStudentRepository attendanceStudents;

    public GroupService(GroupRepository repo, ru.raspisa.repository.AttendanceStudentRepository attendanceStudents) {
        this.repo = repo;
        this.attendanceStudents = attendanceStudents;
    }

    @Transactional(readOnly = true)
    public List<GroupDto> list() {
        return repo.findAll().stream()
                .sorted(Comparator.comparing(Group::getNumber, GroupNames.COMPARATOR))
                .map(GroupDto::from)
                .toList();
    }

    @Transactional
    public GroupDto create(GroupRequest req) {
        String number = GroupNames.normalize(req.number());
        if (repo.findByNumber(number) != null) {
            throw new IllegalArgumentException("Группа " + number + " уже существует");
        }
        Group g = new Group();
        g.setNumber(number);
        g.setShift(parseShift(req.shift()));
        return GroupDto.from(repo.save(g));
    }

    @Transactional
    public GroupDto update(Long id, GroupRequest req) {
        Group g = repo.findById(id).orElseThrow();
        String number = GroupNames.normalize(req.number());
        Group other = repo.findByNumber(number);
        if (other != null && !other.getId().equals(id)) {
            throw new IllegalArgumentException("Группа " + number + " уже существует");
        }
        g.setNumber(number);
        g.setShift(parseShift(req.shift()));
        return GroupDto.from(repo.save(g));
    }

    @Transactional
    public void delete(Long id) {
        Group g = repo.findById(id).orElseThrow();
        if (attendanceStudents.existsByGroupId(id)) {
            throw new IllegalArgumentException("Нельзя удалить группу с журналом посещаемости: в нём хранятся ученики и их история.");
        }
        if (g.getLessons() != null && !g.getLessons().isEmpty()) {
            throw new IllegalArgumentException(
                    "Нельзя удалить группу с занятиями (" + g.getLessons().size() + " шт.). Сначала уберите её занятия.");
        }
        repo.delete(g);
    }

    private Shift parseShift(String s) {
        try {
            return Shift.valueOf(s);
        } catch (Exception e) {
            return Shift.MORNING;
        }
    }
}
