import { ChevronLeft, ChevronRight, CalendarDays } from 'lucide-react'
import { fmtWeekRangeLong, weekYears } from '../../dates'

export default function WeekBar({ monday, onPrev, onNext, onOpenCalendar }) {
  const nav =
    'grid size-12 shrink-0 place-items-center rounded-tile border-[calc(2px/(var(--ui-base)*var(--ui-zoom)))] ' +
    'border-line text-muted transition hover:border-brand hover:text-brand active:scale-95'

  return (
    <div className="mt-4 flex items-center gap-2.5 rounded-card border-[calc(2px/(var(--ui-base)*var(--ui-zoom)))] border-line bg-surface p-2">
      <button type="button" className={nav} onClick={onPrev} title="Прошлая неделя" aria-label="Прошлая неделя">
        <ChevronLeft className="size-7" />
      </button>

      <button
        type="button"
        onClick={onOpenCalendar}
        title="Открыть календарь"


        className="flex flex-1 flex-col items-center gap-0.5 rounded-tile px-2 py-1 leading-tight transition hover:bg-brand/10 active:scale-[0.99]"
      >
        <span className="flex items-center gap-2 text-base font-extrabold tracking-tight text-ink">
          <CalendarDays className="size-[18px] shrink-0 text-brand" />
          {fmtWeekRangeLong(monday)}
        </span>
        <span className="text-[11px] leading-none font-bold tracking-[0.12em] text-muted uppercase">
          {weekYears(monday)}
        </span>
      </button>

      <button type="button" className={nav} onClick={onNext} title="Следующая неделя" aria-label="Следующая неделя">
        <ChevronRight className="size-7" />
      </button>
    </div>
  )
}
