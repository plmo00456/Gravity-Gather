import { createApp } from 'vue'
import App from './App.vue'
import { createPinia } from "pinia";
import {router} from "@/router";
import { library } from '@fortawesome/fontawesome-svg-core'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import Swal from 'sweetalert2'
import tippy from 'tippy.js';
import 'tippy.js/dist/tippy.css';
import 'tippy.js/themes/light.css';
import { Eggy } from '@s-r0/eggy-js';
import '@imengyu/vue3-context-menu/lib/vue3-context-menu.css'
import ContextMenu from '@imengyu/vue3-context-menu'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import 'dayjs/locale/ko'
import langKR from 'element-plus/dist/locale/ko'
import VueDatePicker from '@vuepic/vue-datepicker';
import '@vuepic/vue-datepicker/dist/main.css'

import { fas } from '@fortawesome/free-solid-svg-icons'
import { far } from '@fortawesome/free-regular-svg-icons'
import { fab } from '@fortawesome/free-brands-svg-icons'

import '@/styles.css';
import "@/assets/css/font.css";

// import '@s-r0/eggy-js/build/css/eggy.css';
// import '@s-r0/eggy-js/build/css/progressbar.css';
// import '@s-r0/eggy-js/build/css/theme.css';

const app = createApp(App);

const soundData = new Audio();
soundData.autoplay = true;
soundData.src = "data:audio/wav;base64,SUQzAwAAAAAfdlRJVDIAAAABAAAAVFBFMQAAAAEAAABUQUxCAAAAAQAAAFRZRVIAAAABAAAAVENPTgAAAAEAAABUUkNLAAAAAQAAAENPTU0AAAAYAAAAZW5nAGF1ZGlvLWV4dHJhY3Rvci5uZXQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA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SsPDA0IjdqHKGflFmkKjk4Ik0j4zgs3a0kWGyBGbWmeDmtRkCIHVDHpwEeNEaLBsz4cx5804w4TYdcnBAmeiGeGm7ViiI3IEzTQzQM2KeBQdvHkDjcaxl9iATkp+cOn7R4novacbeWQgbAuOwDBNF4SGoNzoKw4IoEyEHgTloGhWDxuNZC+xzGvsRL8WF2G+Uv7Gvh2ThEL5bKhoIZ8I48GojlIsHaMwMiYX0YkF0fyQtWQvsRN2WNxL4HG41lYySjMGT9ZEvtG04vseJ3llllVMQU1FMy4xMDBVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVF/sIAAAA//uSZPsA9kxkRCjeomIAAA0gAAABH7WjSe3pjfAAADSAAAAENXiUvAKCxxjhO5KnMr1avLaGqZJuahanKsFgOIbpUmgXpOmBQkaDGBRqiVosmDZSQSlhTFSaKLKSYS5THFUWMtiTctzW2MZrMf/3bY3NZ3//t47mlJsa//7uYoVYx3Zhvc8xVEl1Yk3iH///5JEqsqkmo2ZDIWEIPAeDgjFATAoAoDAqBwPgoFwwLExCWOlC5QumkqsmkuwfJhSRDIeGyhGSOajV1aSaqyqyaS6BtpCsqkmoupC6urrSEhgoYGCDhHMuyyxgoYGCBhHQmWWWWyyyobKGBggYR0Q//1VlgOOjmrWWWStZUchLBQQMEHCPZZZSFAQCGCggYQOglUxBTUUzLjEwMFVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVf/7kmTCAvN3Fk1hjzFyAAANIAAAARJ1sH4kpHHIAAA0gAAABFVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVU=";

function timeAgo(dateString) {
    const now = new Date();
    const past = new Date(dateString);
    const diffMilliseconds = now - past;

    const seconds = diffMilliseconds / 1000;
    const minutes = seconds / 60;
    const hours = minutes / 60;
    const days = hours / 24;
    const months = days / 30;
    const years = days / 365;

    if (seconds < 60) {
        return `${Math.round(seconds)}초 전`;
    } else if (minutes < 60) {
        return `${Math.round(minutes)}분 전`;
    } else if (hours < 24) {
        return `${Math.round(hours)}시간 전`;
    } else if (days < 30) {
        return `${Math.round(days)}일 전`;
    } else if (months < 12) {
        return `${Math.round(months)}개월 전`;
    } else {
        return `${Math.round(years)}년 전`;
    }
}

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

function dateToUnix(date)
{
    return Math.floor(date.getTime() / 1000);
}

function unixToFormat(unix_timestamp, format) {
    var date = new Date(unix_timestamp * 1000); // UNIX 타임스탬프를 밀리초로 변환

    var map = {
        '%Y': date.getFullYear(),
        '%m': ('0' + (date.getMonth() + 1)).slice(-2),
        '%d': ('0' + date.getDate()).slice(-2),
        '%H': ('0' + date.getHours()).slice(-2),
        '%M': ('0' + date.getMinutes()).slice(-2),
        '%S': ('0' + date.getSeconds()).slice(-2)
    };

    return format.replace(/%[YmdHMS]/g, function(m) { return map[m]; });
}

function dateToFormat(date, format) {
    var map = {
        '%Y': date.getFullYear(),
        '%m': ('0' + (date.getMonth() + 1)).slice(-2),
        '%d': ('0' + date.getDate()).slice(-2),
        '%H': ('0' + date.getHours()).slice(-2),
        '%M': ('0' + date.getMinutes()).slice(-2),
        '%S': ('0' + date.getSeconds()).slice(-2)
    };

    return format.replace(/%[YmdHMS]/g, function(m) { return map[m]; });
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

    // env 설정
    app.config.globalProperties.$env = {};
    app.config.globalProperties.$env.serverIP = process.env.VUE_APP_SERVER_IP;
    app.config.globalProperties.$env.protocol = process.env.VUE_APP_PROTOCOL;
    app.config.globalProperties.$env.port = process.env.VUE_APP_PORT;

    app.config.globalProperties.utils.chatSound = soundData;

    app.config.globalProperties.utils.timeAgoStr = timeAgo;
    app.config.globalProperties.utils.dateToUnix = dateToUnix;
    app.config.globalProperties.utils.unixToFormat = unixToFormat;
    app.config.globalProperties.utils.dateToFormat = dateToFormat;

    // notify
    app.config.globalProperties.utils.notify = {};
    app.config.globalProperties.utils.notify.success = async (message, title) => {
        const option = {
            message: message,
            type: 'success',
            position: 'bottom-right',
        };
        if(title){
            option.title = title;
        }
        await Eggy(option);
    }
    app.config.globalProperties.utils.notify.error = async (message, title) => {
        const option = {
            message: message,
            type: 'error'
        };
        if(title){
            option.title = title;
        }
        await Eggy(option);
    }

    app.config.globalProperties.utils.notify.info = async (message, title) => {
        const option = {
            message: message,
            type: 'info'
        };
        if(title){
            option.title = title;
        }
        await Eggy(option);
    }

    app.config.globalProperties.utils.notify.warning = async (message, title) => {
        const option = {
            message: message,
            type: 'warning'
        };
        if(title){
            option.title = title;
        }
        await Eggy(option);
    }

    app.config.globalProperties.utils.tippy = tippy;
    app.config.globalProperties.utils.tooltips = tooltips;

    app.provide("utils", app.config.globalProperties.utils);
    app.use(createPinia())
        .use(router)
        .use(ContextMenu)
        .use(ElementPlus, { size: 'small', zIndex: 3000, locale: langKR, })
        .component('font-awesome-icon', FontAwesomeIcon)
        .component('VueDatePicker', VueDatePicker)
        .mount('#app');
}

init();
