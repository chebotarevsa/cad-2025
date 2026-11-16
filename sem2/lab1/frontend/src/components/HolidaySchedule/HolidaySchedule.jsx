import { useState, useEffect, useRef } from 'react'
import { createPortal } from 'react-dom'
import { Plus, Pencil, Trash2, X, LoaderCircle, PartyPopper, Palmtree, Sparkles } from 'lucide-react'
import {
  fetchHolidays, createHoliday, updateHoliday, deleteHoliday,
  fetchAllLessons, createLesson, updateLesson, deleteLesson,
} from '../../api'
import { isoLocal } from '../../dates'
import ConfirmModal from '../SchoolSchedule/ConfirmModal'
import RichTextEditor, { richEmpty } from '../RichTextEditor/RichTextEditor'
import { useToast } from '../Toast/Toast'
import { useBodyScrollLock } from '../../useBodyScrollLock'

const TYPE_LABEL = { EVENT: 'Мероприятие', HOLIDAY: 'Праздник', VACATION: 'Каникулы' }
const STUDIO_CODES = ['ФВ', 'ВР', 'ЗВ', 'АН', 'ДЗ', 'ЭЛ', 'ЛК', 'СВ']

const STUDIO_NAMES = {
  'ФВ': 'Фото-видео производство',
  'ВР': 'Интерактивные цифровые технологии VR и AR',
  'ЗВ': 'Звукорежиссура',
  'АН': 'Анимация и 3D графика',
  'ДЗ': 'Дизайн',
  'ЭЛ': 'Электронная музыка',
  'ЛК': 'Лекторий ШКИ',
  'СВ': 'Студия «Северсталь»',
}



function studioOptionLabel(code) {
  const name = STUDIO_NAMES[code]
  if (!name) return code
  if (name.startsWith('Студия') || name.startsWith('Лектор')) return name
  return `Студия ${name.charAt(0).toLowerCase()}${name.slice(1)}`
}

const DOW = ['SUNDAY', 'MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY']
function dowFromDate(s) {
  const [y, mo, d] = s.split('-').map(Number)
  return DOW[new Date(y, mo - 1, d).getDay()]
}
const hhmm = (t) => (t ? String(t).slice(0, 5) : '')
const toLocalTime = (t) => (t && t.length === 5 ? `${t}:00` : t || null)

function fmtDate(s) {
  if (!s) return ''
  const [y, mo, d] = s.split('-')
  return `${d}.${mo}.${y}`
}

function fmtDateMD(s) {
  if (!s) return ''
  const [, mo, d] = s.split('-')
  return `${d}.${mo}`
}
const dateKey = (x) => (x.kind === 'EVENT' ? x.date : x.startDate) || ''

function resizeImage(file, max = 1000, quality = 0.7) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.onload = () => {
      const img = new Image()
      img.onload = () => {
        let { width, height } = img
        if (width > height && width > max) {
          height = Math.round((height * max) / width)
          width = max
        } else if (height >= width && height > max) {
          width = Math.round((width * max) / height)
          height = max
        }
        const canvas = document.createElement('canvas')
        canvas.width = width
        canvas.height = height
        canvas.getContext('2d').drawImage(img, 0, 0, width, height)
        resolve(canvas.toDataURL('image/jpeg', quality))
      }
      img.onerror = reject
      img.src = reader.result
    }
    reader.onerror = reject
    reader.readAsDataURL(file)
  })
}

export default function HolidaySchedule() {
  const toast = useToast()
  const [items, setItems] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [form, setForm] = useState(null)
  const [confirm, setConfirm] = useState(null)

  async function load(silent = false) {
    if (!silent) setLoading(true)
    setError(null)
    try {
      const [hols, lessons] = await Promise.all([fetchHolidays(), fetchAllLessons()])
      const holidays = hols.map((h) => ({ kind: h.type, ...h }))
      const events = lessons.filter((l) => l.special).map((l) => ({ kind: 'EVENT', ...l }))
      setItems([...holidays, ...events].sort((a, b) => (dateKey(a) < dateKey(b) ? -1 : 1)))
    } catch (e) {
      setError(e.message)
    } finally {
      if (!silent) setLoading(false)
    }
  }

  useEffect(() => {
    load()
  }, [])

  const today = () => isoLocal(new Date())

  function openCreateHoliday(type) {
    setForm({ kind: type, id: null, type, name: '', startDate: today(), endDate: today(), yearly: true })
  }
  function openCreateEvent() {
    setForm({ kind: 'EVENT', id: null, title: '', date: today(), time: '10:00', endTime: '11:00', studioCode: STUDIO_CODES[0], description: '', photos: [] })
  }
  function openEdit(it) {
    if (it.kind === 'EVENT') {
      setForm({
        kind: 'EVENT', id: it.id, title: it.title ?? '', date: it.date ?? today(),
        time: hhmm(it.time), endTime: it.endTime ? hhmm(it.endTime) : '',
        studioCode: it.studioCode ?? STUDIO_CODES[0], description: it.description ?? '', photos: it.photos ?? [],
      })
    } else {
      setForm({ kind: it.type, id: it.id, type: it.type, name: it.name ?? '', startDate: it.startDate, endDate: it.endDate, yearly: !!it.yearly })
    }
  }

  function askDelete(it) {
    const titles = { EVENT: 'Удалить мероприятие?', HOLIDAY: 'Удалить праздник?', VACATION: 'Удалить каникулы?' }
    const name = it.kind === 'EVENT' ? it.title || 'без названия' : it.name || fmtDate(it.startDate)
    setConfirm({
      title: titles[it.kind],
      message: `«${name}». Восстановить потом не получится.`,
      onConfirm: async () => {
        setConfirm(null)
        try {
          if (it.kind === 'EVENT') await deleteLesson(it.id)
          else await deleteHoliday(it.id)
          const labels = { EVENT: 'Мероприятие удалено', HOLIDAY: 'Праздник удалён', VACATION: 'Каникулы удалены' }
          toast?.success(labels[it.kind])
          load(true)
        } catch (e) {
          toast?.error(e.message)
        }
      },
    })
  }

  if (loading) {
    return (
      <div className="flex items-center gap-2 rounded-card border-2 border-line bg-surface p-6 text-muted">
        <LoaderCircle className="size-5 animate-spin" /> Загрузка…
      </div>
    )
  }

  if (error) {
    return (
      <div className="rounded-card border-2 border-red-300 bg-red-50 p-6 text-red-700 dark:border-red-500/40 dark:bg-red-500/10 dark:text-red-300">
        {error}
        <button type="button" onClick={() => load()} className="ml-3 underline">Повторить</button>
      </div>
    )
  }

  const renderRow = (it) => {
    const Icon = it.kind === 'EVENT' ? Sparkles : it.kind === 'HOLIDAY' ? PartyPopper : Palmtree
    const color =
      it.kind === 'EVENT' ? 'bg-violet-500/15 text-violet-600 dark:text-violet-400'
        : it.kind === 'HOLIDAY' ? 'bg-red-500/10 text-red-500'
          : 'bg-brand/10 text-brand'

    let name, period
    if (it.kind === 'EVENT') {
      name = it.title || '—'
      const t = hhmm(it.time)
      const e = it.endTime ? hhmm(it.endTime) : ''
      period = `${fmtDate(it.date)}${t ? `, ${t}${e ? `–${e}` : ''}` : ''}`
    } else {
      name = it.name || '—'
      const f = it.yearly ? fmtDateMD : fmtDate
      period = it.kind === 'HOLIDAY' ? f(it.startDate) : `${f(it.startDate)} – ${f(it.endDate)}`
    }

    return (
      <tr key={`${it.kind}-${it.id}`}>
        <td className="border border-line px-4 py-3 text-center">
          <span className={'inline-flex items-center gap-2 rounded-full px-5 py-2.5 text-2xl font-semibold ' + color}>
            <Icon className="size-7" /> {TYPE_LABEL[it.kind]}
          </span>
        </td>
        <td className="border border-line px-4 py-3 text-center text-2xl text-ink">{name}</td>
        <td className="border border-line px-4 py-3 text-center text-2xl whitespace-nowrap text-ink">
          {period}
          {it.kind === 'EVENT' && it.studioCode && (
            <span className="mt-0.5 block text-base font-medium text-brand">Место: {it.studioCode}</span>
          )}
          {it.kind !== 'EVENT' && it.yearly && (
            <span className="mt-0.5 block text-base font-medium text-brand">ежегодно</span>
          )}
        </td>
        <td className="border border-line px-2 py-3">
          <div className="flex items-center justify-center gap-3">
            <button type="button" onClick={() => openEdit(it)} className="text-muted transition-colors hover:text-brand" title="Изменить">
              <Pencil className="size-6" />
            </button>
            <button type="button" onClick={() => askDelete(it)} className="text-muted transition-colors hover:text-red-500" title="Удалить">
              <Trash2 className="size-6" />
            </button>
          </div>
        </td>
      </tr>
    )
  }

  return (
    <div className="space-y-4">
      <div className="flex flex-wrap gap-3">
        <button
          type="button"
          onClick={openCreateEvent}
          className="flex items-center gap-2 rounded-card border-2 border-brand-ring bg-gradient-to-r from-brand-light to-brand px-5 py-2.5 text-2xl font-semibold text-white transition hover:opacity-90 active:scale-95"
        >
          <Sparkles className="size-6" /> Добавить мероприятие
        </button>
        <button
          type="button"
          onClick={() => openCreateHoliday('HOLIDAY')}
          className="flex items-center gap-2 rounded-card border-2 border-line bg-surface px-5 py-2.5 text-2xl font-semibold text-ink transition hover:border-brand hover:text-brand active:scale-95"
        >
          <PartyPopper className="size-6" /> Добавить праздник
        </button>
        <button
          type="button"
          onClick={() => openCreateHoliday('VACATION')}
          className="flex items-center gap-2 rounded-card border-2 border-line bg-surface px-5 py-2.5 text-2xl font-semibold text-ink transition hover:border-brand hover:text-brand active:scale-95"
        >
          <Palmtree className="size-6" /> Добавить каникулы
        </button>
      </div>

      <div className="overflow-x-auto rounded-card border-2 border-line">
        <table className="w-full border-collapse">
          <thead>
            <tr className="bg-canvas">
              <th className="border border-line px-4 py-3 text-center text-2xl font-semibold text-ink">Тип</th>
              <th className="border border-line px-4 py-3 text-center text-2xl font-semibold text-ink">Название</th>
              <th className="border border-line px-4 py-3 text-center text-2xl font-semibold text-ink">Дата / период</th>
              <th className="w-28 border border-line px-2 py-3 text-2xl font-semibold text-ink">Действия</th>
            </tr>
          </thead>
          <tbody>
            {items.length === 0 ? (
              <tr>
                <td colSpan={4} className="border border-line px-4 py-6 text-center text-2xl text-muted">
                  Пока пусто — добавьте мероприятие, праздник или каникулы.
                </td>
              </tr>
            ) : (
              items.map((it) => renderRow(it))
            )}
          </tbody>
        </table>
      </div>

      {form && form.kind === 'EVENT' && (
        <EventModal form={form} setForm={setForm} onClose={() => setForm(null)} onSaved={() => { setForm(null); load(true) }} />
      )}
      {form && form.kind !== 'EVENT' && (
        <HolidayModal form={form} setForm={setForm} onClose={() => setForm(null)} onSaved={() => { setForm(null); load(true) }} />
      )}

      {confirm && (
        <ConfirmModal
          title={confirm.title}
          message={confirm.message}
          holdSeconds={0}
          onConfirm={confirm.onConfirm}
          onCancel={() => setConfirm(null)}
        />
      )}
    </div>
  )
}

const field = 'mt-1 h-10 w-full rounded-card border-2 border-line bg-surface px-3 text-sm text-ink outline-none focus:border-brand'
const modalWrap = 'fixed inset-0 z-50 grid animate-fade-in place-items-center bg-black/50 p-4'
const modalBox = 'max-h-[94vh] w-full max-w-6xl animate-fade-up overflow-y-auto rounded-card border-2 border-line bg-surface p-6 shadow-xl'
const btnCancel = 'h-10 flex-1 rounded-card border-2 border-line bg-surface text-sm font-semibold text-ink transition hover:border-brand hover:text-brand'
const btnSave = 'flex h-10 flex-1 items-center justify-center gap-2 rounded-card border-2 border-brand-ring bg-gradient-to-r from-brand-light to-brand text-sm font-semibold text-white transition hover:opacity-90 disabled:opacity-60'

function EventModal({ form, setForm, onClose, onSaved }) {
  const toast = useToast()
  const [saving, setSaving] = useState(false)
  const [busy, setBusy] = useState(false)
  const [err, setErr] = useState(null)
  const isEdit = form.id != null
  useBodyScrollLock(true)
  const photoInput = useRef(null)

  const [lightbox, setLightbox] = useState(null)
  const set = (patch) => setForm((f) => ({ ...f, ...patch }))

  async function addPhotos(files) {
    setBusy(true)
    try {
      const urls = []
      for (const f of files) urls.push(await resizeImage(f))
      set({ photos: [...(form.photos ?? []), ...urls] })
    } catch {
      setErr('Не удалось обработать фото')
    } finally {
      setBusy(false)
    }
  }

  async function save(e) {
    e.preventDefault()
    setErr(null)
    setSaving(true)
    const payload = {
      dayOfWeek: dowFromDate(form.date),
      time: toLocalTime(form.time),
      endTime: toLocalTime(form.endTime),
      orderNumber: 1,
      groupNumber: null,
      studioCode: form.studioCode,
      special: true,
      date: form.date,
      title: form.title || null,
      description: richEmpty(form.description) ? null : form.description,
      photos: form.photos ?? [],
    }
    try {
      if (isEdit) await updateLesson(form.id, payload)
      else await createLesson(payload)
      toast?.success(isEdit ? 'Мероприятие обновлено' : 'Мероприятие добавлено')
      onSaved()
    } catch (e2) {
      setErr(e2.message)
      toast?.error(e2.message)
    } finally {
      setSaving(false)
    }
  }

  return createPortal(
    <>
    <div className={modalWrap}>
      <form onSubmit={save} className={modalBox}>
        <div className="mb-5 flex items-center justify-between">
          <h3 className="flex items-center gap-2 text-xl font-bold text-ink">
            <Sparkles className="size-5 text-brand" /> {isEdit ? 'Изменить мероприятие' : 'Добавить мероприятие'}
          </h3>
          <button type="button" onClick={onClose} aria-label="Закрыть" className="grid size-8 place-items-center rounded-md text-muted transition-colors hover:text-ink">
            <X className="size-5" />
          </button>
        </div>

        <label className="block text-sm font-medium text-ink">
          Название мероприятия
          <input type="text" className={field} value={form.title} onChange={(e) => set({ title: e.target.value })} placeholder="Напр. «День открытых дверей»" required />
        </label>

        <label className="mt-4 block text-sm font-medium text-ink">
          Дата
          <input type="date" className={field} value={form.date} onChange={(e) => set({ date: e.target.value })} required />
        </label>
        <div className="mt-4 grid grid-cols-2 gap-3">
          <label className="block text-sm font-medium text-ink">
            Начало
            <input type="time" className={field} value={form.time} onChange={(e) => set({ time: e.target.value })} required />
          </label>
          <label className="block text-sm font-medium text-ink">
            Окончание
            <input type="time" className={field} value={form.endTime} onChange={(e) => set({ endTime: e.target.value })} required />
          </label>
        </div>

        <label className="mt-4 block text-sm font-medium text-ink">
          Место проведения
          <select className={field} value={form.studioCode} onChange={(e) => set({ studioCode: e.target.value })}>
            {STUDIO_CODES.map((c) => (<option key={c} value={c}>{studioOptionLabel(c)}</option>))}
          </select>
        </label>

        <div className="mt-4 text-sm font-medium text-ink">
          Описание
          <div className="mt-2">
            <RichTextEditor
              value={form.description}
              onChange={(html) => set({ description: html })}
              placeholder="Опишите мероприятие…"
            />
          </div>
        </div>

        <div className="mt-4">
          <span className="mb-2 block text-sm font-medium text-ink">Фото мероприятия</span>
          <div className="grid grid-cols-3 gap-3 sm:grid-cols-4">
            {(form.photos ?? []).map((src, i) => (
              <div key={i} className="group relative aspect-square overflow-hidden rounded-card border-2 border-line">
                <button type="button" onClick={() => setLightbox(src)} className="block h-full w-full" title="Открыть фото">
                  <img src={src} alt="" className="h-full w-full object-cover" />
                </button>
                <button
                  type="button"
                  onClick={() => set({ photos: form.photos.filter((_, j) => j !== i) })}
                  className="absolute top-1 right-1 grid size-8 place-items-center rounded-full bg-black/60 text-white transition hover:bg-red-500"
                  title="Удалить фото"
                >
                  <Trash2 className="size-4" />
                </button>
              </div>
            ))}
            <button
              type="button"
              onClick={() => photoInput.current?.click()}
              disabled={busy}
              className="grid aspect-square place-items-center rounded-card border-2 border-dashed border-line text-muted transition hover:border-brand hover:text-brand disabled:opacity-60"
            >
              <span className="flex flex-col items-center gap-1 text-sm">
                <Plus className="size-8" /> добавить
              </span>
            </button>
            <input ref={photoInput} type="file" accept="image/*" multiple hidden onChange={(e) => { addPhotos([...e.target.files]); e.target.value = '' }} />
          </div>
        </div>

        {err && (
          <p className="mt-4 rounded-card border-2 border-red-300 bg-red-50 px-3 py-2 text-lg text-red-700 dark:border-red-500/40 dark:bg-red-500/10 dark:text-red-300">{err}</p>
        )}

        <div className="mt-8 flex gap-4">
          <button type="button" onClick={onClose} className={btnCancel}>Отмена</button>
          <button type="submit" disabled={saving || busy} className={btnSave}>
            {saving && <LoaderCircle className="size-5 animate-spin" />}
            {saving ? 'Сохранение…' : 'Сохранить'}
          </button>
        </div>
      </form>
    </div>

    {lightbox && (
      <div className="fixed inset-0 z-[80] grid place-items-center bg-black/90 p-4" onClick={() => setLightbox(null)}>
        <img src={lightbox} alt="Фото" className="max-h-full max-w-full object-contain" onClick={(e) => e.stopPropagation()} />
        <button
          type="button"
          onClick={() => setLightbox(null)}
          aria-label="Закрыть"
          className="absolute top-4 right-4 grid size-11 place-items-center rounded-full bg-white/15 text-white transition hover:bg-white/30"
        >
          <X className="size-6" />
        </button>
      </div>
    )}
    </>,
    document.body,
  )
}

function HolidayModal({ form, setForm, onClose, onSaved }) {
  const toast = useToast()
  const [saving, setSaving] = useState(false)
  const [err, setErr] = useState(null)
  const isEdit = form.id != null
  const isHoliday = form.type === 'HOLIDAY'
  useBodyScrollLock(true)
  const set = (patch) => setForm((f) => ({ ...f, ...patch }))

  async function save(e) {
    e.preventDefault()
    setErr(null)
    setSaving(true)
    const payload = {
      type: form.type,
      name: form.name || null,
      startDate: form.startDate,
      endDate: isHoliday ? form.startDate : form.endDate,
      yearly: !!form.yearly,
    }
    try {
      if (isEdit) await updateHoliday(form.id, payload)
      else await createHoliday(payload)
      const what = isHoliday ? 'Праздник' : 'Каникулы'
      toast?.success(isEdit ? `${what}: изменения сохранены` : `${what} добавлены`)
      onSaved()
    } catch (e2) {
      setErr(e2.message)
      toast?.error(e2.message)
    } finally {
      setSaving(false)
    }
  }

  return createPortal(
    <div className={modalWrap}>
      <form onSubmit={save} className={modalBox}>
        <div className="mb-5 flex items-center justify-between">
          <h3 className="text-xl font-bold text-ink">
            {isEdit ? 'Изменить запись' : isHoliday ? 'Добавить праздник' : 'Добавить каникулы'}
          </h3>
          <button type="button" onClick={onClose} aria-label="Закрыть" className="grid size-8 place-items-center rounded-md text-muted transition-colors hover:text-ink">
            <X className="size-5" />
          </button>
        </div>

        <label className="block text-sm font-medium text-ink">
          Тип
          <select className={field} value={form.type} onChange={(e) => set({ type: e.target.value })}>
            <option value="HOLIDAY">Праздник (один день)</option>
            <option value="VACATION">Каникулы (период)</option>
          </select>
        </label>

        <label className="mt-4 block text-sm font-medium text-ink">
          Название {isHoliday && <span className="text-muted">(напр. «День России»)</span>}
          <input
            type="text"
            className={field}
            value={form.name}
            onChange={(e) => set({ name: e.target.value })}
            placeholder={isHoliday ? 'Название праздника' : 'Напр. «Зимние каникулы»'}
            required={isHoliday}
          />
        </label>

        {isHoliday ? (
          <label className="mt-4 block text-sm font-medium text-ink">
            Дата
            <input type="date" className={field} value={form.startDate} onChange={(e) => set({ startDate: e.target.value })} required />
          </label>
        ) : (
          <div className="mt-4 grid grid-cols-1 gap-3 sm:grid-cols-2">
            <label className="block text-sm font-medium text-ink">
              С
              <input type="date" className={field} value={form.startDate} onChange={(e) => set({ startDate: e.target.value })} required />
            </label>
            <label className="block text-sm font-medium text-ink">
              По
              <input type="date" className={field} value={form.endDate} onChange={(e) => set({ endDate: e.target.value })} required />
            </label>
          </div>
        )}

        <label className="mt-5 flex w-fit cursor-pointer items-center gap-3 select-none">
          <input type="checkbox" checked={!!form.yearly} onChange={(e) => set({ yearly: e.target.checked })} className="size-6 accent-brand" />
          <span className="text-sm font-medium text-ink">Каждый год (повторять ежегодно)</span>
        </label>

        {err && (
          <p className="mt-4 rounded-card border-2 border-red-300 bg-red-50 px-3 py-2 text-lg text-red-700 dark:border-red-500/40 dark:bg-red-500/10 dark:text-red-300">{err}</p>
        )}

        <div className="mt-8 flex gap-4">
          <button type="button" onClick={onClose} className={btnCancel}>Отмена</button>
          <button type="submit" disabled={saving} className={btnSave}>
            {saving && <LoaderCircle className="size-5 animate-spin" />}
            {saving ? 'Сохранение…' : 'Сохранить'}
          </button>
        </div>
      </form>
    </div>,
    document.body,
  )
}
