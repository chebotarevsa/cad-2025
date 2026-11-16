import { useState, useEffect } from 'react'
import { useNavigate, useLocation } from 'react-router-dom'
import { CalendarDays, Building2, PartyPopper, ChevronDown, ClipboardCheck } from 'lucide-react'
import AttendanceJournal from '../../components/AttendanceJournal/AttendanceJournal'
import AppHeader from '../../components/AppHeader/AppHeader'
import SearchBar from '../../components/SearchBar/SearchBar'
import SettingsModal from '../../components/SettingsModal/SettingsModal'
import AccountModal from '../../components/AccountModal/AccountModal'
import SchoolSchedule from '../../components/SchoolSchedule/SchoolSchedule'
import HolidaySchedule from '../../components/HolidaySchedule/HolidaySchedule'
import StudioEditor from '../../components/StudioEditor/StudioEditor'
import { ToastProvider } from '../../components/Toast/Toast'
import { fetchGroups, fetchTeachers } from '../../api'
import { getUser } from '../../auth'
import { useUiSettings } from '../../useUiSettings'

export default function DashboardPage() {
  const navigate = useNavigate()
  const location = useLocation()
  const user = getUser()
  const { theme, toggleTheme, zoom, setZoom } = useUiSettings()

  const [query, setQuery] = useState('')
  const [groups, setGroups] = useState([])
  const [teachers, setTeachers] = useState([])
  const [settingsOpen, setSettingsOpen] = useState(false)

  const [accountOpen, setAccountOpen] = useState(() => !!location.state?.showAccount)

  const [collapsed, setCollapsed] = useState(() => {
    try {
      return JSON.parse(localStorage.getItem('dash-collapsed') || '{}')
    } catch {
      return {}
    }
  })
  const toggleSection = (id) => setCollapsed((c) => ({ ...c, [id]: !c[id] }))

  useEffect(() => {
    localStorage.setItem('dash-collapsed', JSON.stringify(collapsed))
  }, [collapsed])

  useEffect(() => {
    if (location.state?.showAccount) {
      navigate(location.pathname, { replace: true, state: null })
    }

  }, [])

  useEffect(() => {
    fetchGroups().then(setGroups).catch(() => setGroups([]))
    fetchTeachers().then(setTeachers).catch(() => setTeachers([]))
  }, [])

  return (
    <ToastProvider>
    <div className="flex min-h-[100dvh] flex-col">

      <AppHeader
        wide
        title="Панель управления"
        to="/"
        theme={theme}
        onToggleTheme={toggleTheme}
        onOpenSettings={() => setSettingsOpen(true)}
      />

      <div>
        <div className="mx-auto max-w-[1140px] px-3 pb-3 md:px-6 md:pb-4 [zoom:calc(var(--ui-base)*var(--ui-zoom))]">
          <SearchBar
            query={query}
            onQueryChange={setQuery}
            groups={groups}
            teachers={teachers}
            onSelectGroup={(n) => navigate(`/dashboard/group/${encodeURIComponent(n)}`)}
            onSelectTeacher={(id) => navigate(`/dashboard/teacher/${id}`)}
          />
        </div>
      </div>

      <main className="mx-auto w-full max-w-[1140px] flex-1 overflow-x-hidden px-3 pb-12 md:px-6 [zoom:calc(var(--ui-base)*var(--ui-zoom))]">
        <CollapsibleSection icon={ClipboardCheck} title="Посещаемость" collapsed={collapsed.attendance} onToggle={() => toggleSection('attendance')}>
          <AttendanceJournal />
        </CollapsibleSection>
        <CollapsibleSection
          wide
          icon={CalendarDays}
          title="Общее расписание школы"
          collapsed={collapsed.schedule}
          onToggle={() => toggleSection('schedule')}
        >
          <SchoolSchedule />
        </CollapsibleSection>

        <CollapsibleSection
          icon={PartyPopper}
          title="Расписание мероприятий, праздников и каникул"
          collapsed={collapsed.holidays}
          onToggle={() => toggleSection('holidays')}
        >
          <HolidaySchedule />
        </CollapsibleSection>

        <CollapsibleSection
          icon={Building2}
          title="Редактирование студий"
          collapsed={collapsed.studios}
          onToggle={() => toggleSection('studios')}
        >
          <StudioEditor />
        </CollapsibleSection>
      </main>

      <AccountModal open={accountOpen} onClose={() => setAccountOpen(false)} user={user} />

      <SettingsModal
        open={settingsOpen}
        onClose={() => setSettingsOpen(false)}
        theme={theme}
        onToggleTheme={toggleTheme}
        zoom={zoom}
        onZoomChange={setZoom}
      />
    </div>
    </ToastProvider>
  )
}



function CollapsibleSection({ icon: Icon, title, collapsed, onToggle, wide = false, children }) {
  return (
    <section className="mt-8 md:mt-14">
      <button
        type="button"
        onClick={onToggle}
        aria-expanded={!collapsed}
        className="mb-3 flex w-full items-center gap-2 text-left text-2xl font-bold text-ink transition-colors hover:text-brand sm:text-3xl md:text-4xl"
      >
        <Icon className="size-8 shrink-0 text-brand" />
        <span className="flex-1">{title}</span>
        <ChevronDown className={'size-8 shrink-0 text-muted transition-transform ' + (collapsed ? '-rotate-90' : '')} />
      </button>
      {

                                                                  }
      <div
        className={
          collapsed
            ? 'hidden'


            : (wide ? 'w-full overflow-x-auto ' : 'w-full ') +
              'rounded-card border-[calc(2px/(var(--ui-base)*var(--ui-zoom)))] border-line bg-surface p-3 md:p-4'
        }
      >
        {children}
      </div>
    </section>
  )
}
