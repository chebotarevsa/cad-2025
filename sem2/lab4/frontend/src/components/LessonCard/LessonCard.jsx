import { hhmm, lessonEndTime, isCurrentLesson, shortName } from '../../utils'

const toMinutes = (t) => {
  const [h, m] = t.split(':').map(Number)
  return h * 60 + m
}


function progressOf(lesson, now = new Date()) {
  const start = toMinutes(lesson.time)
  const finish = toMinutes(lessonEndTime(lesson))
  const cur = now.getHours() * 60 + now.getMinutes()
  if (finish <= start) return 0
  return Math.min(1, Math.max(0, (cur - start) / (finish - start)))
}

export default function LessonCard({ lesson, index = 0, byTeacher = false, highlightCurrent = true, onOpenStudio }) {


  const current = highlightCurrent && !lesson.special && isCurrentLesson(lesson)
  const finish = lessonEndTime(lesson)

  const title = lesson.special
    ? lesson.title || lesson.studioName
    : byTeacher
      ? `${lesson.groupNumber} группа`
      : lesson.studioName

  const subtitle = lesson.special
    ? `Место: ${lesson.studioName}`
    : byTeacher
      ? lesson.studioName
      : shortName(lesson.teacherName)



  if (current) {
    return (
      <article
        id={`lesson-${lesson.id}`}
        style={{ animationDelay: `${index * 60}ms` }}
        onClick={() => onOpenStudio?.(lesson)}
        className="relative animate-fade-up cursor-pointer overflow-hidden rounded-sheet bg-gradient-to-br from-brand-light to-brand px-6 py-5 text-white shadow-[0_20px_44px_-22px_var(--color-brand)] transition select-none hover:-translate-y-0.5 active:scale-[0.99]"
      >
        <span
          aria-hidden
          className="pointer-events-none absolute -top-28 -right-16 size-64 rounded-full bg-white/15"
        />
        <span className="relative inline-flex items-center gap-2 rounded-full bg-white/25 px-3 py-1.5 text-[11px] font-extrabold tracking-[0.14em] uppercase">
          <i className="size-[7px] animate-pulse rounded-full bg-white" />
          Идёт сейчас
        </span>
        <h3 className="relative mt-3.5 text-[28px] leading-tight font-extrabold tracking-tight">{title}</h3>
        <p className="relative mt-1 text-[15px] opacity-90">{subtitle}</p>
        <div className="relative mt-4 flex items-center gap-2.5 text-[15px] font-bold tabular-nums">
          <span>{hhmm(lesson.time)}</span>
          <span className="h-1.5 flex-1 overflow-hidden rounded-full bg-white/30">
            <i className="block h-full rounded-full bg-white" style={{ width: `${progressOf(lesson) * 100}%` }} />
          </span>
          <span>{finish}</span>
        </div>
      </article>
    )
  }

  return (
    <article
      id={`lesson-${lesson.id}`}
      style={{ animationDelay: `${index * 60}ms` }}
      onClick={() => onOpenStudio?.(lesson)}
      className={
        'flex animate-fade-up cursor-pointer items-stretch gap-4 rounded-card px-5 py-4 transition select-none hover:-translate-y-0.5 active:scale-[0.99] ' +
        (lesson.special
          ? 'border-[calc(2px/(var(--ui-base)*var(--ui-zoom)))] border-transparent [background:linear-gradient(var(--color-surface),var(--color-surface))_padding-box,linear-gradient(135deg,var(--color-brand-ring),var(--color-brand))_border-box]'
          : 'border-[calc(2px/(var(--ui-base)*var(--ui-zoom)))] border-line bg-surface hover:border-brand')
      }
    >
      <div className="flex w-14 shrink-0 flex-col items-center justify-center gap-1 tabular-nums">
        <span className="text-[15px] font-bold text-ink">{hhmm(lesson.time)}</span>
        <span className="grid size-7 place-items-center rounded-[9px] bg-canvas text-[13px] font-extrabold text-ink">
          {lesson.special ? '–' : lesson.orderNumber}
        </span>
        <span className="text-[13px] text-muted">{finish}</span>
      </div>

      <div className="w-0.5 shrink-0 self-stretch rounded-full bg-line" />

      <div className="flex min-w-0 flex-col justify-center gap-1">
        {
                                                                   }
        <h3 className="text-[18px] leading-snug font-bold tracking-tight text-ink">
          {lesson.special && <span className="mr-1.5 text-[1.15em] text-brand">✦</span>}
          {title}
        </h3>
        <p className="text-sm text-muted">{subtitle}</p>
      </div>
    </article>
  )
}
