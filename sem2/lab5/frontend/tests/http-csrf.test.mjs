import test, { afterEach } from 'node:test'
import assert from 'node:assert/strict'
import { apiFetch } from '../src/http.js'

const originalFetch = globalThis.fetch
afterEach(() => { globalThis.fetch = originalFetch })

test('reading data does not request a CSRF token', async () => {
  const calls = []
  globalThis.fetch = async (...args) => { calls.push(args); return new Response('[]') }
  await apiFetch('/api/lessons')
  assert.equal(calls.length, 1)
  assert.equal(calls[0][1].credentials, 'same-origin')
})

test('writing preserves custom headers and includes the session CSRF token', async () => {
  const calls = []
  globalThis.fetch = async (url, options) => {
    calls.push([url, options])
    return url === '/api/auth/csrf'
      ? Response.json({ headerName: 'X-CSRF-TOKEN', token: 'token-1' })
      : new Response(null, { status: 204 })
  }
  await apiFetch('/api/attendance/marks', { method: 'PUT', headers: { 'X-Attendance-Request': '1' }, body: '{}' })
  assert.equal(calls.length, 2)
  assert.equal(calls[0][1].cache, 'no-store')
  assert.equal(calls[1][1].headers.get('X-CSRF-TOKEN'), 'token-1')
  assert.equal(calls[1][1].headers.get('X-Attendance-Request'), '1')
  assert.equal(calls[1][1].body, '{}')
})

test('a failed token request prevents a write', async () => {
  let calls = 0
  globalThis.fetch = async () => { calls++; return new Response(null, { status: 500 }) }
  await assert.rejects(apiFetch('/api/lessons/1', { method: 'DELETE' }))
  assert.equal(calls, 1)
})

test('a new token is obtained after login or role switching', async () => {
  let sequence = 0
  const sent = []
  globalThis.fetch = async (url, options) => {
    if (url === '/api/auth/csrf') return Response.json({ headerName: 'X-CSRF-TOKEN', token: String(++sequence) })
    sent.push(options.headers.get('X-CSRF-TOKEN'))
    return new Response(null, { status: 204 })
  }
  await apiFetch('/api/auth/login', { method: 'POST' })
  await apiFetch('/api/lessons', { method: 'POST' })
  assert.deepEqual(sent, ['1', '2'])
})
