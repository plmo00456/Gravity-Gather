module.exports = {
  darkMode: 'class',
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
      animation:{
        'shake': 'shake 0.62s cubic-bezier(.36,.07,.19,.97) both',
      },
      keyframes: {
        'shake' : {
          '10%, 90%': {
            transform: 'translate3d(-1px, 0, 0)'
          },
          '20%, 80%' : {
            transform: 'translate3d(2px, 0, 0)'
          },
          '30%, 50%, 70%': {
            transform: 'translate3d(-4px, 0, 0)'
          },
          '40%, 60%': {
            transform: 'translate3d(4px, 0, 0)'
          }
        }
      },
    }
  },
  variants: {
    extend: {
      userSelect: ['responsive'],
    }
  },
  plugins: [],
}
