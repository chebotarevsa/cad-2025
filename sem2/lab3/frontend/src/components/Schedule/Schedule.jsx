import DaySection from '../DaySection/DaySection'
import { DAY_ORDER } from '../../utils'
import { addDays, dayStatus, sameDay, isoLocal } from '../../dates'

export default function Schedule({ lessons, monday, byTeacher = false, onOpenStudio, holidays }) {
  const today = new Date()

  const days = DAY_ORDER.map((day, i) => {
    const date = addDays(monday, i)
    const iso = isoLocal(date)

    const dayLessons = lessons.filter((l) => l.dayOfWeek === day && (!l.date || l.date === iso))
    return { day, date, status: dayStatus(date, holidays), dayLessons }
  })



  return (
    <div className="flex flex-col gap-11">
      {days.map(({ day, date, status, dayLessons }) => (
        <DaySection
          key={day}
          day={day}
          date={date}
          status={status}
          lessons={dayLessons}
          byTeacher={byTeacher}
          isToday={sameDay(date, today)}
          onOpenStudio={onOpenStudio}
        />
      ))}
    </div>
  )
}
