package ru.raspisa.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.raspisa.entity.*;
import ru.raspisa.repository.*;
import java.time.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {"spring.datasource.url=jdbc:h2:mem:attendance-test;DB_CLOSE_DELAY=-1", "spring.jpa.show-sql=false", "spring.jpa.hibernate.ddl-auto=create-drop"})
@Transactional
class AttendanceServiceTest {
    @Autowired AttendanceService service;
    @Autowired GroupService groupService;
    @Autowired GroupRepository groups;
    @Autowired LessonRepository lessons;
    @Autowired HolidayRepository holidays;
    @Autowired AttendanceStudentRepository students;
    @Autowired AttendanceMarkRepository marks;

    private Lesson lesson() {
        Group group = new Group(); group.setNumber("journal-test"); groups.save(group);
        Lesson lesson = new Lesson(); lesson.setGroup(group); lesson.setDayOfWeek(DayOfWeek.MONDAY);
        lesson.setTime(LocalTime.of(10, 0)); lesson.setTitle("Тестовое занятие");
        return lessons.save(lesson);
    }
    @Test void generatesDatesSkipsBreaksAndKeepsTwoLessonsOnSameDay() {
        Lesson lesson = lesson();
        Lesson second = new Lesson(); second.setGroup(lesson.getGroup()); second.setDayOfWeek(DayOfWeek.MONDAY);
        second.setTime(LocalTime.of(11, 0)); lessons.save(second);
        Holiday holiday = new Holiday(); holiday.setType(HolidayType.HOLIDAY);
        holiday.setStartDate(LocalDate.of(2026, 9, 14)); holiday.setEndDate(holiday.getStartDate()); holidays.save(holiday);
        var journal = service.journal(lesson.getGroup().getId(), "2026-09");
        assertEquals(6, journal.columns().size());
        assertTrue(journal.columns().stream().noneMatch(c -> c.date().getDayOfMonth() == 14));
    }
    @Test void savesUpdatesClearsAndRetainsHistoryAfterLessonDeletion() {
        Lesson lesson = lesson(); long groupId = lesson.getGroup().getId();
        var student = service.addStudents(groupId, List.of("Иванов Иван")).get(0);
        LocalDate date = LocalDate.of(2026, 9, 7);
        service.mark(student.id, lesson.getId(), date, "Н");
        service.mark(student.id, lesson.getId(), date, "5");
        assertEquals(1, service.journal(groupId, "2026-09").marks().size());
        assertEquals("5", service.journal(groupId, "2026-09").marks().get(0).mark);
        lessons.delete(lesson); lessons.flush();
        service.updateStudent(student.id, student.name, true);
        var historical = service.journal(groupId, "2026-09");
        assertEquals(1, historical.students().size());
        assertTrue(historical.columns().get(0).historical());
        service.mark(student.id, lesson.getId(), date, "");
        assertTrue(service.journal(groupId, "2026-09").marks().isEmpty());
    }
    @Test void rejectsWrongGroupWrongDateAndInvalidMarks() {
        Lesson lesson = lesson();
        var student = service.addStudents(lesson.getGroup().getId(), List.of("Ученица")).get(0);
        assertThrows(IllegalArgumentException.class, () -> service.mark(student.id, lesson.getId(), LocalDate.of(2026, 9, 8), "П"));
        assertThrows(IllegalArgumentException.class, () -> service.mark(student.id, lesson.getId(), LocalDate.of(2026, 9, 7), "1"));
        assertThrows(IllegalArgumentException.class, () -> service.mark(student.id, lesson.getId(), LocalDate.of(2026, 9, 7), "6"));
        Group other = new Group(); other.setNumber("other-test"); groups.save(other);
        var foreign = service.addStudents(other.getId(), List.of("Другая группа")).get(0);
        assertThrows(IllegalArgumentException.class, () -> service.mark(foreign.id, lesson.getId(), LocalDate.of(2026, 9, 7), "П"));
        assertTrue(marks.findByGroupIdAndLessonDateBetween(other.getId(), LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 30)).isEmpty());
    }
    @Test void handlesYearlyWinterBreakAndExplicitDates() {
        Lesson lesson = new Lesson(); lesson.setDayOfWeek(DayOfWeek.MONDAY);
        Holiday vacation = new Holiday(); vacation.setType(HolidayType.VACATION); vacation.setYearly(true);
        vacation.setStartDate(LocalDate.of(2025, 12, 30)); vacation.setEndDate(LocalDate.of(2026, 1, 10));
        assertFalse(AttendanceService.occurs(lesson, LocalDate.of(2027, 1, 4), List.of(vacation)));
        assertTrue(AttendanceService.occurs(lesson, LocalDate.of(2027, 1, 11), List.of(vacation)));
        lesson.setDate(LocalDate.of(2027, 1, 5));
        assertTrue(AttendanceService.occurs(lesson, LocalDate.of(2027, 1, 5), List.of(vacation)));
        assertFalse(AttendanceService.occurs(lesson, LocalDate.of(2027, 1, 12), List.of()));
    }
    @Test void deletesOnlySelectedStudentAndTheirMarks() {
        Lesson lesson = lesson(); long groupId = lesson.getGroup().getId();
        var roster = service.addStudents(groupId, List.of("Удаляемый", "Остающийся"));
        LocalDate date = LocalDate.of(2026, 9, 7);
        service.mark(roster.get(0).id, lesson.getId(), date, "П");
        service.mark(roster.get(1).id, lesson.getId(), date, "5");
        service.deleteStudent(roster.get(0).id);
        students.flush(); marks.flush();
        var journal = service.journal(groupId, "2026-09");
        assertEquals(1, journal.students().size());
        assertEquals(roster.get(1).id, journal.students().get(0).id);
        assertEquals(1, journal.marks().size());
        assertEquals("5", journal.marks().get(0).mark);
    }
    @Test void cannotDeleteGroupWithJournal() {
        Group group = new Group(); group.setNumber("protected-journal"); groups.save(group);
        service.addStudents(group.getId(), List.of("Ученик"));
        assertThrows(IllegalArgumentException.class, () -> groupService.delete(group.getId()));
    }
}
