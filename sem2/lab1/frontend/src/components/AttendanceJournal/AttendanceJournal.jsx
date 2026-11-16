import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { fetchGroupsList } from '../../api'
import { clearUser } from '../../auth'
import { isoLocal } from '../../dates'
import { FileSpreadsheet } from 'lucide-react'
import './AttendanceJournal.css'

async function request(path, options = {}) {
  const res = await fetch(`/api/attendance${path}`, { ...options, cache: 'no-store', headers: { 'Content-Type': 'application/json', 'X-Attendance-Request': '1' } })
  if (!res.ok) {
    let message = res.status === 401 ? 'Войдите заново, чтобы открыть журнал.' : 'Не удалось сохранить или загрузить журнал. Попробуйте ещё раз.'
    try { const body = await res.json(); if (body.error && res.status !== 401) message = body.error } catch {                      }
    const error = new Error(message); error.status = res.status; throw error
  }
  return res.status === 204 ? null : res.json()
}
const cellKey = (studentId, lessonId, date) => `${studentId}:${lessonId}:${date}`

export default function AttendanceJournal() {
  const navigate = useNavigate()
  const [groups, setGroups] = useState([])
  const [groupId, setGroupId] = useState('')
  const [month, setMonth] = useState(() => isoLocal(new Date()).slice(0, 7))
  const [journal, setJournal] = useState(null)
  const [loading, setLoading] = useState(true)
  const [busy, setBusy] = useState(false)
  const [error, setError] = useState(null)
  const [notice, setNotice] = useState('')
  const [revision, setRevision] = useState(0)
  const [names, setNames] = useState('')
  const [rosterOpen, setRosterOpen] = useState(false)
  const [editing, setEditing] = useState(null)
  const [deleteId, setDeleteId] = useState(null)
  const [subject, setSubject] = useState('')
  const [showArchived, setShowArchived] = useState(false)
  const [reportOpen, setReportOpen] = useState(false)
  const today = isoLocal(new Date())
  const [reportGroup, setReportGroup] = useState('current')
  const [reportStart, setReportStart] = useState(() => `${new Date().getFullYear()}-09-01`)
  const [reportEnd, setReportEnd] = useState(today)

  useEffect(() => {
    let active = true
    fetchGroupsList().then(data => {
      if (!active) return
      setGroups(data); setGroupId(current => data.some(g => String(g.id) === current) ? current : String(data[0]?.id || ''))
      if (!data.length) setLoading(false)
    }).catch(err => { if (active) { setError(err); setLoading(false) } })
    return () => { active = false }
  }, [revision])

  useEffect(() => {
    if (!groupId || !month) return
    const controller = new AbortController()
    setLoading(true); setError(null); setJournal(null); setNotice('')
    request(`?groupId=${groupId}&month=${month}`, { signal: controller.signal })
      .then(data => { if (!controller.signal.aborted) setJournal(data) })
      .catch(err => { if (!controller.signal.aborted) setError(err) })
      .finally(() => { if (!controller.signal.aborted) setLoading(false) })
    return () => controller.abort()
  }, [groupId, month, revision])

  async function save(action, success) {
    setBusy(true); setError(null); setNotice('')
    try { await action(); success?.(); setNotice('Сохранено') }
    catch (err) { setError(err) }
    finally { setBusy(false) }
  }
  function changeMark(student, col, value) {
    save(() => request('/marks', { method: 'PUT', body: JSON.stringify({ studentId: student.id, lessonId: col.lessonId, date: col.date, value }) }), () => {
      setJournal(current => ({ ...current, marks: [...current.marks.filter(m => cellKey(m.studentId, m.lessonId, m.lessonDate) !== cellKey(student.id, col.lessonId, col.date)),
        ...(value ? [{ studentId: student.id, lessonId: col.lessonId, lessonDate: col.date, mark: value }] : [])] }))
    })
  }
  function addStudents(event) {
    event.preventDefault()
    const list = names.split('\n').map(name => name.trim()).filter(Boolean)
    save(() => request(`/students?groupId=${groupId}`, { method: 'POST', body: JSON.stringify({ names: list }) }), () => { setNames(''); setRevision(n => n + 1) })
  }
  function updateStudent(archived) {
    save(() => request(`/students/${editing.id}`, { method: 'PUT', body: JSON.stringify({ name: editing.name, archived }) }), () => { setEditing(null); setRevision(n => n + 1) })
  }
  const subjects = [...new Set(journal?.columns.map(c => c.subject) || [])]
  const columns = journal?.columns.filter(c => !subject || c.subject === subject) || []
  const values = new Map(journal?.marks.map(m => [cellKey(m.studentId, m.lessonId, m.lessonDate), m.mark]) || [])
  const markedStudents = new Set(journal?.marks.map(m => m.studentId) || [])
  const visibleStudents = journal?.students.filter(s => !s.archived || showArchived || markedStudents.has(s.id)) || []

  return <div className="attendance-journal">
    <div className="attendance-toolbar">
      <label>Группа<select value={groupId} disabled={busy} onChange={e => { setGroupId(e.target.value); setEditing(null); setNames('') }}><option value="" disabled>Выберите группу</option>{groups.map(g => <option key={g.id} value={g.id}>{g.number}</option>)}</select></label>
      <label>Месяц<input aria-label="Месяц журнала" type="month" min="2000-01" max="2100-12" required value={month} disabled={busy} onChange={e => { if (e.target.value) setMonth(e.target.value) }} /></label>
      <label>Предмет<select value={subject} disabled={busy || loading} onChange={e => setSubject(e.target.value)}><option value="">Все предметы</option>{subject && !subjects.includes(subject) && <option value={subject}>{subject}</option>}{subjects.map(s => <option key={s}>{s}</option>)}</select></label>
      <button type="button" disabled={busy || !journal} onClick={() => setRosterOpen(v => !v)} aria-expanded={rosterOpen}>Список учеников</button>
      <button type="button" disabled={busy || loading} onClick={() => setRevision(n => n + 1)}>Обновить</button>
      <button type="button" className="attendance-report-button" disabled={busy || !groups.length} onClick={() => setReportOpen(v => !v)} aria-expanded={reportOpen}><FileSpreadsheet size={18} /> Отчёт в Excel</button>
    </div>
    <p className="attendance-help">Даты берутся из расписания группы. Н — отсутствовал, П — присутствовал, У — уважительная причина, Б — болеет, 2–5 — оценка. Пустая ячейка — ещё не отмечено.</p>
    {error && <div className="attendance-error" role="alert">{error.message}{error.status === 401 && <button type="button" onClick={() => { clearUser(); navigate('/login') }}>Войти</button>}</div>}
    <div role="status" className="attendance-status">{busy ? 'Сохраняем…' : loading ? 'Загружаем журнал…' : notice}</div>
    {!loading && !groups.length && !error && <p>Сначала создайте группу в расписании школы, затем нажмите «Обновить».</p>}
    {journal && <>
      {reportOpen && <AttendanceReport groups={groups} currentGroup={groupId} selection={reportGroup} setSelection={setReportGroup} start={reportStart} setStart={setReportStart} end={reportEnd} setEnd={setReportEnd} busy={busy} onBusy={setBusy} onError={setError} onNotice={setNotice} />}
      {rosterOpen && <div className="attendance-roster">
        <form onSubmit={addStudents}><label htmlFor="attendance-names">Добавить учеников — каждый с новой строки</label><textarea id="attendance-names" rows={4} value={names} onChange={e => setNames(e.target.value)} placeholder={'Иванов Иван\nПетрова Анна'} disabled={busy} required /><button disabled={busy || !names.trim()}>Добавить в группу</button></form>
        <p className="attendance-help">Чтобы исправить имя или убрать ученика из списка, нажмите на его имя в таблице. Старые отметки сохранятся в архиве.</p>
        <label><span><input type="checkbox" checked={showArchived} onChange={e => setShowArchived(e.target.checked)} /> Показать учеников из архива</span></label>
      </div>}
      {editing && <form className="attendance-student-edit" onSubmit={e => { e.preventDefault(); updateStudent(editing.archived) }}>
        <label>Имя ученика<input value={editing.name} maxLength={150} required onChange={e => setEditing({ ...editing, name: e.target.value })} disabled={busy} /></label>
        <button disabled={busy}>Сохранить имя</button>
        <button type="button" disabled={busy} onClick={() => updateStudent(!editing.archived)}>{editing.archived ? 'Вернуть в список' : 'В архив (сохранить отметки)'}</button>
        <button className="attendance-delete" type="button" disabled={busy} onClick={() => setDeleteId(editing.id)}>Удалить ученика</button>
        <button type="button" disabled={busy} onClick={() => { setEditing(null); setDeleteId(null) }}>Закрыть</button>
        {deleteId === editing.id && <div className="attendance-delete-confirm" role="alert">
          <p>Удалить ученика «{editing.name}» из группы? Все его отметки за все месяцы тоже будут удалены. Это действие нельзя отменить. Чтобы сохранить историю, выберите архив.</p>
          <button type="button" disabled={busy} onClick={() => setDeleteId(null)}>Отмена</button>
          <button className="attendance-delete" type="button" disabled={busy} onClick={() => save(() => request(`/students/${editing.id}`, { method: 'DELETE' }), () => { setEditing(null); setDeleteId(null); setRevision(n => n + 1) })}>Удалить вместе с отметками</button>
        </div>}
      </form>}
      {!visibleStudents.length && <div className="attendance-empty"><strong>В группе пока нет учеников</strong><p>Добавьте список, чтобы начать отмечать посещаемость.</p><button type="button" onClick={() => setRosterOpen(true)}>Добавить учеников</button></div>}
      {!columns.length && <p className="attendance-help">В выбранном месяце нет занятий{subject ? ' по этому предмету' : ''}. Проверьте расписание, праздники и каникулы.</p>}
      {!!visibleStudents.length && <div className="attendance-scroll" tabIndex={0} role="region" aria-label="Таблица посещаемости">
        <table><caption className="sr-only">Посещаемость группы {groups.find(g => String(g.id) === groupId)?.number}, {month}</caption><thead><tr><th scope="col" className="attendance-name">Ученик <span>· {visibleStudents.length}</span></th>{columns.map(col => <th scope="col" key={`${col.lessonId}:${col.date}`}><strong>{col.date.slice(8)}.{col.date.slice(5, 7)}</strong><small>{col.time?.slice(0, 5)}</small><span>{col.subject}</span>{col.historical && <small>Из истории</small>}</th>)}</tr></thead>
          <tbody>{visibleStudents.map((student, index) => <tr key={student.id}><th scope="row" className="attendance-name"><button disabled={busy} onClick={() => setEditing({ ...student })}>{index + 1}. {student.name}</button>{student.archived && <small>В архиве</small>}</th>{columns.map(col => {
            const value = values.get(cellKey(student.id, col.lessonId, col.date)) || ''
            return <td key={`${col.lessonId}:${col.date}`} data-mark={value}><select aria-label={`${student.name}, ${col.date}, ${col.subject}, ${col.time || ''}`} value={value} disabled={busy || (student.archived && !value) || (col.historical && !value)} onChange={e => changeMark(student, col, e.target.value)}><option value="">—</option>{['Н', 'П', 'У', 'Б', '2', '3', '4', '5'].map(mark => <option key={mark}>{mark}</option>)}</select></td>
          })}</tr>)}</tbody></table>
      </div>}
    </>}
  </div>
}

function monthsInRange(start, end) {
  const from = new Date(`${start}T00:00:00`)
  const to = new Date(`${end}T00:00:00`)
  if (Number.isNaN(from) || Number.isNaN(to) || from > to) throw new Error('Укажите корректный период отчёта.')
  const months = []
  let cursor = new Date(from.getFullYear(), from.getMonth(), 1)
  const last = new Date(to.getFullYear(), to.getMonth(), 1)
  while (cursor <= last) {
    months.push(`${cursor.getFullYear()}-${String(cursor.getMonth() + 1).padStart(2, '0')}`)
    cursor = new Date(cursor.getFullYear(), cursor.getMonth() + 1, 1)
  }
  if (months.length > 24) throw new Error('Для одного отчёта выберите период не больше 24 месяцев.')
  return months
}

function safeSheetName(name, used) {
  const base = `Группа ${name}`.replace(/[\\/*?:\[\]]/g, ' ').slice(0, 31) || 'Группа'
  let candidate = base; let index = 2
  while (used.has(candidate)) candidate = `${base.slice(0, 28)} ${index++}`
  used.add(candidate); return candidate
}

function markKey(mark) { return cellKey(mark.studentId, mark.lessonId, mark.lessonDate) }

function AttendanceReport({ groups, currentGroup, selection, setSelection, start, setStart, end, setEnd, busy, onBusy, onError, onNotice }) {
  async function exportReport(event) {
    event.preventDefault()
    onError(null); onNotice('')
    try {
      const months = monthsInRange(start, end)
      const chosen = selection === 'all' ? groups : groups.filter(group => String(group.id) === (selection === 'current' ? currentGroup : selection))
      if (!chosen.length) throw new Error('Выберите группу для отчёта.')
      onBusy(true)
      const reports = await Promise.all(chosen.map(async group => {
        const journals = await Promise.all(months.map(month => request(`?groupId=${group.id}&month=${month}`)))
        const students = new Map(); const columns = new Map(); const marks = new Map()
        journals.forEach(journal => {
          journal.students.forEach(student => students.set(student.id, student))
          journal.columns.forEach(column => { if (column.date >= start && column.date <= end) columns.set(`${column.lessonId}:${column.date}`, column) })
          journal.marks.forEach(mark => { if (mark.lessonDate >= start && mark.lessonDate <= end) marks.set(markKey(mark), mark.mark) })
        })
        return { group, students: [...students.values()].sort((a, b) => a.name.localeCompare(b.name, 'ru')), columns: [...columns.values()].sort((a, b) => a.date.localeCompare(b.date) || (a.time || '').localeCompare(b.time || '') || a.lessonId - b.lessonId), marks }
      }))
      const mod = await import('exceljs/dist/exceljs.min.js')
      const ExcelJS = mod.default || mod
      const workbook = new ExcelJS.Workbook()
      workbook.creator = 'Школа креативных индустрий'
      workbook.created = new Date()
      const usedNames = new Set()
      const thin = { style: 'thin', color: { argb: 'FFB8B8B8' } }
      for (const report of reports) {
        const sheet = workbook.addWorksheet(safeSheetName(report.group.number, usedNames), { views: [{ state: 'frozen', xSplit: 1, ySplit: 3 }] })
        sheet.columns = [{ width: 29 }, ...report.columns.map(() => ({ width: 15 }))]
        const lastCol = report.columns.length + 1
        sheet.mergeCells(1, 1, 1, Math.max(1, lastCol))
        sheet.getCell(1, 1).value = `Отчёт посещаемости — группа ${report.group.number}`
        sheet.getCell(1, 1).font = { bold: true, size: 15, color: { argb: 'FF333333' } }
        sheet.getCell(1, 1).alignment = { horizontal: 'center', vertical: 'middle' }
        sheet.getCell(1, 1).fill = { type: 'pattern', pattern: 'solid', fgColor: { argb: 'FFFFE0BE' } }
        sheet.getRow(1).height = 28
        sheet.mergeCells(2, 1, 2, Math.max(1, lastCol))
        sheet.getCell(2, 1).value = `Период: ${start.split('-').reverse().join('.')} — ${end.split('-').reverse().join('.')}. Н — отсутствовал, П — присутствовал, У — уважительная причина, Б — болеет, 2–5 — оценка.`
        sheet.getCell(2, 1).alignment = { wrapText: true, vertical: 'middle' }
        sheet.getCell(2, 1).font = { italic: true, color: { argb: 'FF666666' } }
        sheet.getRow(2).height = 33
        sheet.getCell(3, 1).value = 'Ученик'
        report.columns.forEach((column, index) => {
          const cell = sheet.getCell(3, index + 2)
          cell.value = `${column.date.split('-').reverse().slice(0, 2).join('.')}\n${column.time?.slice(0, 5) || ''}\n${column.subject}`
        })
        for (let column = 1; column <= lastCol; column++) {
          const cell = sheet.getCell(3, column)
          cell.font = { bold: true, size: 10, color: { argb: 'FF333333' } }
          cell.fill = { type: 'pattern', pattern: 'solid', fgColor: { argb: 'FFF3F5EE' } }
          cell.alignment = { wrapText: true, horizontal: 'center', vertical: 'middle' }
          cell.border = { top: thin, left: thin, bottom: thin, right: thin }
        }
        sheet.getCell(3, 1).alignment = { horizontal: 'left', vertical: 'middle' }
        sheet.getRow(3).height = 52
        report.students.forEach((student, rowIndex) => {
          const row = rowIndex + 4
          const name = sheet.getCell(row, 1); name.value = `${rowIndex + 1}. ${student.name}${student.archived ? ' (архив)' : ''}`
          name.alignment = { wrapText: true, vertical: 'middle' }
          report.columns.forEach((column, columnIndex) => {
            const cell = sheet.getCell(row, columnIndex + 2); const value = report.marks.get(cellKey(student.id, column.lessonId, column.date)) || ''
            cell.value = value; cell.alignment = { horizontal: 'center', vertical: 'middle' }
            if (value === 'Н') cell.font = { bold: true, color: { argb: 'FFB64536' } }
            if (value === 'П') cell.font = { bold: true, color: { argb: 'FF458255' } }
            if (value === 'У') cell.font = { bold: true, color: { argb: 'FF916E00' } }
            if (value === 'Б') cell.font = { bold: true, color: { argb: 'FF356C9A' } }
          })
          for (let column = 1; column <= lastCol; column++) sheet.getCell(row, column).border = { top: thin, left: thin, bottom: thin, right: thin }
        })
        if (!report.students.length) sheet.getCell(4, 1).value = 'В группе нет учеников за выбранный период.'
        sheet.autoFilter = { from: { row: 3, column: 1 }, to: { row: Math.max(3, report.students.length + 3), column: Math.max(1, lastCol) } }
      }
      const buffer = await workbook.xlsx.writeBuffer()
      const link = document.createElement('a'); link.href = URL.createObjectURL(new Blob([buffer], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' }))
      link.download = `Посещаемость_${start}_${end}${selection === 'all' ? '_все_группы' : ''}.xlsx`; link.click(); URL.revokeObjectURL(link.href)
      onNotice('Отчёт Excel сформирован')
    } catch (error) { onError(error) } finally { onBusy(false) }
  }
  return <form className="attendance-report" onSubmit={exportReport}>
    <strong><FileSpreadsheet size={19} /> Отчёт посещаемости в Excel</strong>
    <label>Группа<select value={selection} disabled={busy} onChange={e => setSelection(e.target.value)}><option value="current">Текущая группа</option><option value="all">Все группы</option>{groups.map(group => <option key={group.id} value={group.id}>Группа {group.number}</option>)}</select></label>
    <label>С<input type="date" value={start} max={end} disabled={busy} onChange={e => setStart(e.target.value)} required /></label>
    <label>По<input type="date" value={end} min={start} max={isoLocal(new Date())} disabled={busy} onChange={e => setEnd(e.target.value)} required /></label>
    <button className="attendance-report-button" disabled={busy}><FileSpreadsheet size={18} /> Скачать Excel</button>
  </form>
}
