export async function apiFetch(url, options = {}) {
  const method = (options.method || 'GET').toUpperCase()
  const headers = new Headers(options.headers)
  if (!['GET', 'HEAD', 'OPTIONS'].includes(method)) {
    const response = await fetch('/api/auth/csrf', {
      credentials: 'same-origin',
      cache: 'no-store',
    })
    if (!response.ok) throw new Error('Не удалось получить токен запроса')
    const csrf = await response.json()
    headers.set(csrf.headerName, csrf.token)
  }
  return fetch(url, { ...options, method, headers, credentials: 'same-origin' })
}
