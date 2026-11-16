export async function fetchGroupLessons(groupNumber) {
  const res = await fetch(`/api/lessons?group=${encodeURIComponent(groupNumber)}`)
  if (!res.ok) throw new Error('Ошибка загрузки расписания')
  return res.json()
}

export async function fetchTeacherLessons(teacherId) {
  const res = await fetch(`/api/lessons?teacher=${teacherId}`)
  if (!res.ok) throw new Error('Ошибка загрузки расписания')
  return res.json()
}

export async function fetchGroups() {
  const res = await fetch('/api/groups')
  if (!res.ok) throw new Error('Ошибка загрузки групп')
  return res.json()
}

export async function fetchGroupsList() {
  const res = await fetch('/api/groups/list')
  if (!res.ok) throw new Error('Ошибка загрузки групп')
  return res.json()
}

async function sendGroup(url, method, payload) {
  const res = await fetch(url, {
    method,
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload),
  })
  if (!res.ok) {
    let message = 'Не удалось сохранить группу'
    try {
      const data = await res.json()
      if (data?.error) message = data.error
    } catch {

    }
    throw new Error(message)
  }
  return res.json()
}

export function createGroup(payload) {
  return sendGroup('/api/groups', 'POST', payload)
}

export function updateGroup(id, payload) {
  return sendGroup(`/api/groups/${id}`, 'PUT', payload)
}

export async function deleteGroup(id) {
  const res = await fetch(`/api/groups/${id}`, { method: 'DELETE' })
  if (!res.ok) {
    let message = 'Не удалось удалить группу'
    try {
      const data = await res.json()
      if (data?.error) message = data.error
    } catch {

    }
    throw new Error(message)
  }
}

export async function fetchTeachers() {
  const res = await fetch('/api/teachers')
  if (!res.ok) throw new Error('Ошибка загрузки преподавателей')
  return res.json()
}

export async function fetchAllLessons() {
  const res = await fetch('/api/lessons')
  if (!res.ok) throw new Error('Ошибка загрузки расписания')
  return res.json()
}

async function sendLesson(url, method, payload) {
  const res = await fetch(url, {
    method,
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload),
  })
  if (!res.ok) {
    let message = 'Не удалось сохранить занятие'
    try {
      const data = await res.json()
      if (data?.error) message = data.error
    } catch {
    }
    throw new Error(message)
  }
  return res.json()
}

export function createLesson(payload) {
  return sendLesson('/api/lessons', 'POST', payload)
}

export function updateLesson(id, payload) {
  return sendLesson(`/api/lessons/${id}`, 'PUT', payload)
}

export async function deleteLesson(id) {
  const res = await fetch(`/api/lessons/${id}`, { method: 'DELETE' })
  if (!res.ok) throw new Error('Не удалось удалить занятие')
}

export async function fetchHolidays() {
  const res = await fetch('/api/holidays')
  if (!res.ok) throw new Error('Ошибка загрузки праздников')
  return res.json()
}

async function sendHoliday(url, method, payload) {
  const res = await fetch(url, {
    method,
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload),
  })
  if (!res.ok) {
    let message = 'Не удалось сохранить запись'
    try {
      const data = await res.json()
      if (data?.error) message = data.error
    } catch {

    }
    throw new Error(message)
  }
  return res.json()
}

export function createHoliday(payload) {
  return sendHoliday('/api/holidays', 'POST', payload)
}

export function updateHoliday(id, payload) {
  return sendHoliday(`/api/holidays/${id}`, 'PUT', payload)
}

export async function deleteHoliday(id) {
  const res = await fetch(`/api/holidays/${id}`, { method: 'DELETE' })
  if (!res.ok) throw new Error('Не удалось удалить запись')
}

export async function fetchStudios() {
  const res = await fetch('/api/studios')
  if (!res.ok) throw new Error('Ошибка загрузки студий')
  return res.json()
}

export async function updateStudio(id, payload) {
  const res = await fetch(`/api/studios/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload),
  })
  if (!res.ok) {
    let message = 'Не удалось сохранить студию'
    try {
      const data = await res.json()
      if (data?.error) message = data.error
    } catch {

    }
    throw new Error(message)
  }
  return res.json()
}

export async function getPushPublicKey() {
  const res = await fetch('/api/push/public-key')
  if (!res.ok) throw new Error('Не удалось получить ключ уведомлений')
  return res.json()
}

export async function savePushSubscription(subscription) {
  const res = await fetch('/api/push/subscribe', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(subscription),
  })
  if (!res.ok) throw new Error('Не удалось сохранить подписку на уведомления')
}

export async function login(loginValue, password) {
  const res = await fetch('/api/auth/login', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ login: loginValue, password }),
  })
  if (!res.ok) {
    let message = 'Неверный логин или пароль'
    try {
      const data = await res.json()
      if (data?.error) message = data.error
    } catch {
    }
    throw new Error(message)
  }
  return res.json()
}
