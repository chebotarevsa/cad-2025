import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import tailwindcss from '@tailwindcss/vite'
import { VitePWA } from 'vite-plugin-pwa'

export default defineConfig({
  plugins: [
    react(),
    tailwindcss(),
    VitePWA({
      registerType: 'autoUpdate',
      injectRegister: false,
      includeAssets: ['favicon.ico', 'apple-touch-icon-180x180.png'],
      pwaAssets: {
        config: true,
        overrideManifestIcons: true,


        injectThemeColor: false,
      },
      manifest: {



        id: '/',
        name: 'Расписание ШКИ',
        short_name: 'РасписаШКИ',
        description: 'Расписание Школы креативных индустрий г. Строитель',
        lang: 'ru',
        dir: 'ltr',
        theme_color: '#e9e9e9',
        background_color: '#e9e9e9',
        display: 'standalone',



        display_override: ['standalone', 'minimal-ui'],
        orientation: 'portrait',
        start_url: '/',
        scope: '/',
      },
      workbox: {
        navigateFallback: '/index.html',


        navigateFallbackDenylist: [/^\/api\//],
        globIgnores: ['**/exceljs*.js'],
        cleanupOutdatedCaches: true,
        clientsClaim: true,
        skipWaiting: true,

        importScripts: ['/push-sw.js'],
        runtimeCaching: [
          {
            urlPattern: ({ url }) => url.pathname.startsWith('/api/') && !url.pathname.startsWith('/api/map/') && !url.pathname.startsWith('/api/attendance') && !url.pathname.startsWith('/api/auth/'),
            handler: 'NetworkFirst',
            options: {
              cacheName: 'api-cache',
              expiration: { maxEntries: 200, maxAgeSeconds: 60 * 60 * 24 },
              cacheableResponse: { statuses: [0, 200] },
            },
          },
          {



            urlPattern: ({ url }) =>
              url.origin === 'https://fonts.googleapis.com' || url.origin === 'https://fonts.gstatic.com',
            handler: 'CacheFirst',
            options: {
              cacheName: 'google-fonts',
              expiration: { maxEntries: 30, maxAgeSeconds: 60 * 60 * 24 * 365 },
              cacheableResponse: { statuses: [0, 200] },
            },
          },
        ],
      },
      devOptions: { enabled: true },
    }),
  ],
  server: {
    host: '127.0.0.1',
    port: 5173,
    proxy: {
      '/api': 'http://localhost:8083',
    },
  },
})
