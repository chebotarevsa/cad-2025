const KEY = 'auth-user'

export function getUser() {
  try {
    const raw = localStorage.getItem(KEY) ?? sessionStorage.getItem(KEY)
    return raw ? JSON.parse(raw) : null
  } catch {
    return null
  }
}

export function setUser(user, remember = true) {
  const raw = JSON.stringify(user)
  if (remember) {
    localStorage.setItem(KEY, raw)
    sessionStorage.removeItem(KEY)
  } else {
    sessionStorage.setItem(KEY, raw)
    localStorage.removeItem(KEY)
  }
}

export function clearUser() {
  fetch('/api/auth/logout', { method: 'POST' }).catch(() => {})
  localStorage.removeItem(KEY)
  sessionStorage.removeItem(KEY)
}

export function roleLabel(role) {
  if (role === 'ADMIN') return 'Администратор'
  if (role === 'TEACHER') return 'Преподаватель'
  return role || ''
}
