import { useState, useEffect } from 'react'
import { ArrowUp } from 'lucide-react'

export default function ScrollTopButton() {
  const [visible, setVisible] = useState(false)

  useEffect(() => {
    const onScroll = () => setVisible(window.scrollY > 300)
    onScroll()
    window.addEventListener('scroll', onScroll, { passive: true })
    return () => window.removeEventListener('scroll', onScroll)
  }, [])



  return (
    <div className="pointer-events-none fixed inset-x-0 bottom-[calc(94px+env(safe-area-inset-bottom))] z-55">
      <div className="mx-auto flex max-w-[772px] justify-end px-3 md:px-6 [zoom:calc(var(--ui-base)*var(--ui-zoom))]">
        <button
          type="button"
          title="Наверх"
          onClick={() => window.scrollTo({ top: 0, behavior: 'smooth' })}
          className={
            'pointer-events-auto grid size-12 place-items-center rounded-full ' +
            'bg-gradient-to-br from-brand-light to-brand text-white shadow-[0_14px_30px_-12px_var(--color-brand)] ' +
            'transition-all hover:brightness-105 active:scale-95 ' +
            (visible ? 'opacity-100' : 'pointer-events-none translate-y-2 opacity-0')
          }
        >
          <ArrowUp className="size-6" strokeWidth={2.5} />
        </button>
      </div>
    </div>
  )
}
