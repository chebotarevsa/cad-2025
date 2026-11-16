import { useState, useEffect, useRef } from 'react'
import { useParams, useNavigate, useLocation } from 'react-router-dom'
import AppHeader from '../../components/AppHeader/AppHeader'
import TabBar from '../../components/TabBar/TabBar'
import SearchBar from '../../components/SearchBar/SearchBar'
import WeekBar from '../../components/WeekBar/WeekBar'
import Schedule from '../../components/Schedule/Schedule'
import ScheduleSkeleton from '../../components/ScheduleSkeleton/ScheduleSkeleton'
import ScrollTopButton from '../../components/ScrollTopButton/ScrollTopButton'
import SettingsModal from '../../components/SettingsModal/SettingsModal'
import SiteFooter from '../../components/SiteFooter/SiteFooter'
import CalendarModal from '../../components/CalendarModal/CalendarModal'
import StudioSheet from '../../components/StudioSheet/StudioSheet'
import { fetchGroupLessons, fetchTeacherLessons, fetchGroups, fetchTeachers, fetchHolidays, fetchStudios } from '../../api'
import { setLastSelection } from '../../defaultSelection'
import { getCached, setCache } from '../../cache'
import { useSwipeTabs } from '../../useSwipeTabs'
import { DAY_ORDER } from '../../utils'
import { mondayOf, addDays, sameDay, defaultWeekStart } from '../../dates'
import { useUiSettings } from '../../useUiSettings'






export default function SchedulePage({ base = '', embedded = false, selection: selProp = null, active = true }) {
  const params = useParams()
  const number = params.number ?? (selProp?.type === 'group' ? String(selProp.value) : undefined)
  const teacherId = params.teacherId ?? (selProp?.type === 'teacher' ? String(selProp.value) : undefined)
  const navigate = useNavigate()



  const { key: navKey } = useLocation()
  const selection = number || teacherId


  useSwipeTabs(base ? null : '/')

  const [query, setQuery] = useState('')
  const [lessons, setLessons] = useState([])
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)
  const [groups, setGroups] = useState([])
  const [teachers, setTeachers] = useState([])
  const [holidays, setHolidays] = useState(null)
  const [studios, setStudios] = useState([])
  const [settingsOpen, setSettingsOpen] = useState(false)
  const [calendarOpen, setCalendarOpen] = useState(false)
  const [studioLesson, setStudioLesson] = useState(null)
  const [monday, setMonday] = useState(() => defaultWeekStart())
  const { theme, toggleTheme, zoom, setZoom } = useUiSettings()

  useEffect(() => {
    fetchGroups().then(setGroups).catch(() => setGroups([]))
    fetchTeachers().then(setTeachers).catch(() => setTeachers([]))
    fetchHolidays().then(setHolidays).catch(() => setHolidays([]))
    fetchStudios().then(setStudios).catch(() => setStudios([]))
  }, [])

  useEffect(() => {
    if (number) setQuery(`${number} группа`)
  }, [number, navKey])

  useEffect(() => {
    if (teacherId && teachers.length) {
      const t = teachers.find((x) => String(x.id) === String(teacherId))
      if (t) setQuery(t.fullName)
    }
  }, [teacherId, teachers, navKey])

  useEffect(() => {
    if (!selection) {
      setLessons([])
      return
    }


    const key = number ? `lessons:g:${number}` : `lessons:t:${teacherId}`
    const cached = getCached(key)
    if (cached) {
      setLessons(cached)
      setLoading(false)
    } else {
      setLoading(true)
    }
    setError(null)

    const request = number ? fetchGroupLessons(number) : fetchTeacherLessons(teacherId)
    request
      .then((data) => {
        setCache(key, data)
        setLessons(data)
      })
      .catch((e) => { if (!cached) { setError(e.message); setLessons([]) } })
      .finally(() => setLoading(false))
  }, [number, teacherId, selection, navKey])

  useEffect(() => {
    setMonday((m) => (sameDay(m, defaultWeekStart()) ? m : defaultWeekStart()))
  }, [number, teacherId])



  useEffect(() => {
    if (base) return
    if (number) setLastSelection({ type: 'group', value: number })
    else if (teacherId) setLastSelection({ type: 'teacher', value: teacherId })
  }, [number, teacherId, base])




  const scrolledFor = useRef(null)

  useEffect(() => {
    if (!lessons.length) return
    if (embedded && !active) return
    const key = `${number || ''}|${teacherId || ''}`
    if (scrolledFor.current === key) return
    scrolledFor.current = key
    const today = new Date()
    const isCurrentWeek = sameDay(monday, mondayOf(today))



    const dayId = `day-${DAY_ORDER[(today.getDay() + 6) % 7]}`







    const jump = (behavior) => {
      if (isCurrentWeek) {
        const el = document.getElementById(dayId)
        if (el) {
          const headerH = parseFloat(
            getComputedStyle(document.documentElement).getPropertyValue('--header-h'),
          ) || 84
          const top = el.getBoundingClientRect().top + window.scrollY - headerH - 9
          window.scrollTo({ top: Math.max(top, 0), behavior })
          return
        }
      }
      window.scrollTo({ top: 0, behavior })
    }

    const raf = requestAnimationFrame(() => jump('smooth'))
    const settle = setTimeout(() => jump('auto'), 450)
    return () => {
      cancelAnimationFrame(raf)
      clearTimeout(settle)
    }
  }, [lessons])

  const weekMounted = useRef(false)
  useEffect(() => {
    if (!weekMounted.current) {
      weekMounted.current = true
      return
    }
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }, [monday])

  return (



    <div className={embedded ? 'w-full' : 'flex min-h-[100dvh] flex-col [--ui-base:1]'}>

      {!embedded && (
        <AppHeader
          title="Расписание"
          to={base ? '/dashboard' : '/login'}
          theme={theme}
          onToggleTheme={toggleTheme}
          onOpenSettings={() => setSettingsOpen(true)}
        />
      )}

      <div>
        {
                                                                            }
        <div className="mx-auto max-w-[772px] px-3 pt-[18px] pb-11 md:px-6 [zoom:calc(var(--ui-base)*var(--ui-zoom))]">
          <SearchBar
            query={query}
            onQueryChange={setQuery}
            groups={groups}
            teachers={teachers}
            onSelectGroup={(n) => navigate(`${base}/group/${encodeURIComponent(n)}`)}
            onSelectTeacher={(id) => navigate(`${base}/teacher/${id}`)}
          />

          {selection && (
            <WeekBar
              monday={monday}
              onPrev={() => setMonday((m) => addDays(m, -7))}
              onNext={() => setMonday((m) => addDays(m, 7))}
              onOpenCalendar={() => setCalendarOpen(true)}
            />
          )}
        </div>
      </div>

      <main className="flex flex-1 flex-col">

        {(!selection || error || (selection && !loading && lessons.length === 0)) && (
          <div className="mx-auto flex w-full max-w-[772px] flex-1 items-center px-3 pb-10 md:px-6 [zoom:calc(var(--ui-base)*var(--ui-zoom))]">
            <div className="flex w-full flex-col items-center gap-3 px-6 py-14 text-center">
              {!selection && (
                <>
                  <p className="max-w-sm text-lg leading-snug font-semibold text-ink">
                    Введите номер группы или имя преподавателя
                  </p>
                  <p className="text-sm text-muted">Расписание появится здесь</p>
                </>
              )}
              {error && <p className="text-lg font-semibold text-red-600">{error}</p>}
              {selection && !loading && !error && lessons.length === 0 && (
                <p className="text-lg font-semibold text-muted">Расписание не найдено</p>
              )}
            </div>
          </div>
        )}

        {selection && (loading || lessons.length > 0) && (
          <div className="mx-auto w-full max-w-[772px] px-3 pb-10 md:px-6 [zoom:calc(var(--ui-base)*var(--ui-zoom))]">
            {loading ? (
              <ScheduleSkeleton />
            ) : (
              <Schedule
                lessons={lessons}
                monday={monday}
                byTeacher={!!teacherId}
                onOpenStudio={setStudioLesson}
                holidays={holidays}
              />
            )}
          </div>
        )}
      </main>

      {!embedded && <SiteFooter />}
      {!embedded && <ScrollTopButton />}

      {
                                                                         }
      {!embedded && (!base || selection) && <TabBar active="schedule" />}

      <SettingsModal
        open={settingsOpen}
        onClose={() => setSettingsOpen(false)}
        theme={theme}
        onToggleTheme={toggleTheme}
        zoom={zoom}
        onZoomChange={setZoom}
        groups={groups}
        teachers={teachers}
        showTheme={false}
      />

      {calendarOpen && (
        <CalendarModal
          open
          onClose={() => setCalendarOpen(false)}
          monday={monday}
          onSelect={setMonday}
        />
      )}

      <StudioSheet
        open={!!studioLesson}
        onClose={() => setStudioLesson(null)}
        lesson={studioLesson}
        studios={studios}
      />
    </div>
  )
}
