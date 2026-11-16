package ru.raspisa.dto;

import ru.raspisa.entity.Group;
import ru.raspisa.entity.Shift;

public record GroupDto(Long id, String number, String shift) {

    public static GroupDto from(Group g) {
        Shift s = g.getShift() == null ? Shift.MORNING : g.getShift();
        return new GroupDto(g.getId(), g.getNumber(), s.name());
    }
}
