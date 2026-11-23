import react from '@vitejs/plugin-react'
import { defineConfig } from 'vitest/config'

// Ports are fixed so FlatLedger never takes a port another project uses.
// strictPort: fail instead of silently moving to the next free port.
export default defineConfig({
  plugins: [react()],
  server: {
    host: '127.0.0.1',
    port: 5175,
    strictPort: true,
    proxy: {
      // The browser calls /api on the Vite server; Vite forwards it to the backend.
      '/api': 'http://127.0.0.1:8095',
    },
  },
  preview: {
    host: '127.0.0.1',
    port: 5176,
    strictPort: true,
  },
  test: {
    environment: 'jsdom',
    setupFiles: ['./src/test/setup.ts'],
  },
})
