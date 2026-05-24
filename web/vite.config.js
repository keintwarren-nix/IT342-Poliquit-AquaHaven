import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import fs from 'fs'
import path from 'path'

export default defineConfig({
  plugins: [react(), {
    name: 'copy-redirects',
    closeBundle() {
      const src = path.resolve(__dirname, '_redirects')
      const dest = path.resolve(__dirname, 'dist/_redirects')
      if (fs.existsSync(src)) {
        fs.copyFileSync(src, dest)
      }
    }
  }],
  server: {
    port: 5173,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  },
  build: {
    outDir: 'dist',
    sourcemap: false
  }
})
