import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { X, Bell, BellOff, BellRing, Info, Trash2, Moon, Scaling, CircleUserRound, LogOut, LoaderCircle, Star, Users, User, Sparkles } from 'lucide-react'
import { getBgEffects, setBgEffects } from '../../useBgEffects'
import { getUser, clearUser } from '../../auth'
import { useBodyScrollLock } from '../../useBodyScrollLock'
import { enablePush, getPushState } from '../../push'
import { getDefaultSelection, setDefaultSelection } from '../../defaultSelection'
import { normalizeGroupNumber, shortName } from '../../utils'



export default function SettingsModal({ open, onClose, theme, onToggleTheme, zoom, onZoomChange, groups = [], teachers = [], showTheme = true }) {
  const navigate = useNavigate()
  useBodyScrollLock(open)

  const [pushState, setPushState] = useState('loading')
  const [pushBusy, setPushBusy] = useState(false)
  const [pushError, setPushError] = useState(null)

  const [bgOn, setBgOn] = useState(getBgEffects)
  const [defaultSel, setDefaultSel] = useState(() => getDefaultSelection())
  const [pickQuery, setPickQuery] = useState('')
  const [pickOpen, setPickOpen] = useState(false)

  useEffect(() => {
    if (!open) return
    setPushError(null)
    getPushState().then(setPushState).catch(() => setPushState('unsupported'))
    setDefaultSel(getDefaultSelection())
    setBgOn(getBgEffects())
    setPickQuery('')
    setPickOpen(false)
  }, [open])

  const pq = pickQuery.trim()
  let pickSuggestions = []
  if (pq) {
    const groupQuery = normalizeGroupNumber(pq.replace(/\s+групп[аы]?$/iu, ''))
    const lq = pq.toLocaleLowerCase('ru-RU')
    const groupItems = groups
      .filter((number) => normalizeGroupNumber(number).includes(groupQuery))
      .map((number) => ({ key: `g${number}`, icon: Users, label: `${number} группа`, sel: { type: 'group', value: number, label: `${number} группа` } }))
    const teacherItems = teachers
      .filter((t) => t.fullName.toLocaleLowerCase('ru-RU').includes(lq))
      .map((t) => ({ key: `t${t.id}`, icon: User, label: t.fullName, sel: { type: 'teacher', value: t.id, label: t.fullName } }))
    pickSuggestions = [...groupItems, ...teacherItems].slice(0, 8)
  }

  function chooseDefault(sel) {
    setDefaultSelection(sel)
    setDefaultSel(sel)
    setPickQuery('')
    setPickOpen(false)
  }

  function clearDefault() {
    setDefaultSelection(null)
    setDefaultSel(null)
  }

  async function handleEnablePush() {
    setPushBusy(true)
    setPushError(null)
    try {
      const state = await enablePush()
      setPushState(state)
    } catch (err) {
      setPushError(err.message)
    } finally {
      setPushBusy(false)
    }
  }

  if (!open) return null
  const dark = theme === 'dark'
  const loggedIn = !!getUser()

  const stubBtn =
    'flex w-full items-center gap-3 rounded-card border-2 border-line bg-canvas px-4 py-4 text-left text-base text-ink transition-colors disabled:cursor-not-allowed disabled:opacity-50'

  const activeBtn = stubBtn + ' cursor-pointer hover:border-brand hover:text-brand'

  function openAccount() {
    onClose()
    navigate('/dashboard', { state: { showAccount: true } })
  }

  function logout() {
    clearUser()
    onClose()
    navigate('/login')
  }

  return (

    <div className="fixed inset-0 z-40 grid animate-fade-in place-items-center bg-black/50 p-4">
      <div className="w-full max-w-md animate-fade-up rounded-card border-2 border-line bg-surface p-6 shadow-xl">
        <div className="mb-5 flex items-center justify-between">
          <h2 className="text-2xl font-semibold text-ink">Настройки</h2>
          <button
            type="button"
            onClick={onClose}
            aria-label="Закрыть"
            className="grid size-9 place-items-center rounded-md text-muted transition-colors hover:text-ink"
          >
            <X className="size-6" />
          </button>
        </div>

        {showTheme && (
          <div className="mb-3 flex items-center justify-between rounded-card border-2 border-line bg-canvas px-4 py-4">
            <span className="flex items-center gap-3 text-base text-ink">
              <Moon className="size-5 text-muted" /> Тёмная тема
            </span>
            <button
              type="button"
              role="switch"
              aria-checked={dark}
              onClick={onToggleTheme}
              className={
                'relative h-7 w-12 shrink-0 rounded-full transition-colors ' +
                (dark ? 'bg-brand' : 'bg-line')
              }
            >
              <span
                className={
                  'absolute top-1 size-5 rounded-full bg-white transition-all ' +
                  (dark ? 'left-6' : 'left-1')
                }
              />
            </button>
          </div>
        )}

        <div className="mb-3 flex items-center justify-between rounded-card border-2 border-line bg-canvas px-4 py-4">
          <span className="flex items-center gap-3 text-base text-ink">
            <Sparkles className="size-5 text-muted" /> Живые обои
          </span>
          <button
            type="button"
            role="switch"
            aria-checked={bgOn}
            onClick={() => {
              setBgEffects(!bgOn)
              setBgOn(!bgOn)
            }}
            className={
              'relative h-7 w-12 shrink-0 rounded-full transition-colors ' + (bgOn ? 'bg-brand' : 'bg-line')
            }
          >
            <span
              className={
                'absolute top-1 size-5 rounded-full bg-white transition-all ' + (bgOn ? 'left-6' : 'left-1')
              }
            />
          </button>
        </div>

        <div className="rounded-card border-2 border-line bg-canvas px-4 py-4">
          <div className="mb-3 flex items-center justify-between">
            <span className="flex items-center gap-3 text-base text-ink">
              <Scaling className="size-5 text-muted" /> Масштаб
            </span>
            <span className="text-base text-muted">{Math.round(zoom * 100)}%</span>
          </div>
          <input
            type="range"
            min="0.7"
            max="1.3"
            step="0.05"
            value={zoom}
            onChange={(e) => onZoomChange(Number(e.target.value))}
            className="w-full cursor-pointer accent-brand"
          />
        </div>

        {(groups.length > 0 || teachers.length > 0) && (
          <div className="mt-3 rounded-card border-2 border-line bg-canvas px-4 py-4">
            <div className="mb-3 flex items-center gap-3 text-base text-ink">
              <Star className="size-5 text-muted" /> Открывать при входе
            </div>

            {defaultSel ? (
              <div className="flex items-center justify-between gap-3 rounded-card border-2 border-brand/50 bg-surface px-4 py-3">
                <span className="flex min-w-0 items-center gap-2 text-base text-ink">
                  {defaultSel.type === 'teacher'
                    ? <User className="size-5 shrink-0 text-brand" />
                    : <Users className="size-5 shrink-0 text-brand" />}
                  <span className="truncate">
                    {defaultSel.type === 'teacher' ? shortName(defaultSel.label) : defaultSel.label}
                  </span>
                </span>
                <button
                  type="button"
                  onClick={clearDefault}
                  aria-label="Сбросить выбор по умолчанию"
                  className="grid size-8 shrink-0 place-items-center rounded-md bg-canvas text-red-400 transition-colors hover:text-red-500"
                >
                  <X className="size-5" />
                </button>
              </div>
            ) : (
              <div className="relative">
                <input
                  value={pickQuery}
                  onChange={(e) => { setPickQuery(e.target.value); setPickOpen(true) }}
                  onFocus={() => setPickOpen(true)}
                  onBlur={() => setTimeout(() => setPickOpen(false), 120)}
                  placeholder="№ группы или имя преподавателя"
                  className="w-full rounded-card border-2 border-line bg-surface px-4 py-3 text-base text-ink outline-none placeholder:text-muted/70 focus:border-brand"
                />
                {pickOpen && pickSuggestions.length > 0 && (
                  <ul className="absolute right-0 left-0 z-10 mt-2 max-h-60 overflow-auto rounded-card border-2 border-line bg-surface shadow-lg">
                    {pickSuggestions.map((item) => {
                      const Icon = item.icon
                      return (
                        <li key={item.key} className="border-line/60 [&:not(:last-child)]:border-b">
                          <button
                            type="button"
                            onMouseDown={() => chooseDefault(item.sel)}
                            className="flex w-full items-center gap-3 px-4 py-3 text-left text-base text-ink transition-colors hover:bg-canvas"
                          >
                            <Icon className="size-5 shrink-0 text-muted" /> {item.label}
                          </button>
                        </li>
                      )
                    })}
                  </ul>
                )}
              </div>
            )}
          </div>
        )}

        <div className="mt-4 flex flex-col gap-3">

          {pushState === 'subscribed' ? (
            <div className="flex w-full items-center gap-3 rounded-card border-2 border-green-500/40 bg-green-500/10 px-4 py-4 text-base text-green-700 dark:text-green-300">
              <BellRing className="size-5" /> Уведомления включены
            </div>
          ) : pushState === 'denied' ? (
            <div className="flex w-full items-center gap-3 rounded-card border-2 border-line bg-canvas px-4 py-4 text-base text-muted">
              <BellOff className="size-5" /> Уведомления заблокированы в браузере
            </div>
          ) : pushState === 'unsupported' ? (
            <div className="flex w-full items-center gap-3 rounded-card border-2 border-line bg-canvas px-4 py-4 text-base text-muted">
              <BellOff className="size-5" /> Уведомления не поддерживаются
            </div>
          ) : (
            <button
              type="button"
              onClick={handleEnablePush}
              disabled={pushBusy || pushState === 'loading'}
              className="flex w-full items-center gap-3 rounded-card border-2 border-line bg-canvas px-4 py-4 text-left text-base text-ink transition-colors hover:border-brand hover:text-brand disabled:opacity-60"
            >
              {pushBusy ? <LoaderCircle className="size-5 animate-spin text-muted" /> : <Bell className="size-5 text-muted" />}
              {pushBusy ? 'Включаем…' : 'Включить уведомления о мероприятиях'}
            </button>
          )}
          {pushError && <p className="-mt-1 px-1 text-sm text-red-500">{pushError}</p>}

          {loggedIn && (
            <button
              type="button"
              onClick={logout}
              className="flex w-full items-center gap-3 rounded-card border-2 border-line bg-canvas px-4 py-4 text-left text-base text-red-500 transition-colors hover:border-red-400 hover:text-red-600"
            >
              <LogOut className="size-5" /> Выйти
            </button>
          )}
        </div>
      </div>
    </div>
  )
}
