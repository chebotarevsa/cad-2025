import { getPushPublicKey, savePushSubscription } from './api'

function urlBase64ToUint8Array(base64String) {
  const padding = '='.repeat((4 - (base64String.length % 4)) % 4)
  const base64 = (base64String + padding).replace(/-/g, '+').replace(/_/g, '/')
  const raw = atob(base64)
  const arr = new Uint8Array(raw.length)
  for (let i = 0; i < raw.length; i++) arr[i] = raw.charCodeAt(i)
  return arr
}

export function pushSupported() {
  return 'serviceWorker' in navigator && 'PushManager' in window && 'Notification' in window
}

export async function getPushState() {
  if (!pushSupported()) return 'unsupported'
  if (Notification.permission === 'denied') return 'denied'
  const reg = await navigator.serviceWorker.getRegistration()
  const sub = reg ? await reg.pushManager.getSubscription() : null
  return sub ? 'subscribed' : 'default'
}

export async function enablePush() {
  if (!pushSupported()) {
    throw new Error('Уведомления не поддерживаются этим браузером')
  }
  const { enabled, publicKey } = await getPushPublicKey()
  if (!enabled || !publicKey) {
    throw new Error('Уведомления пока не настроены на сервере')
  }

  const permission = await Notification.requestPermission()
  if (permission !== 'granted') {
    throw new Error('Вы не разрешили уведомления')
  }

  const reg = await navigator.serviceWorker.ready
  let sub = await reg.pushManager.getSubscription()
  if (!sub) {
    sub = await reg.pushManager.subscribe({
      userVisibleOnly: true,
      applicationServerKey: urlBase64ToUint8Array(publicKey),
    })
  }

  const keys = sub.toJSON().keys
  await savePushSubscription({ endpoint: sub.endpoint, p256dh: keys.p256dh, auth: keys.auth })
  return 'subscribed'
}
