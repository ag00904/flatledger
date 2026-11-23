import { render, screen } from '@testing-library/react'
import { afterEach, describe, expect, it, vi } from 'vitest'
import App from './App'

function stubFetch(impl: () => Promise<Response>) {
  const fetchMock = vi.fn(impl)
  vi.stubGlobal('fetch', fetchMock)
  return fetchMock
}

describe('App', () => {
  afterEach(() => {
    vi.unstubAllGlobals()
  })

  it('shows ok when the API answers ok', async () => {
    const fetchMock = stubFetch(async () => Response.json({ status: 'ok' }))
    render(<App />)
    expect(await screen.findByText('API: ok')).toBeInTheDocument()
    expect(fetchMock).toHaveBeenCalledWith('/api/ping', expect.anything())
  })

  it('shows unreachable when the API returns an error', async () => {
    stubFetch(async () => new Response('down', { status: 503 }))
    render(<App />)
    expect(await screen.findByText('API: unreachable')).toBeInTheDocument()
  })

  it('shows unreachable when the network fails', async () => {
    stubFetch(async () => {
      throw new TypeError('Failed to fetch')
    })
    render(<App />)
    expect(await screen.findByText('API: unreachable')).toBeInTheDocument()
  })
})
