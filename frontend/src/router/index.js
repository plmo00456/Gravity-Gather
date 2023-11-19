import {createRouter, createWebHistory} from "vue-router";
import homeView from "@/components/HomeView.vue";
import userLogin from "@/components/UserLogin.vue";
import userEmailVerify from "@/components/UserEmailVerify.vue";
import {useUserStore} from "@/stores/user";
import meetRoomView from "@/components/MeetRoomView.vue";
import {useCommonStore} from "@/stores/common";
import writeView from "@/components/WriteView.vue";
import scheduleView from "@/components/ScheduleView.vue";

const routes = [
    {
        path: '/',
        name: 'MainView',
        meta: {
            title: '끌림'
        },
        component: homeView,
        beforeEnter(to, from, next) {
            const userStore = useUserStore();

            if (userStore.isLoggedIn) {
                next();
            } else {
                next({name: "LoginView"});
            }
        },
    },
    {
        path: '/user/login',
        name: 'LoginView',
        meta: {
            title: '로그인',
            hideMenu: true,
        },
        component: userLogin,
        beforeEnter(to, from, next) {
            const userStore = useUserStore();

            if (userStore.isLoggedIn) {
                next({name: "MainView"});
            } else {
                next();
            }
        },
    },
    {
        path: '/user/email-verify',
        name: 'emailVerify',
        meta: {
            title: '이메일 검증',
            hideMenu: true,
        },
        component: userEmailVerify,
        beforeEnter(to, from, next) {
            const userStore = useUserStore();
            if (userStore.isLoggedIn) {
                next({name: "MainView"});
            } else {
                next();
            }
        },
    },
    {
        path: '/write',
        name: 'writeView',
        meta: {
            title: '글쓰기',
        },
        component: writeView,
    },
    {
        path: '/room/:id',
        name: 'room',
        meta: {
            title: '미팅 방',
            hideMenu: false,
        },
        component: meetRoomView,
    },
    {
        path: '/schedule',
        name: 'scheduleView',
        meta: {
            title: '일정',
        },
        component: scheduleView,
    },

]


const router = createRouter({
    history: createWebHistory(),
    routes
})

router.beforeEach((to, from, next) => {
    // 브라우저 타이틀 지정
    if (to.meta && to.meta.title) {
        document.title = to.meta.title;
    } else {
        document.title = '끌림';
    }

    // 화면 이동 시 알림 가져옴
    const user = useUserStore();
    if (!to.meta.hideMenu && user.userInfo != null) {
        const common = useCommonStore();
        try{
            common.getAlarm(user.userInfo.seq);
            console.log(common.alarmList);
        }catch (e){
            console.error(e);
        }
    }

    next();
})

export {router}
