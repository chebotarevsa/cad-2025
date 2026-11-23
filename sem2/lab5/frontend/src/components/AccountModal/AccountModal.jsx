import { X, UserRound, ShieldCheck } from 'lucide-react'
import { roleLabel } from '../../auth'
import { useBodyScrollLock } from '../../useBodyScrollLock'

export default function AccountModal({ open, onClose, user }) {
  useBodyScrollLock(open)
  if (!open) return null

  return (

    <div className="fixed inset-0 z-40 grid animate-fade-in place-items-center bg-black/50 p-4">
      <div className="w-full max-w-md animate-fade-up rounded-card border-2 border-line bg-surface p-6 shadow-xl">
        <div className="mb-1 flex items-center justify-end">
          <button
            type="button"
            onClick={onClose}
            aria-label="Закрыть"
            className="grid size-9 place-items-center rounded-md text-muted transition-colors hover:text-ink"
          >
            <X className="size-6" />
          </button>
        </div>

        <div className="px-2 pb-4 text-center">
          <div className="mx-auto grid size-20 place-items-center rounded-full bg-gradient-to-b from-brand-light to-brand text-white">
            <UserRound className="size-10" strokeWidth={2} />
          </div>

          <p className="mt-5 text-sm text-muted">Вы вошли как</p>
          <p className="mt-1 text-2xl font-bold text-ink">{user?.fullName || user?.login}</p>
          <p className="text-base text-muted">@{user?.login}</p>

          <span className="mt-4 inline-flex items-center gap-1.5 rounded-full border border-brand-ring bg-brand/10 px-3 py-1 text-sm font-medium text-brand">
            <ShieldCheck className="size-4" />
            {roleLabel(user?.role)}
          </span>

          <p className="mt-6 text-sm text-muted">
            Здесь будет личный кабинет: управление расписанием, студиями и преподавателями.
          </p>
        </div>
      </div>
    </div>
  )
}
