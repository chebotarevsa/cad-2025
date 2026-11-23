
export default function SiteFooter() {
  return (
    <footer className="border-t border-line pb-[calc(110px+env(safe-area-inset-bottom))]">
      <div className="mx-auto max-w-[772px] px-3 py-7 text-center text-muted md:px-6">
        <p className="text-base font-medium">Школа креативных индустрий г. Строитель</p>
        <div className="mt-3 mb-3 flex flex-wrap justify-center gap-x-6 gap-y-2 text-base underline">
          <a href="https://vk.com/shkistroitel" className="transition-colors hover:text-brand">ВКонтакте</a>
          <a
            href="https://vk.com/away.php?to=https%3A%2F%2Fweb.max.ru%2F-69221720244297&utf=1"
            className="transition-colors hover:text-brand"
          >
            MAX
          </a>
          <a href="https://rutube.ru/channel/77788736/" className="transition-colors hover:text-brand">RUTUBE</a>
        </div>
        <a href="https://vk.com/kir1lka" className="text-sm text-muted/70 transition-colors hover:text-brand">
          Made by kirill
        </a>
      </div>
    </footer>
  )
}
