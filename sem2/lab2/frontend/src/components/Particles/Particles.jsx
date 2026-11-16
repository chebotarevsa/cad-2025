import { useEffect, useRef } from 'react'
import { useBgEffects } from '../../useBgEffects'

const SPEED = 0.06



const countFor = (w) => (w < 640 ? 24 : w < 1024 ? 45 : 70)
const linkDistFor = (w) => (w < 640 ? 90 : 130)









export default function Particles() {
  const canvasRef = useRef(null)
  const enabled = useBgEffects()

  useEffect(() => {
    if (!enabled) return
    if (window.matchMedia('(prefers-reduced-motion: reduce)').matches) return

    const canvas = canvasRef.current
    const ctx = canvas.getContext('2d')
    let raf = 0
    let w = 0
    let h = 0
    let pixelRatio = 0
    const dots = []

    const resize = () => {
      const dpr = Math.min(window.devicePixelRatio || 1, 2)
      const nextWidth = canvas.clientWidth
      const nextHeight = canvas.clientHeight
      if (!nextWidth || !nextHeight) return
      if (nextWidth === w && nextHeight === h && dpr === pixelRatio) return


      if (w && nextWidth !== w) {
        dots.forEach(dot => { dot.x *= nextWidth / w; dot.y *= nextHeight / h })
      }
      w = nextWidth
      h = nextHeight
      pixelRatio = dpr
      canvas.width = w * dpr
      canvas.height = h * dpr
      ctx.setTransform(dpr, 0, 0, dpr, 0, 0)
      const count = countFor(w)
      if (dots.length > count) dots.length = count
      while (dots.length < count) dots.push(createDot())
      paint()
    }

    const createDot = () => ({
      x: Math.random() * w,
      y: Math.random() * h,
      vx: (Math.random() - 0.5) * SPEED * 2,
      vy: (Math.random() - 0.5) * SPEED * 2,
      r: 1 + Math.random() * 1.4,
    })

    const accent = () =>
      getComputedStyle(document.documentElement).getPropertyValue('--color-brand').trim() || '#3b82f6'




    let last = 0
    const draw = (now = 0) => {
      raf = requestAnimationFrame(draw)
      if (now - last < 33) return
      last = now

      for (const d of dots) {
        d.x += d.vx
        d.y += d.vy

        if ((d.x < 0 && d.vx < 0) || (d.x > w && d.vx > 0)) d.vx *= -1
        if ((d.y < 0 && d.vy < 0) || (d.y > h && d.vy > 0)) d.vy *= -1
      }
      paint()
    }

    const paint = () => {
      const color = accent()
      const dark = document.documentElement.classList.contains('dark')
      const linkDist = linkDistFor(w)
      ctx.clearRect(0, 0, w, h)

      ctx.strokeStyle = color
      for (let i = 0; i < dots.length; i++) {
        for (let j = i + 1; j < dots.length; j++) {
          const dx = dots[i].x - dots[j].x
          const dy = dots[i].y - dots[j].y
          const dist = Math.hypot(dx, dy)
          if (dist > linkDist) continue


          ctx.globalAlpha = (1 - dist / linkDist) * (dark ? 0.16 : 0.26)
          ctx.beginPath()
          ctx.moveTo(dots[i].x, dots[i].y)
          ctx.lineTo(dots[j].x, dots[j].y)
          ctx.stroke()
        }
      }

      ctx.fillStyle = color
      ctx.globalAlpha = dark ? 0.4 : 0.55
      for (const d of dots) {
        ctx.beginPath()
        ctx.arc(d.x, d.y, d.r, 0, Math.PI * 2)
        ctx.fill()
      }
      ctx.globalAlpha = 1
    }

    const start = () => {
      cancelAnimationFrame(raf)
      raf = requestAnimationFrame(draw)
    }
    const stop = () => cancelAnimationFrame(raf)
    const onVisibility = () => (document.hidden ? stop() : start())
    const onResize = resize

    resize()
    if (!document.hidden) start()



    const ro = new ResizeObserver(onResize)
    ro.observe(canvas)
    window.addEventListener('resize', onResize)
    document.addEventListener('visibilitychange', onVisibility)

    return () => {
      stop()
      ro.disconnect()
      window.removeEventListener('resize', onResize)
      document.removeEventListener('visibilitychange', onVisibility)
    }
  }, [enabled])

  if (!enabled) return null

  return (
    <canvas
      ref={canvasRef}
      aria-hidden
      className="live-background pointer-events-none fixed left-0 top-0 -z-10 w-full"
    />
  )
}
