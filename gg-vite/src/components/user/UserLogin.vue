<template>
    <div class="flex items-center justify-between h-screen bg-login_background bg-cover">
      <div class="flex flex-col items-center justify-center w-1/2 h-full">
        <div class="bg-white p-10 rounded-[8px] shadow-md w-[40%] text-start">
          <p class="text-2xl font-bold mb-5 pb-2 border-b">테스트 계정</p>
          <p class="flex flex-col mb-4 border-amber-300 border-l-4 pl-3 -ml-4 ">
            <span class="font-bold">1. 테스터1</span>
            <span>아이디 : test</span>
            <span>비밀번호 : test12#$</span>
          </p>
          <p class="flex flex-col mb-4 border-amber-300 border-l-4 pl-3 -ml-4 ">
            <span class="font-bold">2. 테스터2</span>
            <span>아이디 : test2</span>
            <span>비밀번호 : test12#$</span>
          </p>
          <p class="flex flex-col mb-5 border-amber-300 border-l-4 pl-3 -ml-4 ">
            <span class="font-bold">3. 테스터3</span>
            <span>아이디 : test3</span>
            <span>비밀번호 : test12#$</span>
          </p>
          <span class="text-sm text-gray-500">
            <font-awesome-icon class="text-sm" icon="flag-checkered"></font-awesome-icon> 회원가입도 가능합니다!</span>
        </div>
      </div>
      <div class="flex flex-col items-center justify-center w-1/2 h-full mr-10">
        <div class="relative bg-white p-10 rounded-[8px] shadow-md w-[55%]">
          <div class="absolute top-0 left-8 w-[80px] h-[3px] bg-red-400"></div>
          <form @submit.prevent="submitForm">
            <div class="flex flex-col space-y-0 mb-52">
              <div class="flex mt-6 mb-8 w-[40%] h-[2.5rem]">
                <img class="" src="../../assets/image/logo.png" alt="로고">
              </div>
              <span class="flex text-xl pb-8 font-bold">로그인</span>
              <div class="relative">
                <input type="text" autofocus v-model.trim.lazy="id" placeholder="아이디" @focus="handleFocus('id', true)"
                       @blur="handleFocus('id', false)"
                       :class="{ 'border-blue-500': idFocused, 'border-b-2': idFocused, 'border-gray-300' : !idFocused }"
                       class="pl-2 pr-4 py-2 mb-5 border-b w-full focus:outline-none"
                       maxlength="20">
              </div>
              <div class="relative">
                <input type="password" v-model.trim.lazy="password" placeholder="비밀번호"
                       @focus="handleFocus('password', true)"
                       @blur="handleFocus('password', false)"
                       :class="{'border-blue-500': passwordFocused, 'border-b-2': passwordFocused, 'border-gray-300' : idFocused }"
                       class="pl-2 pr-4 py-2 border-b w-full focus:outline-none"
                       maxlength="20">
              </div>
            </div>
            <div class="absolute w-full flex justify-center bottom-10 left-0">
              <p v-if='showError' class='absolute top-2 text-red-500 text-sm'>아이디와 비밀번호를 입력하세요.</p>
            </div>
            <button type="submit"
                    class="absolute -right-8 bottom-28 mt-10 bg-blue-500 hover:bg-blue-700 w-[80px] h-[80px] py-2 text-white font-semibold rounded-full focus:outline-none">
              <font-awesome-icon class="fa-xl text-white" icon="arrow-right"></font-awesome-icon>
            </button>
          </form>
          <div class="mt-3 w-full text-blue-500 text-sm font-semibold">
            <span class="hover:text-blue-300 cursor-pointer">비밀번호 찾기</span>
            <span class="px-3">|</span>
            <span class="hover:text-blue-300 cursor-pointer">아이디 찾기</span>
            <span class="px-3">|</span>
            <span class="hover:text-blue-300 cursor-pointer">
              <router-link to="/user/register">회원가입</router-link>
          </span>
          </div>
        </div>
      </div>
    </div>
</template>

<script>
import {useUserStore} from '@/stores/user.js';
import {FontAwesomeIcon} from "@fortawesome/vue-fontawesome";

export default {
  components: {FontAwesomeIcon},
    data() {
        return {
            id: null,
            password: null,
            idFocused: false,
            passwordFocused: false,
            showError: false,
            dataResponse: '',
        }
    },
  setup(){
      const userStore = useUserStore();
      const user = userStore;
    return{
      user,
      userStore
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
                console.log(import.meta.env);
                try {
                    await userStore.login(loginUser);
                    this.dataResponse = userStore.dataResponse;
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
                        this.$router.push({name : 'MainView'});
                    }else{
                        this.utils.msgError(this.dataResponse?.data?.custom ? this.dataResponse.data.message : this.utils.normalErrorMsg);
                    }
                } catch (error) {
                    console.error(error);
                    this.utils.msgError((error?.response?.data) || this.utils.normalErrorMsg);
                }
            } else {
                this.showError = true;
            }
        }

    }
}
</script>
