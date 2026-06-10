import { createApp } from 'vue'
import App from './App.vue'
import router from './router' // 아까 만든 router/index.js 임포트

const app = createApp(App)
app.use(router) // 앱에 라우터 장착
app.mount('#app')