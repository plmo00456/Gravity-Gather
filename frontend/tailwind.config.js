module.exports = {
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      backgroundImage: theme => ({
        'login_background': "url('@/assets/background/login_bg.jpg')",
      })
    }
  },
  plugins: [],
}