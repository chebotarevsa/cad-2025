const MONTHS_GEN = [
  'января', 'февраля', 'марта', 'апреля', 'мая', 'июня',
  'июля', 'августа', 'сентября', 'октября', 'ноября', 'декабря',
]
const MONTHS_SHORT = [
  'янв', 'фев', 'мар', 'апр', 'мая', 'июн',
  'июл', 'авг', 'сен', 'окт', 'ноя', 'дек',
]
export const MONTHS_NOM = [
  'Январь', 'Февраль', 'Март', 'Апрель', 'Май', 'Июнь',
  'Июль', 'Август', 'Сентябрь', 'Октябрь', 'Ноябрь', 'Декабрь',
]

export function startOfDay(date) {
  const d = new Date(date)
  d.setHours(0, 0, 0, 0)
  return d
}

export function addDays(date, n) {
  const d = new Date(date)
  d.setDate(d.getDate() + n)
  return d
}

export function mondayOf(date) {
  const d = startOfDay(date)
  const offset = (d.getDay() + 6) % 7
  return addDays(d, -offset)
}

export function sameDay(a, b) {
  return a.getFullYear() === b.getFullYear() && a.getMonth() === b.getMonth() && a.getDate() === b.getDate()
}

export function defaultWeekStart(now = new Date()) {
  const m = mondayOf(now)
  return now.getDay() === 0 ? addDays(m, 7) : m
}







export function parseIsoLocal(s) {
  const [y, m, d] = s.split('-').map(Number)
  return new Date(y, m - 1, d)
}

export function isoLocal(date) {
  const m = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  return `${date.getFullYear()}-${m}-${day}`
}

export const fmtDayMonth = (date) => `${date.getDate()} ${MONTHS_GEN[date.getMonth()]}`
export const fmtShort = (date) => `${date.getDate()} ${MONTHS_SHORT[date.getMonth()]}`

export function fmtWeekRange(monday) {
  return `${fmtShort(monday)} – ${fmtShort(addDays(monday, 6))}`
}






export function fmtWeekRangeLong(monday) {
  const sunday = addDays(monday, 6)
  if (monday.getMonth() === sunday.getMonth()) {
    return `${monday.getDate()} – ${sunday.getDate()} ${MONTHS_SHORT[monday.getMonth()]}`
  }
  return `${fmtShort(monday)} – ${fmtShort(sunday)}`
}


export function weekYears(monday) {
  const a = monday.getFullYear()
  const b = addDays(monday, 6).getFullYear()
  return a === b ? String(a) : `${a}–${b}`
}

const VACATIONS = [
  ['2025-10-26', '2025-11-04'],
  ['2025-12-31', '2026-01-11'],
  ['2026-03-29', '2026-04-05'],
  ['2026-06-01', '2026-08-31'],
]
const HOLIDAYS = {
  '2025-11-04': 'День народного единства',
  '2026-01-07': 'Рождество Христово',
  '2026-02-23': 'День защитника Отечества',
  '2026-03-08': 'Международный женский день',
  '2026-05-01': 'Праздник Весны и Труда',
  '2026-05-09': 'День Победы',
  '2026-06-12': 'День России',
}

export function dayStatus(date, holidays = null) {
  const key = isoLocal(date)
  if (Array.isArray(holidays)) {
    const md = key.slice(5)
    const matchHoliday = (h) => (h.yearly ? md === h.startDate.slice(5) : key === h.startDate)
    const matchVacation = (h) => {
      if (!h.yearly) return key >= h.startDate && key <= h.endDate
      const s = h.startDate.slice(5)
      const e = h.endDate.slice(5)
      return s <= e ? md >= s && md <= e : md >= s || md <= e
    }
    for (const h of holidays) {
      if (h.type === 'HOLIDAY' && matchHoliday(h)) {
        return { type: 'holiday', label: 'Праздник', name: h.name }
      }
    }
    for (const h of holidays) {
      if (h.type === 'VACATION' && matchVacation(h)) {
        return { type: 'vacation', label: 'Каникулы', name: h.name }
      }
    }
    return { type: 'school' }
  }
  if (HOLIDAYS[key]) return { type: 'holiday', label: 'Праздник', name: HOLIDAYS[key] }
  for (const [s, e] of VACATIONS) {
    if (key >= s && key <= e) return { type: 'vacation', label: 'Каникулы' }
  }
  return { type: 'school' }
}
