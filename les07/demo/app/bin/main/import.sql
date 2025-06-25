INSERT INTO demo_group (ID, NUMBER, DESCRIPTION) VALUES (1,  12002308, 'гр. 12002308. 1112. Институт инженерных и цифровых технологий, очная форма обучения');
INSERT INTO demo_group (ID, NUMBER, DESCRIPTION) VALUES (2,  12002309, 'гр. 12002309. 1112. Институт инженерных и цифровых технологий, очная форма обучения');

INSERT INTO demo_course (ID, DESCRIPTION) VALUES (1, 'Функциональные компоненты цифровых систем');
INSERT INTO demo_course (ID, DESCRIPTION) VALUES (2, 'Технологии разработки кросс-платформенных приложений на Java и C++');
INSERT INTO demo_course (ID, DESCRIPTION) VALUES (3, 'Информационные системы экологического мониторинга');

INSERT INTO demo_student ( NAME, GROUP_ID) VALUES ( 'Иванов Иван', 1);
INSERT INTO demo_student ( NAME, GROUP_ID) VALUES ( 'Петров Пётр', 1);
INSERT INTO demo_student ( NAME, GROUP_ID) VALUES ( 'Сидорова Мария', 2);
INSERT INTO demo_student ( NAME, GROUP_ID) VALUES ('Алексеева Анна', 2);
INSERT INTO demo_student ( NAME, GROUP_ID) VALUES ( 'Кузнецов Дмитрий', 1);
INSERT INTO demo_student ( NAME, GROUP_ID) VALUES ( 'Фёдорова Елена', 2);
INSERT INTO demo_student ( NAME, GROUP_ID) VALUES ( 'Новиков Алексей', 1);
INSERT INTO demo_student ( NAME, GROUP_ID) VALUES ( 'Смирнова Ольга', 2);
INSERT INTO demo_student ( NAME, GROUP_ID) VALUES ( 'Соколов Игорь', 1);
INSERT INTO demo_student ( NAME, GROUP_ID) VALUES ('Михайлова Дарья', 1);

INSERT INTO demo_group_course (GROUP_ID, COURSE_ID) VALUES (1, 1);
INSERT INTO demo_group_course (GROUP_ID, COURSE_ID) VALUES (2, 2);
INSERT INTO demo_group_course (GROUP_ID, COURSE_ID) VALUES (1, 3);