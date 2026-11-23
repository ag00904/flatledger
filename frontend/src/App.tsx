import { useEffect, useState } from 'react'
import { fetchPing } from './api/ping'

type ApiState = 'checking' | 'ok' | 'unreachable'

const LABELS: Record<ApiState, string> = {
  checking: 'API: checking…',
  ok: 'API: ok',
  unreachable: 'API: unreachable',
}

export default function App() {
  const [api, setApi] = useState<ApiState>('checking')

  useEffect(() => {
    // Abort on unmount (React StrictMode mounts twice in development).
    const controller = new AbortController()
    fetchPing(controller.signal)
      .then((status) => setApi(status === 'ok' ? 'ok' : 'unreachable'))
      .catch(() => {
        if (!controller.signal.aborted) setApi('unreachable')
      })
    return () => controller.abort()
  }, [])

  return (
    <main>
      <h1>FlatLedger</h1>
      <p role="status">{LABELS[api]}</p>
    </main>
  )
}
