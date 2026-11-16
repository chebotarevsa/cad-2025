import { useState, useEffect } from 'react'
import { createPortal } from 'react-dom'
import { TriangleAlert } from 'lucide-react'
import { useBodyScrollLock } from '../../useBodyScrollLock'

export default function ConfirmModal({
  title,
  message,
  confirmLabel = 'Да',
  cancelLabel = 'Нет',
  holdSeconds = 0,
  onConfirm,
  onCancel,
}) {
  const [left, setLeft] = useState(holdSeconds)
  useBodyScrollLock(true)

  useEffect(() => {
    setLeft(holdSeconds)
    if (holdSeconds <= 0) return
    const id = setInterval(() => {
      setLeft((v) => {
        if (v <= 1) {
          clearInterval(id)
          return 0
        }
        return v - 1
      })
    }, 1000)
    return () => clearInterval(id)
  }, [holdSeconds])

  const locked = left > 0

  return createPortal(
    <div className="fixed inset-0 z-[60] grid animate-fade-in place-items-center bg-black/50 p-4">
      <div className="w-full max-w-md animate-fade-up rounded-card border-2 border-line bg-surface p-6 shadow-xl">
        <div className="mb-4 flex items-center gap-4">
          <span className="grid size-12 shrink-0 place-items-center rounded-full bg-red-500/10 text-red-500">
            <TriangleAlert className="size-7" />
          </span>
          <h3 className="text-2xl font-bold text-ink">{title}</h3>
        </div>
        <p className="text-base text-muted">{message}</p>

        <div className="mt-6 flex gap-3">
          <button
            type="button"
            onClick={onCancel}
            className="h-10 flex-1 rounded-card border-2 border-line bg-surface text-sm font-semibold text-ink transition hover:border-brand hover:text-brand"
          >
            {cancelLabel}
          </button>
          <button
            type="button"
            onClick={onConfirm}
            disabled={locked}
            className="h-10 flex-1 rounded-card border-2 border-red-600 bg-red-500 text-sm font-semibold text-white transition hover:bg-red-600 disabled:cursor-not-allowed disabled:opacity-60"
          >
            {locked ? `${confirmLabel} (${left})` : confirmLabel}
          </button>
        </div>
      </div>
    </div>,
    document.body,
  )
}
