import { createContext, useContext, useState, useCallback, useMemo } from 'react'
import { createPortal } from 'react-dom'
import { CheckCircle2, AlertCircle, Info, X } from 'lucide-react'

const ToastCtx = createContext(null)

export function useToast() {
  return useContext(ToastCtx)
}

let counter = 0

const STYLES = {
  success: { Icon: CheckCircle2, ring: 'border-green-500/40', bar: 'bg-green-500', icon: 'text-green-500' },
  error: { Icon: AlertCircle, ring: 'border-red-500/40', bar: 'bg-red-500', icon: 'text-red-500' },
  info: { Icon: Info, ring: 'border-brand-ring', bar: 'bg-brand', icon: 'text-brand' },
}

function ToastItem({ toast, onClose }) {
  const s = STYLES[toast.type] || STYLES.info
  const { Icon } = s
  return (
    <div
      role="status"
      className={`pointer-events-auto relative flex w-80 max-w-[88vw] animate-fade-up items-start gap-3 overflow-hidden rounded-card border-2 ${s.ring} bg-surface px-4 py-3 pl-5 shadow-xl`}
    >
      <span className={`absolute left-0 top-0 h-full w-1.5 ${s.bar}`} />
      <Icon className={`mt-0.5 size-6 shrink-0 ${s.icon}`} />
      <p className="flex-1 text-base font-medium text-ink">{toast.message}</p>
      <button
        type="button"
        onClick={onClose}
        aria-label="Закрыть"
        className="grid size-7 shrink-0 place-items-center rounded-md text-muted transition-colors hover:text-ink"
      >
        <X className="size-5" />
      </button>
    </div>
  )
}

export function ToastProvider({ children }) {
  const [toasts, setToasts] = useState([])

  const remove = useCallback((id) => {
    setToasts((list) => list.filter((t) => t.id !== id))
  }, [])

  const push = useCallback((message, type = 'success') => {
    if (!message) return
    const id = ++counter
    setToasts((list) => [...list, { id, message: String(message), type }])
    setTimeout(() => remove(id), 3500)
  }, [remove])

  const api = useMemo(() => ({
    success: (m) => push(m, 'success'),
    error: (m) => push(m, 'error'),
    info: (m) => push(m, 'info'),
  }), [push])

  return (
    <ToastCtx.Provider value={api}>
      {children}
      {createPortal(
        <div className="pointer-events-none fixed top-4 right-4 z-[90] flex flex-col gap-3">
          {toasts.map((t) => (
            <ToastItem key={t.id} toast={t} onClose={() => remove(t.id)} />
          ))}
        </div>,
        document.body,
      )}
    </ToastCtx.Provider>
  )
}
