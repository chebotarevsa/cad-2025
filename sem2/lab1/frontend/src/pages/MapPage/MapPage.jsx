import { useEffect, useRef, useState } from 'react'
import { flushSync } from 'react-dom'
import * as maplibregl from 'maplibre-gl'
import workerUrl from 'maplibre-gl/dist/maplibre-gl-worker.mjs?worker&url'
import { MapPin, Plus, X, LocateFixed, Pencil, Music2, ImagePlus, LogOut, Trash2 } from 'lucide-react'
import 'maplibre-gl/dist/maplibre-gl.css'
import baseStyle from './base-style.json'
import './MapPage.css'

maplibregl.setWorkerUrl(workerUrl)

const CENTER = [36.483, 50.788]
const VIEW = { center: CENTER, zoom: 13.1, bearing: 0, pitch: 0 }

const CITY_BOUNDS = [[36.420, 50.745], [36.535, 50.840]]
const clampPoint = ({ lat, lng }) => ({
  latitude: Math.max(CITY_BOUNDS[0][1], Math.min(CITY_BOUNDS[1][1], lat)),
  longitude: Math.max(CITY_BOUNDS[0][0], Math.min(CITY_BOUNDS[1][0], lng)),
})
const imageUrl = place => `/api/map/places/${place.id}/image?v=${place.imageId}`
const EMPTY = { title: '', description: '', latitude: CENTER[1], longitude: CENTER[0], icon: 'pin' }
const PLACE_ICONS = [

  { id: 'kindergarten', label: 'Детский сад', path: 'M10 16c.5.3 1.2.5 2 .5s1.5-.2 2-.5M15 12h.01M19.38 6.813A9 9 0 0 1 20.8 10.2a2 2 0 0 1 0 3.6 9 9 0 0 1-17.6 0 2 2 0 0 1 0-3.6A9 9 0 0 1 12 3c2 0 3.5 1.1 3.5 2.5s-.9 2.5-2 2.5c-.8 0-1.5-.4-1.5-1M9 12h.01' },
  { id: 'pin', label: 'Место', path: 'M20 10c0 6-8 12-8 12S4 16 4 10a8 8 0 1 1 16 0ZM15 10a3 3 0 1 1-6 0 3 3 0 0 1 6 0' },
  { id: 'school', label: 'Школа', path: 'M3 22V10l9-7 9 7v12H3ZM9 22v-6h6v6M7 11v2m10-2v2M12 3V1m-2 8h4' },
  { id: 'hospital', label: 'Больница', path: 'M5 22V3h14v19H5ZM9 7h6m-3-3v6M9 22v-6h6v6M2 22h20' },
  { id: 'park', label: 'Парк', path: 'M12 2 5 11h4l-5 7h16l-5-7h4L12 2ZM12 18v4' },
  { id: 'church', label: 'Храм', path: 'M12 2v20M9 5h6M6 9h12M8 15l8 4' },
  { id: 'museum', label: 'Музей', path: 'm3 8 9-6 9 6H3ZM5 11v8m5-8v8m4-8v8m5-8v8M3 22h18M2 19h20' },
  { id: 'cafe', label: 'Кафе', path: 'M4 8h13v9a4 4 0 0 1-4 4H8a4 4 0 0 1-4-4V8ZM17 8h2a3 3 0 0 1 0 6h-2M7 2v3m4-3v3m4-3v3' },
  { id: 'music', label: 'Музыка', path: 'M9 18V5l12-3v13M9 8l12-3M9 18a3 3 0 1 1-6 0 3 3 0 0 1 6 0ZM21 15a3 3 0 1 1-6 0 3 3 0 0 1 6 0' },
]
const placeIcon = name => PLACE_ICONS.find(icon => icon.id === name) || PLACE_ICONS.find(icon => icon.id === 'pin')

function PlaceIcon({ name }) {
  return <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round" aria-hidden="true"><path d={placeIcon(name).path} /></svg>
}

function MapPhoto({ src, alt, className, onError }) {
  const [status, setStatus] = useState('loading')
  return <div className={`map-photo ${className} is-${status}`} aria-busy={status === 'loading'}>
    {status === 'loading' && <span className="map-photo-skeleton" role="status"><span className="map-sr-only">Загрузка фотографии</span></span>}
    {status === 'error' ? <span className="map-photo-error" role="status">Не удалось загрузить фотографию</span>
      : <img src={src} alt={alt} decoding="async" onLoad={() => setStatus('loaded')} onError={() => { setStatus('error'); onError?.() }} />}
  </div>
}

async function request(path, options = {}) {
  const response = await fetch(`/api/map${path}`, {
    cache: 'no-store', ...options, headers: { 'X-Map-Request': '1', ...options.headers },
  })
  if (!response.ok) {
    let message = response.status === 401 ? 'Войдите в редактор ещё раз: сессия завершилась.'
      : response.status === 403 ? 'Для редактирования нужна учётная запись администратора.'
        : response.status === 413 ? 'Слишком большой файл: песня — до 20 МБ, изображение — до 8 МБ.' : 'Не удалось выполнить запрос. Попробуйте ещё раз.'
    try { const data = await response.json(); if (data.error && [400, 401].includes(response.status) && !data.path) message = data.error } catch {                               }
    throw Object.assign(new Error(message), { status: response.status })
  }
  const text = await response.text()
  return text ? JSON.parse(text) : null
}

function Login({ onClose, onLogin }) {
  const dialog = useRef(null)
  const [error, setError] = useState('')
  const [busy, setBusy] = useState(false)
  useEffect(() => { dialog.current.showModal() }, [])
  async function submit(event) {
    event.preventDefault()
    setBusy(true)
    setError('')
    const form = new FormData(event.currentTarget)
    try {
      await request('/session', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(Object.fromEntries(form)) })
      onLogin()
    } catch (err) { setError(err.message) } finally { setBusy(false) }
  }
  return <dialog className="map-login map-card" ref={dialog} onCancel={onClose} aria-labelledby="map-login-title">
    <button className="map-icon map-panel-close" onClick={onClose} aria-label="Закрыть вход"><X /></button>
    <span className="map-eyebrow">РЕДАКТОР КАРТЫ</span>
    <h2 id="map-login-title">Ваши места и музыка</h2>
    <p>Войдите с логином и паролем администратора сайта, чтобы добавлять места.</p>
    <form onSubmit={submit} className="map-form">
      <label>Логин<input name="login" autoComplete="username" required autoFocus /></label>
      <label>Пароль<input name="password" type="password" autoComplete="current-password" required /></label>
      {error && <p className="map-error" role="alert">{error}</p>}
      <button className="map-primary" disabled={busy}>{busy ? 'Входим…' : 'Войти в редактор'}</button>
    </form>
  </dialog>
}

function PlaceEditor({ draft, onChange, onClose, onSave, busy, error, onUseCenter }) {
  const [file, setFile] = useState(null)
  const [fileError, setFileError] = useState('')
  const [removeAudio, setRemoveAudio] = useState(false)
  const [preview, setPreview] = useState('')
  const [imageFile, setImageFile] = useState(null)
  const [imageError, setImageError] = useState('')
  const [imagePreview, setImagePreview] = useState('')
  const [removeImage, setRemoveImage] = useState(false)
  function pickImage(event) {
    const next = event.target.files?.[0]
    setImageError(''); setImageFile(null); setImagePreview('')
    if (!next) return
    if (next.size > 8 * 1024 * 1024 || !/\.(jpe?g|png)$/i.test(next.name)) {
      setImageError('Выберите JPG или PNG размером до 8 МБ.')
      event.target.value = ''
      return
    }
    setImageFile(next); setImagePreview(URL.createObjectURL(next)); setRemoveImage(false)
  }
  useEffect(() => () => { if (imagePreview) URL.revokeObjectURL(imagePreview) }, [imagePreview])
  function pickFile(event) {
    const next = event.target.files?.[0]
    setFileError('')
    setFile(null)
    setPreview('')
    if (!next) return
    if (next.size > 20 * 1024 * 1024 || !/\.(mp3|wav|ogg)$/i.test(next.name)) {
      setFileError('Выберите MP3, WAV или OGG размером до 20 МБ.')
      event.target.value = ''
      return
    }
    setFile(next)
    setPreview(URL.createObjectURL(next))
    setRemoveAudio(false)
  }
  useEffect(() => () => { if (preview) URL.revokeObjectURL(preview) }, [preview])
  return <aside className="map-panel map-card" aria-labelledby="map-editor-title">
    <button className="map-icon map-panel-close" onClick={onClose} disabled={busy} aria-label="Закрыть редактор места"><X /></button>
    <span className="map-eyebrow">РЕДАКТОР КАРТЫ</span>
    <h2 id="map-editor-title">{draft.id ? 'Изменить место' : 'Новое место'}</h2>
    <p>Нажмите на карту, чтобы поставить метку. Её можно перетаскивать.</p>
    <form className="map-form" onSubmit={event => { event.preventDefault(); if (!fileError && !imageError) onSave(file, removeAudio, imageFile, removeImage) }}>
      <fieldset disabled={busy}>
        <label>Название места<input autoFocus required maxLength={120} value={draft.title} placeholder="Как называется это место?" onChange={e => onChange({ ...draft, title: e.target.value })} /></label>
        <div className="map-icon-picker" role="group" aria-labelledby="map-icon-label">
          <span id="map-icon-label">Иконка на карте</span>
          <div className="map-icon-options">{PLACE_ICONS.map(icon => <button key={icon.id} type="button" className="map-icon-option" aria-pressed={placeIcon(draft.icon).id === icon.id} onClick={() => onChange({ ...draft, icon: icon.id })}><PlaceIcon name={icon.id} /><span>{icon.label}</span></button>)}</div>
        </div>
        <label>Описание<textarea maxLength={3000} rows={3} value={draft.description} placeholder="Что здесь интересного, как добраться…" onChange={e => onChange({ ...draft, description: e.target.value })} /></label>
        <div className="map-coordinates"><MapPin size={16} /><span>{draft.latitude.toFixed(5)}, {draft.longitude.toFixed(5)}</span><button type="button" onClick={onUseCenter}>В центре карты</button></div>
        <label className="map-upload"><span><ImagePlus size={18} /> Изображение в описании</span><small>JPG или PNG · до 8 МБ</small><input type="file" accept=".jpg,.jpeg,.png,image/jpeg,image/png" onChange={pickImage} /></label>
        {imageError && <p className="map-error" role="alert">{imageError}</p>}
        {(imagePreview || (draft.imageId && !removeImage)) && <MapPhoto key={imagePreview || imageUrl(draft)} className="map-image-preview" src={imagePreview || imageUrl(draft)} alt="Предпросмотр изображения места" onError={() => setImageError('Не удалось открыть изображение. Выберите другой JPG или PNG.')} />}
        {draft.imageId && !imageFile && <label className="map-checkbox"><input type="checkbox" checked={removeImage} onChange={e => { setRemoveImage(e.target.checked); setImageError('') }} /> Удалить текущее изображение</label>}
        <label className="map-upload"><span><Music2 size={18} /> Песня для этого места</span><small>MP3, WAV или OGG · до 20 МБ</small><input type="file" accept=".mp3,.wav,.ogg,audio/mpeg,audio/wav,audio/ogg" onChange={pickFile} /></label>
        {fileError && <p className="map-error" role="alert">{fileError}</p>}
        {file && preview ? <audio controls src={preview} /> : draft.audioId && !removeAudio ? <><small className="map-filename">{draft.audioName}</small><audio controls src={`/api/map/places/${draft.id}/audio`} /></> : null}
        {draft.audioId && !file && <label className="map-checkbox"><input type="checkbox" checked={removeAudio} onChange={e => setRemoveAudio(e.target.checked)} /> Удалить текущую песню</label>}
        <div className="map-form-actions"><button className="map-secondary" type="button" onClick={onClose}>Отмена</button><button className="map-primary" disabled={!!fileError || !!imageError || !draft.title.trim()}>{busy ? 'Сохраняем…' : 'Сохранить место'}</button></div>
      </fieldset>
      {error && <p className="map-error" role="alert">{error}</p>}
    </form>
  </aside>
}

export default function MapPage() {
  const container = useRef(null)
  const mapRef = useRef(null)
  const draftMarker = useRef(null)
  const editing = useRef(false)
  const saving = useRef(false)
  const audioRef = useRef(null)
  const [ready, setReady] = useState(false)
  const [mapError, setMapError] = useState('')
  const [places, setPlaces] = useState([])
  const [loadError, setLoadError] = useState('')
  const [editor, setEditor] = useState(false)
  const [loginOpen, setLoginOpen] = useState(false)
  const [draft, setDraft] = useState(null)
  const [selected, setSelected] = useState(null)
  const [busy, setBusy] = useState(false)
  const [error, setError] = useState('')
  const [confirmDelete, setConfirmDelete] = useState(false)

  useEffect(() => {
    const audio = audioRef.current
    return () => { audio?.pause() }
  }, [selected?.id, selected?.audioId])

  useEffect(() => {
    let cancelled = false
    const title = document.title
    document.title = 'Карта Строителя · Яковлевский район'
    document.documentElement.classList.add('map-mode')
    request('/places').then(data => { if (!cancelled) setPlaces(data) }).catch(() => { if (!cancelled) setLoadError('Не удалось загрузить ваши места. Обновите страницу или попробуйте позже.') })
    request('/session').then(data => { if (!cancelled) setEditor(data.editor) }).catch(() => {})
    let map
    try {
      map = new maplibregl.Map({ container: container.current, style: structuredClone(baseStyle), ...VIEW,
        minZoom: 12.3, maxZoom: 19, maxBounds: CITY_BOUNDS, renderWorldCopies: false, dragRotate: false, touchPitch: false, maxPitch: 0, attributionControl: false,
        locale: { 'NavigationControl.ZoomIn': 'Приблизить', 'NavigationControl.ZoomOut': 'Отдалить', 'AttributionControl.ToggleAttribution': 'Источники карты' },
      })
      mapRef.current = map
      map.touchZoomRotate.disableRotation()
      map.addControl(new maplibregl.NavigationControl({ showCompass: false }), 'bottom-right')
      map.addControl(new maplibregl.AttributionControl({ compact: true }), 'bottom-left')
      map.addControl(new maplibregl.ScaleControl({ unit: 'metric' }), 'bottom-left')
      map.on('load', () => { setReady(true); setMapError('') })
      map.on('error', () => setMapError('Часть карты не загрузилась. Проверьте подключение к интернету.'))
      map.on('idle', () => { if (map.areTilesLoaded()) setMapError('') })
      map.on('click', event => {
        if (!editing.current || saving.current) return
        setDraft(current => current ? { ...current, ...clampPoint(event.lngLat) } : current)
      })
    } catch {

      queueMicrotask(() => { if (!cancelled) setMapError('Браузер не смог открыть карту. Включите аппаратное ускорение или попробуйте другой браузер.') })
    }
    return () => {
      cancelled = true
      map?.remove()
      mapRef.current = null
      document.documentElement.classList.remove('map-mode')
      document.title = title
    }
  }, [])

  useEffect(() => {
    const map = mapRef.current
    if (!ready || !map) return
    const markers = places.filter(place => place.id !== draft?.id).map(place => {
      const button = document.createElement('button')
      button.className = `map-place-marker${selected?.id === place.id ? ' is-selected' : ''}`
      button.type = 'button'
      button.setAttribute('aria-label', `Открыть место: ${place.title}`)
      const dot = document.createElement('span')
      dot.className = 'map-marker-dot'
      const svg = document.createElementNS('http://www.w3.org/2000/svg', 'svg')
      for (const [key, value] of Object.entries({ viewBox: '0 0 24 24', fill: 'none', stroke: 'currentColor', 'stroke-width': '1.8', 'stroke-linecap': 'round', 'stroke-linejoin': 'round', 'aria-hidden': 'true' })) svg.setAttribute(key, value)
      const path = document.createElementNS('http://www.w3.org/2000/svg', 'path')
      path.setAttribute('d', placeIcon(place.icon).path)
      svg.append(path)
      dot.append(svg)
      const label = document.createElement('span')
      label.className = 'map-marker-label'
      label.textContent = place.title
      button.append(dot, label)
      button.addEventListener('click', event => {
        event.stopPropagation()
        if (editing.current) return

        flushSync(() => { setSelected(place); setConfirmDelete(false); setError('') })
        const audio = audioRef.current
        if (place.audioId && audio) {
          audio.play().catch(err => {
            if (err.name !== 'AbortError' && audioRef.current === audio)
              setError(err.name === 'NotAllowedError' ? 'Нажмите ▶ в плеере, чтобы включить песню.' : 'Не удалось включить песню. Попробуйте запустить её в плеере.')
          })
        }
      })
      return new maplibregl.Marker({ element: button, anchor: 'bottom' }).setLngLat([place.longitude, place.latitude]).addTo(map)
    })
    return () => markers.forEach(marker => marker.remove())
  }, [places, ready, selected?.id, draft?.id])

  useEffect(() => {
    editing.current = !!draft
    saving.current = busy
    const map = mapRef.current
    if (!map || !ready) return
    map.getCanvas().style.cursor = draft ? 'crosshair' : ''
    if (draft) {
      if (!draftMarker.current) {
        const element = document.createElement('div')
        element.className = 'map-place-marker is-selected'
        const dot = document.createElement('span')
        dot.className = 'map-marker-dot'
        const svg = document.createElementNS('http://www.w3.org/2000/svg', 'svg')
        for (const [key, value] of Object.entries({ viewBox: '0 0 24 24', fill: 'none', stroke: 'currentColor', 'stroke-width': '1.8', 'stroke-linecap': 'round', 'stroke-linejoin': 'round' })) svg.setAttribute(key, value)
        svg.append(document.createElementNS('http://www.w3.org/2000/svg', 'path'))
        dot.append(svg)
        element.append(dot)
        draftMarker.current = new maplibregl.Marker({ element, anchor: 'bottom', draggable: true }).setLngLat([draft.longitude, draft.latitude]).addTo(map)
        draftMarker.current.on('dragend', () => {
          const point = draftMarker.current.getLngLat()
          setDraft(current => current ? { ...current, ...clampPoint(point) } : current)
        })
      }
      draftMarker.current.getElement().querySelector('path').setAttribute('d', placeIcon(draft.icon).path)
      draftMarker.current.setLngLat([draft.longitude, draft.latitude]).setDraggable(!busy)
    } else {
      draftMarker.current?.remove()
      draftMarker.current = null
    }
  }, [draft, ready, busy])

  function startDraft(place) {
    const center = mapRef.current.getCenter()
    setDraft(place ? { ...place } : { ...EMPTY, latitude: center.lat, longitude: center.lng })
    setSelected(null); setError(''); setConfirmDelete(false)
  }

  async function save(file, removeAudio, imageFile, removeImage) {
    setBusy(true); setError('')
    const form = new FormData()
    for (const key of ['title', 'description', 'latitude', 'longitude']) form.append(key, draft[key])
    form.append('icon', placeIcon(draft.icon).id)
    form.append('removeAudio', String(removeAudio))
    if (file) form.append('audio', file)
    form.append('removeImage', String(removeImage))
    if (imageFile) form.append('image', imageFile)
    try {
      const saved = await request(`/places${draft.id ? `/${draft.id}` : ''}`, { method: draft.id ? 'PUT' : 'POST', body: form })
      setPlaces(current => [...current.filter(place => place.id !== saved.id), saved])
      setDraft(null); setSelected(saved)
    } catch (err) { setError(err.message); if (err.status === 401) setLoginOpen(true) } finally { setBusy(false) }
  }

  async function deletePlace() {
    setBusy(true); setError('')
    try {
      await request(`/places/${selected.id}`, { method: 'DELETE' })
      setPlaces(current => current.filter(place => place.id !== selected.id))
      setSelected(null); setConfirmDelete(false)
    } catch (err) { setError(err.message); if (err.status === 401) setLoginOpen(true) } finally { setBusy(false) }
  }

  async function logout() {
    try { await request('/session', { method: 'DELETE' }); setEditor(false); setDraft(null); setError('') }
    catch (err) { setLoadError(err.message) }
  }

  return <main className="district-map" data-no-pull-to-refresh>
    <div className="map-project-title">
      <h1>Услышано в Строителе</h1>
      <p>медиапроект звуковой карты</p>
    </div>
    <div ref={container} className="map-canvas" aria-label="Карта улиц Строителя" />
    <div className="map-brand"><button type="button" className="map-location map-card" onClick={() => { if (!editor) setLoginOpen(true) }} aria-label={editor ? 'Строитель, редактор активен' : 'Строитель: вход в редактор карты'} aria-haspopup={!editor ? 'dialog' : undefined}><span className="map-location-icon"><MapPin size={22} /></span><div><strong>Строитель</strong><span>Город и ближайшие окрестности</span></div></button><p className="map-credit">Сделано в Школе креативных индустрий</p></div>
    <div className="map-toolbar">
      {editor ? <><button className="map-primary map-add" disabled={!ready || !!draft || busy} onClick={() => startDraft()}><Plus size={19} /> Добавить место</button><button className="map-icon map-card" disabled={!!draft || busy} onClick={logout} aria-label="Выйти из редактора"><LogOut size={19} /></button></>
        : null}
    </div>
    {(!ready || mapError || loadError) && <div className="map-status map-card" role="status">{mapError || loadError || 'Загружаем карту…'}{(mapError || loadError) && <button onClick={() => window.location.reload()}>Повторить</button>}</div>}
    <button className="map-home map-icon map-card" disabled={!ready} onClick={() => mapRef.current?.flyTo(VIEW)} aria-label="Вернуться к Строителю" title="Вернуться к Строителю"><LocateFixed size={21} /></button>
    {editor && !draft && !selected && ready && <div className="map-editor-hint map-card"><span className="map-live-dot" />Режим редактирования<span>Добавьте место и прикрепите песню</span></div>}
    {draft && <PlaceEditor key={draft.id || 'new'} draft={draft} onChange={setDraft} onClose={() => setDraft(null)} onSave={save} busy={busy} error={error} onUseCenter={() => { const center = mapRef.current.getCenter(); setDraft({ ...draft, latitude: center.lat, longitude: center.lng }) }} />}
    {selected && <aside className="map-panel map-card" aria-labelledby="map-place-title">
      <button className="map-icon map-panel-close" disabled={busy} onClick={() => setSelected(null)} aria-label="Закрыть место"><X /></button>
      <span className="map-eyebrow">МЕСТО НА КАРТЕ</span><h2 id="map-place-title">{selected.title}</h2>
      {selected.imageId && <MapPhoto key={imageUrl(selected)} className="map-place-image" src={imageUrl(selected)} alt={selected.title} />}
      {selected.description && <p className="map-description">{selected.description}</p>}
      {selected.audioId ? <div className="map-track" key={`${selected.id}-${selected.audioId}`}><audio ref={audioRef} controls loop preload="metadata" src={`/api/map/places/${selected.id}/audio`} onError={() => setError('Не удалось воспроизвести песню. Проверьте соединение или замените аудиофайл.')} /></div> : <p>К этому месту пока не добавлена песня.</p>}
      {editor && <div className="map-form-actions"><button className="map-secondary" disabled={busy} onClick={() => startDraft(selected)}><Pencil size={16} /> Изменить</button><button className="map-icon map-danger" disabled={busy} onClick={() => setConfirmDelete(true)} aria-label="Удалить место"><Trash2 size={18} /></button></div>}
      {confirmDelete && <div className="map-delete-confirm"><p>Удалить место, его песню и изображение?</p><button className="map-secondary" disabled={busy} onClick={() => setConfirmDelete(false)}>Отмена</button><button className="map-danger" disabled={busy} onClick={deletePlace}>{busy ? 'Удаляем…' : 'Удалить'}</button></div>}
      {error && <p className="map-error" role="alert">{error}</p>}
    </aside>}
    {loginOpen && <Login onClose={() => setLoginOpen(false)} onLogin={() => { setEditor(true); setLoginOpen(false) }} />}
  </main>
}
