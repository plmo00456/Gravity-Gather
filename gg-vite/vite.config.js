import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
export default defineConfig({
  resolve: {
    alias: [
      { find: '@', replacement: '/src' },
      { find: /^~@/, replacement: '/src' }
    ]
  },
  plugins: [vue()],
  server:{
    host: '0.0.0.0',
    port: 3000,
  }
})
