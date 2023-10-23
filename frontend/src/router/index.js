import { createRouter, createWebHistory } from "vue-router";
import userLogin from "@/components/UserLogin.vue";
import userEmailVerify from "@/components/UserEmailVerify.vue";
import {useUserStore} from "@/stores/user";

const routes = [
    {
        path: '/',
        name: 'login',
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