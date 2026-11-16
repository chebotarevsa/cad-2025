


const store = new Map()

export function getCached(key) {
  return store.get(key)
}

export function loadCached(key, loader) {
  if (store.has(key)) return Promise.resolve(store.get(key))
  return loader().then((value) => {
    store.set(key, value)
    return value
  })
}

export function setCache(key, value) {
  store.set(key, value)
}


export function dropCache(key) {
  if (key) store.delete(key)
  else store.clear()
}
