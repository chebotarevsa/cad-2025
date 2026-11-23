SELECT current_database(), version();

SELECT table_name FROM information_schema.tables
WHERE table_schema = 'public' ORDER BY table_name;

SELECT t.id, t.title, t.created_at, t.priority, t.category, t.status,
       u.login, COUNT(s.id) AS subtasks
FROM tasks t
JOIN users u ON u.id = t.user_id
LEFT JOIN sub_task s ON s.task_id = t.id
WHERE NOT t.deleted
GROUP BY t.id, u.login
ORDER BY t.id;

SELECT id, description, completed, task_id FROM sub_task ORDER BY id;

SELECT h.id, h.logged_at, u.login, h.ip_address
FROM login_history h JOIN users u ON u.id = h.user_id
ORDER BY h.logged_at DESC;

SELECT h.id, h.changed_at, h.action, u.login, h.old_state, h.new_state
FROM task_history h JOIN users u ON u.id = h.user_id
WHERE h.task_id = 1 ORDER BY h.id;

SELECT id, login, full_name, email, role FROM users ORDER BY id;
