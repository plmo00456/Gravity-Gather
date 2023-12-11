<template>
  <div class="flex flex-col items-center justify-center h-screen bg-login_background bg-cover">
    <div class="bg-white p-10 rounded shadow-md w-full max-w-md relative">
      <form @submit.prevent="submitForm">
        <div class="relative w-full flex flex-col mb-3">
          <p class="text-sm w-fit">
            {{ maskEmail }} 주소로 보내드린 인증번호를 입력하세요.
          </p>
          <p class="text-sm w-fit text-red-400">
            인증키 유효시간 <span class="font-semibold">{{
              timer.minutes
            }} : {{ timer.seconds < 10 ? '0' + timer.seconds : timer.seconds }}</span> 남았습니다.
          </p>
        </div>
        <div class="flex flex-col space-y-0">
          <div class="relative flex">
            <input type="text" v-model.trim.lazy="code" placeholder="인증번호" @focus="handleFocus('verify', true)"
                   @blur="handleFocus('verify', false)"
                   :class="{ 'border-blue-500': idFocused}"
                   class="text-xs pl-8 pr-4 py-2 border-b-2 w-full focus:outline-none text-gray-400"
                   maxlength="6">
            <span class="flex justify-center absolute left-2 top-1/2 transform -translate-y-1/2">
                          <font-awesome-icon class="fa-sm text-gray-400"
                                             icon="fa-envelope-circle-check"></font-awesome-icon>
                        </span>
            <button type="button" @click="resendMail"
                class="text-xs border-blue-500 text-blue-500 w-full border rounded ml-5 hover:border-blue-600 hover:text-blue-600">
              <font-awesome-icon v-if='sendMailLoading' class="fa-md text-blue-500 fa-spin"
                                 icon="fa-spinner"></font-awesome-icon>
              인증키재발급
            </button>
          </div>
        </div>
        <div class="relative w-full flex justify-center">
          <p v-if='showError' class='absolute top-2 text-red-500 text-sm'>잘못된 인증번호 입니다.</p>
        </div>
        <div class="flex justify-around">
          <button type="submit"
                  class="text-sm mt-10 bg-blue-500 hover:bg-blue-700 w-1/2 mr-1 py-2 text-white font-semibold rounded focus:outline-none">
            확인
          </button>
          <button type="submit" @click="this.$router.go(-1);"
                  class="text-sm mt-10 bg-gray-400 hover:bg-gray-600 w-3/6 ml-1 py-2 text-white font-semibold rounded focus:outline-none">
            취소
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import { useUserStore} from '@/stores/user';

import {FontAwesomeIcon} from "@fortawesome/vue-fontawesome";
import {reactive, onMounted, onBeforeUnmount} from 'vue';
import {maskEmailFn} from "@/utils/utils";

export default {
  components: {FontAwesomeIcon},
  data() {
    return {
      password: '',
      idFocused: false,
      passwordFocused: false,
      showError: false,
      isVerify: false,
      sendMailLoading: false,
      code: null,
    }
  },
  setup() {
    const userStore = useUserStore();

    const email = history.state.email;
    userStore.sendMail({email : email});
    const maskEmail = maskEmailFn(email);
    const timer = reactive({
      minutes: 5,
      seconds: 0,
    });

    let intervalId;

    const timeover = () => {
      this.isVerify = false;
    };

    const startTimer = () => {
      intervalId = setInterval(() => {
        if (timer.seconds === 0) {
          if (timer.minutes === 0) {
            clearInterval(intervalId);
            timeover();
          } else {
            timer.minutes--;
            timer.seconds = 59;
          }
        } else {
          timer.seconds--;
        }
      }, 1000);
    };
    const stopTimer = () => {
      clearInterval(intervalId);
    };

    onMounted(() => {
      startTimer();
    });

    onBeforeUnmount(() => {
      stopTimer();
    });
    return {timer, email, maskEmail, startTimer, stopTimer};
  },
  methods: {
    async resendMail() {
      this.sendMailLoading = true;
      this.stopTimer();

      this.timer.minutes = 5;
      this.timer.seconds = 0;

      const userStore = useUserStore();
      await userStore.sendMail({email:this.email});

      this.startTimer();
      this.sendMailLoading = false;
    },

    handleFocus(ele, on) {
      // on : focus 유무
      if (ele === 'verify' && on) {
        this.showError = false;
      }
    },
    async submitForm() {
      const userStore = useUserStore();

      if (this.code) {
        try {
          await userStore.emailVerify({email:this.email, code: this.code});
          this.dataResponse = userStore.dataResponse;
          console.log(this.dataResponse);
          if(this?.dataResponse?.status === 200){
            console.log(this?.dataResponse?.data?.result);
            if(!this?.dataResponse?.data?.result){
              this.showError = true;
              return;
            }
            this.utils.notify.success("계정이 생성 되었습니다.", "생성 완료!");
            this.$router.push({
                name: 'LoginView'
            });
          }else{
            this.utils.msgError(this.utils.normalErrorMsg);
          }
        } catch (error) {
          console.error(error);
          this.utils.msgError((error?.response?.data) || this.normalErrorMsg);
        }
      } else {
        this.showError = true;
      }
    }
  }
}
</script>
