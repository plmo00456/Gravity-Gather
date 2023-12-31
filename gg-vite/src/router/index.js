import {createRouter, createWebHistory} from "vue-router";
import homeView from "@/components/HomeView.vue";
import userLogin from "@/components/user/UserLogin.vue";
import userEmailVerify from "@/components/user/UserEmailVerify.vue";
import {useUserStore} from "@/stores/user";
import meetRoomView from "@/components/article/MeetRoomView.vue";
import {useCommonStore} from "@/stores/common";
import writeView from "@/components/article/WriteView.vue";
import scheduleView from "@/components/task/ScheduleView.vue";
import communityView from "@/components/article/CommunityView.vue";
import writeViewer from "@/components/article/WriteViewer.vue";
import userRegister from "@/components/user/UserRegister.vue";
import scrapedView from "@/components/common/ScrapedView.vue";
import {useRoomStore} from "@/stores/room";

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
            if (userStore.isLoggedIn && userStore.token.access && userStore.token.refresh) {
                const roomStore = useRoomStore();
                roomStore.isInTheRoom()
                .then(result => {
                    if(result){
                        roomStore.getSingleRoom(result.roomSeq)
                        .then(roomInfo => {
                            next({
                                path: `/room/${result.roomSeq}`,
                                state: {roomInfo: JSON.stringify(roomInfo)},
                                params: {id: roomInfo.roomSeq}
                            });
                        })
                    }else{
                        next();
                    }
                })

            } else {
                userStore.token = {};
                userStore.userInfo = null
                userStore.isLoggedIn = false;
                next({name: "LoginView"});
            }
        },
    },
    {
        path: '/user/login',
        name: 'LoginView',
        meta: {
            title: '끌림 - 로그인',
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
        path: '/user/register',
        name: 'RegisterView',
        meta: {
            title: '끌림 - 회원가입',
            hideMenu: true,
        },
        component: userRegister,
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
            title: '끌림 - 이메일 검증',
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
            title: '끌림 - 글쓰기',
        },
        component: writeView,
    },
    {
        path: '/room/:id',
        name: 'room',
        meta: {
            title: '끌림 - 미팅룸',
            hideMenu: false,
        },
        component: meetRoomView,
    },
    {
        path: '/schedule',
        name: 'scheduleView',
        meta: {
            title: '끌림 - 일정',
        },
        component: scheduleView,
    },
    {
        path: '/community',
        name: 'communityView',
        meta: {
            title: '끌림 - 커뮤니티',
        },
        component: communityView,
    },
    {
        path: '/scraped',
        name: 'scrapedView',
        meta: {
            title: '끌림 - 스크랩',
        },
        component: scrapedView,
    },
    {
        path: '/community/:seq',
        name: 'writeViewer',
        meta: {
            title: '끌림 - 게시글',
        },
        component: writeViewer,
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
        }catch (e){
            console.error(e);
        }
    }

    next();
})

export {router}
