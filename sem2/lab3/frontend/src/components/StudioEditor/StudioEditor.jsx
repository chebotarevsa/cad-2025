import { useState, useEffect, useRef } from 'react'
import { createPortal } from 'react-dom'
import { X, LoaderCircle, Image as ImageIcon, User, Plus, Trash2 } from 'lucide-react'
import { fetchStudios, updateStudio } from '../../api'
import RichTextEditor, { richEmpty } from '../RichTextEditor/RichTextEditor'
import { useToast } from '../Toast/Toast'
import { useBodyScrollLock } from '../../useBodyScrollLock'

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

export default function StudioEditor() {
  const [studios, setStudios] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [editing, setEditing] = useState(null)

  function load(silent = false) {
    if (!silent) setLoading(true)
    setError(null)
    fetchStudios()
      .then(setStudios)
      .catch((e) => setError(e.message))
      .finally(() => {
        if (!silent) setLoading(false)
      })
  }

  useEffect(() => {
    load()
  }, [])

  if (loading) {
    return (
      <div className="flex items-center gap-2 rounded-card border-2 border-line bg-surface p-6 text-muted">
        <LoaderCircle className="size-5 animate-spin" /> Загрузка студий…
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

  return (
    <div>
      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-3">
        {studios.map((s) => (
          <button
            key={s.id}
            type="button"
            onClick={() => setEditing(s)}
            className="group flex flex-col overflow-hidden rounded-card border-2 border-line bg-surface text-left transition hover:border-brand active:scale-[0.99]"
          >
            <div className="relative h-44 w-full bg-canvas">
              {s.photos?.length ? (
                <img src={s.photos[0]} alt={s.name} className="h-full w-full object-cover" />
              ) : (
                <div className="grid h-full place-items-center text-muted">
                  <ImageIcon className="size-12" strokeWidth={1.5} />
                </div>
              )}
              {s.photos?.length > 1 && (
                <span className="absolute right-2 bottom-2 rounded-full bg-black/60 px-2 py-0.5 text-sm font-medium text-white">
                  +{s.photos.length - 1} фото
                </span>
              )}
            </div>
            <div className="flex flex-1 items-center justify-between gap-2 px-4 py-3">
              <span className="text-2xl font-bold text-ink">{s.name}</span>
              <span className="rounded-md bg-canvas px-2 py-1 text-base font-semibold text-muted">{s.code}</span>
            </div>
          </button>
        ))}
      </div>

      {editing && (
        <StudioModal
          studio={editing}
          onClose={() => setEditing(null)}
          onSaved={() => {
            setEditing(null)
            load(true)
          }}
        />
      )}
    </div>
  )
}

function StudioModal({ studio, onClose, onSaved }) {
  const toast = useToast()
  const [photos, setPhotos] = useState(studio.photos ?? [])
  const [teacherPhoto, setTeacherPhoto] = useState(studio.teacherPhoto ?? '')
  const [teacherName, setTeacherName] = useState(studio.teacherName ?? '')
  const [description, setDescription] = useState(studio.description ?? '')
  const [saving, setSaving] = useState(false)
  const [busy, setBusy] = useState(false)
  const [err, setErr] = useState(null)
  const [lightbox, setLightbox] = useState(null)
  useBodyScrollLock(true)
  const photoInput = useRef(null)
  const teacherInput = useRef(null)

  async function addPhotos(files) {
    setBusy(true)
    try {
      const urls = []
      for (const f of files) urls.push(await resizeImage(f))
      setPhotos((p) => [...p, ...urls])
    } catch {
      setErr('Не удалось обработать фото')
    } finally {
      setBusy(false)
    }
  }

  async function setTeacher(file) {
    setBusy(true)
    try {
      setTeacherPhoto(await resizeImage(file, 800, 0.75))
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
    try {
      await updateStudio(studio.id, {
        description: richEmpty(description) ? null : description,
        photos,
        teacherPhoto: teacherPhoto || null,
        teacherName: teacherName.trim() || null,
      })
      toast?.success(`Студия «${studio.name}» сохранена`)
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
    <div className="fixed inset-0 z-50 grid animate-fade-in place-items-center bg-black/50 p-4">
      <form
        onSubmit={save}
        className="max-h-[94vh] w-full max-w-4xl animate-fade-up overflow-y-auto rounded-card border-2 border-line bg-surface p-6 shadow-xl"
      >
        <div className="mb-5 flex items-center justify-between">
          <h3 className="text-xl font-bold text-ink">{studio.name}</h3>
          <button type="button" onClick={onClose} aria-label="Закрыть" className="grid size-8 place-items-center rounded-md text-muted transition-colors hover:text-ink">
            <X className="size-5" />
          </button>
        </div>

        <div className="text-sm font-medium text-ink">
          Описание студии
          <div className="mt-2">
            <RichTextEditor
              value={description}
              onChange={setDescription}
              placeholder="Расскажите о студии…"
            />
          </div>
        </div>

        <p className="mt-6 mb-2 text-sm font-medium text-ink">Фото преподавателя</p>
        <div className="flex items-center gap-4">
          <button
            type="button"
            onClick={() => teacherPhoto && setLightbox(teacherPhoto)}
            className="grid size-20 place-items-center overflow-hidden rounded-card border-2 border-line bg-canvas text-muted transition hover:border-brand"
            title={teacherPhoto ? 'Открыть фото' : undefined}
          >
            {teacherPhoto ? (
              <img src={teacherPhoto} alt="" className="h-full w-full object-cover" />
            ) : (
              <User className="size-10" strokeWidth={1.5} />
            )}
          </button>
          <div className="flex flex-col gap-2">
            <button type="button" onClick={() => teacherInput.current?.click()} disabled={busy} className="rounded-card border-2 border-line bg-surface px-3 py-1.5 text-sm font-semibold text-ink transition hover:border-brand hover:text-brand disabled:opacity-60">
              {teacherPhoto ? 'Заменить' : 'Загрузить'}
            </button>
            {teacherPhoto && (
              <button type="button" onClick={() => setTeacherPhoto('')} className="text-sm text-red-500 transition hover:text-red-600">Удалить</button>
            )}
          </div>
          <input ref={teacherInput} type="file" accept="image/*" hidden onChange={(e) => { if (e.target.files[0]) setTeacher(e.target.files[0]); e.target.value = '' }} />
        </div>

        <label className="mt-6 block text-sm font-medium text-ink">
          ФИО преподавателя
          <input
            type="text"
            value={teacherName}
            onChange={(e) => setTeacherName(e.target.value)}
            placeholder="Напр. «Иванов Иван Иванович»"
            className="mt-1.5 h-10 w-full rounded-card border-2 border-line bg-surface px-3 text-sm text-ink outline-none focus:border-brand"
          />
        </label>

        <p className="mt-6 mb-2 text-sm font-medium text-ink">Фото студии</p>
        <div className="grid grid-cols-4 gap-2 sm:grid-cols-6">
          {photos.map((src, i) => (
            <div key={i} className="group relative aspect-square overflow-hidden rounded-card border-2 border-line">
              <button type="button" onClick={() => setLightbox(src)} className="block h-full w-full" title="Открыть фото">
                <img src={src} alt="" className="h-full w-full object-cover" />
              </button>
              <button
                type="button"
                onClick={() => setPhotos((p) => p.filter((_, j) => j !== i))}
                className="absolute top-1 right-1 grid size-7 place-items-center rounded-full bg-black/60 text-white transition hover:bg-red-500"
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
            <span className="flex flex-col items-center gap-1 text-xs">
              <Plus className="size-6" /> добавить
            </span>
          </button>
          <input ref={photoInput} type="file" accept="image/*" multiple hidden onChange={(e) => { addPhotos([...e.target.files]); e.target.value = '' }} />
        </div>

        {err && (
          <p className="mt-4 rounded-card border-2 border-red-300 bg-red-50 px-3 py-2 text-sm text-red-700 dark:border-red-500/40 dark:bg-red-500/10 dark:text-red-300">{err}</p>
        )}

        <div className="mt-8 flex gap-4">
          <button type="button" onClick={onClose} className="h-10 flex-1 rounded-card border-2 border-line bg-surface text-sm font-semibold text-ink transition hover:border-brand hover:text-brand">Отмена</button>
          <button type="submit" disabled={saving || busy} className="flex h-10 flex-1 items-center justify-center gap-2 rounded-card border-2 border-brand-ring bg-gradient-to-r from-brand-light to-brand text-sm font-semibold text-white transition hover:opacity-90 disabled:opacity-60">
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
