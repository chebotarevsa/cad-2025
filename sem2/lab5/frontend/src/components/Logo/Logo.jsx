import { Link } from 'react-router-dom'

export default function Logo({ to = '/login' }) {
  return (
    <Link
      to={to}
      className="inline-flex cursor-pointer items-center transition duration-150 select-none hover:opacity-80 active:scale-95"
    >
      <span className="text-3xl font-bold text-ink md:text-[40px]">Расписа</span>
      <span className="ml-1 rounded-md bg-gradient-to-b from-brand-light to-brand px-2 text-3xl font-bold text-white md:text-[40px]">
        ШКИ
      </span>
    </Link>
  )
}
