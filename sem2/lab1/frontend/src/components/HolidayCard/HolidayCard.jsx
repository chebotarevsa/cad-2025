import { useState } from 'react'
import confetti from 'canvas-confetti'
import { PartyPopper, Palmtree } from 'lucide-react'

function launchConfetti(e) {




  const canvas = document.createElement('canvas')
  canvas.className = 'confetti-layer'
  canvas.style.top = `${window.scrollY}px`
  document.body.appendChild(canvas)

  const fire = confetti.create(canvas, { resize: true })
  const animation = fire({
    particleCount: 120,
    spread: 80,
    startVelocity: 40,
    disableForReducedMotion: true,
    origin: { x: e.clientX / window.innerWidth, y: e.clientY / window.innerHeight },
  })

  Promise.resolve(animation).finally(() => canvas.remove())
}







export default function HolidayCard({ label, type, name }) {
  const [active, setActive] = useState(false)
  const Icon = type === 'holiday' ? PartyPopper : Palmtree

  function handleClick(e) {
    launchConfetti(e)
    setActive(false)
    requestAnimationFrame(() => setActive(true))
  }

  return (
    <article
      onClick={handleClick}



      onAnimationEnd={(e) => { if (e.target === e.currentTarget) setActive(false) }}
      className={




        'flex animate-fade-up cursor-pointer items-center justify-center gap-2.5 rounded-card border-[calc(2px/(var(--ui-base)*var(--ui-zoom)))] border-dashed border-line px-5 py-5 text-center transition select-none active:scale-[0.99] ' +
        (active ? 'animate-border-rainbow' : '')
      }
    >
      <Icon className="size-6 shrink-0 text-muted" />
      <span className="text-lg font-bold text-ink">{name || label}</span>
    </article>
  )
}
