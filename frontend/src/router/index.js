import { createRouter, createWebHistory } from "vue-router";
import homeView from "@/components/HomeView.vue";
import userLogin from "@/components/UserLogin.vue";
import userEmailVerify from "@/components/UserEmailVerify.vue";
import {useUserStore} from "@/stores/user";

const routes = [
    {
        path: '/',
        name: 'mainView',
        meta: {
            title: '끌림'
        },
        component: homeView,
        beforeEnter(to, from, next) {
            const userStore = useUserStore();

            if (userStore.isLoggedIn) {
                next();
            } else {
                next({ name: "loginView" });
            }
        },
    },
    {
        path: '/user/login',
        name: 'loginView',
        meta: {
          title: '로그인'
        },
        component: userLogin,
        beforeEnter(to, from, next) {
            const userStore = useUserStore();

            if (userStore.isLoggedIn) {
                next({ name: "HelloWorld" });
            } else {
                next();
            }
        },
    },
    {
        path: '/user/email-verify',
        name: 'emailVerify',
        meta: {
            title: '이메일 검증'
        },
        component: userEmailVerify,
        beforeEnter(to, from, next) {
            const userStore = useUserStore();
            if (userStore.isLoggedIn) {
                next({ name: "HelloWorld" });
            } else {
                next();
            }
        },
    },
]


const router = createRouter({
    history: createWebHistory(),
    routes
})

router.beforeEach((to, from, next) => {
    // 이동할 경로가 메타 타이틀을 가지고 있다면,
    if (to.meta && to.meta.title) {
        document.title = to.meta.title;
    }else{
        document.title = '끌림';
    }

    next();
})

export { router }
