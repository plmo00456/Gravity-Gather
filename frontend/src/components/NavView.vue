<template>
    <div id="nav-view" class="nav-wrap flex justify-center z-10">
        <div class="flex justify-start w-4/6 items-center">
            <div class="flex w-5/6 items-center">
                <router-link to="/">
                    <img src="@/assets/image/logo.png" class="h-12 mr-16" alt="끌림 로고">
                </router-link>
                <ul class="flex text-gray-200">
                    <li class="mr-5 hover:text-white"><router-link to="/user/login">Login</router-link></li>
                    <li class="mr-5 hover:text-white">메뉴2</li>
                    <li class="hover:text-white">메뉴3</li>
                </ul>
            </div>
            <div class="w-1/6 text-white">
                {{user.email}}
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