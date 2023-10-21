import { createApp } from 'vue'
import App from './App.vue'
import { createPinia } from "pinia";
import {router} from "@/router";
import '@/styles.css';

async function init(){
    const app = createApp(App);
    app.use(createPinia())
        .use(router)
        .mount('#app');
}

init();
