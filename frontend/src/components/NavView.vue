<template>
    <div id="nav-view" class="nav-wrap flex justify-center z-10" v-if="user">
        <div class="flex justify-start w-4/6 items-center">
            <div class="flex w-5/6 items-center">
                <router-link to="/">
                    <img src="@/assets/image/logo.png" class="h-12 mr-16" alt="끌림 로고">
                </router-link>
                <ul class="flex text-gray-100 font-bold">
                    <li class="mr-5 hover:text-gray-200"><router-link to="/user/login">메뉴1</router-link></li>
                    <li class="mr-5 hover:text-gray-200">메뉴2</li>
                    <li class="hover:text-gray-200">메뉴3</li>
                </ul>
            </div>
            <div class="flex w-1/6 text-white items-center">
                <div class="w-10 h-10 rounded-3xl overflow-hidden mr-2 border border-gray-500">
                    <img class="w-full h-full object-cover bg-white" :src="user.photo" alt="프로필 사진" v-if="user.photo">
                    <div v-if="!user.photo" class="w-full h-full flex justify-center items-center font-bold text-xl bg-green-700 text-white">
                        <span v-if="user.nickname">{{user.nickname[0]}}</span>
                        <span v-if="!user.nickname">{{user.name[0]}}</span>
                    </div>
                </div>
                <div class="flex">
                    {{user.nickname}}
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import {useUserStore} from "@/stores/user";
export default {
    name: "NavView",
    setup() {
        const userStore = useUserStore();
        const user = userStore.userInfo;
        return {
            userStore, user
        };
    }
}
</script>

<style scoped>
    #nav-view{
        background: #132043;
        height: 7%;
    }
</style>