


const KEY = 'default-selection'

export function getDefaultSelection() {
  try {
    const raw = localStorage.getItem(KEY)
    return raw ? JSON.parse(raw) : null
  } catch {
    return null
  }
}

export function setDefaultSelection(sel) {
  if (sel) localStorage.setItem(KEY, JSON.stringify(sel))
  else localStorage.removeItem(KEY)
}


export function defaultSelectionPath(sel, base = '') {
  if (!sel) return null
  const value = encodeURIComponent(sel.value)
  return sel.type === 'teacher' ? `${base}/teacher/${value}` : `${base}/group/${value}`
}



const LAST_KEY = 'last-selection'

export function getLastSelection() {
  try {
    const raw = localStorage.getItem(LAST_KEY)
    return raw ? JSON.parse(raw) : null
  } catch {
    return null
  }
}

export function setLastSelection(sel) {
  try {
    if (sel) localStorage.setItem(LAST_KEY, JSON.stringify(sel))
    else localStorage.removeItem(LAST_KEY)
  } catch {

  }
}
