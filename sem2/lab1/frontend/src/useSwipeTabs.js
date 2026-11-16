import { useEffect } from 'react'
import { useNavigate } from 'react-router-dom'


const TABS = ['/', '/studios', '/events']

const MIN_DISTANCE = 70
const MAX_VERTICAL = 60
const MAX_TIME = 700








export function useSwipeTabs(current) {
  const navigate = useNavigate()

  useEffect(() => {


    return

    const index = TABS.indexOf(current)
    if (index === -1) return

    let x = 0
    let y = 0
    let t = 0
    let ignore = false

    const onStart = (e) => {
      const touch = e.touches[0]
      x = touch.clientX
      y = touch.clientY
      t = Date.now()

      ignore = !!e.target.closest?.('[data-no-swipe], [role="dialog"], .overflow-x-auto')
    }

    const onEnd = (e) => {
      if (ignore) return
      const touch = e.changedTouches[0]
      const dx = touch.clientX - x
      const dy = touch.clientY - y
      if (Date.now() - t > MAX_TIME) return
      if (Math.abs(dy) > MAX_VERTICAL) return
      if (Math.abs(dx) < MIN_DISTANCE) return

      const next = dx < 0 ? index + 1 : index - 1
      if (next < 0 || next >= TABS.length) return
      navigate(TABS[next])
    }

    window.addEventListener('touchstart', onStart, { passive: true })
    window.addEventListener('touchend', onEnd, { passive: true })
    return () => {
      window.removeEventListener('touchstart', onStart)
      window.removeEventListener('touchend', onEnd)
    }
  }, [current, navigate])
}
