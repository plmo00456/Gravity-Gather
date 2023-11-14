<template>
  <div id="nav-view" class="nav-wrap flex justify-center z-10" v-if="user">
    <div class="flex justify-start w-4/6 items-center">
      <div class="flex w-5/6 items-center">
        <router-link to="/" class="cursor-pointer">
          <img src="@/assets/image/logo.png" class="h-12 mr-16" alt="끌림 로고">
        </router-link>
        <ul class="flex text-gray-100 font-bold">
          <li class="mr-5 hover:text-gray-200">
            <router-link to="/user/login">메뉴1</router-link>
          </li>
          <li class="mr-5 hover:text-gray-200">메뉴2</li>
          <li class="hover:text-gray-200">메뉴3</li>
        </ul>
      </div>
      <div class="flex w-2/6 text-white justify-end items-center cursor-pointer" @click="profileClick" id="profile-wrap">
        <div class="w-10 h-10 rounded-3xl overflow-hidden mr-2 border border-gray-500">
          <img class="w-full h-full object-cover bg-white" :src="user.photo" alt="프로필 사진" v-if="user.photo">
          <div v-if="!user.photo"
               class="w-full h-full flex justify-center items-center font-bold text-xl bg-green-700 text-white">
            <span v-if="user.nickname">{{ user.nickname[0] }}</span>
            <span v-if="!user.nickname">{{ user.name[0] }}</span>
          </div>
        </div>
        <div class="flex">
          {{ user.nickname }}
        </div>
        <context-menu
            v-model:show="isProfileClick"
            :options="profileContextOption"
        >
          <context-menu-item label="내 정보 관리" @click="myInfoShowFn" class="cursor-pointer">
            <template #icon>
              <font-awesome-icon class="fa-md font-bold text-gray-600" icon="fa-user"></font-awesome-icon>
            </template>
          </context-menu-item>
          <context-menu-separator/>
          <context-menu-item label="로그아웃" @click="logout()" class="cursor-pointer">
            <template #icon>
              <font-awesome-icon class="fa-md font-bold text-gray-600"
                                 icon="arrow-right-from-bracket"></font-awesome-icon>
            </template>
          </context-menu-item>
          <!--          <context-menu-group label="Menu with child">-->
          <!--            <context-menu-item label="Item1" @click="onMenuClick(2)">-->
          <!--            </context-menu-item>-->
          <!--            <context-menu-item label="Item2" @click="onMenuClick(3)"/>-->
          <!--            <context-menu-group label="Child with v-for 50">-->
          <!--              <context-menu-item v-for="index of 50" :key="index" :label="'Item3-'+index"-->
          <!--                                 @click="onLoopMenuClick(index)"/>-->
          <!--            </context-menu-group>-->
          <!--          </context-menu-group>-->
        </context-menu>
      </div>
    </div>
    <PopupWindow
        :show="myInfoShow" @close="myInfoShow=false"
        :title="'내 정보 관리'"
        :widthClass="'w-[26rem]'"
        :heightClass="'h-[45rem]'"
        :alignClass="'justify-start'"
    >
      <form @submit.prevent="userUpdate">
        <div class="flex flex-col items-center p-5">
          <div class="flex w-[6rem] items-center h-[6rem] mb-3 justify-center relative cursor-pointer" @click="uploadImage('profile-photo-input')">
            <div class="w-full h-full relative rounded-full overflow-hidden border-2 border-gray-400" @mouseover="profileImageHover = true" @mouseleave="profileImageHover = false">
              <div id="profile-photo-prev" class="w-full h-full bg-cover cursor-pointer flex items-center justify-center font-bold text-3xl bg-green-700 text-white">
                <span v-if="!myInfoValue.photo">{{user.nickname[0]}}</span>
                <img id="profile-photo-prev" v-if="myInfoValue.photo" class="w-full h-full bg-cover cursor-pointer" :src="myInfoValue.photo" alt="프로필사진">
              </div>
              <Transition>
                <div v-if="profileImageHover" class="absolute rounded-b-full bottom-0 right-0 w-full h-2/5 text-white bg-gray-900 bg-opacity-40 flex justify-center items-start py-1">
                  <span class="text-lg text-white">변경</span>
                </div>
              </Transition>
            </div>
            <div class="absolute bottom-0 right-0 bg-gray-200 w-8 h-8 rounded-full flex justify-center items-center border border-gray-400">
              <font-awesome-icon class=" w-full text-gray-400 text-md" icon="fa-regular fa-image"/>
            </div>
            <input id="profile-photo-input" name="profile-image" @click="handleImageUpload" class="hidden" type="file" v-on:change="handleImageUpload" accept="image/*">
          </div>

          <div class="flex w-full items-center h-[3.5rem] mb-1">
            <p class="flex flex-col w-2/5 justify-center items-start">
              <span class="flex w-full items-center font-semibold ml-1">
                이름
                <b class="ml-1 text-red-500">*</b>
              </span>
              <span>( {{ myInfoValue.name.length }} / 10 )</span>
            </p>
            <p class="flex w-3/5 h-full items-center">
              <input type="text" class="w-full border rounded border-gray-300 h-[2.5rem] px-2 py-1" v-model.trim="myInfoValue.name" maxlength="10">
            </p>
          </div>

          <div class="flex w-full items-center h-[3.5rem] mb-3">
            <p class="flex flex-col w-2/5 justify-center items-start">
              <span class="flex w-full items-center font-semibold">
                닉네임
                <b class="ml-1 text-red-500">*</b>
              </span>
              <span>( {{ myInfoValue.nickname.length }} / 10 )</span>
            </p>
            <p class="flex w-3/5 h-full items-center">
              <input type="text" class="w-full border rounded border-gray-300 h-[2.5rem] px-2 py-1" v-model.trim="myInfoValue.nickname" maxlength="10">
            </p>
          </div>

          <div class="flex w-full items-center h-[3.5rem] mb-6">
            <p class="flex flex-col w-2/5 justify-center items-start">
              <span class="flex w-full items-center font-semibold">방 캐릭터</span>
            </p>
            <p class="flex w-3/5 h-full cursor-pointer">
              <Multiselect
                  v-model="roomCharacterValue"
                  label="name"
                  :searchable="false"
                  :close-on-select="true"
                  :allow-empty="false"
                  :multiple="false"
                  :options="roomCharacter"
                  :show-labels="false"
                  class=""
              >
                <template v-slot:singleLabel="{ option }">
                  <div class="w-full flex justify-center">
                    <img class="option__image h-16 self-center" :src="option.img" alt="캐릭터">
                  </div>
                </template>
                <template v-slot:option="{ option }">
                  <div class="w-full flex justify-center">
                    <img class="option__image h-16 self-center" :src="option.img" alt="캐릭터">
                  </div>
                </template>
              </Multiselect>
            </p>
          </div>

          <div class="flex w-full items-center h-[3.5rem] mb-2">
            <p class="flex flex-col w-2/5 justify-center items-start">
              <span class="flex w-full items-center font-semibold">방 테마</span>
            </p>
            <p class="flex w-3/5 h-full cursor-pointer justify-center items-center">
              <Multiselect
                  v-model="roomMapValue"
                  label="name"
                  :searchable="false"
                  :close-on-select="true"
                  :allow-empty="false"
                  :multiple="false"
                  :options="roomMap"
              />
            </p>
          </div>

          <div class="flex w-full items-center h-[3.5rem]">
            <p class="flex flex-col w-2/5 justify-center items-start">
              <span class="flex w-full items-center font-semibold">
                현재 비밀번호
                <b class="ml-1 text-red-500">*</b>
              </span>
            </p>
            <p class="flex w-3/5 h-full items-center">
              <input type="password"
                     placeholder="비밀번호 변경 시 입력"
                     v-model.trim="myInfoValue.password"
                     class="w-full border rounded border-gray-300 h-[2.5rem] px-2 py-1" maxlength="30">
            </p>
          </div>
          <div class="flex w-full items-center h-[3.5rem]">
            <p class="flex flex-col w-2/5 justify-center items-start">
              <span class="flex w-full items-center font-semibold">
                새 비밀번호
                <b class="ml-1 text-red-500">*</b>
              </span>
              <span>( {{ myInfoValue.newPassword ? myInfoValue.newPassword.length : 0 }} / 30 )</span>
            </p>
            <p class="flex w-3/5 h-full items-center">
              <input type="password"
                     v-model.trim="myInfoValue.newPassword"
                     class="w-full border rounded border-gray-300 h-[2.5rem] px-2 py-1" maxlength="30">
            </p>
          </div>
          <div class="flex w-full items-center h-[3.5rem] mb-5">
            <p class="flex flex-col w-2/5 justify-center items-start">
              <span class="flex w-full items-center font-semibold">
                새 비밀번호 확인
              </span>
            </p>
            <p class="flex w-3/5 h-full items-center">
              <input type="password"
                     v-model.trim="myInfoValue.newPasswordVali"
                     class="w-full border rounded border-gray-300 h-[2.5rem] px-2 py-1" maxlength="30">
            </p>
          </div>

          <p class="flex justify-center text-white w-full">
            <button class="px-5 py-2 bg-blue-600 rounded text-sm hover:bg-blue-500 mr-2 w-1/2 h-[3rem]" type="submit">확인</button>
            <button class="px-5 py-2 bg-gray-500 rounded text-sm hover:bg-gray-400 ml-2 w-1/2 h-[3rem]" type="button"
                    @click="myInfoShow = false">취소
            </button>
          </p>

        </div>
      </form>
    </PopupWindow>
  </div>
</template>

<script>
import {useUserStore} from "@/stores/user";
import {ContextMenuItem, ContextMenu, ContextMenuSeparator} from "@imengyu/vue3-context-menu";
import {router} from "@/router";
import PopupWindow from "@/components/PopupWindow.vue";
import Multiselect from 'vue-multiselect'

export default {
  name: "NavView",
  components: {PopupWindow, ContextMenuSeparator, ContextMenuItem, ContextMenu, Multiselect,},
  data() {
    return {
      isProfileClick: false,
      profileContextOption: {
        zIndex: 3,
        minWidth: 230,
      },
      myInfoShow: false,
      profileImageHover: false,
      roomCharacterValue: {roomCharacter: 1, img: `${require('@/assets/image/character/c1.png')}`},
      roomCharacter: [
        {roomCharacter: 1, img: `${require('@/assets/image/character/c1.png')}`},
        {roomCharacter: 2, img: `${require('@/assets/image/character/c2.png')}`},
        {roomCharacter: 3, img: `${require('@/assets/image/character/c3.png')}`},
        {roomCharacter: 4, img: `${require('@/assets/image/character/c4.png')}`},
        {roomCharacter: 5, img: `${require('@/assets/image/character/c5.png')}`},
      ],
      roomMapValue: {name: '1번', roomMap: 1},
      roomMap: [
        {name: '1번', roomMap: 1},
        {name: '2번', roomMap: 2},
        {name: '3번', roomMap: 3},
      ],
      myInfoValue: {
        photo: null,
        name: null,
        nickname: null,
        roomCharacter: 1,
        roomMap: 1,
        password: null,
        newPassword: null,
        newPasswordVali: null,
      },
    }
  },
  setup() {
    const userStore = useUserStore();
    const user = userStore.userInfo;

    if (user == null) {
      router.push({
        name: 'LoginView',
      });
    }

    return {
      userStore, user
    };
  },
  methods: {
    profileClick(e) {
      const rect = e.target.closest("#profile-wrap").getBoundingClientRect();
      this.isProfileClick = !this.isProfileClick;
      this.profileContextOption.x = rect.left;
      this.profileContextOption.y = rect.top + rect.height + 10;
    },
    logout() {
      const userStore = useUserStore();
      userStore.logout();
    },
    myInfoShowFn() {
      this.myInfoValue.photo = this.user.photo;
      this.myInfoValue.name = this.user.name;
      this.myInfoValue.nickname = this.user.nickname;
      this.myInfoValue.roomCharacter = this.user.roomCharacter;
      this.myInfoValue.roomMap = this.user.roomMap;
      this.myInfoValue.password = null;
      this.myInfoValue.newPassword = null;
      this.myInfoValue.newPasswordVali = null;
      this.myInfoValue.nickname = this.user.nickname;
      this.roomCharacterValue = {
        roomCharacter: this.user.roomCharacter,
        img: `${require(`@/assets/image/character/c${this.user.roomCharacter}.png`)}`
      }

      this.roomMapValue = {
        name: this.user.roomMap + '번',
        roomMap: this.user.roomMap
      }

      this.myInfoShow = true;
    },
    uploadImage(id) {
      const fileInput = document.querySelector('#'+id);
      fileInput.click();
    },
    handleImageUpload(event) {
      console.log(event.target);
      const self = this;
      if(event.target.files.length > 0){
        const reader = new FileReader();
        reader.onload = function(e) {
          self.myInfoValue.photo = e.target.result;
        }
        reader.readAsDataURL(event.target.files[0]);
      }else{
        this.myInfoValue.photo = this.user.photo;
      }
    },
    userUpdate() {
      if(this.myInfoValue.name == null){
        this.utils.msgError("이름을 입력하세요");
        return;
      }
      if(this.myInfoValue.nickname == null){
        this.utils.msgError("닉네임을 입력하세요");
        return;
      }

      if(this.myInfoValue.password != null){
        if(!this.myInfoValue.newPassword) {
          this.utils.msgError("새 비밀번호를 입력해 주세요. ");
          return;
        }

        if(this.myInfoValue.newPassword != this.myInfoValue.newPasswordVali){
          this.utils.msgError("새 비밀번호가 틀립니다.");
          return;
        }
      }

      this.$swal.fire({
        title: "저장 하시겠습니까?",
        showCancelButton: true,
        confirmButtonText: "저장",
        cancelButtonText: "취소"
      }).then(async (result) => {
        if (result.isConfirmed) {
          const userStore = useUserStore();
          try {
            const data = {
              seq: this.user.seq,
              name: this.myInfoValue.name,
              nickname: this.myInfoValue.nickname,
              roomCharacter: this.roomCharacterValue.roomCharacterValue,
              roomMap: this.roomMapValue.roomMapValue,
            };

            if(document.querySelector('#profile-photo-input').value != null && document.querySelector('#profile-photo-input').value !== '')
              data['profile-image'] = document.querySelector('#profile-photo-input').value;
            if(this.myInfoValue.password != null){
              data['password'] = this.myInfoValue.password;
              data['newPassword'] = this.myInfoValue.newPassword;
            }

            await userStore.userInfoUpdate(data);
            if (userStore?.dataResponse.status === 200) {
              await userStore.userInfoStateUpdate(this.user.seq);

              this.utils.notify.success("내 정보가 변경되었습니다.", "변경 완료!");
            } else {
              this.utils.msgError(this.dataResponse.data || this.utils.normalErrorMsg);
            }
          } catch (error) {
            this.utils.msgError((error?.response?.data) || this.utils.normalErrorMsg);
          }
        }
      }).catch((error) => {
        this.utils.msgError((error?.response?.data) || this.utils.normalErrorMsg);
      });
    },
  }
}
</script>

<style scoped>
#nav-view {
  background: #132043;
  height: 7%;
}
.v-enter-active,
.v-leave-active {
  transition: opacity 0.3s ease;
}

.v-enter-from,
.v-leave-to {
  opacity: 0;
}
</style>
