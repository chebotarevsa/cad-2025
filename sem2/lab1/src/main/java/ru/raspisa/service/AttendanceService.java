package ru.raspisa.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.raspisa.entity.*;
import ru.raspisa.repository.*;
import java.time.*;
import java.util.*;

@Service
@Transactional
public class AttendanceService {
    private final AttendanceStudentRepository students;
    private final AttendanceMarkRepository marks;
    private final GroupRepository groups;
    private final LessonRepository lessons;
    private final HolidayRepository holidays;
    public AttendanceService(AttendanceStudentRepository students, AttendanceMarkRepository marks,
            GroupRepository groups, LessonRepository lessons, HolidayRepository holidays) {
        this.students = students; this.marks = marks; this.groups = groups;
        this.lessons = lessons; this.holidays = holidays;
    }
    public record Column(Long lessonId, LocalDate date, LocalTime time, String subject, boolean historical) {}
    public record Journal(List<AttendanceStudent> students, List<Column> columns, List<AttendanceMark> marks) {}

    @Transactional(readOnly = true)
    public Journal journal(long groupId, String month) {
        Group group = groups.findById(groupId).orElseThrow();
        YearMonth period;
        try { period = YearMonth.parse(month); } catch (Exception e) { throw new IllegalArgumentException("Выберите месяц"); }
        if (period.getYear() < 2000 || period.getYear() > 2100) throw new IllegalArgumentException("Недопустимый год");
        List<AttendanceMark> saved = marks.findByGroupIdAndLessonDateBetween(groupId, period.atDay(1), period.atEndOfMonth());
        Map<String, Column> columns = new LinkedHashMap<>();
        List<Holiday> breaks = holidays.findAll();
        for (Lesson lesson : lessons.findByGroup(group)) {
            if (lesson.isSpecial()) continue;
            for (LocalDate date = period.atDay(1); !date.isAfter(period.atEndOfMonth()); date = date.plusDays(1)) {
                if (occurs(lesson, date, breaks)) {
                    Column c = new Column(lesson.getId(), date, lesson.getTime(), subject(lesson), false);
                    columns.put(key(c.lessonId(), date), c);
                }
            }
        }

        for (AttendanceMark mark : saved) columns.putIfAbsent(key(mark.lessonId, mark.lessonDate),
                new Column(mark.lessonId, mark.lessonDate, mark.lessonTime, mark.subject, true));
        return new Journal(students.findByGroupIdOrderByNameAsc(groupId),
                columns.values().stream().sorted(Comparator.comparing(Column::date)
                        .thenComparing(Column::time, Comparator.nullsLast(Comparator.naturalOrder()))
                        .thenComparing(Column::lessonId)).toList(), saved);
    }
    private String key(Long id, LocalDate date) { return id + ":" + date; }
    private String subject(Lesson lesson) {
        return lesson.getStudio() != null ? lesson.getStudio().getName() : Objects.toString(lesson.getTitle(), "Занятие");
    }
    public static boolean occurs(Lesson lesson, LocalDate date, List<Holiday> breaks) {
        if (lesson.isSpecial()) return false;
        if (lesson.getDate() != null) return lesson.getDate().equals(date);
        if (lesson.getDayOfWeek() != date.getDayOfWeek()) return false;
        return breaks.stream().noneMatch(h -> {
            if (!h.isYearly()) return !date.isBefore(h.getStartDate()) && !date.isAfter(h.getEndDate());
            MonthDay day = MonthDay.from(date), start = MonthDay.from(h.getStartDate()), end = MonthDay.from(h.getEndDate());
            return start.compareTo(end) <= 0 ? day.compareTo(start) >= 0 && day.compareTo(end) <= 0
                    : day.compareTo(start) >= 0 || day.compareTo(end) <= 0;
        });
    }
    public List<AttendanceStudent> addStudents(long groupId, List<String> names) {
        groups.findById(groupId).orElseThrow();
        if (names == null || names.isEmpty() || names.size() > 100) throw new IllegalArgumentException("Введите от 1 до 100 учеников");
        List<AttendanceStudent> added = new ArrayList<>();
        for (String name : names) {
            AttendanceStudent student = new AttendanceStudent();
            student.groupId = groupId; student.name = validName(name);
            added.add(students.save(student));
        }
        return added;
    }
    private String validName(String name) {
        if (name == null || name.isBlank() || name.strip().length() > 150) throw new IllegalArgumentException("Имя должно содержать от 1 до 150 символов");
        return name.strip();
    }
    public AttendanceStudent updateStudent(long id, String name, boolean archived) {
        AttendanceStudent student = students.findById(id).orElseThrow();
        student.name = validName(name); student.archived = archived;
        return students.save(student);
    }
    public void deleteStudent(long id) {
        AttendanceStudent student = students.findById(id).orElseThrow();
        marks.deleteByStudentId(id);
        students.delete(student);
    }
    public void mark(long studentId, long lessonId, LocalDate date, String value) {
        if (date == null || value == null || !Set.of("", "Н", "П", "У", "Б", "2", "3", "4", "5").contains(value))
            throw new IllegalArgumentException("Выберите Н, П, У, Б или оценку от 2 до 5");
        AttendanceStudent student = students.findById(studentId).orElseThrow();
        AttendanceMark record = marks.findByStudentIdAndLessonIdAndLessonDate(studentId, lessonId, date).orElse(null);
        if (record == null && value.isEmpty()) return;
        if (record == null) {
            if (student.archived) throw new IllegalArgumentException("Ученик находится в архиве");
            Lesson lesson = lessons.findById(lessonId).orElseThrow();
            if (lesson.getGroup() == null || !lesson.getGroup().getId().equals(student.groupId)
                    || !occurs(lesson, date, holidays.findAll())) throw new IllegalArgumentException("В этот день у группы нет такого занятия");
            record = new AttendanceMark(); record.studentId = studentId; record.groupId = student.groupId;
            record.lessonId = lessonId; record.lessonDate = date; record.subject = subject(lesson); record.lessonTime = lesson.getTime();
        }
        if (value.isEmpty()) { marks.delete(record); return; }
        record.mark = value; marks.save(record);
    }
}
