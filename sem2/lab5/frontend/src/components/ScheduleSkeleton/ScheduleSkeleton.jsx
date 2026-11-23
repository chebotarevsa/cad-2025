

function SkeletonCard() {
  return (
    <div className="flex items-stretch gap-4 rounded-card border-[calc(2px/(var(--ui-base)*var(--ui-zoom)))] border-line bg-surface px-5 py-4">
      <div className="flex w-14 shrink-0 flex-col items-center justify-center gap-1">
        <div className="h-4 w-11 rounded bg-line/70" />
        <div className="my-1 size-7 rounded-[9px] bg-line/70" />
        <div className="h-3.5 w-11 rounded bg-line/60" />
      </div>
      <div className="w-0.5 shrink-0 self-stretch rounded-full bg-line" />
      <div className="flex flex-1 flex-col justify-center gap-2">
        <div className="h-[18px] w-2/3 rounded bg-line/70" />
        <div className="h-3.5 w-2/5 rounded bg-line/60" />
      </div>
    </div>
  )
}

function SkeletonDayHead() {
  return (
    <div className="mb-3.5 flex items-center gap-3 rounded-card border-[calc(2px/(var(--ui-base)*var(--ui-zoom)))] border-line bg-canvas px-3 py-2">
      <div className="size-11 shrink-0 rounded-tile bg-line/70" />
      <div className="flex flex-col gap-1.5">
        <div className="h-[15px] w-24 rounded bg-line/70" />
        <div className="h-2.5 w-12 rounded bg-line/60" />
      </div>
      <div className="ml-auto h-7 w-20 rounded-full bg-line/70" />
    </div>
  )
}

export default function ScheduleSkeleton() {
  return (
    <div className="flex animate-pulse flex-col gap-11" aria-hidden>
      {[0, 1].map((section) => (
        <section key={section}>
          <SkeletonDayHead />
          <div className="flex flex-col gap-2.5">
            {[0, 1, 2].map((i) => (
              <SkeletonCard key={i} />
            ))}
          </div>
        </section>
      ))}
    </div>
  )
}
