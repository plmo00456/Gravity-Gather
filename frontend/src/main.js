import { createApp } from 'vue'
import App from './App.vue'
import { createPinia } from "pinia";
import {router} from "@/router";
import { library } from '@fortawesome/fontawesome-svg-core'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import Vue from 'vue'
import Swal from 'sweetalert2'

import { fas } from '@fortawesome/free-solid-svg-icons'
import { far } from '@fortawesome/free-regular-svg-icons'
import { fab } from '@fortawesome/free-brands-svg-icons'

import '@/styles.css';

const app = createApp(App);

function msg(text){
    this.$swal.fire(text);
}

function msgSuccess(text, title){
    const option  = {};
    option.icon = 'success'
    option.confirmButtonText = '확인';
    option.confirmButtonColor =  '#3c82f6';
    option.text = text;
    if(title)
        option.title = title;
    this.$swal.fire(option);
}

function msgError(text, title){
    const option  = {};
    option.icon = 'error'
    option.confirmButtonText = '확인';
    option.confirmButtonColor =  '#3c82f6';
    option.text = text;
    if(title)
        option.title = title;
    this.$swal.fire(option);
}

async function init(){
    // 폰트어썸 라이브러리 추가

    library.add(
        fas,
        far,
        fab
    )

    // SweetAlert2
    app.config.globalProperties.$swal = Swal;
    app.config.globalProperties.msg = msg;
    app.config.globalProperties.msgSuccess = msgSuccess;
    app.config.globalProperties.msgError = msgError;


    app.use(createPinia())
        .use(router)
        .component('font-awesome-icon', FontAwesomeIcon)
        .mount('#app');
}

init();
