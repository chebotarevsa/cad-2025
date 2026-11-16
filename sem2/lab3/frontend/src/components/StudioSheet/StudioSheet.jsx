import { useState, useEffect, useRef } from 'react'
import { createPortal } from 'react-dom'
import { X, ChevronLeft, ChevronRight, Image as ImageIcon, User } from 'lucide-react'
import { RichText } from '../RichTextEditor/RichTextEditor'
import angryImg from '../../assets/angry.png'

const SLIDES = 3
const CLOSE_THRESHOLD = 120
const SLIDE_INTERVAL = 6000

const BORDER = 'border-[calc(2px/(var(--ui-base)*var(--ui-zoom)))]'
const BOX = `h-80 w-full rounded-card ${BORDER} border-line bg-canvas sm:h-96 md:h-[30rem]`

const SLIDER_BOX = `aspect-video w-full overflow-hidden rounded-card ${BORDER} border-line bg-black/40`

const TEACHER_BOX = `mx-auto aspect-[3/4] w-56 overflow-hidden rounded-card ${BORDER} border-line bg-canvas sm:mx-0 sm:aspect-[16/21] sm:w-full`

const DESCRIPTION =
  'Здесь будет описание студии. https://emojipedia.org/pouting-face Каждый из нас понимает очевидную вещь: перспективное планирование способствует повышению качества поставленных обществом задач. Банальные, но неопровержимые выводы, а также явные признаки победы институционализации будут объявлены нарушающими общечеловеческие нормы этики и морали. Равным образом, консультация с широким активом играет определяющее значение для распределения внутренних резервов и ресурсов. Каждый из нас понимает очевидную вещь: перспективное планирование способствует повышению качества поставленных обществом задач. Банальные, но неопровержимые выводы, а также явные признаки победы институционализации будут объявлены нарушающими общечеловеческие нормы этики и морали. Равным образом, консультация с широким активом играет определяющее значение для распределения внутренних резервов и ресурсов. Каждый из нас понимает очевидную вещь: перспективное планирование способствует повышению качества поставленных обществом задач. Банальные, но неопровержимые выводы, а также явные признаки победы институционализации будут объявлены нарушающими общечеловеческие нормы этики и морали. Равным образом, консультация с широким активом играет определяющее значение для распределения внутренних резервов и ресурсов.'

export default function StudioSheet({ open, onClose, lesson, studios }) {
  const [slide, setSlide] = useState(0)
  const [shown, setShown] = useState(false)
  const [loading, setLoading] = useState(true)
  const [dragY, setDragY] = useState(0)
  const [dragging, setDragging] = useState(false)
  const [isDesktop, setIsDesktop] = useState(() => window.matchMedia('(min-width: 640px)').matches)
  const [fullIndex, setFullIndex] = useState(null)
  const startY = useRef(null)
  const scrollRef = useRef(null)
  const slideStartX = useRef(null)

  const studio = lesson && !lesson.special ? (studios || []).find((s) => s.code === lesson.studioCode) : null

  const photos = (lesson && lesson.special ? lesson.photos : studio?.photos) || []
  const slideCount = Math.max(photos.length, 1)

  useEffect(() => {
    const mq = window.matchMedia('(min-width: 640px)')
    const fn = () => setIsDesktop(mq.matches)
    mq.addEventListener('change', fn)
    return () => mq.removeEventListener('change', fn)
  }, [])

  useEffect(() => {
    if (open && lesson) {
      setSlide(0)
      setDragY(0)
      setFullIndex(null)
      setLoading(true)
      const raf = requestAnimationFrame(() => setShown(true))
      const t = setTimeout(() => setLoading(false), 700)
      return () => {
        cancelAnimationFrame(raf)
        clearTimeout(t)
      }
    }
    setShown(false)
  }, [open, lesson])

  useEffect(() => {
    if (!open || !shown || loading || slideCount <= 1) return
    const id = setTimeout(() => setSlide((s) => (s + 1) % slideCount), SLIDE_INTERVAL)
    return () => clearTimeout(id)
  }, [open, shown, loading, slide, slideCount])

  useEffect(() => {
    if (!open) return
    const prev = document.body.style.overflow
    document.body.style.overflow = 'hidden'
    return () => {
      document.body.style.overflow = prev
    }
  }, [open])


  useEffect(() => {
    if (fullIndex === null) return
    function onKey(e) {
      if (e.key === 'Escape') setFullIndex(null)
      else if (e.key === 'ArrowLeft') setFullIndex((i) => (i - 1 + slideCount) % slideCount)
      else if (e.key === 'ArrowRight') setFullIndex((i) => (i + 1) % slideCount)
    }
    window.addEventListener('keydown', onKey)
    return () => window.removeEventListener('keydown', onKey)
  }, [fullIndex, slideCount])

  if (!open || !lesson) return null

  function close() {
    setShown(false)
    setDragY(0)
    setTimeout(onClose, 300)
  }

  function onHeaderTouchStart(e) {
    startY.current = e.touches[0].clientY
    setDragging(true)
  }
  function onHeaderTouchMove(e) {
    if (startY.current === null) return
    const dy = e.touches[0].clientY - startY.current
    if (dy > 0) setDragY(dy)
  }

  function onContentTouchStart(e) {
    startY.current = e.touches[0].clientY
  }
  function onContentTouchMove(e) {
    if (startY.current === null) return
    const y = e.touches[0].clientY
    const atTop = (scrollRef.current?.scrollTop ?? 0) <= 0
    if (!atTop) {
      startY.current = y
      if (dragY) setDragY(0)
      if (dragging) setDragging(false)
      return
    }
    const dy = y - startY.current
    if (dy > 0) {
      if (!dragging) setDragging(true)
      setDragY(dy)
    } else if (dragY) {
      setDragY(0)
    }
  }
  function onTouchEnd() {
    setDragging(false)
    if (dragY > CLOSE_THRESHOLD) close()
    else setDragY(0)
    startY.current = null
  }

  function angryBurst(e) {
    const img = document.createElement('img')
    img.src = angryImg
    img.style.cssText =
      `position:fixed;left:${e.clientX}px;top:${e.clientY}px;width:34px;height:34px;` +
      'z-index:60;pointer-events:none;transform:translate(-50%,-50%);will-change:transform,opacity'
    document.body.appendChild(img)
    const dx = (Math.random() - 0.5) * 140
    const dy = (Math.random() < 0.5 ? -1 : 1) * (90 + Math.random() * 120)
    const rot = (Math.random() - 0.5) * 100
    const anim = img.animate(
      [
        { transform: 'translate(-50%,-50%) scale(0.5) rotate(0deg)', opacity: 1, offset: 0 },
        { opacity: 1, offset: 0.6 },
        {
          transform: `translate(calc(-50% + ${dx}px), calc(-50% + ${dy}px)) scale(1) rotate(${rot}deg)`,
          opacity: 0,
          offset: 1,
        },
      ],
      { duration: 900, easing: 'cubic-bezier(.2,.6,.3,1)' },
    )
    anim.onfinish = () => img.remove()
  }

  function sliderTouchStart(e) {
    e.stopPropagation()
    slideStartX.current = e.touches[0].clientX
  }
  function sliderTouchEnd(e) {
    if (slideStartX.current === null) return
    const dx = e.changedTouches[0].clientX - slideStartX.current
    if (dx < -40) setSlide((s) => (s + 1) % slideCount)
    else if (dx > 40) setSlide((s) => (s - 1 + slideCount) % slideCount)
    slideStartX.current = null
  }

  const fullPrev = () => setFullIndex((i) => (i - 1 + slideCount) % slideCount)
  const fullNext = () => setFullIndex((i) => (i + 1) % slideCount)

  const sheetStyle = isDesktop
    ? {
        zoom: 'calc(var(--ui-base) * var(--ui-zoom))',
        opacity: shown ? 1 : 0,
        transform: shown ? 'scale(1)' : 'scale(0.96)',
        transition: 'opacity 0.25s ease-out, transform 0.25s ease-out',
      }
    : {
        zoom: 'calc(var(--ui-base) * var(--ui-zoom))',
        transform: shown ? `translateY(${dragY}px)` : 'translateY(100%)',
        transition: dragging ? 'none' : 'transform 0.3s ease-out',
      }

  const arrow =
    'absolute top-1/2 grid size-9 -translate-y-1/2 place-items-center rounded-full bg-surface/80 text-ink shadow transition hover:bg-surface active:scale-95'

  const isEvent = !!lesson.special

  const sliderPhoto = (
      <div
        className={`relative touch-pan-y ${SLIDER_BOX}`}
        onTouchStart={sliderTouchStart}
        onTouchEnd={sliderTouchEnd}
      >
        {photos.length ? (
          <div
            className="flex h-full"
            style={{
              width: `${slideCount * 100}%`,
              transform: `translateX(-${(100 / slideCount) * (slide % slideCount)}%)`,
              transition: 'transform 0.45s ease',
            }}
          >
            {photos.map((src, i) => (
              <div key={i} className="h-full" style={{ width: `${100 / slideCount}%` }}>
                <img
                  src={src}
                  alt="Фото студии"
                  onClick={() => setFullIndex(i)}
                  className="h-full w-full cursor-zoom-in object-cover"
                />
              </div>
            ))}
          </div>
        ) : (
          <div className="grid h-full place-items-center text-muted">
            <ImageIcon className="size-12" strokeWidth={1.5} />
          </div>
        )}
        {slideCount > 1 && (
          <>
            <button
              type="button"
              className={arrow + ' left-2'}
              onClick={() => setSlide((s) => (s - 1 + slideCount) % slideCount)}
              aria-label="Предыдущее фото"
            >
              <ChevronLeft className="size-5" />
            </button>
            <button
              type="button"
              className={arrow + ' right-2'}
              onClick={() => setSlide((s) => (s + 1) % slideCount)}
              aria-label="Следующее фото"
            >
              <ChevronRight className="size-5" />
            </button>
            <div className="absolute bottom-3 left-1/2 flex -translate-x-1/2 gap-1.5">
              {Array.from({ length: slideCount }).map((_, i) => (
                <span
                  key={i}
                  className={'size-2 rounded-full ' + (i === slide % slideCount ? 'bg-brand' : 'bg-line')}
                />
              ))}
            </div>
          </>
        )}
      </div>
  )
  const sliderCaption = (
    <p className="mt-3 text-center text-sm text-muted">{isEvent ? 'Фото мероприятия' : 'Фото студии'}</p>
  )
  const sliderBlock = (
    <>
      {sliderPhoto}
      {sliderCaption}
    </>
  )

  const teacherPhoto = (
      <div
        onClick={angryBurst}
        className={`grid cursor-pointer place-items-center text-muted transition-transform active:scale-[0.99] ${TEACHER_BOX}`}
      >
        {studio?.teacherPhoto ? (
          <img src={studio.teacherPhoto} alt={lesson.teacherName} className="h-full w-full object-cover" />
        ) : (
          <User className="size-12" strokeWidth={1.5} />
        )}
      </div>
  )
  const teacherCaption = (
    <>
      <p className="mt-3 text-center text-[17px] leading-snug font-bold text-ink">{lesson.teacherName}</p>
      <p className="text-center text-sm text-muted">Преподаватель студии</p>
    </>
  )
  const teacherBlock = (
    <>
      {teacherPhoto}
      {teacherCaption}
    </>
  )

  const descriptionBlock = (
    <>
      <h3 className="text-lg font-bold text-ink sm:text-xl">{isEvent ? 'Мероприятие' : 'Описание'}</h3>
      {
                                                                          }
      {(isEvent ? lesson?.description : studio?.description) ? (
        <RichText
          value={isEvent ? lesson.description : studio.description}
          className="mt-2 text-[15px] leading-relaxed text-ink/80 sm:text-[17px] md:text-lg"
        />
      ) : (
        <p className="mt-2 text-[15px] leading-relaxed text-ink/80 sm:text-[17px] md:text-lg">
          {isEvent ? 'Описание появится позже.' : 'Описание студии появится позже.'}
        </p>
      )}
    </>
  )




  return createPortal(
    <>
    <div


      data-no-pull-to-refresh
      role="dialog"
      aria-modal="true"
      className="fixed inset-0 z-60 flex items-end justify-center bg-black/50 transition-opacity duration-300 sm:items-center sm:p-4"
      style={{ opacity: shown ? 1 : 0 }}
      onClick={close}
    >
      <div
        className={`flex h-[calc(88svh/(var(--ui-base)*var(--ui-zoom)))] w-full flex-col rounded-t-3xl ${BORDER} border-line bg-surface pb-[env(safe-area-inset-bottom)] shadow-2xl sm:h-auto sm:max-h-[calc(94svh/(var(--ui-base)*var(--ui-zoom)))] sm:max-w-[1500px] sm:rounded-3xl sm:pb-0`}
        style={sheetStyle}
        onClick={(e) => e.stopPropagation()}
      >

        <div
          className="shrink-0 touch-none px-6 pt-3 sm:pt-5"
          onTouchStart={onHeaderTouchStart}
          onTouchMove={onHeaderTouchMove}
          onTouchEnd={onTouchEnd}
        >
          <div className="mx-auto h-1.5 w-12 rounded-full bg-line sm:hidden" />
          <div className="mt-3 flex items-start justify-between gap-4 sm:mt-0">
            <h2 className="text-2xl font-bold text-ink sm:text-3xl md:text-4xl mb-3">{lesson.special && lesson.title ? lesson.title : lesson.studioName}</h2>
            <button
              type="button"
              onClick={close}
              aria-label="Закрыть"
              className="grid size-10 shrink-0 place-items-center rounded-full text-muted transition hover:text-ink active:scale-95"
            >
              <X className="size-6" />
            </button>
          </div>
        </div>

        {loading ? (

          (() => {
            const sliderSk = <div className={`${SLIDER_BOX} !border-line/50 bg-line/50`} />
            const teacherPhotoSk = <div className={`${TEACHER_BOX} !border-line/50 bg-line/50`} />
            const teacherCaptionSk = (
              <>
                <div className="mx-auto mt-3 h-5 w-3/4 rounded bg-line/50" />
                <div className="mx-auto mt-2 h-4 w-1/2 rounded bg-line/50" />
              </>
            )
            const teacherSk = (
              <div>
                {teacherPhotoSk}
                {teacherCaptionSk}
              </div>
            )
            const descSk = (
              <div>
                <div className="h-6 w-40 rounded bg-line/50 sm:h-8" />
                <div className="mt-3 space-y-2.5">
                  <div className="h-4 w-full rounded bg-line/50" />
                  <div className="h-4 w-full rounded bg-line/50" />
                  <div className="h-4 w-11/12 rounded bg-line/50" />
                  <div className="h-4 w-full rounded bg-line/50" />
                  <div className="h-4 w-4/5 rounded bg-line/50" />
                </div>
              </div>
            )
            return (
              <div className="flex-1 overflow-hidden px-6 pt-4">
                <div className="animate-pulse">
                  {isDesktop ? (

                    <>
                      <div className="flex items-stretch gap-4">
                        <div className={isEvent ? 'w-full' : 'flex-[7]'}>{sliderSk}</div>
                        {!isEvent && <div className="flex-[3] flex flex-col">{teacherPhotoSk}</div>}
                      </div>
                      <div className="flex gap-4">
                        <div className={isEvent ? 'w-full' : 'flex-[7]'}>
                          <div className="mx-auto mt-3 h-4 w-1/3 rounded bg-line/50" />
                        </div>
                        {!isEvent && <div className="flex-[3]">{teacherCaptionSk}</div>}
                      </div>
                      <div className="mt-6">{descSk}</div>
                    </>
                  ) : isEvent ? (

                    <div className="flex flex-col gap-4">
                      {sliderSk}
                      {descSk}
                    </div>
                  ) : (

                    <div className="flex flex-col gap-6">
                      {teacherSk}
                      {descSk}
                      {sliderSk}
                    </div>
                  )}
                </div>
              </div>
            )
          })()
        ) : isDesktop ? (

          <div className="flex-1 overflow-x-hidden overflow-y-auto px-6 pt-4 pb-12">
            <div className="flex items-stretch gap-4">
              <div className={isEvent ? 'w-full min-w-0' : 'flex-[7] min-w-0'}>{sliderPhoto}</div>
              {!isEvent && <div className="flex-[3] min-w-0 flex flex-col">{teacherPhoto}</div>}
            </div>
            <div className="flex gap-4">
              <div className={isEvent ? 'w-full min-w-0' : 'flex-[7] min-w-0'}>{sliderCaption}</div>
              {!isEvent && <div className="flex-[3] min-w-0">{teacherCaption}</div>}
            </div>
            <div className="mt-6 min-w-0">{descriptionBlock}</div>
          </div>
        ) : (

          <div
            ref={scrollRef}
            className="flex-1 overflow-x-hidden overflow-y-auto overscroll-y-contain px-6 pt-4 pb-28"
            onTouchStart={onContentTouchStart}
            onTouchMove={onContentTouchMove}
            onTouchEnd={onTouchEnd}
          >
            <div className="flex flex-col gap-4">
              {isEvent ? (
                <>
                  <div>{sliderBlock}</div>
                  <div>{descriptionBlock}</div>
                </>
              ) : (
                <>
                  <div className="mb-6">{teacherBlock}</div>
                  <div>{descriptionBlock}</div>
                  <div>{sliderBlock}</div>
                </>
              )}
            </div>
          </div>
        )}
      </div>
    </div>

    {fullIndex !== null && photos[fullIndex] && (
      <div
        data-no-pull-to-refresh
        role="dialog"
        aria-modal="true"
        className="fixed inset-0 z-[70] grid place-items-center bg-black/90 p-4"
        onClick={() => setFullIndex(null)}
      >
        <img src={photos[fullIndex]} alt="Фото" className="max-h-full max-w-full object-contain" onClick={(e) => e.stopPropagation()} />

        {slideCount > 1 && (
          <>
            <button
              type="button"
              onClick={(e) => { e.stopPropagation(); fullPrev() }}
              aria-label="Предыдущее фото"
              className="absolute top-1/2 left-4 grid size-12 -translate-y-1/2 place-items-center rounded-full bg-white/15 text-white transition hover:bg-white/25"
            >
              <ChevronLeft className="size-7" />
            </button>
            <button
              type="button"
              onClick={(e) => { e.stopPropagation(); fullNext() }}
              aria-label="Следующее фото"
              className="absolute top-1/2 right-4 grid size-12 -translate-y-1/2 place-items-center rounded-full bg-white/15 text-white transition hover:bg-white/25"
            >
              <ChevronRight className="size-7" />
            </button>
            <div className="absolute bottom-5 left-1/2 flex -translate-x-1/2 gap-1.5">
              {photos.map((_, i) => (
                <span key={i} className={'size-2 rounded-full ' + (i === fullIndex ? 'bg-white' : 'bg-white/40')} />
              ))}
            </div>
          </>
        )}

        <button
          type="button"
          onClick={() => setFullIndex(null)}
          aria-label="Закрыть"
          className="absolute top-[calc(env(safe-area-inset-top)+1rem)] right-[calc(env(safe-area-inset-right)+1rem)] z-10 grid size-12 place-items-center rounded-full bg-white/20 text-white shadow-lg transition hover:bg-white/30 active:scale-95"
        >
          <X className="size-7" />
        </button>
      </div>
    )}
    </>,
    document.body,
  )
}
