import { useState, useEffect, Fragment } from 'react'
import AppHeader from '../../components/AppHeader/AppHeader'
import TabBar from '../../components/TabBar/TabBar'
import SettingsModal from '../../components/SettingsModal/SettingsModal'
import StudioSheet from '../../components/StudioSheet/StudioSheet'
import SiteFooter from '../../components/SiteFooter/SiteFooter'
import { fetchAllLessons, fetchStudios } from '../../api'
import { getCached, loadCached } from '../../cache'
import { useUiSettings } from '../../useUiSettings'
import { useSwipeTabs } from '../../useSwipeTabs'
import { hhmm } from '../../utils'
import { MONTHS_NOM, parseIsoLocal, startOfDay } from '../../dates'

const MONTH_SHORT = MONTHS_NOM.map((m) => m.slice(0, 3).toLowerCase())


export default function EventsPage({ embedded = false }) {
  const { theme, toggleTheme, zoom, setZoom } = useUiSettings()
  useSwipeTabs('/events')

  const [events, setEvents] = useState(() => getCached('events') ?? [])
  const [loading, setLoading] = useState(() => !getCached('events'))
  const [studios, setStudios] = useState(() => getCached('studios') ?? [])
  const [settingsOpen, setSettingsOpen] = useState(false)
  const [openEvent, setOpenEvent] = useState(null)

  useEffect(() => {
    loadCached('events', () =>
      fetchAllLessons().then((all) =>
        all.filter((l) => l.special && l.date).sort((a, b) => a.date.localeCompare(b.date)),
      ),
    )
      .then(setEvents)
      .catch(() => setEvents([]))
      .finally(() => setLoading(false))

    loadCached('studios', fetchStudios).then(setStudios).catch(() => setStudios([]))
  }, [])









  const today = startOfDay(new Date())
  const windowEnd = new Date(today.getFullYear(), today.getMonth() + 2, 1)

  const upcoming = events.filter((e) => {
    const d = parseIsoLocal(e.date)
    return d >= today && d < windowEnd
  })


  const hasLater = events.some((e) => parseIsoLocal(e.date) >= windowEnd)




  const months = []
  upcoming.forEach((e, i) => {
    const d = parseIsoLocal(e.date)
    const key = `${d.getFullYear()}-${d.getMonth()}`
    let group = months.find((m) => m.key === key)
    if (!group) {
      group = { key, title: `${MONTHS_NOM[d.getMonth()]} ${d.getFullYear()}`, items: [] }
      months.push(group)
    }


    group.items.push({ event: e, index: i })
  })

  return (
    <div className={embedded ? 'w-full' : 'flex min-h-[100dvh] flex-col [--ui-base:1]'}>
      {!embedded && (
        <AppHeader
          title="События"
          theme={theme}
          onToggleTheme={toggleTheme}
          onOpenSettings={() => setSettingsOpen(true)}
        />
      )}

      <main className="flex-1">
        <div className="mx-auto w-full max-w-[772px] px-3 pt-[18px] pb-10 md:px-6 [zoom:calc(var(--ui-base)*var(--ui-zoom))]">
          {loading ? (
            <div className="flex animate-pulse flex-col gap-3">
              {Array.from({ length: 4 }).map((_, i) => (
                <div
                  key={i}
                  className="flex items-stretch gap-4 rounded-card border-[calc(2px/(var(--ui-base)*var(--ui-zoom)))] border-line px-4 py-4"
                >
                  <div className="h-[58px] w-[62px] shrink-0 rounded-tile bg-line/50" />
                  <div className="flex flex-1 flex-col justify-center gap-2">
                    <div className="h-4 w-2/3 rounded bg-line/50" />
                    <div className="h-3 w-1/2 rounded bg-line/50" />
                  </div>
                </div>
              ))}
            </div>
          ) : upcoming.length === 0 ? (
            <div className="rounded-card border-[calc(2px/(var(--ui-base)*var(--ui-zoom)))] border-dashed border-line px-5 py-10 text-center text-base text-muted">
              В ближайшие время мероприятий нет
            </div>
          ) : (
            <div className="flex flex-col gap-3">
              {months.map((month, mi) => (
                <Fragment key={month.key}>
                  {
                                                                }
                  <p
                    className={
                      (mi > 0 ? 'mt-5 ' : '') +
                      'mb-1 text-center text-[13px] font-bold tracking-[0.11em] text-muted uppercase'
                    }
                  >
                    {month.title}
                  </p>
                  {month.items.map(({ event: e, index: i }) => {
                    const d = parseIsoLocal(e.date)
                    return (
                  <article
                    key={e.id}
                    style={{ animationDelay: `${i * 60}ms` }}
                    onClick={() => setOpenEvent(e)}
                    className="flex animate-fade-up cursor-pointer items-start gap-4 rounded-card border-[calc(2px/(var(--ui-base)*var(--ui-zoom)))] border-transparent px-4 py-4 transition select-none hover:-translate-y-0.5 [background:linear-gradient(var(--color-surface),var(--color-surface))_padding-box,linear-gradient(135deg,var(--color-brand-ring),var(--color-brand))_border-box]"
                  >
                    {
                                                                               }
                    <div className="flex w-[62px] shrink-0 flex-col items-center justify-center rounded-tile border-[calc(2px/(var(--ui-base)*var(--ui-zoom)))] border-line bg-canvas py-2 tabular-nums">
                      <b className="text-xl font-extrabold tracking-tight text-ink">{d.getDate()}</b>
                      <i className="text-[11px] font-bold tracking-[0.1em] text-muted uppercase not-italic">
                        {MONTH_SHORT[d.getMonth()]}
                      </i>
                    </div>

                    <div className="flex min-w-0 flex-col justify-center gap-1">
                      {
                                                                                     }
                      <h3 className="text-[18px] leading-snug font-bold text-ink">
                        <span className="mr-1.5 text-[1.15em] text-brand">✦</span>
                        {e.title || e.studioName}
                      </h3>
                      <p className="text-sm leading-snug text-muted">
                        {hhmm(e.time)}
                        {e.endTime ? ` – ${hhmm(e.endTime)}` : ''} · {e.studioName}
                      </p>
                    </div>
                  </article>
                    )
                  })}
                </Fragment>
              ))}

              {
                                                                      }
              {hasLater && (
                <div className="mt-2 rounded-card border-[calc(2px/(var(--ui-base)*var(--ui-zoom)))] border-dashed border-line px-5 py-6 text-center text-base text-muted">
                  Дальше тоже запланированы мероприятия – появятся здесь ближе к дате
                </div>
              )}
            </div>
          )}
        </div>
      </main>

      {!embedded && (
        <>
          <SiteFooter />
          <TabBar active="events" />
          <SettingsModal
            open={settingsOpen}
            onClose={() => setSettingsOpen(false)}
            theme={theme}
            onToggleTheme={toggleTheme}
            zoom={zoom}
            onZoomChange={setZoom}
            showTheme={false}
          />
        </>
      )}

      <StudioSheet
        open={!!openEvent}
        onClose={() => setOpenEvent(null)}
        lesson={openEvent}
        studios={studios}
      />
    </div>
  )
}
