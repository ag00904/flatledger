/** Calls the backend's GET /api/ping and returns its status ("ok" when the database answered). */
export async function fetchPing(signal?: AbortSignal): Promise<string> {
  const res = await fetch('/api/ping', { signal })
  if (!res.ok) {
    throw new Error(`ping failed: HTTP ${res.status}`)
  }
  const body: { status?: unknown } = await res.json()
  if (typeof body.status !== 'string') {
    throw new Error('ping: unexpected response')
  }
  return body.status
}
