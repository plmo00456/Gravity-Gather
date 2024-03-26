<template>
    <div class="flex flex-col items-center justify-center h-screen bg-login_background bg-cover">
        <div class="bg-white p-10 rounded shadow-md w-1/5 glass">
            <form @submit.prevent="submitForm">
                <div class="flex flex-col">
                    <div class="flex justify-center mb-8">
                        <img class="w-4/6" src="../../assets/image/logo.png" alt="로고">
                    </div>
                    <div class="relative">
                        <input type="email" v-model.trim.lazy="value.email" placeholder="이메일" autofocus
                               @input="updateEmail"
                               class="text-sm pl-10 pr-4 py-2 rounded-md border w-full focus:outline-none"
                               maxlength="40">
                        <span class="absolute left-3 top-1/2 transform -translate-y-1/2">
                            <font-awesome-icon class="fa-sm text-gray-400" icon="envelope"></font-awesome-icon>
                        </span>
                    </div>
                    <div v-if="setting.emailIsShow"
                         class="text-xs text-white flex justify-start flex-col items-start my-2 ml-1">
                        <span>
                            <font-awesome-icon class="text-red-400 text-sm" icon="xmark"/>
                            이메일 형식을 확인해 주세요.</span>
                    </div>
                    <div v-if="setting.emailDuplIsShow"
                         class="text-xs text-white flex justify-start flex-col items-start my-2 ml-1">
                        <span>
                            <font-awesome-icon class="text-red-400 text-sm" icon="xmark"/>
                            {{setting.emailDuplIsShow}}</span>
                    </div>
                    <div class="relative mt-2">
                        <input type="text" v-model.trim.lazy="value.id" placeholder="아이디"
                               @input="updateId"
                               class="text-sm pl-10 pr-4 py-2 rounded-md border w-full focus:outline-none"
                               maxlength="20">
                        <span class="absolute left-3 top-1/2 transform -translate-y-1/2">
                            <font-awesome-icon class="fa-sm text-gray-400" icon="user"></font-awesome-icon>
                        </span>
                    </div>
                    <div v-if="setting.idDuplIsShow"
                         class="text-xs text-white flex justify-start flex-col items-start my-2 ml-1">
                        <span>
                            <font-awesome-icon class="text-red-400 text-sm" icon="xmark"/>
                            {{setting.idDuplIsShow}}</span>
                    </div>
                    <div class="relative mt-2">
                        <input type="password" v-model.trim.lazy="value.password" placeholder="비밀번호"
                               @focus="setting.passwordIsShow = true"
                               @input="updatePassword"
                               class="text-sm pl-10 pr-4 py-2 rounded-md border w-full focus:outline-none"
                               maxlength="20">
                        <span class="absolute left-3 top-1/2 transform -translate-y-1/2">
                            <font-awesome-icon class="fa-sm text-gray-400" icon="lock"></font-awesome-icon>
                        </span>
                    </div>
                    <div v-if="setting.passwordIsShow"
                        class="text-xs text-white flex justify-start flex-col items-start my-2 ml-1">
                        <span>
                            <font-awesome-icon v-if="!setting.passwordOne" class="text-red-400 text-sm" icon="xmark"/>
                            <font-awesome-icon v-if="setting.passwordOne" class="text-green-400 text-sm" icon="check"/>
                            영문/숫자/특수문자 2가지 이상 조합 (8~20자)</span>
                        <span>
                            <font-awesome-icon v-if="!setting.passwordTwo" class="text-red-400 text-sm" icon="xmark"/>
                            <font-awesome-icon v-if="setting.passwordTwo" class="text-green-400 text-sm" icon="check"/>
                            3개 이상 연속되거나 동일한 문자/숫자 제외</span>
                    </div>
                    <div class="relative mt-2">
                        <input type="password" v-model.trim.lazy="value.passwordVali" placeholder="비밀번호 확인"
                               @focus="setting.passwordValiIsShow = true"
                               @input="updatePasswordVali"
                               class="text-sm pl-10 pr-4 py-2 rounded-md border w-full focus:outline-none"
                               maxlength="20">
                        <span class="absolute left-3 top-1/2 transform -translate-y-1/2">
                            <font-awesome-icon class="fa-sm text-gray-400" icon="lock"></font-awesome-icon>
                        </span>
                    </div>
                    <div v-if="setting.passwordValiIsShow"
                         class="text-xs text-white flex justify-start flex-col items-start my-2 ml-1">
                        <span>
                            <font-awesome-icon v-if="!setting.passwordValiOne" class="text-red-400 text-sm" icon="xmark"/>
                            <font-awesome-icon v-if="setting.passwordValiOne" class="text-green-400 text-sm" icon="check"/>
                            확인을 위해 동일한 비밀번호를 입력해주세요.</span>
                    </div>
                    <div class="relative mt-2">
                        <input type="text" v-model.trim.lazy="value.name" placeholder="이름"
                               class="text-sm pl-10 pr-4 py-2 rounded-md border w-full focus:outline-none"
                               maxlength="20">
                        <span class="absolute left-3 top-1/2 transform -translate-y-1/2">
                            <font-awesome-icon class="fa-sm text-gray-400" icon="user-pen"></font-awesome-icon>
                        </span>
                    </div>
                    <div class="relative mt-2">
                        <input type="text" v-model.trim.lazy="value.nickname" placeholder="닉네임"
                               class="text-sm pl-10 pr-4 py-2 rounded-md border w-full focus:outline-none"
                               maxlength="20">
                        <span class="absolute left-3 top-1/2 transform -translate-y-1/2">
                            <font-awesome-icon class="fa-sm text-gray-400" icon="user-pen"></font-awesome-icon>
                        </span>
                    </div>
                </div>
                <div class="relative w-full flex justify-center">
                    <p v-if='showError' class='absolute top-2 text-red-500 text-sm'>아이디와 비밀번호를 입력하세요.</p>
                </div>
                <button type="submit"
                        class="mt-10 bg-blue-500 hover:bg-blue-700 w-full py-2 text-white font-semibold rounded focus:outline-none">
                    가입하기
                </button>
            </form>
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
            setting:{
                emailIsShow: false,
                emailDuplIsShow: false,
                idDuplIsShow: false,
                passwordIsShow: false,
                passwordOne: false,
                passwordTwo: false,
                passwordValiIsShow: false,
                passwordValiOne: false,
            },
            value: {
                email: null,
                id: null,
                password: null,
                passwordVali: null,
                name: null,
                nickname: null,
            }
        }
    },
    watch: {
        'value.email'(newPassword) {
            const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
            this.setting.emailIsShow = !emailRegex.test(newPassword);
            if(emailRegex.test(newPassword)){
                const userStore = useUserStore();
                userStore.checkEmailDuplication({
                    email: newPassword,
                }).then(result => {
                   if(result.custom)
                       this.setting.emailDuplIsShow = result.message;
                   else
                       this.setting.emailDuplIsShow = false;
                });
            }else{
                this.setting.emailDuplIsShow = false;
            }
        },
        'value.id'(newPassword) {
            if(newPassword.length > 3){
                const userStore = useUserStore();
                userStore.checkIdDuplication({
                    id: newPassword,
                }).then(result => {
                    if(result.custom)
                        this.setting.idDuplIsShow = result.message;
                    else
                        this.setting.idDuplIsShow = false;
                });
            }else{
                this.setting.idDuplIsShow = false;
            }
        },
        'value.password'(newPassword) {
            const passwordRegex = /^(?:(?=.*\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&*])|(?=.*\d)(?=.*[!@#$%^&*])(?=.*[a-zA-Z])|(?=.*[a-zA-Z])(?=.*[!@#$%^&*])(?=.*\d)).{8,20}$/;
            const sequenceRegex = /(.)\1\1+|123|234|345|456|567|678|789|012|210|321|432|543|654|765|876|987|098/;
            this.setting.passwordOne = passwordRegex.test(newPassword);
            this.setting.passwordTwo = !sequenceRegex.test(newPassword);
        },
        'value.passwordVali'(newPassword) {
            this.setting.passwordValiOne = this.value.password === newPassword;
        },
        deep: true,
    },
    methods: {
        async submitForm() {
            if(!this.value.email){
                this.utils.msgError("이메일을 입력하세요.");
                return;
            }
            if(this.setting.emailIsShow){
                this.utils.msgError("이메일을 형식을 확인하세요.");
                return;
            }
            if(this.setting.emailDuplIsShow){
                this.utils.msgError(this.setting.emailDuplIsShow);
                return;
            }

            if(!this.value.id){
                this.utils.msgError("아이디를 입력하세요.");
                return;
            }
            if(this.setting.idDuplIsShow){
                this.utils.msgError(this.setting.idDuplIsShow)
                return;
            }
            if(!this.value.password || !this.value.passwordVali){
              this.utils.msgError("비밀번호를 입력하세요.");
              return;
            }

            if(!this.setting.passwordOne || !this.setting.passwordTwo || !this.setting.passwordValiOne){
              this.utils.msgError("비밀번호 형식을 확인하세요.");
              return;
            }
            if(!this.value.name){
              this.utils.msgError("이름을 입력하세요.");
              return;
            }
            if(!this.value.nickname){
                this.utils.msgError("닉네임을 입력하세요.");
                return;
            }

            const userStore = useUserStore();
            try {
                await userStore.register(this.value)
                .then( () => {
                    this.$router.push({
                        path: '/user/email-verify',
                        state: {
                            email : this.value.email,
                        }
                    });
                })
                .catch((error) => {
                    this.utils.msgError((error?.response?.data) || this.utils.normalErrorMsg);
                })
            } catch (error) {
                console.error(error);
                this.utils.msgError((error?.response?.data) || this.utils.normalErrorMsg);
            }
        },
        updatePassword(event){
            this.value.password = event.target.value;
        },
        updatePasswordVali(event){
            this.value.passwordVali = event.target.value;
        },
        updateEmail(event){
            this.value.email = event.target.value;
        },
        updateId(event){
            this.value.id = event.target.value;
        },

    }
}
</script>
