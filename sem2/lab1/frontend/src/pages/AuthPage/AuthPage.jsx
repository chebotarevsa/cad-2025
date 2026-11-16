import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { Eye, EyeOff, User, Lock, LoaderCircle } from 'lucide-react'
import { login as apiLogin } from '../../api'
import { setUser } from '../../auth'

export default function AuthPage() {
  const [login, setLogin] = useState('')
  const [password, setPassword] = useState('')
  const [showPassword, setShowPassword] = useState(false)
  const [remember, setRemember] = useState(false)
  const [error, setError] = useState(null)
  const [submitting, setSubmitting] = useState(false)
  const navigate = useNavigate()

  async function handleSubmit(e) {
    e.preventDefault()
    setError(null)
    setSubmitting(true)
    try {
      const user = await apiLogin(login.trim(), password)
      setUser(user, remember)

      navigate('/dashboard', { state: { showAccount: true } })
    } catch (err) {
      setError(err.message)
    } finally {
      setSubmitting(false)
    }
  }

  const field =
    'flex h-14 w-full items-center gap-3 rounded-card border-2 border-line bg-surface px-4 transition-colors focus-within:border-brand'
  const input =
    'min-w-0 flex-1 bg-transparent text-base text-ink outline-none placeholder:text-muted/70'

  return (
    <div className="flex min-h-[100dvh] w-full flex-col items-center justify-start overflow-y-auto px-4 pt-[calc(env(safe-area-inset-top)+1.5rem)] pb-[calc(env(safe-area-inset-bottom)+2.5rem)] sm:justify-center">
      <form
        onSubmit={handleSubmit}
        className="w-full max-w-md rounded-card border-2 border-line bg-surface p-6 shadow-xl sm:p-8"
      >
        <h1 className="text-2xl font-bold text-ink">Вход</h1>
        <p className="mt-1 text-sm text-muted">Войдите, чтобы управлять расписанием</p>

        <label className="mt-6 block">
          <span className="mb-1.5 block text-sm font-medium text-ink">Логин</span>
          <div className={field}>
            <User className="size-5 shrink-0 text-muted" strokeWidth={2} />
            <input
              className={input}
              value={login}
              onChange={(e) => setLogin(e.target.value)}
              placeholder="Введите логин"
              autoComplete="username"
            />
          </div>
        </label>

        <label className="mt-4 block">
          <span className="mb-1.5 block text-sm font-medium text-ink">Пароль</span>
          <div className={field}>
            <Lock className="size-5 shrink-0 text-muted" strokeWidth={2} />
            <input
              className={input}
              type={showPassword ? 'text' : 'password'}
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="Введите пароль"
              autoComplete="current-password"
            />
            <button
              type="button"
              onClick={() => setShowPassword((s) => !s)}
              className="shrink-0 text-muted transition hover:text-brand active:scale-95"
              aria-label={showPassword ? 'Скрыть пароль' : 'Показать пароль'}
            >
              {
                                                             }
              {showPassword ? <Eye className="size-5" /> : <EyeOff className="size-5" />}
            </button>
          </div>
        </label>

        <label className="mt-4 flex w-fit cursor-pointer items-center gap-2.5 select-none">
          <input
            type="checkbox"
            checked={remember}
            onChange={(e) => setRemember(e.target.checked)}
            className="size-5 accent-brand"
          />
          <span className="text-sm text-ink">Запомнить меня</span>
        </label>

        {error && (
          <p className="mt-4 rounded-card border-2 border-red-300 bg-red-50 px-4 py-2.5 text-sm font-medium text-red-700 dark:border-red-500/40 dark:bg-red-500/10 dark:text-red-300">
            {error}
          </p>
        )}

        <button
          type="submit"
          disabled={submitting}
          className="mt-6 flex h-12 w-full items-center justify-center gap-2 rounded-card border-2 border-brand-ring bg-gradient-to-r from-brand-light to-brand text-lg font-semibold text-white transition hover:opacity-90 active:scale-[0.98] disabled:opacity-60 disabled:active:scale-100"
        >
          {submitting && <LoaderCircle className="size-5 animate-spin" />}
          {submitting ? 'Вход…' : 'Войти'}
        </button>

        <Link
          to="/"
          className="mt-4 block text-center text-sm text-muted transition hover:text-brand"
        >
          Вернуться к расписанию
        </Link>
      </form>
    </div>
  )
}
