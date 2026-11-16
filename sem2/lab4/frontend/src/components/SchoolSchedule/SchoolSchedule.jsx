import { useState, useEffect, useMemo, useCallback } from 'react'
import { createPortal } from 'react-dom'
import { Plus, Pencil, Trash2, X, Check, LoaderCircle, GripVertical, Sparkles, FileSpreadsheet, Clock, Info, RefreshCw, Users } from 'lucide-react'
import { fetchAllLessons, fetchGroupsList, createLesson, updateLesson, deleteLesson, createGroup, updateGroup, deleteGroup } from '../../api'
import ConfirmModal from './ConfirmModal'
import { useToast } from '../Toast/Toast'
import { useBodyScrollLock } from '../../useBodyScrollLock'
import { compareGroupNumbers, normalizeGroupNumber } from '../../utils'

function fmtDateShort(s) {
  if (!s) return ''
  const [, mo, d] = s.split('-')
  return `${d}.${mo}`
}

const DAYS = [
  { key: 'MONDAY', label: 'Понедельник' },
  { key: 'TUESDAY', label: 'Вторник' },
  { key: 'WEDNESDAY', label: 'Среда' },
  { key: 'THURSDAY', label: 'Четверг' },
  { key: 'FRIDAY', label: 'Пятница' },
  { key: 'SATURDAY', label: 'Суббота' },
]

const MORNING_TIMES = ['09:00', '09:50', '10:40', '11:30', '12:20']
const AFTERNOON_TIMES = ['15:20', '16:10', '17:00', '17:50', '18:40']
const WED_TIMES = ['15:00', '15:50', '16:40', '17:30', '18:20']

const DEFAULT_STUDIO_CODES = ['ФВ', 'ВР', 'ЗВ', 'АН', 'ДЗ', 'ЭЛ', 'ЛК', 'СВ']

const SHIFTS = [
  { key: 'MORNING', label: 'Первая смена (утро)' },
  { key: 'AFTERNOON', label: 'Вторая смена (день)' },
]
const sessionToShift = (session) => (session === 'morning' ? 'MORNING' : 'AFTERNOON')

const hhmm = (t) => (t ? String(t).slice(0, 5) : '')

function lessonMinutes(lesson, fallback) {
  if (!lesson.customTime || !lesson.endTime) return fallback
  const minutes = t => { const [h, m] = hhmm(t).split(':').map(Number); return h * 60 + m }
  return Math.max(0, minutes(lesson.endTime) - minutes(lesson.time))
}

function addMinutes(t, mins) {
  const [h, m] = hhmm(t).split(':').map(Number)
  const total = h * 60 + m + mins
  return `${String(Math.floor(total / 60) % 24).padStart(2, '0')}:${String(total % 60).padStart(2, '0')}`
}

function defaultTime(dayKey, session, index) {
  const arr = session === 'morning' ? MORNING_TIMES : dayKey === 'WEDNESDAY' ? WED_TIMES : AFTERNOON_TIMES
  if (index < arr.length) return arr[index]
  return addMinutes(arr[arr.length - 1], 50 * (index - arr.length + 1))
}

export default function SchoolSchedule() {
  const toast = useToast()
  const [lessons, setLessons] = useState([])
  const [groups, setGroups] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [form, setForm] = useState(null)
  const [timeEdit, setTimeEdit] = useState(null)
  const [extraMorning, setExtraMorning] = useState(
    () => Number(localStorage.getItem('school-m-row-adjustment')) || 0,
  )
  const [extraAfternoon, setExtraAfternoon] = useState(
    () => Number(localStorage.getItem('school-a-row-adjustment')) || 0,
  )
  const [busy, setBusy] = useState(false)
  const [confirm, setConfirm] = useState(null)
  const [timeOverrides, setTimeOverrides] = useState({})
  const [dragLesson, setDragLesson] = useState(null)

  const initNum = (key, fallbackKey, def) => {
    const v = localStorage.getItem(key) ?? localStorage.getItem(fallbackKey)
    return v == null ? def : Number(v)
  }
  const [morningLessonMin, setMorningLessonMin] = useState(() => initNum('school-m-lesson', 'school-lesson-min', 40))
  const [morningBreakMin, setMorningBreakMin] = useState(() => initNum('school-m-break', 'school-break-min', 10))
  const [afternoonLessonMin, setAfternoonLessonMin] = useState(() => initNum('school-a-lesson', 'school-lesson-min', 40))
  const [afternoonBreakMin, setAfternoonBreakMin] = useState(() => initNum('school-a-break', 'school-break-min', 10))

  useEffect(() => { localStorage.setItem('school-m-lesson', String(morningLessonMin)) }, [morningLessonMin])
  useEffect(() => { localStorage.setItem('school-m-break', String(morningBreakMin)) }, [morningBreakMin])
  useEffect(() => { localStorage.setItem('school-a-lesson', String(afternoonLessonMin)) }, [afternoonLessonMin])
  useEffect(() => { localStorage.setItem('school-a-break', String(afternoonBreakMin)) }, [afternoonBreakMin])
  useEffect(() => { localStorage.setItem('school-m-row-adjustment', String(extraMorning)) }, [extraMorning])
  useEffect(() => { localStorage.setItem('school-a-row-adjustment', String(extraAfternoon)) }, [extraAfternoon])

  const sessionLesson = (s) => Number(s === 'morning' ? morningLessonMin : afternoonLessonMin) || 40
  const sessionBreak = (s) => Number(s === 'morning' ? morningBreakMin : afternoonBreakMin) || 0

  function load(silent = false) {
    if (!silent) setLoading(true)
    setError(null)
    fetchAllLessons()

      .then((data) => setLessons(data.filter((l) => !l.special)))
      .catch((e) => setError(e.message))
      .finally(() => {
        if (!silent) setLoading(false)
      })
  }

  const reload = () => load(true)

  async function exportExcel() {
   try {

    const mod = await import('exceljs/dist/exceljs.min.js')
    const ExcelJS = mod.default || mod
    const wb = new ExcelJS.Workbook()
    const ws = wb.addWorksheet('Расписание ШКИ')

    const DAY_COLS = [
      ['MONDAY', 'Понедельник'], ['TUESDAY', 'Вторник'], ['WEDNESDAY', 'Среда'],
      ['THURSDAY', 'Четверг'], ['FRIDAY', 'Пятница'], ['SATURDAY', 'Суббота'],
    ]
    const NCOLS = 1 + DAY_COLS.length

    ws.columns = [{ width: 6 }, ...DAY_COLS.map(() => ({ width: 18 }))]

    const med = { style: 'medium', color: { argb: 'FF000000' } }
    const border = { top: med, left: med, bottom: med, right: med }

    let r = 1
    const styleRow = (font, valign) => {
      for (let cc = 1; cc <= NCOLS; cc++) {
        const cell = ws.getCell(r, cc)
        cell.border = border
        cell.alignment = { horizontal: 'center', vertical: valign || 'middle', wrapText: true }
        if (font) cell.font = font
      }
    }

    const title = (text) => {
      ws.mergeCells(r, 1, r, NCOLS)
      ws.getCell(r, 1).value = text
      styleRow({ bold: true, size: 13 })
      ws.getRow(r).height = 24
      r++
    }

    const header = () => {
      ws.getCell(r, 1).value = '№'
      DAY_COLS.forEach(([, label], i) => { ws.getCell(r, 2 + i).value = label })
      styleRow({ bold: true, size: 12 })
      ws.getRow(r).height = 24
      r++
    }

    const cellList = (day, order) =>
      lessons
        .filter((l) => !l.special && l.dayOfWeek === day && l.orderNumber === order)
        .sort((a, b) => compareGroupNumbers(a.groupNumber, b.groupNumber))

    const cellText = (day, order) => {
      const list = cellList(day, order)
      if (!list.length) return ''
      const session = order <= layout.morningRows ? 'morning' : 'afternoon'
      const regular = list.find(l => !l.customTime)
      const start = regular ? hhmm(regular.time) : null
      const lines = list.map((l) => `${l.groupNumber} ${l.studioCode}${l.customTime ? ` (${hhmm(l.time)}–${hhmm(l.endTime)})` : ''}`)
      return [...(start ? [`${start}–${addMinutes(start, sessionLesson(session))}`] : []), ...lines].join('\n')
    }

    const period = (order) => {
      ws.getCell(r, 1).value = order
      let maxLines = 1
      DAY_COLS.forEach(([key], i) => {
        const txt = cellText(key, order)
        ws.getCell(r, 2 + i).value = txt
        if (txt) maxLines = Math.max(maxLines, txt.split('\n').length)
      })
      styleRow(null, 'top')
      ws.getCell(r, 1).alignment = { horizontal: 'center', vertical: 'middle' }
      ws.getCell(r, 1).font = { bold: true }
      ws.getRow(r).height = maxLines * 15 + 6
      r++
    }

    title('Расписание ШКИ общее — первая смена (утро)')
    header()
    for (let i = 0; i < layout.morningRows; i++) period(i + 1)

    title('Вторая смена (день)')
    header()
    for (let i = 0; i < layout.afternoonRows; i++) period(layout.firstAfternoon + i)

    const fmtHours = (min) => {
      const h = Math.floor(min / 60)
      const m = min % 60
      if (h === 0) return `${m} мин`
      return m === 0 ? `${h} ч` : `${h} ч ${m} мин`
    }
    const shiftOf = (order) => (order <= layout.morningRows ? 'morning' : 'afternoon')
    const shiftLabel = (set) => {
      const m = set.has('morning'), a = set.has('afternoon')
      if (m && a) return 'Утро и день'
      if (m) return 'Утро (1 смена)'
      if (a) return 'День (2 смена)'
      return '—'
    }

    const reg = lessons.filter((l) => !l.special && l.groupNumber != null)
    const totalLessons = reg.length
    let totalMin = 0
    const byGroup = {}
    const byStudio = {}
    reg.forEach((l) => {
      const s = shiftOf(l.orderNumber)
      const dur = lessonMinutes(l, sessionLesson(s))
      totalMin += dur
      const g = byGroup[l.groupNumber] || (byGroup[l.groupNumber] = { count: 0, shifts: new Set() })
      g.count++
      g.shifts.add(s)
      const code = l.studioCode || '—'
      const st = byStudio[code] || (byStudio[code] = { name: l.studioName || code, count: 0, min: 0 })
      st.count++
      st.min += dur
    })

    const spacer = () => { r++ }
    const fillBorders = () => { for (let cc = 1; cc <= NCOLS; cc++) ws.getCell(r, cc).border = border }
    const sumTitle = (text) => {
      ws.mergeCells(r, 1, r, NCOLS)
      ws.getCell(r, 1).value = text
      fillBorders()
      ws.getCell(r, 1).font = { bold: true, size: 13 }
      ws.getCell(r, 1).alignment = { horizontal: 'left', vertical: 'middle', wrapText: true }
      ws.getRow(r).height = 24
      r++
    }

    const gridRow = (cells, opts = {}) => {
      cells.forEach(([value, c1, c2]) => {
        if (c2 > c1) ws.mergeCells(r, c1, r, c2)
        ws.getCell(r, c1).value = value
      })
      fillBorders()
      for (let cc = 1; cc <= NCOLS; cc++) {
        ws.getCell(r, cc).alignment = { horizontal: opts.left ? 'left' : 'center', vertical: 'middle', wrapText: true }
        ws.getCell(r, cc).font = { bold: !!opts.bold, size: 11 }
      }
      ws.getRow(r).height = opts.h || 18
      r++
    }

    spacer()
    sumTitle('Сводка по расписанию (за неделю)')
    gridRow([['Всего занятий в неделю', 1, 5], [totalLessons, 6, NCOLS]], { left: true })
    gridRow([['Длительность занятия — первая смена (утро)', 1, 5], [`${sessionLesson('morning')} мин`, 6, NCOLS]], { left: true })
    gridRow([['Длительность занятия — вторая смена (день)', 1, 5], [`${sessionLesson('afternoon')} мин`, 6, NCOLS]], { left: true })
    gridRow([['Общее время занятий в неделю', 1, 5], [fmtHours(totalMin), 6, NCOLS]], { left: true })

    spacer()
    sumTitle('Группы')
    gridRow([['Группа', 1, 2], ['Занятий в неделю', 3, 5], ['Смена', 6, NCOLS]], { bold: true, h: 22 })
    Object.keys(byGroup)
      .sort(compareGroupNumbers)
      .forEach((num) => {
        const g = byGroup[num]
        gridRow([[`${num} группа`, 1, 2], [g.count, 3, 5], [shiftLabel(g.shifts), 6, NCOLS]])
      })
    gridRow([['Всего групп', 1, 2], [Object.keys(byGroup).length, 3, 5], ['', 6, NCOLS]], { bold: true })

    spacer()
    sumTitle('Часы по студиям')
    gridRow([['Студия', 1, 4], ['Занятий', 5, 5], ['Часов в неделю', 6, NCOLS]], { bold: true, h: 22 })
    Object.keys(byStudio)
      .sort((a, b) => byStudio[b].min - byStudio[a].min)
      .forEach((code) => {
        const st = byStudio[code]
        gridRow([[`${st.name} (${code})`, 1, 4], [st.count, 5, 5], [fmtHours(st.min), 6, NCOLS]])
      })

    const buf = await wb.xlsx.writeBuffer()
    const blob = new Blob([buf], {
      type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
    })
    const url = URL.createObjectURL(blob)
    const a = document.createElement('a')
    a.href = url
    a.download = 'Расписание_ШКИ.xlsx'
    a.click()
    URL.revokeObjectURL(url)
   } catch (e) {
     console.error('Ошибка экспорта в Excel:', e)
     alert('Не удалось сформировать Excel: ' + (e?.message || e))
   }
  }

  const reloadGroups = () => fetchGroupsList().then(setGroups).catch(() => {})

  useEffect(() => {
    load(false)
    fetchGroupsList().then(setGroups).catch(() => setGroups([]))
  }, [])

  const studioCodes = useMemo(() => {
    const set = new Set(DEFAULT_STUDIO_CODES)
    lessons.forEach((l) => l.studioCode && set.add(l.studioCode))
    return [...set].sort()
  }, [lessons])

  const groupNumbers = useMemo(() => {
    if (groups.length) return groups.map((g) => g.number).sort(compareGroupNumbers)
    return [...new Set(lessons.map((l) => l.groupNumber).filter(Boolean))].sort(compareGroupNumbers)
  }, [groups, lessons])

  function allowedGroupNumbers(session) {
    if (!groups.length) return groupNumbers
    const want = sessionToShift(session)
    return groups
      .filter((g) => (g.shift || 'MORNING') === want)
      .map((g) => g.number)
      .sort(compareGroupNumbers)
  }














  const groupShift = useMemo(() => {
    const map = new Map()
    groups.forEach((g) => map.set(normalizeGroupNumber(g.number), g.shift || 'MORNING'))
    return map
  }, [groups])

  const isAfternoonLesson = useCallback(
    (l) => {
      const groupNumber = normalizeGroupNumber(l.groupNumber)
      return groupShift.has(groupNumber) ? groupShift.get(groupNumber) === 'AFTERNOON' : hhmm(l.time) >= '14:00'
    },
    [groupShift],
  )

  const layout = useMemo(() => {
    const ordersOf = (pred) => lessons.filter(pred).map((l) => l.orderNumber).filter(Boolean)
    const afternoonOrders = ordersOf(isAfternoonLesson)
    const morningOrders = ordersOf((l) => !isAfternoonLesson(l))
    const maxM = morningOrders.length ? Math.max(...morningOrders) : 0

    if (!afternoonOrders.length) {


      const morningRows = Math.max(maxM, 5 + extraMorning, 1)
      return {
        morningRows,
        firstAfternoon: morningRows + 1,
        afternoonRows: Math.max(5 + extraAfternoon, 1),
      }
    }

    const minA = Math.min(...afternoonOrders)
    const maxA = Math.max(...afternoonOrders)




    const morningRows = Math.max(minA - 1, 1)
    const afternoonRows = Math.max(maxA - minA + 1, 5 + extraAfternoon, 1)
    return { morningRows, firstAfternoon: morningRows + 1, afternoonRows }
  }, [lessons, isAfternoonLesson, extraMorning, extraAfternoon])

  function cellLessons(dayKey, order) {
    return lessons
      .filter((l) => l.dayOfWeek === dayKey && l.orderNumber === order)
      .sort((a, b) => compareGroupNumbers(a.groupNumber, b.groupNumber))
  }

  function modalGroupNumbers(form) {
    if (!form) return groupNumbers
    const base = form.session ? allowedGroupNumbers(form.session) : groupNumbers
    const cur = form.groupNumber != null && form.groupNumber !== '' ? String(form.groupNumber) : null
    if (cur != null && !base.some((number) => normalizeGroupNumber(number) === normalizeGroupNumber(cur))) {
      return [...base, cur].sort(compareGroupNumbers)
    }
    return base
  }

  function openCreate(dayKey, time, order) {
    const session = order <= layout.morningRows ? 'morning' : 'afternoon'
    const allowed = allowedGroupNumbers(session)
    setForm({
      id: null,
      customTime: false,
      defaultTime: time,
      defaultEndTime: addMinutes(time, sessionLesson(session)),
      dayOfWeek: dayKey,
      time,
      endTime: addMinutes(time, sessionLesson(session)),
      orderNumber: order,
      groupNumber: allowed[0] ?? '',
      studioCode: studioCodes[0] ?? 'ФВ',
      session,
    })
  }

  function openEdit(l) {
    const session = (l.orderNumber ?? 1) <= layout.morningRows ? 'morning' : 'afternoon'
    const time = hhmm(l.time)
    const order = l.orderNumber ?? 1
    const regular = cellLessons(l.dayOfWeek, order).find(item => !item.customTime)
    const slotTime = regular ? hhmm(regular.time) : timeOverrides[`${l.dayOfWeek}-${order}`]
      ?? defaultTime(l.dayOfWeek, session, order - (session === 'morning' ? 1 : layout.firstAfternoon))
    setForm({
      id: l.id,
      customTime: !!l.customTime,
      defaultTime: slotTime,
      defaultEndTime: addMinutes(slotTime, sessionLesson(session)),
      dayOfWeek: l.dayOfWeek,
      time,
      endTime: l.endTime ? hhmm(l.endTime) : addMinutes(time, sessionLesson(session)),
      orderNumber: l.orderNumber ?? 1,
      groupNumber: l.groupNumber,
      studioCode: l.studioCode,
      session,
    })
  }

  async function moveLesson(lesson, targetDay, targetOrder, targetTime) {
    if (!lesson || (lesson.dayOfWeek === targetDay && lesson.orderNumber === targetOrder)) return
    const targetSession = targetOrder <= layout.morningRows ? 'morning' : 'afternoon'
    const targetEndTime = addMinutes(targetTime, sessionLesson(targetSession))
    setBusy(true)
    try {
      await updateLesson(lesson.id, lessonPayload(lesson, {
        dayOfWeek: targetDay,
        orderNumber: targetOrder,
        time: lesson.customTime ? lesson.time : `${targetTime}:00`,
        endTime: lesson.customTime ? lesson.endTime : `${targetEndTime}:00`,
      }))
      toast?.success('Занятие перенесено')
      reload()
    } catch (e) {
      toast?.error(e.message)
    } finally {
      setBusy(false)
      setDragLesson(null)
    }
  }

  function askDeleteLesson(l) {
    const what = l.special
      ? `Мероприятие «${l.title || 'без названия'}» (${l.studioCode})`
      : `Группа ${l.groupNumber}, ${l.studioCode}`
    setConfirm({
      title: l.special ? 'Удалить мероприятие?' : 'Удалить занятие?',
      message: `${what}. Восстановить потом не получится.`,
      holdSeconds: 0,
      onConfirm: async () => {
        setConfirm(null)
        try {
          await deleteLesson(l.id)
          toast?.success(l.special ? 'Мероприятие удалено' : 'Занятие удалено')
          reload()
        } catch (e) {
          toast?.error(e.message)
        }
      },
    })
  }

  function askDeleteRow(session, order) {
    const inRow = lessons.filter((l) => l.orderNumber === order)
    setConfirm({
      title: `Удалить строку №${order}?`,
      message: inRow.length
        ? `Будут удалены все занятия пары №${order} (${inRow.length} шт.), а номера ниже сдвинутся вверх.`
        : 'Пустая строка будет убрана.',
      holdSeconds: 0,
      onConfirm: async () => {
        setConfirm(null)
        await deleteRow(session, order)
      },
    })
  }

  async function deleteRow(session, order) {
    const inRow = lessons.filter((l) => l.orderNumber === order)
    const after = lessons.filter((l) => l.orderNumber > order)
    if (inRow.length === 0 && after.length === 0) {
      if (session === 'afternoon') setExtraAfternoon((n) => n - 1)
      else setExtraMorning((n) => n - 1)
      toast?.success(`Строка №${order} удалена`)
      return
    }
    setBusy(true)
    try {
      await Promise.all(inRow.map((l) => deleteLesson(l.id)))
      await Promise.all(after.map((l) => updateLesson(l.id, lessonPayload(l, { orderNumber: l.orderNumber - 1 }))))
      if (session === 'afternoon') setExtraAfternoon((n) => n - 1)
      else setExtraMorning((n) => n - 1)
      toast?.success(`Строка №${order} удалена`)
      reload()
    } catch (e) {
      toast?.error(e.message)
    } finally {
      setBusy(false)
    }
  }

  function askDeleteAll(session, label) {
    const target = lessons.filter((l) => (session === 'afternoon') === isAfternoonLesson(l))
    setConfirm({
      title: 'Удалить все данные?',
      message: `Будут безвозвратно удалены ВСЕ занятия таблицы «${label}» (${target.length} шт.). Вы уверены?`,
      holdSeconds: 5,
      onConfirm: async () => {
        setConfirm(null)
        setBusy(true)
        try {
          await Promise.all(target.map((l) => deleteLesson(l.id)))
          if (session === 'afternoon') setExtraAfternoon(0)
          else setExtraMorning(0)
          toast?.success(`Все занятия таблицы «${label}» удалены`)
          reload()
        } catch (e) {
          toast?.error(e.message)
        } finally {
          setBusy(false)
        }
      },
    })
  }

  async function applyTimeEdit() {
    const newTime = timeEdit.value
    if (!newTime) return
    const { day, order } = timeEdit

    const isMorning = order <= layout.morningRows
    const session = isMorning ? 'morning' : 'afternoon'
    const lastOrder = isMorning ? layout.morningRows : layout.firstAfternoon + layout.afternoonRows - 1
    const step = sessionLesson(session) + sessionBreak(session)

    const updates = []
    const overrides = {}
    for (let p = order; p <= lastOrder; p++) {
      const start = addMinutes(newTime, (p - order) * step)
      const cell = lessons.filter((l) => !l.customTime && l.dayOfWeek === day && l.orderNumber === p)
      if (cell.length) {
        const end = addMinutes(start, sessionLesson(session))
        cell.forEach((l) => updates.push(updateLesson(l.id, lessonPayload(l, {
          time: `${start}:00`,
          endTime: `${end}:00`,
        }))))
      } else {
        overrides[`${day}-${p}`] = start
      }
    }
    try {
      if (updates.length) await Promise.all(updates)
      setTimeOverrides((m) => ({ ...m, ...overrides }))
      setTimeEdit(null)
      toast?.success('Время занятий обновлено')
      reload()
    } catch (e) {
      toast?.error(e.message)
    }
  }

  async function recomputeShift(session) {
    const step = sessionLesson(session) + sessionBreak(session)
    const firstOrder = session === 'morning' ? 1 : layout.firstAfternoon
    const lastOrder = session === 'morning'
      ? layout.morningRows
      : layout.firstAfternoon + layout.afternoonRows - 1
    const updates = []
    const overrides = {}
    for (const { key: day } of DAYS) {
      const anchorLessons = lessons.filter((l) => !l.customTime && l.dayOfWeek === day && l.orderNumber === firstOrder)
      const anchor = anchorLessons.length
        ? hhmm(anchorLessons[0].time)
        : timeOverrides[`${day}-${firstOrder}`] ?? defaultTime(day, session, 0)
      for (let p = firstOrder; p <= lastOrder; p++) {
        const start = addMinutes(anchor, (p - firstOrder) * step)
        const cell = lessons.filter((l) => !l.customTime && l.dayOfWeek === day && l.orderNumber === p)
        if (cell.length) {
          const end = addMinutes(start, sessionLesson(session))
          cell.forEach((l) => updates.push(updateLesson(l.id, lessonPayload(l, {
            time: `${start}:00`,
            endTime: `${end}:00`,
          }))))
        }
        else overrides[`${day}-${p}`] = start
      }
    }
    setBusy(true)
    try {
      if (updates.length) await Promise.all(updates)
      setTimeOverrides((m) => ({ ...m, ...overrides }))
      toast?.success(session === 'morning' ? 'Время первой смены пересчитано' : 'Время второй смены пересчитано')
      reload()
    } catch (e) {
      toast?.error(e.message)
    } finally {
      setBusy(false)
    }
  }

  async function addMorningRow() {
    const afternoon = lessons.filter(isAfternoonLesson)
    if (!afternoon.length) {


      setExtraMorning((n) => n + 1)
      return
    }
    setBusy(true)
    try {
      await Promise.all(afternoon.map((l) => updateLesson(l.id, lessonPayload(l, { orderNumber: (l.orderNumber ?? 0) + 1 }))))
      reload()
    } catch (e) {
      alert(e.message)
    } finally {
      setBusy(false)
    }
  }

  async function changeGroupShift(g, shift) {
    try {
      await updateGroup(g.id, { number: g.number, shift })
      toast?.success(`Группа ${g.number}: смена обновлена`)
      reloadGroups()
    } catch (e) {
      toast?.error(e.message)
    }
  }

  async function addGroup(number, shift) {
    await createGroup({ number: normalizeGroupNumber(number), shift })
    toast?.success(`Группа ${number} добавлена`)
    reloadGroups()
  }

  function askDeleteGroup(g) {
    setConfirm({
      title: `Удалить группу ${g.number}?`,
      message: 'Группу можно удалить, только если у неё нет занятий в расписании.',
      holdSeconds: 0,
      onConfirm: async () => {
        setConfirm(null)
        try {
          await deleteGroup(g.id)
          toast?.success(`Группа ${g.number} удалена`)
          reloadGroups()
        } catch (e) {
          toast?.error(e.message)
        }
      },
    })
  }

  function Cell({ dayKey, session, index, order }) {
    const items = cellLessons(dayKey, order)
    const regularItem = items.find(l => !l.customTime)
    const time = regularItem
      ? hhmm(regularItem.time)
      : timeOverrides[`${dayKey}-${order}`] ?? defaultTime(dayKey, session, index)
    const editing = timeEdit && timeEdit.day === dayKey && timeEdit.order === order

    return (
      <div
        className="min-w-[150px] space-y-1 p-2"
        onDragOver={(e) => {
          if (dragLesson) e.preventDefault()
        }}
        onDrop={() => moveLesson(dragLesson, dayKey, order, time)}
      >
        {editing ? (
          <div className="flex items-center gap-1.5">
            <input
              type="time"
              value={timeEdit.value}
              onChange={(e) => setTimeEdit((s) => ({ ...s, value: e.target.value }))}
              className="h-10 min-w-0 flex-1 rounded-md border-2 border-brand bg-surface px-2 text-base text-ink outline-none"
            />
            <button type="button" onClick={() => applyTimeEdit()} className="grid size-9 shrink-0 place-items-center rounded-md text-brand hover:bg-canvas" title="Сохранить время">
              <Check className="size-5" />
            </button>
            <button type="button" onClick={() => setTimeEdit(null)} className="grid size-9 shrink-0 place-items-center rounded-md text-muted hover:bg-canvas" title="Отмена">
              <X className="size-5" />
            </button>
          </div>
        ) : (
          <div className="flex items-center justify-between gap-1 text-xl font-medium text-ink">
            <span>{time}–{addMinutes(time, sessionLesson(session))}</span>
            <button type="button" onClick={() => setTimeEdit({ day: dayKey, order, value: time })} className="text-muted transition-colors hover:text-brand" title="Изменить время">
              <Pencil className="size-6" />
            </button>
          </div>
        )}

        {items.map((l) => (
          <div
            key={l.id}
            draggable={!l.special}
            onDragStart={() => !l.special && setDragLesson(l)}
            onDragEnd={() => setDragLesson(null)}
            className={
              'flex items-center gap-1.5 rounded-md border bg-canvas px-2.5 py-2 text-xl ' +
              (l.special ? 'border-brand' : 'border-line')
            }
          >
            {!l.special && (
              <GripVertical className="size-6 shrink-0 cursor-grab text-muted/50" title="Перетащить" />
            )}
            {l.groupNumber != null ? (
              <span className="min-w-8 shrink-0 text-center text-2xl font-bold text-ink">{l.groupNumber}</span>
            ) : (
              <span className="shrink-0 text-base font-semibold text-brand">все</span>
            )}
            <span className="flex min-w-0 flex-1 flex-col text-left text-ink">
              <span className="flex items-center gap-1">
                <span className="truncate">{l.studioCode}</span>
                {l.special && <Sparkles className="size-5 shrink-0 text-brand" />}
              </span>
              {l.special && l.date && <span className="truncate text-base font-medium text-brand">{fmtDateShort(l.date)}</span>}
              {l.customTime && <span className="whitespace-nowrap text-sm font-medium text-brand">{hhmm(l.time)}–{hhmm(l.endTime)}</span>}
            </span>
            <button type="button" onClick={() => openEdit(l)} className="text-muted transition-colors hover:text-brand" title="Изменить">
              <Pencil className="size-6" />
            </button>
            <button type="button" onClick={() => askDeleteLesson(l)} className="text-muted transition-colors hover:text-red-500" title="Удалить">
              <Trash2 className="size-6" />
            </button>
          </div>
        ))}

        <button
          type="button"
          onClick={() => openCreate(dayKey, time, order)}
          className="flex w-full items-center justify-center gap-1 rounded-md border border-dashed border-line py-2 text-lg text-muted transition-colors hover:border-brand hover:text-brand"
        >
          <Plus className="size-6" /> добавить
        </button>
      </div>
    )
  }

  function Table({ session, count, startOrder, onAddRow, addLabel }) {
    return (
      <div className="overflow-x-auto rounded-card border-2 border-line">
        <table className="w-full border-collapse">
          <thead>
            <tr className="bg-canvas">
              <th className="w-14 border border-line px-2 py-3 text-2xl font-semibold text-ink">№</th>
              {DAYS.map((d) => (
                <th key={d.key} className="border border-line px-4 py-3 text-2xl font-semibold text-ink">{d.label}</th>
              ))}
            </tr>
          </thead>
          <tbody>
            {Array.from({ length: count }, (_, index) => {
              const order = startOrder + index
              return (
                <tr key={order}>
                  <th className="border border-line bg-canvas px-2 py-2 align-middle">
                    <div className="flex flex-col items-center gap-1.5">
                      <span className="text-3xl font-bold text-ink">{order}</span>
                      <button
                        type="button"
                        onClick={() => askDeleteRow(session, order)}
                        className="text-muted transition-colors hover:text-red-500"
                        title="Удалить строку"
                      >
                        <Trash2 className="size-6" />
                      </button>
                    </div>
                  </th>
                  {DAYS.map((d) => (
                    <td key={d.key} className="border border-line align-top">
                      <Cell dayKey={d.key} session={session} index={index} order={order} />
                    </td>
                  ))}
                </tr>
              )
            })}
            <tr>
              <td colSpan={DAYS.length + 1} className="border border-line p-2">
                <button
                  type="button"
                  onClick={onAddRow}
                  disabled={busy}
                  className="flex w-full items-center justify-center gap-1.5 rounded-md border border-dashed border-line py-2.5 text-base font-medium text-muted transition-colors hover:border-brand hover:text-brand disabled:opacity-60"
                >
                  <Plus className="size-5" /> {addLabel}
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    )
  }

  if (loading) {
    return (
      <div className="flex items-center gap-2 rounded-card border-2 border-line bg-surface p-6 text-muted">
        <LoaderCircle className="size-5 animate-spin" /> Загрузка расписания…
      </div>
    )
  }

  if (error) {
    return (
      <div className="rounded-card border-2 border-red-300 bg-red-50 p-6 text-red-700 dark:border-red-500/40 dark:bg-red-500/10 dark:text-red-300">
        {error}
        <button type="button" onClick={() => load(false)} className="ml-3 underline">Повторить</button>
      </div>
    )
  }

  return (
    <div className="space-y-6 pt-4 md:pt-6">

      <div className="w-full min-w-0 space-y-6">
        <div>
          <div className="mb-3 flex flex-wrap items-center justify-between gap-3">
            <p className="text-2xl font-bold md:text-3xl">Первая смена (утро)</p>
            <DeleteAllButton onClick={() => askDeleteAll('morning', 'Первая смена')} disabled={busy} />
          </div>
          <SettingsBlock
            label="первой смены"
            lessonMin={morningLessonMin}
            setLessonMin={setMorningLessonMin}
            breakMin={morningBreakMin}
            setBreakMin={setMorningBreakMin}
            onRecompute={() => recomputeShift('morning')}
            busy={busy}
          />
          <Table session="morning" count={layout.morningRows} startOrder={1} onAddRow={addMorningRow} addLabel="Добавить занятие" />
        </div>
        <div className="mt-14 border-t-2 border-line pt-10">
          <div className="mb-3 flex flex-wrap items-center justify-between gap-3">
            <p className="text-2xl font-bold md:text-3xl">Вторая смена (день)</p>
            <DeleteAllButton onClick={() => askDeleteAll('afternoon', 'Вторая смена')} disabled={busy} />
          </div>
          <SettingsBlock
            label="второй смены"
            lessonMin={afternoonLessonMin}
            setLessonMin={setAfternoonLessonMin}
            breakMin={afternoonBreakMin}
            setBreakMin={setAfternoonBreakMin}
            onRecompute={() => recomputeShift('afternoon')}
            busy={busy}
          />
          <Table session="afternoon" count={layout.afternoonRows} startOrder={layout.firstAfternoon} onAddRow={() => setExtraAfternoon((n) => n + 1)} addLabel="Добавить занятие" />
        </div>

        <div className="mt-14 border-t-2 border-line pt-10">
          <div className="mb-3 flex items-center gap-2">

            <p className="text-2xl font-bold md:text-3xl">Группы и смены</p>
          </div>
          <p className="mb-4 text-lg text-muted">
            Выберите смену для каждой группы. Группу можно добавить в расписание только в её смене:
            первая смена — в таблицу «Первая смена (утро)», вторая — в «Вторая смена (день)».
          </p>
          <GroupShiftTable
            groups={groups}
            onChangeShift={changeGroupShift}
            onAdd={addGroup}
            onDelete={askDeleteGroup}
          />
        </div>

        <div className="mt-14 border-t-2 border-line pt-10">
          <p className="mb-3 text-2xl font-bold md:text-3xl">Сводка по группам</p>
          <p className="mb-4 text-lg text-muted">
            Количество занятий каждой группы в текущем недельном расписании.
          </p>
          <GroupSummaryTable
            groups={groups}
            lessons={lessons}
            isAfternoonLesson={isAfternoonLesson}
          />

          <div className="mt-10">
            <p className="mb-4 text-2xl font-bold md:text-3xl">Часы по студиям</p>
            <StudioSummaryTable
              lessons={lessons}
              isAfternoonLesson={isAfternoonLesson}
              morningLessonMin={morningLessonMin}
              afternoonLessonMin={afternoonLessonMin}
            />
          </div>
        </div>

        <button
          type="button"
          onClick={exportExcel}
          className="flex w-full items-center justify-center gap-2 rounded-card border-2 border-green-600/60 bg-surface py-4 text-2xl font-semibold text-green-700 transition hover:border-green-600 hover:bg-green-600/10 active:scale-[0.99] dark:text-green-400"
        >
          <FileSpreadsheet className="size-8" /> Экспорт в Excel
        </button>
      </div>

      {confirm && (
        <ConfirmModal
          title={confirm.title}
          message={confirm.message}
          holdSeconds={confirm.holdSeconds}
          onConfirm={confirm.onConfirm}
          onCancel={() => setConfirm(null)}
        />
      )}

      {form && (
        <LessonModal
          form={form}
          setForm={setForm}
          days={DAYS}
          groupNumbers={modalGroupNumbers(form)}
          studioCodes={studioCodes}
          onClose={() => setForm(null)}
          onSaved={() => {
            setForm(null)
            reload()
          }}
        />
      )}
    </div>
  )
}

function GroupShiftTable({ groups, onChangeShift, onAdd, onDelete }) {
  const toast = useToast()
  const [newNumber, setNewNumber] = useState('')
  const [newShift, setNewShift] = useState('MORNING')
  const [adding, setAdding] = useState(false)

  const morning = groups.filter((g) => (g.shift || 'MORNING') === 'MORNING').length
  const afternoon = groups.length - morning

  async function submitAdd(e) {
    e.preventDefault()
    if (!newNumber) return
    setAdding(true)
    try {
      await onAdd(newNumber, newShift)
      setNewNumber('')
    } catch (error) {
      toast?.error(error.message)
    } finally {
      setAdding(false)
    }
  }

  const cell = 'border border-line px-4 py-3 text-xl text-ink'
  const select = 'h-12 w-full max-w-[280px] rounded-card border-2 border-line bg-surface px-3 text-lg text-ink outline-none focus:border-brand'

  return (
    <div className="overflow-x-auto rounded-card border-2 border-line">
      <table className="w-full border-collapse">
        <thead>
          <tr className="bg-canvas">
            <th className="w-28 border border-line px-4 py-3 text-2xl font-semibold text-ink">Группа</th>
            <th className="border border-line px-4 py-3 text-2xl font-semibold text-ink">Смена</th>
            <th className="w-24 border border-line px-4 py-3 text-2xl font-semibold text-ink">Удалить</th>
          </tr>
        </thead>
        <tbody>
          {groups.length === 0 && (
            <tr>
              <td colSpan={3} className="border border-line px-4 py-6 text-center text-lg text-muted">
                Групп пока нет — добавьте первую ниже.
              </td>
            </tr>
          )}
          {groups.map((g) => (
            <tr key={g.id}>
              <td className={cell + ' text-center text-3xl font-bold'}>{g.number}</td>
              <td className={cell}>
                <select
                  value={g.shift || 'MORNING'}
                  onChange={(e) => onChangeShift(g, e.target.value)}
                  className={select}
                >
                  {SHIFTS.map((s) => (<option key={s.key} value={s.key}>{s.label}</option>))}
                </select>
              </td>
              <td className={cell + ' text-center'}>
                <button
                  type="button"
                  onClick={() => onDelete(g)}
                  className="text-muted transition-colors hover:text-red-500"
                  title="Удалить группу"
                >
                  <Trash2 className="mx-auto size-6" />
                </button>
              </td>
            </tr>
          ))}

          <tr>
            <td colSpan={3} className="border border-line p-3">
              <form onSubmit={submitAdd} className="flex flex-wrap items-center gap-3">
                <input
                  type="text"
                  maxLength={32}
                  pattern="[A-Za-zА-Яа-яЁё0-9]+(?:-[A-Za-zА-Яа-яЁё0-9]+)*"
                  autoComplete="off"
                  value={newNumber}
                  onChange={(e) => setNewNumber(e.target.value)}
                  placeholder="7 или ВР-1"
                  title="Буквы, цифры и дефис"
                  className="h-12 w-40 rounded-card border-2 border-line bg-surface px-3 text-lg text-ink outline-none focus:border-brand"
                />
                <select value={newShift} onChange={(e) => setNewShift(e.target.value)} className={select}>
                  {SHIFTS.map((s) => (<option key={s.key} value={s.key}>{s.label}</option>))}
                </select>
                <button
                  type="submit"
                  disabled={adding || !newNumber}
                  className="flex h-12 items-center gap-2 rounded-card border-2 border-brand-ring bg-gradient-to-r from-brand-light to-brand px-5 text-lg font-semibold text-white transition hover:opacity-90 active:scale-95 disabled:opacity-60"
                >
                  {adding ? <LoaderCircle className="size-5 animate-spin" /> : <Plus className="size-5" />} Добавить группу
                </button>
                <span className="ml-auto text-base text-muted">
                  Всего: {groups.length} · утро: {morning} · день: {afternoon}
                </span>
              </form>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  )
}

function GroupSummaryTable({ groups, lessons, isAfternoonLesson }) {
  const rows = useMemo(() => {
    const counts = new Map()
    const lessonShifts = new Map()

    lessons.forEach((lesson) => {
      if (lesson.special || lesson.groupNumber == null) return
      const number = normalizeGroupNumber(lesson.groupNumber)
      counts.set(number, (counts.get(number) || 0) + 1)
      const shifts = lessonShifts.get(number) || new Set()
      shifts.add(isAfternoonLesson(lesson) ? 'AFTERNOON' : 'MORNING')
      lessonShifts.set(number, shifts)
    })

    const configured = new Map(groups.map((group) => [normalizeGroupNumber(group.number), group.shift || 'MORNING']))
    const numbers = new Set([...configured.keys(), ...counts.keys()])

    return [...numbers]
      .sort(compareGroupNumbers)
      .map((number) => {
        const configuredShift = configured.get(number)
        const shifts = lessonShifts.get(number) || new Set()
        let shift = configuredShift
          ? SHIFTS.find((item) => item.key === configuredShift)?.label
          : null
        if (!shift) {
          shift = shifts.size > 1
            ? 'Утро и день'
            : shifts.has('AFTERNOON')
              ? 'Вторая смена (день)'
              : 'Первая смена (утро)'
        }
        return { number, count: counts.get(number) || 0, shift }
      })
  }, [groups, lessons, isAfternoonLesson])

  const totalLessons = rows.reduce((sum, row) => sum + row.count, 0)
  const cell = 'border border-line px-4 py-3 text-xl text-ink'

  return (
    <div className="overflow-x-auto rounded-card border-2 border-line">
      <table className="w-full border-collapse">
        <thead>
          <tr className="bg-canvas">
            <th className="border border-line px-4 py-3 text-2xl font-semibold text-ink">Группа</th>
            <th className="border border-line px-4 py-3 text-2xl font-semibold text-ink">Занятий в неделю</th>
            <th className="border border-line px-4 py-3 text-2xl font-semibold text-ink">Смена</th>
          </tr>
        </thead>
        <tbody>
          {rows.length === 0 ? (
            <tr>
              <td colSpan={3} className="border border-line px-4 py-6 text-center text-lg text-muted">
                Групп пока нет.
              </td>
            </tr>
          ) : (
            rows.map((row) => (
              <tr key={row.number}>
                <td className={cell + ' text-center text-2xl font-bold'}>{row.number} группа</td>
                <td className={cell + ' text-center text-2xl font-semibold tabular-nums'}>{row.count}</td>
                <td className={cell}>{row.shift}</td>
              </tr>
            ))
          )}
          <tr className="bg-canvas/70 font-bold">
            <td className={cell}>Всего групп: {rows.length}</td>
            <td className={cell + ' text-center tabular-nums'}>{totalLessons}</td>
            <td className={cell}>—</td>
          </tr>
        </tbody>
      </table>
    </div>
  )
}

function StudioSummaryTable({ lessons, isAfternoonLesson, morningLessonMin, afternoonLessonMin }) {
  const rows = useMemo(() => {
    const studios = new Map()

    lessons.forEach((lesson) => {
      if (lesson.special || !lesson.studioCode) return

      const code = lesson.studioCode
      const current = studios.get(code) || {
        code,
        name: lesson.studioName || code,
        count: 0,
        minutes: 0,
      }
      current.count += 1
      current.minutes += lessonMinutes(lesson, Number(isAfternoonLesson(lesson) ? afternoonLessonMin : morningLessonMin) || 40)
      studios.set(code, current)
    })

    return [...studios.values()].sort(
      (a, b) => b.minutes - a.minutes || a.name.localeCompare(b.name, 'ru'),
    )
  }, [lessons, isAfternoonLesson, morningLessonMin, afternoonLessonMin])

  const formatDuration = (minutes) => {
    const hours = Math.floor(minutes / 60)
    const rest = minutes % 60
    if (hours === 0) return `${rest} мин`
    return rest === 0 ? `${hours} ч` : `${hours} ч ${rest} мин`
  }
  const cell = 'border border-line px-4 py-3 text-xl text-ink'

  return (
    <div className="overflow-x-auto rounded-card border-2 border-line">
      <table className="w-full min-w-[640px] border-collapse">
        <thead>
          <tr className="bg-canvas">
            <th className="border border-line px-4 py-3 text-2xl font-semibold text-ink">Студия</th>
            <th className="border border-line px-4 py-3 text-2xl font-semibold text-ink">Занятий</th>
            <th className="border border-line px-4 py-3 text-2xl font-semibold text-ink">Часов в неделю</th>
          </tr>
        </thead>
        <tbody>
          {rows.length === 0 ? (
            <tr>
              <td colSpan={3} className="border border-line px-4 py-6 text-center text-lg text-muted">
                Занятий в студиях пока нет.
              </td>
            </tr>
          ) : (
            rows.map((row) => (
              <tr key={row.code}>
                <td className={cell + ' text-center font-medium'}>{row.name} ({row.code})</td>
                <td className={cell + ' text-center font-semibold tabular-nums'}>{row.count}</td>
                <td className={cell + ' text-center font-semibold tabular-nums'}>{formatDuration(row.minutes)}</td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  )
}

function SettingsBlock({ label, lessonMin, setLessonMin, breakMin, setBreakMin, onRecompute, busy }) {
  const numBox = 'flex h-16 w-36 items-center gap-1 rounded-card border-2 border-line bg-canvas px-4 transition-colors focus-within:border-brand'
  const numInput = 'w-full min-w-0 bg-transparent text-2xl font-bold text-ink outline-none'
  return (
    <div className="mb-4 overflow-hidden rounded-card border-2 border-line bg-surface">

      <div className="flex flex-wrap items-end gap-4 px-6 py-5">
        <label className="flex flex-col gap-1.5">
          <span className="text-lg font-semibold text-ink">Занятие</span>
          <div className={numBox}>
            <input type="number" min="1" value={lessonMin} onChange={(e) => setLessonMin(Number(e.target.value) || 0)} className={numInput} />
            <span className="text-lg text-muted">мин</span>
          </div>
        </label>
        <label className="flex flex-col gap-1.5">
          <span className="text-lg font-semibold text-ink">Перемена</span>
          <div className={numBox}>
            <input type="number" min="0" value={breakMin} onChange={(e) => setBreakMin(Number(e.target.value) || 0)} className={numInput} />
            <span className="text-lg text-muted">мин</span>
          </div>
        </label>
        <button
          type="button"
          onClick={onRecompute}
          disabled={busy}
          className="flex min-h-16 w-full items-center justify-center gap-2 rounded-card border-2 border-brand-ring bg-gradient-to-r from-brand-light to-brand px-6 py-3 text-center text-lg font-semibold text-white transition hover:opacity-90 active:scale-95 disabled:opacity-60 md:w-auto md:text-2xl"
        >
          <RefreshCw className={'size-6 shrink-0 ' + (busy ? 'animate-spin' : '')} /> Пересчитать время занятий
        </button>
      </div>

      <div className="flex items-start gap-2.5 border-t-2 border-line bg-canvas/60 px-6 py-4 text-lg text-muted">
        <Info className="mt-0.5 size-6 shrink-0 text-brand" />
        <span>«Пересчитать время занятий» применит занятие + перемену к этой смене (каждый день — от своего первого занятия).</span>
      </div>
    </div>
  )
}

function DeleteAllButton({ onClick, disabled }) {
  return (
    <button
      type="button"
      onClick={onClick}
      disabled={disabled}
      className="flex shrink-0 items-center gap-2 rounded-md border-2 border-red-500/60 px-4 py-2.5 text-base font-medium text-red-500 transition hover:border-red-500 hover:bg-red-500/10 active:scale-95 disabled:opacity-50 md:px-5 md:text-xl"
    >
      <Trash2 className="size-5 md:size-6" /> Удалить все
    </button>
  )
}

function lessonPayload(l, override = {}) {
  return {
    dayOfWeek: l.dayOfWeek,
    time: hhmm(l.time).length === 5 ? `${hhmm(l.time)}:00` : l.time,
    orderNumber: l.orderNumber ?? 1,
    groupNumber: l.groupNumber,
    studioCode: l.studioCode,
    special: !!l.special,
    date: l.date ?? null,
    endTime: l.endTime ?? null,
    customTime: !!l.customTime,
    title: l.title ?? null,
    description: l.description ?? null,
    ...override,
  }
}

function LessonModal({ form, setForm, days, groupNumbers, studioCodes, onClose, onSaved }) {
  const toast = useToast()
  const [saving, setSaving] = useState(false)
  const [err, setErr] = useState(null)
  const isEdit = form.id != null
  useBodyScrollLock(true)

  const set = (patch) => setForm((f) => ({ ...f, ...patch }))

  async function save(e) {
    e.preventDefault()
    setErr(null)
    if (form.customTime && (!form.time || !form.endTime || form.endTime <= form.time)) {
      setErr('Окончание занятия должно быть позже начала')
      return
    }
    setSaving(true)
    const payload = {
      dayOfWeek: form.dayOfWeek,
      time: form.time.length === 5 ? `${form.time}:00` : form.time,
      orderNumber: Number(form.orderNumber) || 1,
      groupNumber: normalizeGroupNumber(form.groupNumber),
      studioCode: form.studioCode,
      special: false,
      customTime: !!form.customTime,
      endTime: form.endTime.length === 5 ? `${form.endTime}:00` : form.endTime,
    }
    try {
      if (isEdit) await updateLesson(form.id, payload)
      else await createLesson(payload)
      toast?.success(isEdit ? 'Занятие изменено' : 'Занятие добавлено')
      onSaved()
    } catch (e2) {
      setErr(e2.message)
      toast?.error(e2.message)
    } finally {
      setSaving(false)
    }
  }

  const field = 'mt-1 h-10 w-full rounded-card border-2 border-line bg-surface px-3 text-sm text-ink outline-none focus:border-brand'

  return createPortal(
    <div className="fixed inset-0 z-50 grid animate-fade-in place-items-center bg-black/50 p-4">
      <form onSubmit={save} className="max-h-[90dvh] w-full max-w-lg overflow-y-auto animate-fade-up rounded-card border-2 border-line bg-surface p-6 shadow-xl">
        <div className="mb-5 flex items-center justify-between">
          <h3 className="text-xl font-bold text-ink">
            {isEdit ? 'Изменить занятие' : `Добавить занятие (занятие №${form.orderNumber})`}
          </h3>
          <button type="button" onClick={onClose} aria-label="Закрыть" className="grid size-8 place-items-center rounded-md text-muted transition-colors hover:text-ink">
            <X className="size-5" />
          </button>
        </div>

        <label className="block text-sm font-medium text-ink">
          День недели
          <select className={field} value={form.dayOfWeek} onChange={(e) => set({ dayOfWeek: e.target.value })}>
            {days.map((d) => (<option key={d.key} value={d.key}>{d.label}</option>))}
          </select>
        </label>

        <div className="mt-4 grid grid-cols-2 gap-3">
          <label className="block text-sm font-medium text-ink">
            Группа
            <select className={field} value={form.groupNumber} onChange={(e) => set({ groupNumber: e.target.value })}>
              {groupNumbers.map((n) => (<option key={n} value={n}>{n}</option>))}
            </select>
          </label>
          <label className="block text-sm font-medium text-ink">
            Студия (кабинет)
            <select className={field} value={form.studioCode} onChange={(e) => set({ studioCode: e.target.value })}>
              {studioCodes.map((c) => (<option key={c} value={c}>{c}</option>))}
            </select>
          </label>
        </div>

        <label className="mt-4 flex items-center gap-3 text-sm font-medium text-ink">
          <input type="checkbox" className="size-5 accent-brand" checked={!!form.customTime} onChange={e => set(e.target.checked
            ? { customTime: true }
            : { customTime: false, time: form.defaultTime, endTime: form.defaultEndTime })} />
          Своё время занятия
        </label>
        {form.customTime && <div className="mt-3 grid grid-cols-2 gap-3">
          <label className="block min-w-0 text-sm font-medium text-ink">Начало
            <input type="time" required className={field} value={form.time} onChange={e => set({ time: e.target.value })} />
          </label>
          <label className="block min-w-0 text-sm font-medium text-ink">Окончание
            <input type="time" required className={field} value={form.endTime} onChange={e => set({ endTime: e.target.value })} />
          </label>
        </div>}

        {err && (
          <p className="mt-4 rounded-card border-2 border-red-300 bg-red-50 px-3 py-2 text-sm text-red-700 dark:border-red-500/40 dark:bg-red-500/10 dark:text-red-300">{err}</p>
        )}

        <div className="mt-6 flex gap-3">
          <button type="button" onClick={onClose} className="h-10 flex-1 rounded-card border-2 border-line bg-surface text-sm font-semibold text-ink transition hover:border-brand hover:text-brand">Отмена</button>
          <button type="submit" disabled={saving} className="flex h-10 flex-1 items-center justify-center gap-2 rounded-card border-2 border-brand-ring bg-gradient-to-r from-brand-light to-brand text-sm font-semibold text-white transition hover:opacity-90 disabled:opacity-60">
            {saving && <LoaderCircle className="size-5 animate-spin" />}
            {saving ? 'Сохранение…' : 'Сохранить'}
          </button>
        </div>
      </form>
    </div>,
    document.body,
  )
}
