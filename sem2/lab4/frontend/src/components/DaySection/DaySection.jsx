import LessonCard from '../LessonCard/LessonCard'
import HolidayCard from '../HolidayCard/HolidayCard'
import { DAY_NAMES, MONTHS_SHORT_LOWER, lessonsWord } from '../../utils'








export default function DaySection({ day, date, status, lessons, byTeacher = false, isToday = false, onOpenStudio }) {
  const isOff = status.type !== 'school'
  const events = lessons.filter((l) => l.special)
  const visibleLessons = isOff ? events : lessons
  const showHolidayCard = isOff && events.length === 0



  const counter = isOff ? status.label : visibleLessons.length ? lessonsWord(visibleLessons.length) : 'свободно'

  return (


    <section
      id={`day-${day}`}
      className="scroll-mt-[calc((var(--header-h,84px)+9px)/(var(--ui-base)*var(--ui-zoom)))]"
    >
      <div


        className={
          'app-day-heading sticky top-[calc((var(--header-h,84px)+5px)/(var(--ui-base)*var(--ui-zoom)))] z-20 mb-3.5 flex items-center gap-3 rounded-card px-3 py-2 ' +
          (isToday
            ? 'border-[calc(2px/(var(--ui-base)*var(--ui-zoom)))] border-white/40 bg-gradient-to-br from-brand-light to-brand text-white'
            : 'border-[calc(2px/(var(--ui-base)*var(--ui-zoom)))] border-line bg-canvas')
        }
      >
        <span
          className={
            'grid size-11 shrink-0 place-items-center rounded-tile border-[calc(2px/(var(--ui-base)*var(--ui-zoom)))] text-lg font-extrabold tabular-nums ' +
            (isToday ? 'border-transparent bg-white/25 text-white' : 'border-line bg-surface text-ink')
          }
        >
          {date.getDate()}
        </span>

        <span className="flex min-w-0 flex-col leading-tight">
          <b className="text-[15px] font-extrabold tracking-tight">{DAY_NAMES[day]}</b>
          <i
            className={
              'text-[11px] font-bold tracking-[0.11em] uppercase not-italic ' +
              (isToday ? 'text-white/80' : 'text-muted')
            }
          >
            {MONTHS_SHORT_LOWER[date.getMonth()]}
          </i>
        </span>

        <span
          className={
            'ml-auto shrink-0 rounded-full border-[calc(2px/(var(--ui-base)*var(--ui-zoom)))] px-3 py-1.5 text-xs font-bold whitespace-nowrap ' +
            (isToday ? 'border-transparent bg-white/25 text-white' : 'border-line bg-surface text-muted')
          }
        >
          {counter}
        </span>
      </div>

      <div className="flex flex-col gap-2.5">
        {showHolidayCard ? (
          <HolidayCard label={status.label} type={status.type} name={status.name} />
        ) : visibleLessons.length === 0 ? (
          <div className="rounded-card border-[calc(2px/(var(--ui-base)*var(--ui-zoom)))] border-dashed border-line px-5 py-6 text-center text-base text-muted">
            Занятий нет
          </div>
        ) : (
          visibleLessons.map((lesson, i) => (
            <LessonCard
              key={lesson.id}
              lesson={lesson}
              index={i}
              byTeacher={byTeacher}
              highlightCurrent={isToday}
              onOpenStudio={onOpenStudio}
            />
          ))
        )}
      </div>
    </section>
  )
}
