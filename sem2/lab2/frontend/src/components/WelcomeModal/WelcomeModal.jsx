import { useState } from 'react'
import {
  X,
  ChevronRight,
  ChevronLeft,
  Check,
  CalendarDays,
  PartyPopper,
  Bell,
  Smartphone,
  Globe,
  ArrowLeft,
} from 'lucide-react'
import Logo from '../Logo/Logo'
import { useBodyScrollLock } from '../../useBodyScrollLock'

import ios1 from '../../assets/ios/1.jpg'
import ios2 from '../../assets/ios/2.jpg'
import ios3 from '../../assets/ios/3.jpg'
import ios4 from '../../assets/ios/4.jpg'
import android1 from '../../assets/android/1.jpg'
import android2 from '../../assets/android/2.jpg'
import android3 from '../../assets/android/3.jpg'
import android4 from '../../assets/android/4.jpg'

const GUIDES = {
  ios: {
    label: 'iPhone · iOS',
    hint: 'Открывайте сайт именно в Safari — в других браузерах кнопки установки не будет.',
    steps: [
      { text: 'Откройте сайт в Safari и нажмите кнопку «Поделиться» в нижней панели браузера.', img: ios1 },
      { text: 'Пролистайте меню вниз и выберите «Добавить на экран „Домой“».', img: ios2 },
      { text: 'Нажмите «Добавить» в правом верхнем углу.', img: ios3 },
      { text: 'Готово! Значок «РасписаШКИ» появился на экране «Домой».', img: ios4 },
    ],
  },
  android: {
    label: 'Android',
    hint: 'Шаги показаны для встроенного браузера, в Chrome пункт называется «Установить приложение».',
    steps: [
      { text: 'Нажмите кнопку меню браузера (три точки).', img: android1 },
      { text: 'Выберите пункт «Добавить страницу».', img: android2 },
      { text: 'Выберите «Главный экран».', img: android3 },
      { text: 'Готово! Значок «РасписаШКИ» появился на главном экране.', img: android4 },
    ],
  },
}

const FEATURES = [
  { icon: CalendarDays, text: 'Смотрите расписание своей группы и преподавателей' },
  { icon: PartyPopper, text: 'Узнавайте о мероприятиях и событиях школы' },
  { icon: Bell, text: 'Будьте в курсе — вся школа в одном тапе' },
]

export default function WelcomeModal({ open, onClose }) {
  const [stage, setStage] = useState('welcome')
  const [os, setOs] = useState('ios')
  const [step, setStep] = useState(0)

  useBodyScrollLock(open)
  if (!open) return null

  const guide = GUIDES[os]

  function openGuide(nextOs) {
    setOs(nextOs)
    setStep(0)
    setStage('guide')
  }

  const primaryBtn =
    'flex h-12 w-full items-center justify-center gap-2 rounded-card border-2 border-brand-ring bg-gradient-to-r from-brand-light to-brand text-base font-semibold whitespace-nowrap text-white transition hover:opacity-90 active:scale-[0.98]'
  const ghostBtn =
    'flex h-12 items-center justify-center gap-2 rounded-card border-2 border-line bg-canvas px-5 text-base font-medium whitespace-nowrap text-ink transition hover:border-brand hover:text-brand active:scale-[0.98]'

  return (
    <div className="fixed inset-0 z-50 grid animate-fade-in place-items-center bg-black/60 p-4">
      <div className="flex max-h-[92svh] w-full max-w-lg animate-fade-up flex-col overflow-hidden rounded-2xl border-2 border-line bg-surface shadow-2xl">

        <div className="relative flex shrink-0 items-center justify-between px-5 py-4 text-ink">
          <h2 className="text-lg font-bold">
            {stage === 'welcome' ? 'Добро пожаловать!' : stage === 'choose' ? 'Установить приложение' : `Установка · ${guide.label}`}
          </h2>
          <button
            type="button"
            onClick={onClose}
            aria-label="Закрыть"
            className="grid size-9 place-items-center rounded-full text-muted transition hover:bg-line/40 hover:text-ink active:scale-95"
          >
            <X className="size-5" />
          </button>
        </div>

        <div className="flex-1 overflow-y-auto px-5 py-6">
          {stage === 'welcome' && (
            <div className="flex flex-col items-center text-center">
              <Logo to="#" />
              <p className="mt-5 text-base leading-relaxed text-ink/80">
                «РасписаШКИ» — удобное расписание для учеников Школы креативных индустрий г. Строитель.
              </p>
              <div className="mt-6 flex w-full flex-col gap-3 text-left">
                {FEATURES.map(({ icon: Icon, text }) => (
                  <div
                    key={text}
                    className="flex items-center gap-3 rounded-card border-2 border-line bg-canvas px-4 py-3"
                  >
                    <span className="grid size-10 shrink-0 place-items-center rounded-full bg-brand/15 text-brand">
                      <Icon className="size-5" />
                    </span>
                    <span className="text-base text-ink">{text}</span>
                  </div>
                ))}
              </div>
            </div>
          )}

          {stage === 'choose' && (
            <div className="flex flex-col gap-4">
              <p className="text-center text-base text-ink/80">
                Добавьте «РасписаШКИ» на рабочий стол — будет открываться как обычное приложение, в один клик.
                <br />
                Какое у вас устройство?
              </p>
              <div className="flex flex-col gap-3">
                <DeviceButton icon={Smartphone} title="iPhone · iPad" subtitle="iOS · Safari" onClick={() => openGuide('ios')} />
                <DeviceButton icon={Smartphone} title="Android" subtitle="Chrome и др." onClick={() => openGuide('android')} />
                <DeviceButton icon={Globe} title="Просто открыть сайт" subtitle="Без установки" onClick={onClose} />
              </div>
            </div>
          )}

          {stage === 'guide' && (
            <div>
              <button
                type="button"
                onClick={() => setStage('choose')}
                className="mb-4 inline-flex items-center gap-1.5 text-sm font-medium text-muted transition hover:text-brand"
              >
                <ArrowLeft className="size-4" /> Другое устройство
              </button>

              <ol className="relative">
                {guide.steps.map((s, i) => {
                  const done = i < step
                  const current = i === step
                  const last = i === guide.steps.length - 1
                  return (
                    <li key={i} className="relative flex gap-4 pb-6 last:pb-0">
                      {!last && (
                        <span
                          className={
                            'absolute top-9 bottom-1 left-[17px] w-0.5 ' + (done ? 'bg-brand' : 'bg-line')
                          }
                        />
                      )}
                      <span
                        className={
                          'z-10 grid size-9 shrink-0 place-items-center rounded-full text-sm font-bold transition-colors ' +
                          (done
                            ? 'bg-brand text-white'
                            : current
                              ? 'bg-brand text-white ring-4 ring-brand/20'
                              : 'border-2 border-line bg-canvas text-muted')
                        }
                      >
                        {done ? <Check className="size-5" /> : i + 1}
                      </span>
                      <div className="flex-1 pt-1">
                        <p
                          className={
                            'text-base ' +
                            (current ? 'font-medium text-ink' : done ? 'text-muted' : 'text-muted/60')
                          }
                        >
                          {s.text}
                        </p>
                        {current && (
                          <img
                            src={s.img}
                            alt={`Шаг ${i + 1}`}
                            className="mt-3 w-full rounded-card border-2 border-line"
                          />
                        )}
                      </div>
                    </li>
                  )
                })}
              </ol>

              <p className="mt-2 rounded-card border-2 border-line bg-canvas px-4 py-3 text-sm text-muted">
                {guide.hint}
              </p>
            </div>
          )}
        </div>

        <div className="shrink-0  px-5 py-4 pb-[calc(1rem+env(safe-area-inset-bottom))]">
          {stage === 'welcome' && (
            <div className="flex flex-col gap-4">
              <button type="button" onClick={() => setStage('choose')} className={primaryBtn}>
                Далее
              </button>
              <button
                type="button"
                onClick={onClose}
                className="mx-auto text-sm text-muted transition hover:text-brand"
              >
                Пропустить
              </button>
            </div>
          )}

          {stage === 'choose' && (
            <button type="button" onClick={() => setStage('welcome')} className={ghostBtn + ' w-full'}>
              <ChevronLeft className="size-5" /> Назад
            </button>
          )}

          {stage === 'guide' && (
            <div className="flex gap-3">
              <button
                type="button"
                onClick={() => (step === 0 ? setStage('choose') : setStep((s) => s - 1))}
                className={ghostBtn}
              >
                <ChevronLeft className="size-5" /> Назад
              </button>
              {step < guide.steps.length - 1 ? (
                <button type="button" onClick={() => setStep((s) => s + 1)} className={primaryBtn}>
                  Далее <ChevronRight className="size-5" />
                </button>
              ) : (
                <button type="button" onClick={onClose} className={primaryBtn}>
                  К расписанию <ChevronRight className="size-5" />
                </button>
              )}
            </div>
          )}
        </div>
      </div>
    </div>
  )
}

function DeviceButton({ icon: Icon, title, subtitle, onClick }) {
  return (
    <button
      type="button"
      onClick={onClick}
      className="group flex items-center gap-4 rounded-card border-2 border-line bg-canvas px-4 py-4 text-left transition hover:border-brand active:scale-[0.99]"
    >
      <span className="grid size-11 shrink-0 place-items-center rounded-full bg-brand/15 text-brand">
        <Icon className="size-6" />
      </span>
      <span className="flex-1">
        <span className="block text-base font-semibold text-ink">{title}</span>
        <span className="block text-sm text-muted">{subtitle}</span>
      </span>
      <ChevronRight className="size-5 text-muted transition group-hover:text-brand" />
    </button>
  )
}
