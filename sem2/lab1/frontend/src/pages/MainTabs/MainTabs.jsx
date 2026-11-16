import { useCallback, useEffect, useRef, useState } from 'react'
import { useLocation, useNavigate, useParams } from 'react-router-dom'
import useEmblaCarousel from 'embla-carousel-react'
import AppHeader from '../../components/AppHeader/AppHeader'
import TabBar from '../../components/TabBar/TabBar'
import SiteFooter from '../../components/SiteFooter/SiteFooter'
import ScrollTopButton from '../../components/ScrollTopButton/ScrollTopButton'
import SettingsModal from '../../components/SettingsModal/SettingsModal'
import SchedulePage from '../SchedulePage/SchedulePage'
import StudiosPage from '../StudiosPage/StudiosPage'
import EventsPage from '../EventsPage/EventsPage'
import { useUiSettings } from '../../useUiSettings'
import { getLastSelection, defaultSelectionPath } from '../../defaultSelection'
import { fetchGroups, fetchTeachers } from '../../api'
import { loadCached } from '../../cache'

const TITLES = ['Расписание', 'Студии', 'События']
const KEYS = ['schedule', 'studios', 'events']

const tabForPath = (p) => (p.startsWith('/studios') ? 1 : p.startsWith('/events') ? 2 : 0)












export default function MainTabs() {
  const location = useLocation()
  const navigate = useNavigate()
  const params = useParams()
  const { theme, toggleTheme, zoom, setZoom } = useUiSettings()

  const index = tabForPath(location.pathname)
  const [settingsOpen, setSettingsOpen] = useState(false)
  const [groups, setGroups] = useState([])
  const [teachers, setTeachers] = useState([])
  const [height, setHeight] = useState(null)
  const [dragging, setDragging] = useState(false)

  const slideRefs = useRef([])
  const dragged = useRef(false)
  const viewportRef = useRef(null)

  const [emblaRef, embla] = useEmblaCarousel({
    align: 'start',
    containScroll: 'trimSnaps',
    duration: 20,
    skipSnaps: false,
    dragFree: false,


    watchDrag: false,
  })

  useEffect(() => {
    loadCached('groups', fetchGroups).then(setGroups).catch(() => setGroups([]))
    loadCached('teachers', fetchTeachers).then(setTeachers).catch(() => setTeachers([]))
  }, [])




  const setViewportRef = useCallback(
    (el) => {
      viewportRef.current = el
      emblaRef(el)
    },
    [emblaRef],
  )






  useEffect(() => {
    const el = viewportRef.current
    if (!el) return
    const reset = () => {
      if (el.scrollTop !== 0) el.scrollTop = 0
    }
    el.addEventListener('scroll', reset, { passive: true })
    return () => el.removeEventListener('scroll', reset)
  }, [])



  useEffect(() => {



    if (dragging) return
    const el = slideRefs.current[index]
    if (!el) return
    const apply = () => setHeight(el.offsetHeight)
    apply()
    const ro = new ResizeObserver(apply)
    ro.observe(el)
    return () => ro.disconnect()
  }, [index, dragging])



  useEffect(() => {
    if (!embla) return

    const onPointerDown = () => {
      dragged.current = true
      setDragging(true)
    }
    const onSettle = () => {
      setDragging(false)
      if (!dragged.current) return
      dragged.current = false
      const i = embla.selectedScrollSnap()
      if (i === tabForPath(window.location.pathname)) return
      if (i === 1) navigate('/studios')
      else if (i === 2) navigate('/events')
      else navigate(defaultSelectionPath(getLastSelection()) || '/')
      window.scrollTo({ top: 0 })
    }

    embla.on('pointerDown', onPointerDown)
    embla.on('settle', onSettle)
    return () => {
      embla.off('pointerDown', onPointerDown)
      embla.off('settle', onSettle)
    }
  }, [embla, navigate])


  useEffect(() => {
    if (!embla) return
    if (embla.selectedScrollSnap() !== index) embla.scrollTo(index)
    window.scrollTo({ top: 0 })
  }, [embla, index])




  const routeSel = params.number
    ? { type: 'group', value: params.number }
    : params.teacherId
      ? { type: 'teacher', value: params.teacherId }
      : null
  const scheduleSel = routeSel || getLastSelection()

  const pages = [
    <SchedulePage key="schedule" embedded selection={scheduleSel} active={index === 0} />,
    <StudiosPage key="studios" embedded />,
    <EventsPage key="events" embedded />,
  ]

  return (
    <div className="flex min-h-[100dvh] flex-col [--ui-base:1]">
      <AppHeader
        title={TITLES[index]}
        theme={theme}
        onToggleTheme={toggleTheme}
        onOpenSettings={() => setSettingsOpen(true)}
      />

      <main className="flex flex-1 flex-col">
        <div
          ref={setViewportRef}





          className="overflow-clip transition-[height] duration-200 ease-out"
          style={height ? { height } : undefined}
        >
          {

                                           }
          <div className="flex items-start">
            {pages.map((page, i) => (
              <div
                key={i}
                ref={(el) => { slideRefs.current[i] = el }}
                className="w-full min-w-0 flex-[0_0_100%]"
              >
                {page}
              </div>
            ))}
          </div>
        </div>
      </main>

      <SiteFooter />
      <ScrollTopButton />
      <TabBar active={KEYS[index]} />

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
    </div>
  )
}
