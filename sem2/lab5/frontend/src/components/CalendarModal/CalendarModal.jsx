import { useState } from 'react'
import { createPortal } from 'react-dom'
import { ChevronLeft, ChevronRight } from 'lucide-react'
import { MONTHS_NOM, mondayOf, addDays, startOfDay, fmtShort } from '../../dates'

const WD = ['П', 'В', 'С', 'Ч', 'П', 'С', 'В']

export default function CalendarModal({ open, onClose, monday, onSelect }) {
  const [view, setView] = useState(() => startOfDay(monday || new Date()))
  const [picked, setPicked] = useState(() => startOfDay(monday || new Date()))
  if (!open) return null

  const year = view.getFullYear()
  const month = view.getMonth()
  const first = new Date(year, month, 1)
  const offset = (first.getDay() + 6) % 7
  const gridStart = addDays(startOfDay(first), -offset)
  const cells = Array.from({ length: 42 }, (_, i) => addDays(gridStart, i))

  const wkStart = mondayOf(picked)
  const wkEnd = addDays(wkStart, 6)
  const inWeek = (d) => d >= wkStart && d <= wkEnd

  const navBtn = 'grid size-9 place-items-center rounded-md text-muted transition hover:text-brand active:scale-95'





  return createPortal(
    <div
      className="fixed inset-0 z-60 grid animate-fade-in place-items-center overflow-y-auto bg-black/50 p-4"
      onClick={onClose}
    >
      <div
        className="w-full max-w-sm animate-fade-up rounded-card border-2 border-line bg-surface p-5 shadow-xl"
        onClick={(e) => e.stopPropagation()}
      >

        <div className="mb-4 rounded-card bg-gradient-to-r from-brand-light to-brand p-4 text-white">
          <div className="text-sm opacity-80">{year}</div>
          <div className="text-2xl font-bold">
            {fmtShort(wkStart)} – {fmtShort(wkEnd)}
          </div>
        </div>

        <div className="mb-2 flex items-center justify-between">
          <button type="button" className={navBtn} onClick={() => setView(new Date(year, month - 1, 1))}>
            <ChevronLeft className="size-6" />
          </button>
          <span className="font-medium text-ink">
            {MONTHS_NOM[month]} {year}
          </span>
          <button type="button" className={navBtn} onClick={() => setView(new Date(year, month + 1, 1))}>
            <ChevronRight className="size-6" />
          </button>
        </div>

        <div className="grid grid-cols-7 text-center text-sm text-muted">
          {WD.map((w, i) => (
            <div key={i} className="py-1">{w}</div>
          ))}
        </div>

        <div className="mt-1 grid grid-cols-7 gap-1">
          {cells.map((d, i) => {
            const other = d.getMonth() !== month
            const sel = inWeek(d)
            return (
              <button
                key={i}
                type="button"
                onClick={() => setPicked(d)}
                className={
                  'grid size-9 place-items-center rounded-[11px] text-sm transition ' +
                  (sel ? 'bg-brand font-semibold text-white ' : 'hover:bg-canvas ') +
                  (!sel && other ? 'text-muted/40' : !sel ? 'text-ink' : '')
                }
              >
                {d.getDate()}
              </button>
            )
          })}
        </div>

        <div className="mt-5 flex justify-end gap-5 text-base font-semibold text-brand">
          <button
            type="button"
            onClick={() => {
              const t = startOfDay(new Date())
              setPicked(t)
              setView(t)
            }}
          >
            Текущая неделя
          </button>
          <button
            type="button"
            onClick={() => {
              onSelect(mondayOf(picked))
              onClose()
            }}
          >
            Выбрать
          </button>
        </div>
      </div>
    </div>,
    document.body,
  )
}
