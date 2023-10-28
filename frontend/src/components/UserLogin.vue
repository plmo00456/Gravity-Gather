<template>
    <div class="flex flex-col items-center justify-center h-screen bg-login_background bg-cover">
        <div class="bg-white p-10 rounded shadow-md w-full max-w-xs">
            <form @submit.prevent="submitForm">
                <div class="flex flex-col space-y-0">
                    <div class="relative">
                        <input type="text" v-model.trim.lazy="id" placeholder="아이디" @focus="handleFocus('id', true)"
                               @blur="handleFocus('id', false)"
                               :class="{ 'border-blue-500': idFocused, 'border-b-0' : !idFocused }"
                               class="text-sm pl-10 pr-4 py-2 rounded-t-md border w-full focus:outline-none"
                               maxlength="20">
                        <span class="flex justify-center absolute left-3 top-1/2 transform -translate-y-1/2">
                          <font-awesome-icon class="fa-sm text-gray-400" icon="user"></font-awesome-icon>
                        </span>
                    </div>
                    <div class="relative">
                        <input type="password" v-model.trim.lazy="password" placeholder="비밀번호"
                               @focus="handleFocus('password', true)"
                               @blur="handleFocus('password', false)"
                               :class="{'border-blue-500': passwordFocused, 'border-t-0' : idFocused }"
                               class="text-sm pl-10 pr-4 py-2 rounded-b-md border w-full focus:outline-none"
                               maxlength="20">
                        <span class="absolute left-3 top-1/2 transform -translate-y-1/2">
                            <font-awesome-icon class="fa-sm text-gray-400" icon="lock"></font-awesome-icon>
                        </span>
                    </div>
                </div>
                <div class="relative w-full flex justify-center">
                    <p v-if='showError' class='absolute top-2 text-red-500 text-sm'>아이디와 비밀번호를 입력하세요.</p>
                </div>
                <button type="submit"
                        class="mt-10 bg-blue-500 hover:bg-blue-700 w-full py-2 text-white font-semibold rounded focus:outline-none">
                    로그인
                </button>
            </form>
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
import {useUserStore} from '@/stores/user';

export default {
    data() {
        return {
            id: '',
            password: '',
            idFocused: false,
            passwordFocused: false,
            showError: false,
            dataResponse: '',
        }
    },
    methods: {
        handleFocus(ele, on) {
            // on : focus 유무
            if (ele === 'id') {
                this.idFocused = on;
                if (on)
                    this.showError = !on;
            } else if (ele === 'password') {
                this.passwordFocused = on;
                if (on)
                    this.showError = !on;
            }
        },
        async submitForm() {
            const userStore = useUserStore();
            if (this.id && this.password) {
                const loginUser = {
                    id: this.id,
                    password: this.password,
                }

                try {
                    await userStore.login(loginUser);
                    this.dataResponse = userStore.dataResponse;
                    console.log(this.dataResponse);
                    if(this?.dataResponse?.status === 200){
                        if(this?.dataResponse?.data?.status === 'UNVERIFIED') {
                            this.$router.push({
                                path: '/user/email-verify',
                                state: {
                                    email : this.dataResponse.data.email,
                                }
                            });
                            return;
                        }

                      this.$router.push({name : 'mainView'});
                    }else{
                        this.msgError(this.dataResponse.data || this.normalErrorMsg);
                    }
                } catch (error) {
                    console.error(error);
                    this.msgError((error?.response?.data) || this.normalErrorMsg);
                }
            } else {
                this.showError = true;
            }
        }

    }
}
</script>
