import { createApp } from 'vue'
import App from './App.vue'
import { createPinia } from "pinia";
import {router} from "@/router";
import { library } from '@fortawesome/fontawesome-svg-core'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import Swal from 'sweetalert2'
import tippy from 'tippy.js';
import 'tippy.js/dist/tippy.css';

import { fas } from '@fortawesome/free-solid-svg-icons'
import { far } from '@fortawesome/free-regular-svg-icons'
import { fab } from '@fortawesome/free-brands-svg-icons'

import '@/styles.css';
import "@/assets/css/font.css";

const app = createApp(App);

function msg(text){
    Swal.fire(text);
}

function msgSuccess(text, title){
    const option  = {};
    option.icon = 'success'
    option.confirmButtonText = '확인';
    option.confirmButtonColor =  '#3c82f6';
    option.text = text;
    if(title)
        option.title = title;
    Swal.fire(option);
}

function msgError(text, title){
    const option  = {};
    option.icon = 'error'
    option.confirmButtonText = '확인';
    option.confirmButtonColor =  '#3c82f6';
    option.text = text;
    if(title)
        option.title = title;
    Swal.fire(option);
}

function tooltips(cls){
    [...document.querySelectorAll('*')].forEach(node => {
        if (node._tippy) {
            node._tippy.destroy();
        }
    });

    const clsEle = document.querySelectorAll('.' + cls);

    clsEle.forEach(ele => {
        const dataAtr = ele.dataset.tooltip;
        tippy(ele, {
            content: dataAtr ? dataAtr : ele.innerText,
        });
    })
}

async function init(){
    // 폰트어썸 라이브러리 추가

    library.add(
        fas,
        far,
        fab
    )

    // SweetAlert2
    app.config.globalProperties.utils = {};
    app.config.globalProperties.$swal = Swal;
    app.config.globalProperties.utils.msg = msg;
    app.config.globalProperties.utils.msgSuccess = msgSuccess;
    app.config.globalProperties.utils.msgError = msgError;
    app.config.globalProperties.utils.normalErrorMsg = "오류가 발생하였습니다. 관리자에게 문의해주세요.";

    app.config.globalProperties.utils.tooltips = tooltips;

    app.use(createPinia())
        .use(router)
        .component('font-awesome-icon', FontAwesomeIcon)
        .mount('#app');
}

init();
