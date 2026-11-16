import assert from 'node:assert/strict'
import { readFileSync } from 'node:fs'
import test from 'node:test'
import vm from 'node:vm'



const source = readFileSync(new URL('../src/components/Particles/Particles.jsx', import.meta.url), 'utf8')
const start = source.indexOf('  useEffect(() => {')
const end = source.indexOf('\n  if (!enabled) return null', start)

function scene(width = 390, height = 844) {
  let cleanup, observer, randomCalls = 0, frame = 0, points = []
  const events = new Map(), frames = new Map(), visibilityEvents = new Map()
  const ctx = { clearRect: () => { points = [] }, arc: (x, y, r) => points.push([x, y, r]),
    setTransform() {}, beginPath() {}, moveTo() {}, lineTo() {}, stroke() {}, fill() {} }
  const canvas = { clientWidth: width, clientHeight: height, getContext: () => ctx }
  const document = { hidden: false, documentElement: { classList: { contains: () => false } },
    addEventListener: (name, fn) => visibilityEvents.set(name, fn), removeEventListener: name => visibilityEvents.delete(name) }
  vm.runInNewContext(source.slice(start, end), {
    SPEED: 0.06, countFor: w => w < 640 ? 24 : w < 1024 ? 45 : 70, linkDistFor: w => w < 640 ? 90 : 130,
    enabled: true, canvasRef: { current: canvas }, document,
    window: { devicePixelRatio: 2, matchMedia: () => ({ matches: false }),
      addEventListener: (name, fn) => events.set(name, fn), removeEventListener: name => events.delete(name) },
    Math: Object.assign(Object.create(Math), { random: () => { randomCalls++; return ((randomCalls * 37) % 101) / 101 } }),
    getComputedStyle: () => ({ getPropertyValue: () => '#ff8400' }),
    requestAnimationFrame: fn => { frames.set(++frame, fn); return frame }, cancelAnimationFrame: id => frames.delete(id),
    ResizeObserver: class { constructor(fn) { observer = fn } observe() {} disconnect() { observer = null } },
    useEffect: fn => { cleanup = fn() },
  })
  return { canvas, document, events, visibilityEvents, frames,
    resize: () => events.get('resize')(), observeResize: () => observer(),
    points: () => points.map(p => [...p]), randomCalls: () => randomCalls, cleanup: () => cleanup() }
}

test('mobile resize notifications preserve positions without reseeding', () => {
  const s = scene(), original = s.points(), randomCalls = s.randomCalls()
  for (let i = 0; i < 20; i++) { s.resize(); s.observeResize() }
  assert.deepEqual(s.points(), original)
  assert.equal(s.randomCalls(), randomCalls)
})

test('a change of height preserves all existing dots', () => {
  const s = scene(), original = s.points(), randomCalls = s.randomCalls()
  s.canvas.clientHeight = 760; s.resize()
  assert.deepEqual(s.points(), original)
  assert.equal(s.randomCalls(), randomCalls)
})

test('desktop resizing scales positions instead of randomizing the scene', () => {
  const s = scene(1440, 900), original = s.points(), randomCalls = s.randomCalls()
  s.canvas.clientWidth = 1600; s.canvas.clientHeight = 1000; s.resize()
  assert.equal(s.randomCalls(), randomCalls)
  s.points().forEach((p, i) => {
    assert.ok(Math.abs(p[0] - original[i][0] * 1600 / 1440) < 1e-9)
    assert.ok(Math.abs(p[1] - original[i][1] * 1000 / 900) < 1e-9)
    assert.equal(p[2], original[i][2])
  })
})

test('hidden tabs pause; cleanup removes listeners and animation frames', () => {
  const s = scene()
  s.document.hidden = true; s.visibilityEvents.get('visibilitychange')()
  assert.equal(s.frames.size, 0)
  s.document.hidden = false; s.visibilityEvents.get('visibilitychange')()
  assert.equal(s.frames.size, 1)
  s.cleanup()
  assert.equal(s.frames.size, 0)
  assert.equal(s.events.size, 0)
  assert.equal(s.visibilityEvents.size, 0)
})
