package ru.raspisa.web;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.raspisa.dto.GroupDto;
import ru.raspisa.dto.GroupRequest;
import ru.raspisa.entity.Group;
import ru.raspisa.repository.GroupRepository;
import ru.raspisa.service.GroupService;
import ru.raspisa.util.GroupNames;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    private final GroupRepository groupRepository;
    private final GroupService groupService;

    public GroupController(GroupRepository groupRepository, GroupService groupService) {
        this.groupRepository = groupRepository;
        this.groupService = groupService;
    }

    @GetMapping
    public List<String> numbers() {
        return groupRepository.findAll().stream()
                .map(Group::getNumber)
                .sorted(GroupNames.COMPARATOR)
                .toList();
    }

    @GetMapping("/list")
    public List<GroupDto> list() {
        return groupService.list();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GroupDto create(@Valid @RequestBody GroupRequest req) {
        return groupService.create(req);
    }

    @PutMapping("/{id}")
    public GroupDto update(@PathVariable Long id, @Valid @RequestBody GroupRequest req) {
        return groupService.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        groupService.delete(id);
    }
}
