package ru.raspisa.seed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.raspisa.entity.Group;
import ru.raspisa.entity.Holiday;
import ru.raspisa.entity.HolidayType;
import ru.raspisa.entity.Lesson;
import ru.raspisa.entity.Role;
import ru.raspisa.entity.Shift;
import ru.raspisa.entity.Studio;
import ru.raspisa.entity.Teacher;
import ru.raspisa.entity.User;
import ru.raspisa.repository.GroupRepository;
import ru.raspisa.repository.HolidayRepository;
import ru.raspisa.repository.LessonRepository;
import ru.raspisa.repository.StudioRepository;
import ru.raspisa.repository.TeacherRepository;
import ru.raspisa.repository.UserRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private static final String ADMIN_LOGIN = "admin";
    private static final String ADMIN_PASSWORD = "Raspisa!2026";

    private final StudioRepository studioRepository;
    private final TeacherRepository teacherRepository;
    private final GroupRepository groupRepository;
    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;
    private final HolidayRepository holidayRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(StudioRepository studioRepository,
                      TeacherRepository teacherRepository,
                      GroupRepository groupRepository,
                      LessonRepository lessonRepository,
                      UserRepository userRepository,
                      HolidayRepository holidayRepository,
                      PasswordEncoder passwordEncoder) {
        this.studioRepository = studioRepository;
        this.teacherRepository = teacherRepository;
        this.groupRepository = groupRepository;
        this.lessonRepository = lessonRepository;
        this.userRepository = userRepository;
        this.holidayRepository = holidayRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        seedReferenceData();
        seedExtraStudios();
        loadLessons();
        seedUsers();
        seedHolidays();
        backfillGroupShifts();
    }

    private void backfillGroupShifts() {
        var groups = groupRepository.findAll().stream()
                .filter(g -> g.getShift() == null)
                .toList();
        if (groups.isEmpty()) return;
        groups.forEach(g -> {
            int n;
            try {
                n = Integer.parseInt(g.getNumber());
            } catch (Exception ignored) {
                n = 0;
            }
            g.setShift(n <= 5 ? Shift.MORNING : Shift.AFTERNOON);
        });
        groupRepository.saveAll(groups);
        log.info("Проставлены смены для {} групп (1–5 утро, 6+ день).", groups.size());
    }

    private void seedHolidays() {
        if (holidayRepository.count() > 0) {
            log.info("Календарь праздников уже наполнен ({}) — пропускаю.", holidayRepository.count());
            return;
        }
        log.info("Наполняю календарь праздников и каникул...");

        saveHoliday(HolidayType.HOLIDAY, "День народного единства", "2025-11-04", "2025-11-04", true);
        saveHoliday(HolidayType.HOLIDAY, "Рождество Христово",      "2026-01-07", "2026-01-07", true);
        saveHoliday(HolidayType.HOLIDAY, "День защитника Отечества","2026-02-23", "2026-02-23", true);
        saveHoliday(HolidayType.HOLIDAY, "Международный женский день","2026-03-08", "2026-03-08", true);
        saveHoliday(HolidayType.HOLIDAY, "Праздник Весны и Труда",  "2026-05-01", "2026-05-01", true);
        saveHoliday(HolidayType.HOLIDAY, "День Победы",             "2026-05-09", "2026-05-09", true);
        saveHoliday(HolidayType.HOLIDAY, "День России",            "2026-06-12", "2026-06-12", true);

        saveHoliday(HolidayType.VACATION, "Осенние каникулы", "2025-10-26", "2025-11-04", false);
        saveHoliday(HolidayType.VACATION, "Зимние каникулы",  "2025-12-31", "2026-01-11", false);
        saveHoliday(HolidayType.VACATION, "Весенние каникулы","2026-03-29", "2026-04-05", false);
        saveHoliday(HolidayType.VACATION, "Летние каникулы",  "2026-06-01", "2026-08-31", false);

        log.info("Календарь готов: {} записей.", holidayRepository.count());
    }

    private void saveHoliday(HolidayType type, String name, String start, String end, boolean yearly) {
        Holiday h = new Holiday();
        h.setType(type);
        h.setName(name);
        h.setStartDate(LocalDate.parse(start));
        h.setEndDate(LocalDate.parse(end));
        h.setYearly(yearly);
        holidayRepository.save(h);
    }

    private void seedExtraStudios() {
        ensureStudio("Лекторий", "ЛК", "Лекторий ШКИ");
        ensureStudio("Северсталь", "СВ", "Студия «Северсталь»");
    }

    private void ensureStudio(String oldCode, String newCode, String newName) {
        Studio s = studioRepository.findByCode(newCode);
        if (s == null) s = studioRepository.findByCode(oldCode);
        if (s == null) s = new Studio();
        s.setCode(newCode);
        s.setName(newName);
        studioRepository.save(s);
        log.info("Студия готова: {} ({}).", newCode, newName);
    }

    private void seedReferenceData() {
        if (studioRepository.count() > 0) {
            log.info("Справочники уже есть ({} студий) — пропускаю.", studioRepository.count());
            return;
        }
        log.info("Наполняю справочники (студии, преподы, группы)...");

        Studio fv = saveStudio("ФВ", "Фото-видео производство");
        Studio vr = saveStudio("ВР", "Интерактивные цифровые технологии VR и AR");
        Studio zv = saveStudio("ЗВ", "Звукорежиссура");
        Studio an = saveStudio("АН", "Анимация и 3D графика");
        Studio dz = saveStudio("ДЗ", "Дизайн");
        Studio el = saveStudio("ЭЛ", "Электронная музыка");

        List<Teacher> teachers = List.of(
                saveTeacher("Польшин Никита Сергеевич",     LocalDate.of(1990, 3, 12), "+7-900-100-10-01", fv),
                saveTeacher("Блинов Кирилл Игоревич",       LocalDate.of(1985, 7, 24), "+7-900-100-10-02", vr),
                saveTeacher("Блинов Никита Игоревич",       LocalDate.of(1992, 11, 5), "+7-900-100-10-03", zv),
                saveTeacher("Солдатов Николай Дмитриевич",  LocalDate.of(1988, 1, 30), "+7-900-100-10-04", an),
                saveTeacher("Кирьянова Дарья Денисовна",    LocalDate.of(1995, 9, 18), "+7-900-100-10-05", dz),
                saveTeacher("Александр Александрович Спицын", LocalDate.of(1983, 5, 9), "+7-900-100-10-06", el)
        );

        for (int number = 1; number <= 13; number++) {
            saveGroup(number, teachers.get((number - 1) % teachers.size()));
        }

        log.info("Справочники готовы: студий={}, преподов={}, групп={}.",
                studioRepository.count(), teacherRepository.count(), groupRepository.count());
    }

    private void loadLessons() {
        if (lessonRepository.count() > 0) {
            log.info("Занятия уже загружены ({}) — пропускаю.", lessonRepository.count());
            return;
        }
        log.info("Загружаю занятия из data/lessons.csv...");

        int loaded = 0, skipped = 0;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new ClassPathResource("data/lessons.csv").getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            boolean headerSkipped = false;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                if (!headerSkipped) { headerSkipped = true; continue; }

                String[] f = line.split(",");
                DayOfWeek day = DayOfWeek.valueOf(f[0].trim());
                LocalTime time = LocalTime.parse(f[1].trim());
                int order = Integer.parseInt(f[2].trim());
                String groupNumber = f[3].trim();
                String studioCode = f[4].trim();

                Group group = groupRepository.findByNumber(groupNumber);
                Studio studio = studioRepository.findByCode(studioCode);
                if (group == null || studio == null) {
                    log.warn("Пропуск строки (нет группы {} или студии {}): {}", groupNumber, studioCode, line);
                    skipped++;
                    continue;
                }

                Lesson lesson = new Lesson();
                lesson.setDayOfWeek(day);
                lesson.setTime(time);
                lesson.setOrderNumber(order);
                lesson.setGroup(group);
                lesson.setStudio(studio);
                lessonRepository.save(lesson);
                loaded++;
            }
        } catch (Exception e) {
            log.error("Ошибка загрузки занятий из CSV", e);
        }

        log.info("Занятия загружены: {} (пропущено {}).", loaded, skipped);
    }

    private void seedUsers() {
        User admin = userRepository.findByLogin(ADMIN_LOGIN);

        if (admin == null) {
            admin = new User();
            admin.setLogin(ADMIN_LOGIN);
            admin.setRole(Role.ADMIN);
            admin.setFullName("Администратор");
            admin.setPasswordHash(passwordEncoder.encode(ADMIN_PASSWORD));
            userRepository.save(admin);
            log.info("Создан пользователь admin (роль ADMIN).");
            return;
        }

        if (!passwordEncoder.matches(ADMIN_PASSWORD, admin.getPasswordHash())) {
            admin.setPasswordHash(passwordEncoder.encode(ADMIN_PASSWORD));
            userRepository.save(admin);
            log.info("Пароль пользователя admin обновлён до актуального.");
        } else {
            log.info("Пользователь admin актуален — пропускаю.");
        }
    }

    private Studio saveStudio(String code, String name) {
        Studio s = new Studio();
        s.setCode(code);
        s.setName(name);
        return studioRepository.save(s);
    }

    private Teacher saveTeacher(String fullName, LocalDate birthDate, String phone, Studio studio) {
        Teacher t = new Teacher();
        t.setFullName(fullName);
        t.setBirthDate(birthDate);
        t.setPhone(phone);
        t.setStudio(studio);
        return teacherRepository.save(t);
    }

    private Group saveGroup(int number, Teacher curator) {
        Group g = new Group();
        g.setNumber(String.valueOf(number));
        g.setCurator(curator);
        return groupRepository.save(g);
    }
}
