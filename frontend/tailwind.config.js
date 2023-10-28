module.exports = {
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      backgroundImage: theme => ({
        'login_background': "url('@/assets/background/login_bg.jpg')",
        'main_background': "url('@/assets/background/main_bg.jpg')",
      })
    }
  },
  plugins: [],
}