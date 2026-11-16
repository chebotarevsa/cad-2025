import { lazy, Suspense, useEffect, useRef, useState } from 'react'
import { Routes, Route, Navigate, useLocation } from 'react-router-dom'
import { RefreshCw } from 'lucide-react'
import SchedulePage from './pages/SchedulePage/SchedulePage'
import MainTabs from './pages/MainTabs/MainTabs'
import AuthPage from './pages/AuthPage/AuthPage'
import DashboardPage from './pages/DashboardPage/DashboardPage'
import WelcomeModal from './components/WelcomeModal/WelcomeModal'
import Particles from './components/Particles/Particles'
import WarmBackdrop from './components/WarmBackdrop/WarmBackdrop'
import { getUser } from './auth'
import { getDefaultSelection, defaultSelectionPath, getLastSelection } from './defaultSelection'

const WELCOME_KEY = 'welcome-seen-v1'
const MapPage = lazy(() => import('./pages/MapPage/MapPage'))
const PULL_TO_REFRESH_THRESHOLD = 110
const PULL_TO_REFRESH_KEY = 'pull-to-refresh-active'




function useStandalonePullToRefresh(enabled = true) {
  const gestureRef = useRef(null)
  const [resumed] = useState(() => sessionStorage.getItem(PULL_TO_REFRESH_KEY) === '1')
  const [pullDistance, setPullDistance] = useState(
    resumed ? PULL_TO_REFRESH_THRESHOLD : 0,
  )
  const [refreshing, setRefreshing] = useState(resumed)

  useEffect(() => {
    let reloadTimer = 0
    const finishTimer = resumed
      ? window.setTimeout(() => {
          sessionStorage.removeItem(PULL_TO_REFRESH_KEY)
          setRefreshing(false)
          setPullDistance(0)
        }, 1200)
      : 0

    if (!enabled) return () => clearTimeout(finishTimer)

    const isStandalone =
      window.matchMedia('(display-mode: standalone)').matches ||
      window.navigator.standalone === true
    const isTouchDevice = navigator.maxTouchPoints > 0 || 'ontouchstart' in window
    if (!isStandalone || !isTouchDevice) {
      return () => clearTimeout(finishTimer)
    }

    const reset = (hideIndicator = true) => {
      gestureRef.current = null
      if (hideIndicator) setPullDistance(0)
    }

    const handleTouchStart = (event) => {
      const target = event.target
      const ignorePullToRefresh =
        target instanceof Element && target.closest('[data-no-pull-to-refresh]')

      if (ignorePullToRefresh || event.touches.length !== 1 || window.scrollY > 0) {
        reset()
        return
      }

      const touch = event.touches[0]
      gestureRef.current = {
        startX: touch.clientX,
        startY: touch.clientY,
        distance: 0,
      }
    }

    const handleTouchMove = (event) => {
      const gesture = gestureRef.current
      if (!gesture) return
      if (event.touches.length !== 1) {
        reset()
        return
      }

      const touch = event.touches[0]
      const deltaX = touch.clientX - gesture.startX
      const deltaY = touch.clientY - gesture.startY

      if (Math.abs(deltaX) > Math.abs(deltaY) + 8 || deltaY <= 0) {
        reset()
        return
      }

      gesture.distance = deltaY
      setPullDistance(deltaY)
    }

    const handleTouchEnd = () => {
      const shouldRefresh = gestureRef.current?.distance >= PULL_TO_REFRESH_THRESHOLD
      reset(!shouldRefresh)
      if (!shouldRefresh) return

      sessionStorage.setItem(PULL_TO_REFRESH_KEY, '1')
      setRefreshing(true)
      setPullDistance(PULL_TO_REFRESH_THRESHOLD)


      reloadTimer = window.setTimeout(() => window.location.reload(), 300)
    }

    document.addEventListener('touchstart', handleTouchStart, { passive: true })
    document.addEventListener('touchmove', handleTouchMove, { passive: true })
    document.addEventListener('touchend', handleTouchEnd, { passive: true })
    document.addEventListener('touchcancel', reset, { passive: true })

    return () => {
      document.removeEventListener('touchstart', handleTouchStart)
      document.removeEventListener('touchmove', handleTouchMove)
      document.removeEventListener('touchend', handleTouchEnd)
      document.removeEventListener('touchcancel', reset)
      clearTimeout(reloadTimer)
      clearTimeout(finishTimer)
    }
  }, [enabled, resumed])

  return { pullDistance, refreshing }
}

function PullToRefreshIndicator({ pullDistance, refreshing }) {
  const visible = refreshing || pullDistance > 8
  const offsetY = Math.min(12, pullDistance * 0.45 - 36)
  const rotation = Math.min(300, (pullDistance / PULL_TO_REFRESH_THRESHOLD) * 300)

  return (
    <div
      role="status"
      aria-label={refreshing ? 'Обновление данных' : 'Потяните для обновления'}
      aria-hidden={!visible}
      className="pointer-events-none fixed left-1/2 z-50 grid size-10 place-items-center rounded-full border-2 border-line bg-surface text-brand shadow-lg transition-[opacity,transform] duration-150"
      style={{
        top: 'var(--header-h, 96px)',
        opacity: visible ? 1 : 0,
        transform: `translate(-50%, ${offsetY}px)`,
      }}
    >
      <RefreshCw
        className={'size-5 ' + (refreshing ? 'animate-spin' : '')}
        style={refreshing ? undefined : { transform: `rotate(${rotation}deg)` }}
      />
    </div>
  )
}

function RequireAuth({ children }) {
  return getUser() ? children : <Navigate to="/login" replace />
}

function GuestOnly({ children }) {
  return getUser() ? <Navigate to="/dashboard" replace /> : children
}



function Home() {


  const path = defaultSelectionPath(getDefaultSelection() || getLastSelection())
  return path ? <Navigate to={path} replace /> : <MainTabs />
}

export default function App() {
  const location = useLocation()
  const isMap = location.pathname === '/map' || location.pathname === '/map/'
  const pullRefresh = useStandalonePullToRefresh(!isMap)
  const isPublic = !isMap && !location.pathname.startsWith('/login') && !location.pathname.startsWith('/dashboard')
  const [welcomeOpen, setWelcomeOpen] = useState(() => !localStorage.getItem(WELCOME_KEY))

  function closeWelcome() {
    localStorage.setItem(WELCOME_KEY, '1')
    setWelcomeOpen(false)
  }

  return (
    <>
    {!isMap && <PullToRefreshIndicator {...pullRefresh} />}
    {!isMap && <Particles />}
    {!isMap && <WarmBackdrop />}

    <Routes>
      <Route path="/map" element={<Suspense fallback={null}><MapPage /></Suspense>} />
      {
                                                                         }
      <Route path="/" element={<Home />} />
      <Route path="/group/:number" element={<MainTabs />} />
      <Route path="/teacher/:teacherId" element={<MainTabs />} />
      <Route path="/studios" element={<MainTabs />} />
      <Route path="/events" element={<MainTabs />} />
      <Route path="/login" element={<GuestOnly><AuthPage /></GuestOnly>} />
      <Route
        path="/dashboard"
        element={
          <RequireAuth>
            <DashboardPage />
          </RequireAuth>
        }
      />
      <Route
        path="/dashboard/group/:number"
        element={
          <RequireAuth>
            <SchedulePage base="/dashboard" />
          </RequireAuth>
        }
      />
      <Route
        path="/dashboard/teacher/:teacherId"
        element={
          <RequireAuth>
            <SchedulePage base="/dashboard" />
          </RequireAuth>
        }
      />
    </Routes>

    {isPublic && <WelcomeModal open={welcomeOpen} onClose={closeWelcome} />}
    </>
  )
}
