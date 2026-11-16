import { useEffect, useState } from 'react'
import { apiFetch } from '../../http'
import './EventTasks.css'

const empty = () => ({ title: '', description: '', priority: 'NORMAL', category: 'Подготовка мероприятия', status: 'TODO' })
const statuses = { TODO: 'Новая', IN_PROGRESS: 'В работе', DONE: 'Готово' }
const priorities = { LOW: 'Низкий', NORMAL: 'Обычный', HIGH: 'Высокий' }
const actions = { CREATE: 'Создание', UPDATE: 'Изменение', DELETE: 'Удаление', SUBTASK_ADD: 'Добавление подзадачи', SUBTASK_UPDATE: 'Изменение подзадачи', SUBTASK_DELETE: 'Удаление подзадачи' }
const date = (value) => new Date(value).toLocaleString('ru-RU')

async function request(url, method = 'GET', body) {
  const response = await apiFetch(url, {
    method,
    ...(body === undefined ? {} : { headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(body) }),
  })
  if (!response.ok) {
    const data = await response.json().catch(() => ({}))
    throw new Error(data.error || 'Не удалось выполнить запрос')
  }
  return response.status === 204 ? null : response.json()
}

export default function EventTasks() {
  const [tasks, setTasks] = useState([])
  const [selected, setSelected] = useState(null)
  const [form, setForm] = useState(empty)
  const [subtask, setSubtask] = useState('')
  const [history, setHistory] = useState(null)
  const [busy, setBusy] = useState(false)
  const [message, setMessage] = useState('')
  const [error, setError] = useState('')

  useEffect(() => {
    let active = true
    request('/api/tasks').then((data) => { if (active) setTasks(data) }).catch((e) => { if (active) setError(e.message) })
    return () => { active = false }
  }, [])

  function choose(task) {
    setSelected(task)
    setForm(task ? { title: task.title, description: task.description, priority: task.priority, category: task.category, status: task.status } : empty())
    setHistory(null)
    setMessage('')
    setError('')
    setSubtask('')
  }

  async function run(action, success) {
    setBusy(true)
    setError('')
    setMessage('')
    try {
      await action()
      setTasks(await request('/api/tasks'))
      setMessage(success)
    } catch (e) {
      setError(e.message)
    } finally {
      setBusy(false)
    }
  }

  function accept(task) {
    setSelected(task)
    setHistory(null)
  }

  async function save(event) {
    event.preventDefault()
    await run(async () => {
      const task = await request(selected ? `/api/tasks/${selected.id}` : '/api/tasks', selected ? 'PUT' : 'POST', form)
      accept(task)
    }, 'Задача сохранена')
  }

  const field = (name) => ({ value: form[name], onChange: (event) => setForm({ ...form, [name]: event.target.value }) })

  return (
    <div className="event-tasks">
      <div className="task-toolbar">
        <p>Задачи подготовки мероприятий: {tasks.length}</p>
        <button type="button" onClick={() => choose(null)} disabled={busy}>Новая задача</button>
      </div>
      {error && <p role="alert" className="task-error">{error}</p>}
      {message && <p role="status" className="task-success">{message}</p>}
      <div className="task-columns">
        <div className="task-list">
          {tasks.length === 0 && <p>Задач пока нет</p>}
          {tasks.map((task) => (
            <button type="button" key={task.id} className={'task-card ' + (selected?.id === task.id ? 'selected' : '')} onClick={() => choose(task)} disabled={busy}>
              <strong>{task.title}</strong>
              <span>{statuses[task.status]} · {priorities[task.priority]}</span>
              <span>{task.category}</span>
              <small>Автор: {task.owner} · {date(task.createdAt)}</small>
              <small>Подзадачи: {task.subtasks.filter((s) => s.completed).length}/{task.subtasks.length}</small>
            </button>
          ))}
        </div>
        <div>
          <form className="task-form" onSubmit={save}>
            <h3>{selected ? 'Редактирование задачи' : 'Добавление задачи'}</h3>
            <label>Название задачи<input required maxLength={200} {...field('title')} /></label>
            <label>Описание задачи<textarea rows={3} maxLength={10000} {...field('description')} /></label>
            <div className="task-pair">
              <label>Приоритет<select {...field('priority')}>{Object.entries(priorities).map(([value, text]) => <option key={value} value={value}>{text}</option>)}</select></label>
              <label>Статус задачи<select {...field('status')}>{Object.entries(statuses).map(([value, text]) => <option key={value} value={value}>{text}</option>)}</select></label>
            </div>
            <label>Категория<input required maxLength={100} {...field('category')} /></label>
            <div className="task-toolbar">
              <button type="submit" className="task-primary" disabled={busy}>Сохранить задачу</button>
              {selected && <button type="button" disabled={busy} onClick={() => run(async () => { await request(`/api/tasks/${selected.id}`, 'DELETE'); choose(null) }, 'Задача удалена, история сохранена')}>Удалить задачу</button>}
            </div>
          </form>
          {selected && <div className="task-details">
            <h3>Подзадачи</h3>
            {selected.subtasks.length === 0 && <p>Подзадач пока нет</p>}
            {selected.subtasks.map((s) => <div className="subtask" key={s.id}>
              <label><input type="checkbox" checked={s.completed} disabled={busy} onChange={(event) => run(async () => accept(await request(`/api/tasks/${selected.id}/subtasks/${s.id}`, 'PUT', { description: s.description, completed: event.target.checked })), 'Статус подзадачи сохранён')} />{s.description}</label>
              <button type="button" disabled={busy} aria-label={`Удалить подзадачу ${s.description}`} onClick={() => run(async () => accept(await request(`/api/tasks/${selected.id}/subtasks/${s.id}`, 'DELETE')), 'Подзадача удалена')}>Удалить</button>
            </div>)}
            <form className="task-toolbar" onSubmit={(event) => {
              event.preventDefault()
              run(async () => { accept(await request(`/api/tasks/${selected.id}/subtasks`, 'POST', { description: subtask, completed: false })); setSubtask('') }, 'Подзадача добавлена')
            }}>
              <input aria-label="Описание новой подзадачи" placeholder="Новая подзадача" required maxLength={500} value={subtask} onChange={(event) => setSubtask(event.target.value)} />
              <button type="submit" disabled={busy}>Добавить подзадачу</button>
            </form>
            <button type="button" disabled={busy} onClick={() => run(async () => setHistory(await request(`/api/tasks/${selected.id}/history`)), 'История загружена')}>История изменений</button>
          </div>}
        </div>
      </div>
      {history && <div className="task-history">
        <h3>История задачи «{selected.title}»</h3>
        {history.map((entry) => <details key={entry.id}>
          <summary>{date(entry.changedAt)} · {actions[entry.action]} · {entry.actor}</summary>
          <div className="task-pair"><div><strong>До изменения</strong><pre>{entry.oldState || 'Записи ещё не было'}</pre></div><div><strong>После изменения</strong><pre>{entry.newState}</pre></div></div>
        </details>)}
      </div>}
    </div>
  )
}

export function LoginHistory() {
  const [rows, setRows] = useState([])
  const [error, setError] = useState('')
  async function load() {
    try { setRows(await request('/api/login-history')); setError('') } catch (e) { setError(e.message) }
  }
  useEffect(() => { load() }, [])
  return <div className="event-tasks">
    <div className="task-toolbar"><p>Последние 50 успешных входов</p><button type="button" onClick={load}>Обновить историю входов</button></div>
    {error && <p role="alert">{error}</p>}
    <div className="task-table"><table><thead><tr><th>Дата входа</th><th>Пользователь</th><th>IP-адрес</th></tr></thead><tbody>
      {rows.map((entry) => <tr key={entry.id}><td>{date(entry.loggedAt)}</td><td>{entry.login}</td><td>{entry.ipAddress}</td></tr>)}
      {!rows.length && <tr><td colSpan={3}>Входов пока нет</td></tr>}
    </tbody></table></div>
  </div>
}
