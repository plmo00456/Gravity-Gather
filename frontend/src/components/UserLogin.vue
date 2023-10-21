<template>
    <div class="flex flex-col items-center justify-center h-screen bg-login_background bg-cover">
        <div class="bg-white p-10 rounded shadow-md w-full max-w-xs">
            <form @submit.prevent="submitForm">
                <div class="flex flex-col space-y-0">
                    <div class="relative">
                        <input type="text" v-model.trim.lazy="id" placeholder="아이디" class="text-sm pl-10 pr-4 py-2 rounded-t-md border border-b-0 w-full focus:outline-none">
                        <span class="absolute left-3 top-1/2 transform -translate-y-1/2">
                          <i class="fas fa-user"></i>
                        </span>
                    </div>
                    <div class="relative">
                        <input type="password" v-model.trim.lazy="password" placeholder="비밀번호" class="text-sm pl-10 pr-4 py-2 rounded-b-md border w-full focus:outline-none">
                        <span class="absolute left-3 top-1/2 transform -translate-y-1/2">
                            <i class="fas fa-lock"></i>
                        </span>
                    </div>
                </div>
                <button type="submit" class=" bg-blue-500 hover:bg-blue-700 w-full py-2 text-white font-semibold rounded mt-5">로그인</button>
            </form >
        </div>
        <div class="mt-3 w-full text-gray-400 text-xs">
            <span class="hover:text-gray-300">비밀번호 찾기</span>
            <span class="px-3">|</span>
            <span class="hover:text-gray-300">아이디 찾기</span>
            <span class="px-3">|</span>
            <span class="hover:text-gray-300">회원가입</span>
        </div>
    </div>
</template>

<script>
import { useUserStore } from '@/stores/user';

export default {

    data() {
        return {
            id: '',
            password: ''
        }
    },
    methods: {
        submitForm() {
            const userStore = useUserStore();
            if(this.id && this.password){
                const loginUser = {   // 로그인 데이터
                    id: this.id,
                    password: this.password
                }
                userStore.login(loginUser);
                // router.push({name: "room"})
                console.log(`Id: ${this.id}, Password: ${this.password}`);
            }
        }
    }
}
</script>