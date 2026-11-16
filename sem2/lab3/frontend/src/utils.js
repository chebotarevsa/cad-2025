export const DAY_NAMES = {
  MONDAY: 'Понедельник', TUESDAY: 'Вторник', WEDNESDAY: 'Среда',
  THURSDAY: 'Четверг', FRIDAY: 'Пятница', SATURDAY: 'Суббота', SUNDAY: 'Воскресенье',
}

export const DAY_ORDER = ['MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY']

export const hhmm = (t) => t.slice(0, 5)

export const normalizeGroupNumber = (value) =>
  String(value ?? '').trim().toLocaleUpperCase('ru-RU')

export const compareGroupNumbers = (left, right) =>
  normalizeGroupNumber(left).localeCompare(normalizeGroupNumber(right), 'ru', {
    numeric: true,
    sensitivity: 'base',
  })

export const MONTHS_SHORT_LOWER = [
  'январь', 'февраль', 'март', 'апрель', 'май', 'июнь',
  'июль', 'август', 'сентябрь', 'октябрь', 'ноябрь', 'декабрь',
]


export function lessonsWord(n) {
  const mod100 = n % 100
  const mod10 = n % 10
  if (mod100 >= 11 && mod100 <= 14) return `${n} занятий`
  if (mod10 === 1) return `${n} занятие`
  if (mod10 >= 2 && mod10 <= 4) return `${n} занятия`
  return `${n} занятий`
}

export function shortName(full) {
  if (!full) return ''
  const parts = full.trim().split(/\s+/)
  if (parts.length < 2) return full
  const [last, ...rest] = parts
  const initials = rest.map((p) => p.charAt(0).toUpperCase() + '.').join(' ')
  return `${last} ${initials}`
}

export function endTime(t, durationMinutes = 40) {
  const [h, m] = t.split(':').map(Number)
  const total = h * 60 + m + durationMinutes
  return `${String(Math.floor(total / 60)).padStart(2, '0')}:${String(total % 60).padStart(2, '0')}`
}

export function lessonEndTime(lesson) {
  return lesson.endTime ? hhmm(lesson.endTime) : endTime(lesson.time)
}

const toMinutes = (t) => {
  const [h, m] = t.split(':').map(Number)
  return h * 60 + m
}

export function isCurrentLesson(lesson, now = new Date()) {
  const todayKey = DAY_ORDER[(now.getDay() + 6) % 7]
  if (lesson.dayOfWeek !== todayKey) return false
  const start = toMinutes(lesson.time)
  const finish = toMinutes(lessonEndTime(lesson))
  const cur = now.getHours() * 60 + now.getMinutes()
  return cur >= start && cur < finish
}
