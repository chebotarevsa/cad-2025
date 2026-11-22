# Лабораторная работа №4
# Взаимодействие Spring и PostgreSQL

## Цель работы
Получить практические навыки работы с Spring Framework: подключение
PostgreSQL, JPA/Hibernate, Spring Data JPA.

## Ход работы
1. Изучен теоретический материал.
2. Приложение из лабораторной №3 переведено с хранения в памяти на PostgreSQL.
3. Выполнено дополнительное задание.

## Запуск
1. Создать в PostgreSQL пустую базу `todo_db` (pgAdmin 4).
2. Пароль пользователя `postgres` задать в переменной окружения `DB_PASSWORD`
   (по умолчанию в `application.properties` стоит `password`).
3. Запустить `Lab3Application`. Таблицы создаются автоматически
   (`spring.jpa.hibernate.ddl-auto=update`).
4. Войти: `user`, `moderator` или `admin`, пароль `password`.

## Дополнительное задание
| Таблица | Поля | Связь |
|---|---|---|
| `tasks` | id, title, description, completed, due_date, **created_at, priority, category**, user_id | N:1 с `users` |
| `sub_tasks` | id, description, completed, task_id | N:1 с `tasks` (у задачи много подзадач) |
| `users` | id, name, email, password (BCrypt), role | 1:N с `tasks`, `login_history` |
| `login_history` | id, login_date, user_id, ip_address | N:1 с `users` |
| `task_history` | id, changed_at, old_state, new_state, task_id | N:1 с `tasks` |

- Время создания задаче ставится автоматически (`@PrePersist`).
- Автор задачи — вошедший пользователь.
- Пользователи хранятся в таблице `users`; пароли в виде хэша BCrypt.
  Роли прежние: USER, MODERATOR, ADMIN (отметка выполненной — MODERATOR/ADMIN,
  удаление — ADMIN).
- Каждый успешный вход пишется в `login_history` (`LoginSuccessHandler`).
- Создание задачи и смена её статуса пишутся в `task_history`.
- При удалении задачи каскадно удаляются её подзадачи и история.

## Вывод
Получены практические навыки работы со Spring Data JPA и PostgreSQL.
