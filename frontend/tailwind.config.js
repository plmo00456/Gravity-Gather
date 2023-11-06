module.exports = {
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      userSelect: {
        'none': 'none',
        'text': 'text',
        'all': 'all',
        'auto': 'auto'
      },
      backgroundImage: {
        'login_background': "url('@/assets/background/login_bg.jpg')",
        'main_background': "url('@/assets/background/main_bg.jpg')",
        'room_1_background': "url('@/assets/background/room_bg_1.jpg')",
        'room_2_background': "url('@/assets/background/room_bg_2.jpg')",
        'room_3_background': "url('@/assets/background/room_bg_3.jpg')",
      },
    }
  },
  variants: {
    extend: {
      userSelect: ['responsive']
    }
  },
  plugins: [],
}
