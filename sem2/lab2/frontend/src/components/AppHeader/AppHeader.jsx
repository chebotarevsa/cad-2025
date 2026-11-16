import { useEffect, useRef, useState } from 'react'
import { Link } from 'react-router-dom'
import { Moon, Sun, Settings } from 'lucide-react'










export default function AppHeader({ title = 'Расписание', to = '/login', theme, onToggleTheme, onOpenSettings, wide = false }) {
  const ref = useRef(null)
  const [backdropVisible, setBackdropVisible] = useState(false)




  useEffect(() => {
    const el = ref.current
    if (!el) return
    const apply = () => {
      document.documentElement.style.setProperty('--header-h', `${el.offsetHeight}px`)
    }
    apply()
    const ro = new ResizeObserver(apply)
    ro.observe(el)
    return () => ro.disconnect()
  }, [])



  useEffect(() => {
    let frame = 0
    let settleTimer = 0

    const measure = () => {
      frame = 0
      const visible = window.scrollY > 2
      setBackdropVisible((current) => current === visible ? current : visible)
    }

    const scheduleMeasure = () => {
      if (!frame) frame = requestAnimationFrame(measure)
      clearTimeout(settleTimer)


      settleTimer = window.setTimeout(measure, 90)
    }

    measure()
    window.addEventListener('scroll', scheduleMeasure, { passive: true })
    window.addEventListener('resize', scheduleMeasure)
    const ro = new ResizeObserver(scheduleMeasure)
    if (ref.current) ro.observe(ref.current)

    return () => {
      window.removeEventListener('scroll', scheduleMeasure)
      window.removeEventListener('resize', scheduleMeasure)
      ro.disconnect()
      if (frame) cancelAnimationFrame(frame)
      clearTimeout(settleTimer)
    }
  }, [])

  const iconBtn =
    'grid size-11 shrink-0 place-items-center rounded-tile border-[calc(2px/(var(--ui-base)*var(--ui-zoom)))] ' +
    'border-line bg-surface text-muted transition hover:border-brand hover:text-brand active:scale-95'



  return (
    <>
      <div className="app-header-backdrop-anchor sticky top-0 z-30 h-0" aria-hidden>
        <div
          className={'fade-layer app-header-backdrop ' + (backdropVisible ? 'is-visible' : '')}
        />
      </div>

      <header ref={ref} className="app-header sticky top-0 z-40 pt-[env(safe-area-inset-top)]">
        {

                                                                }
        <div className="relative z-10 pt-3 pb-4 [zoom:calc(var(--ui-base)*var(--ui-zoom))]">
          <div className={'mx-auto px-3 md:px-6 ' + (wide ? 'max-w-[1140px]' : 'max-w-[772px]')}>
            <div className="flex items-center gap-3 rounded-card border-[calc(2px/(var(--ui-base)*var(--ui-zoom)))] border-line bg-surface px-3.5 py-2.5">
              <Link
                to={to}
                title="Войти"
                className="flex min-w-0 items-center gap-2.5 rounded-tile px-1 transition hover:opacity-80 active:scale-[0.98]"
              >
                <span className="flex min-w-0 flex-col leading-tight">
                  <b className="text-xl font-black tracking-tight text-ink">{title}</b>
                  <i className="truncate text-[13px] font-semibold text-muted not-italic">
                    Школа креативных индустрий
                  </i>
                </span>
              </Link>

              <div className="ml-auto flex shrink-0 gap-2">
                <button type="button" className={iconBtn} title="Тема" aria-label="Сменить тему" onClick={onToggleTheme}>
                  {theme === 'dark' ? <Sun className="size-6" /> : <Moon className="size-6" />}
                </button>
                <button type="button" className={iconBtn} title="Настройки" aria-label="Настройки" onClick={onOpenSettings}>
                  <Settings className="size-6" />
                </button>
              </div>
            </div>
          </div>
        </div>
      </header>
    </>
  )
}
