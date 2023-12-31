<template>
  <div id="main-view" class="flex flex-col items-center bg-main_background bg-cover">
    <slideWindow :show="slideShow" @close="slideShow = false" :title="'미팅 생성하기'">
      <div v-if="currentWindow === 'createRoom'">
        <form @submit.prevent="createRoom">
          <p class="flex flex-col text-left mb-5">
            <span>미팅 제목<b class="ml-1 text-red-500">*</b></span>
            <input type="text" v-model.lazy.trim="createRoomTitle" class="border border-gray-300 py-2 px-3"
                   :class="{'border-red-500':isCreateRoomTitle}" @focus="isCreateRoomTitle = false" placeholder="미팅 제목"
                   maxlength="30">
          </p>
          <p class="flex flex-col text-left mb-5">
            <span>미팅 내용<b class="ml-1 text-red-500">*</b></span>
            <input type="text" v-model.lazy.trim="createRoomTopic" class="border border-gray-300 py-2 px-3"
                   :class="{'border-red-500':isCreateRoomTopic}" @focus="isCreateRoomTopic = false" placeholder="미팅 내용"
                   maxlength="30">
          </p>
          <p class="flex flex-col text-left mb-5">
            <span>최대 인원수</span>
            <label>
              <Multiselect
                  v-model="maxParticipantValue"
                  label="name"
                  :searchable="false"
                  :close-on-select="true"
                  :allow-empty="false"
                  :multiple="false"
                  :options="maxParticipant"
                  :select-label="''"
              />
            </label>
          </p>
          <p class="flex flex-col text-left mb-5">
            <span>비밀번호 여부</span>
            <ToggleSwitch v-model="toggleState" :isLocked="true" on-str="잠금" off-str="잠금 해제" size="lg"></ToggleSwitch>
          </p>
          <p class="flex flex-col text-left mb-5">
            <span>비밀번호</span>
            <input type="password" v-model.lazy.trim="createRoomPassword" @input="updateCreateRoomPassword"
                   class="border border-gray-300 py-2 px-3" placeholder="비밀번호">
          </p>
          <p class="flex justify-center text-white mt-10">
            <button class="px-5 py-2 bg-blue-600 rounded text-sm hover:bg-blue-500 mr-2" type="submit">확인</button>
            <button class="px-5 py-2 bg-gray-500 rounded text-sm hover:bg-gray-400 ml-2" type="button"
                    @click="slideShow = false">취소
            </button>
          </p>
        </form>
      </div>
    </slideWindow>
    <div class="flex w-4/6 text-white py-10 justify-between">
      <div>
        <button class="px-3 py-2 bg-blue-600 rounded text-sm hover:bg-blue-500" type="button"
                @click="openSlide('createRoom')">
          <span class="mr-4">미팅 생성하기</span>
          <font-awesome-icon class="fa-md font-bold" icon="fa-plus"></font-awesome-icon>
        </button>
      </div>
      <div class="flex">
        <div class="flex relative">
          <input type="text" placeholder="" v-model="search"
                 class="text-sm pl-10 pr-4 py-2 rounded-l w-full focus:outline-none text-gray-400" maxlength="20">
          <span class="flex justify-center absolute left-3 top-1/2 transform -translate-y-1/2">
            <font-awesome-icon class="fa-sm text-gray-400" icon="magnifying-glass"></font-awesome-icon>
          </span>
        </div>
        <button class="px-3 py-2 bg-blue-600 rounded-r text-sm hover:bg-blue-500" type="button">
          <font-awesome-icon class="fa-md font-bold" icon="magnifying-glass"></font-awesome-icon>
        </button>
      </div>
    </div>
    <div class="rooms-wrap flex flex-wrap w-5/6 mt-10 h-4/6 overflow-auto pl-10 py-5 items-center"
         :class="{'justify-center': filteredRoom.length === 0, 'content-center': filteredRoom.length === 0, 'content-start': filteredRoom.length !== 0}"
         v-if="rooms">
      <div v-if="filteredRoom.length === 0"
           class="non-room flex flex-col w-1/2 h-5/6 rounded select-none cursor-default">
        <div
            class="header flex justify-between w-full px-5 bg-gray-700 rounded-t-md text-center content-center items-center">
          <ul class="flex h-full items-center">
            <li class="w-3 h-3 rounded-3xl bg-red-400 mr-2"></li>
            <li class="w-3 h-3 rounded-3xl bg-yellow-300 mr-2"></li>
            <li class="w-3 h-3 rounded-3xl bg-green-500"></li>
          </ul>
          <div class="flex w-1/2 border border-gray-400 items-center h-3/5 rounded">
            <ul class="flex justify-between w-full items-center">
              <li></li>
              <li class="text-white text-xs pl-5">404 No Results Found!</li>
              <li>
                <font-awesome-icon class="fa-sm text-gray-400 mt-1.5 mr-1" icon="arrow-rotate-right"/>
              </li>
            </ul>
          </div>
          <div class="flex text-center h-full items-center">
            <font-awesome-icon class="fa-sm text-gray-400 mr-3" icon="up-right-from-square"/>
            <font-awesome-icon class="fa-sm text-gray-400 mr-3" icon="plus"/>
            <font-awesome-icon class="fa-sm text-gray-400" icon="fa-regular fa-copy"/>
          </div>
        </div>
        <div class="content flex flex-col justify-center items-center w-full bg-gray-100 rounded-b-md">
          <img class="h-5/6" src="@/assets/image/not-found-data.png" alt="not content">
        </div>
      </div>
      <div v-for="(room, index) in filteredRoom" :key="index"
           class="flex room min-h-2/5 mb-10 rounded mx-5 flex-col px-3 py-4 relative" :data-lock="room.isLocked">
        <div
            class="w-10 h-10 rounded-3xl overflow-hidden absolute -left-5 -top-5 tooltip bg-white border border-gray-500"
            :data-tooltip="room.user.nickname">
          <img class="w-full h-full object-cover" :src="`${$env.protocol}${$env.serverIP}:${$env.port}${room.user.photo}`" alt="프로필 사진" v-if="room.user.photo">
          <div v-if="!room.user.photo"
               class="w-full h-full flex justify-center items-center font-bold text-xl bg-green-700 text-white">
            <span v-if="room.user.nickname">{{ room.user.nickname[0] }}</span>
            <span v-if="!room.user.nickname">{{ room.user.name[0] }}</span>
          </div>
        </div>
        <div class="flex justify-center items-center font-bold">
          <font-awesome-icon v-if="room.isLocked" class="fa-md font-bold text-orange-400"
                             icon="fa-lock"></font-awesome-icon>
          <p class="title text-white truncate max-w-sm mx-1 tooltip" data-tooltip="">
            <span class="w-full" v-html="highlightKeyword(room.title)"></span>
          </p>
          <span class="text-sm"
                :class="{'text-red-300':room.isFull, 'text-blue-300':!room.isFull}">( {{ room.currentParticipant }} / {{
              room.maxParticipant
            }} )</span>
        </div>
        <div>
          <p class="truncate tooltip">
            <span class="text-gray-400 text-sm" title=room.topic>{{ room.topic }}</span>
          </p>
        </div>
        <div class="mt-2">
          <button v-if="!room.isLocked"
                  class="font-bold text-white px-2.5 py-1.5 bg-gray-600 rounded text-sm hover:bg-gray-500"
                  type="button" @click="enterRoom(room)">
            <span class="mx-1">입장 하기</span>
          </button>
          <button v-if="room.isLocked"
                  class="font-bold text-white px-2.5 py-1.5 bg-gray-600 rounded text-sm hover:bg-gray-500"
                  type="button" @click="showPasswordRoom(room)">
            <span class="mx-1">입장 하기</span>
          </button>
        </div>
      </div>

    </div>
    <div v-if="currentPasswordRoom.show" @click.self="noEnterPasswordRoom"
         class="w-full h-full absolute left-0 top-0 bg-black bg-opacity-40 cursor-pointer">
      <div
          class="cursor-default absolute items-center justify-center p-5 flex-col rounded-2xl shadow-hard w-1/3 h-1/2 bg-white self-center left-[50%] top-[50%] flex translate-x-[-50%] translate-y-[-50%]">
        <div class="flex justify-center">
          <span v-if="!currentPasswordRoom.fail"
                class="w-fit h-fit rounded-full bg-blue-500 bg-opacity-40 text-blue-600 px-5 py-4 animate-bounce">
            <font-awesome-icon class="text-4xl" icon="lock"></font-awesome-icon>
          </span>
          <span v-if="currentPasswordRoom.fail"
                  class="w-fit h-fit rounded-full bg-red-500 bg-opacity-40 text-red-600 px-5 py-4 animate-shake">
            <font-awesome-icon class="text-4xl" icon="lock"></font-awesome-icon>
          </span>
        </div>
        <div class="mt-5">
          <span class="text-2xl ">{{ currentPasswordRoom.title }}</span>
        </div>
        <form @submit.prevent="enterPasswordRoom" class="w-full">
          <div class="mt-8 w-full">
            <input class="text-xl border-b-2 border-b-gray-300 w-4/5 focus:outline-none focus:border-b-blue-700"
                   :class="{'border-b-red-500' : currentPasswordRoom.fail, 'focus:border-b-red-500' : currentPasswordRoom.fail}"
                   type="password"
                   v-model.trim="currentPasswordRoom.password"
                   autofocus>
          </div>
          <div class="mt-10">
            <button class="text-white px-5 py-2 bg-blue-600 rounded text-xl font-semibold hover:bg-blue-500"
                    type="submit">입장하기
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import {useUserStore} from "@/stores/user";
import {useRoomStore} from "@/stores/room";
import {onBeforeUnmount, getCurrentInstance, ref, watch, computed, onMounted, watchEffect, nextTick} from "vue";
import slideWindow from "@/components/common/SlideWindow.vue";
import Multiselect from 'vue-multiselect'
import ToggleSwitch from "@/components/common/ToggleSwitch.vue";
import {FontAwesomeIcon} from "@fortawesome/vue-fontawesome";
import {router} from "@/router";

export default {
  name: 'MainView',
  components: {
    FontAwesomeIcon,
    ToggleSwitch,
    slideWindow,
    Multiselect,
  },
  data() {
    return {
      slideShow: false,
      currentWindow: null,
      maxParticipantValue: {name: '2명', maxParticipant: 2},
      maxParticipant: [
        {name: '2명', maxParticipant: 2},
        {name: '3명', maxParticipant: 3},
        {name: '4명', maxParticipant: 4},
        {name: '5명', maxParticipant: 5},
        {name: '6명', maxParticipant: 6},
        {name: '7명', maxParticipant: 7},
        {name: '8명', maxParticipant: 8},
        {name: '9명', maxParticipant: 9},
        {name: '10명', maxParticipant: 10}
      ],
      createRoomTitle: null,
      createRoomTopic: null,
      createRoomIsPassword: false,
      isCreateRoomTitle: false,
      isCreateRoomTopic: false,
      currentPasswordRoom: {
        show: false,
        roomId: null,
        title: null,
        fail: false,
        password: null,
      }
    };
  },
  setup() {
    const userStore = useUserStore();
    const user = userStore.userInfo;
    const socket = new WebSocket('ws://' + import.meta.env.VITE_SERVER_IP + ':8080/gg');
    const instance = getCurrentInstance();
    const roomStore = useRoomStore();
    let rooms = ref([]);
    let search = ref('');
    let createRoomPassword = ref('');

    socket.onopen = async () => {
      const tooltips = instance.appContext.config.globalProperties.utils.tooltips;
      tooltips('tooltip');

      try {
        await roomStore.getRoom();
        if (roomStore?.dataResponse.status === 200) {
          rooms.value = roomStore.rooms;
        } else {
          instance.appContext.config.globalProperties.utils.msgError(this.dataResponse.data || instance.appContext.config.globalProperties.utils.normalErrorMsg);
        }
      } catch (error) {
        console.error(error);
        instance.appContext.config.globalProperties.utils.msgError((error?.response?.data) || instance.appContext.config.globalProperties.utils.normalErrorMsg);
      }
    };

    socket.onmessage = (event) => {
      const data = JSON.parse(event.data);
      if (data.type1 === 'room') {
        if (data.type2 === 'delete') {
          this.chatRooms = this.chatRooms.filter(room => room.id !== data.chatRoomId);
        } else if (data.type2 === 'createMsg') {
          rooms.value.unshift(data.roomInfo);
        } else if (data.type2 === 'updateMsg') {
          const index = rooms.value.findIndex(room => room.seq === data.roomInfo.seq);
          if (index !== -1) {
            rooms.value.splice(index, 1, data.roomInfo);
          }
        } else if (data.type2 === 'invite') {

            if(data.receiveSeq == user.seq){
                instance.appContext.config.globalProperties.$swal.fire({
                    title: "미팅룸 초대",
                    text: data.content,
                    timer: data.timer,
                    timerProgressBar: true,
                    showCancelButton: true,
                    confirmButtonText: "수락",
                    cancelButtonText: "거절",
                    customClass: {
                        timerProgressBar: 'bg-blue-500'
                    },
                }).then((result) => {
                    if (result.isConfirmed) {
                        enterRoom({
                            seq: data.roomId,
                            userSeq: user.seq,
                        });
                    }
                });
            }
        }
      }
    };

    socket.onerror = (error) => {
      console.error(`WebSocket Error : ${error}`);
      instance.appContext.config.globalProperties.utils.msgError("오류가 발생했습니다. 관리자에게 문의해주세요.");
    };

    socket.onclose = () => {
      // console.log('웹소켓 연결 종료');
    };

    const toggleState = ref(false);

    const filteredRoom = computed(() => {
      return rooms.value.filter(r => {
        return r.title.toLowerCase().includes(search.value) || r.topic.toLowerCase().includes(search.value);
      });
    });

    const updateCreateRoomPassword = (event) => {
      createRoomPassword.value = event.target.value;
    };

    watchEffect(() => {
      if (rooms.value.length > 0) {
        nextTick(() => {
          const tooltips = instance.appContext.config.globalProperties.utils.tooltips;
          tooltips('tooltip');
        });
      }
      if (createRoomPassword.value.length > 0) {
        toggleState.value = true;
      } else {
        toggleState.value = false;
      }
    });

    watch(filteredRoom, () => {
      if (rooms.value.length > 0) {
        nextTick(() => {
          const tooltips = instance.appContext.config.globalProperties.utils.tooltips;
          tooltips('tooltip');
        });
      }
    });

    onBeforeUnmount(() => {
      if (socket) {
        socket.close();
      }
    });

    onMounted(() => {
      instance.appContext.config.globalProperties.utils.tooltips('tooltip');
    });

      const enterRoom = async (roomInfo) => {
          const roomStore = useRoomStore();
          try {
              const newRoomInfo = Object.assign({}, roomInfo);
              newRoomInfo.user_seq = user.seq;

              await roomStore.enterRoom(newRoomInfo);

              if (roomStore.dataResponse?.status !== 200) {
                  instance.appContext.config.globalProperties.utils.msgError(roomStore.dataResponse?.data.custom ? roomStore.dataResponse?.data.message : instance.appContext.config.globalProperties.utils.normalErrorMsg);
              } else {
                  const roomInfo = roomStore.dataResponse.data;
                  await router.push({
                      name: 'room',
                      state: {roomInfo: JSON.stringify(roomInfo)},
                      params: {id: roomInfo.seq}
                  });
              }
          } catch (error) {
              console.error(error);
              instance.appContext.config.globalProperties.utils.msgError((error?.response?.data) || instance.appContext.config.globalProperties.utils.normalErrorMsg);
          }
      }

    return {
      userStore, user, socket, rooms, search, filteredRoom, toggleState, createRoomPassword, updateCreateRoomPassword, enterRoom
    };
  },
  methods: {
    highlightKeyword(text) {
      if (this.search) {
        const re = new RegExp(this.search, 'gi')
        return text.replace(re, (matchedText) => `<mark>${matchedText}</mark>`)
      }
      return text
    },
    openSlide(window) {
      this.slideShow = true;
      this.currentWindow = window;
    },
    async createRoom() {
      const roomStore = useRoomStore();
      if (!this.createRoomTitle) this.isCreateRoomTitle = true;
      if (!this.createRoomTopic) this.isCreateRoomTopic = true;
      if (this.createRoomTitle && this.createRoomTopic) {
        const roomInfo = {
          title: this.createRoomTitle,
          topic: this.createRoomTopic,
          maxParticipant: this.maxParticipantValue.maxParticipant,
          isLocked: this.toggleState,
          password: this.createRoomPassword,
        }

        try {
          if (roomInfo.password.length === 0) {
            roomInfo.isLocked = false;
            roomInfo.password = null;
            this.toggleState = false;
          }
          roomStore.createRoom(roomInfo)
          .then(result => {
            this.utils.notify.success("미팅 룸이 생성되었습니다.", "생성 완료!");
            this.$router.push({
              name: 'room',
              state: {roomInfo: JSON.stringify(result)},
              params: {id: result.seq}
            });
          }).catch(error => {
            console.log(error);
            this.utils.msgError(error.custom ? this.dataResponse.data.message : this.utils.normalErrorMsg);
          })
          this.slideShow = false;
        } catch (error) {
          console.error(error);
          this.utils.msgError((error?.response?.data) || this.utils.normalErrorMsg);
        }
      } else {
        this.showError = true;
      }
    },
    async enterPasswordRoom() {
      const roomInfo = {
        seq: this.currentPasswordRoom.roomId,
        password: this.currentPasswordRoom.password,
      }
      const roomStore = useRoomStore();
      try {
        await roomStore.enterRoom(roomInfo);
        this.dataResponse = roomStore.dataResponse;

        if (this?.dataResponse?.status !== 200) {
            // 비밀번호 오류
            if (this?.dataResponse?.status === 401) {
                this.currentPasswordRoom.fail = false;
                nextTick(() => {
                    this.currentPasswordRoom.fail = true;
                })
            }else{
                this.utils.msgError(this.dataResponse?.custom ? this.dataResponse.data.message : this.utils.normalErrorMsg);
            }

        } else {
          const roomInfo = this.dataResponse.data;
          this.$router.push({
            name: 'room',
            state: {roomInfo: JSON.stringify(roomInfo)},
            params: {id: roomInfo.seq}
          });
        }
      } catch (error) {
        console.error(error);
        this.utils.msgError((error?.response?.data) || this.utils.normalErrorMsg);
      }
    },
    showPasswordRoom(roomInfo) {
      this.currentPasswordRoom.roomId = roomInfo.seq;
      this.currentPasswordRoom.title = roomInfo.title;
      this.currentPasswordRoom.show = true;
    },
    noEnterPasswordRoom(event) {
      if (event.target.tagName !== 'DIV') {
        return;
      }
      this.currentPasswordRoom = {
        show: false,
        roomId: null,
        title: null,
        fail: false,
        password: null,
      }
    },
  },
}
</script>

<style scoped>
#main-view {
  height: 93%;
}

.non-room {
  box-shadow: rgba(0, 0, 0, 0.25) 0px 54px 55px, rgba(0, 0, 0, 0.12) 0px -12px 30px, rgba(0, 0, 0, 0.12) 0px 4px 6px, rgba(0, 0, 0, 0.17) 0px 12px 13px, rgba(0, 0, 0, 0.09) 0px -3px 5px;
}

.non-room .header {
  height: 40px;
}

.non-room .content {
  height: calc(100% - 40px);
}

.room {
  width: 15rem;
  background: rgb(255 255 255 / 25%);
  box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.37);
  backdrop-filter: blur(9.5px);
  -webkit-backdrop-filter: blur(9.5px);
  border-radius: 10px;
  border: 1px solid rgba(255, 255, 255, 0.18);
  box-shadow: rgba(0, 0, 0, 0.25) 0px 54px 55px, rgba(0, 0, 0, 0.12) 0px -12px 30px, rgba(0, 0, 0, 0.12) 0px 4px 6px, rgba(0, 0, 0, 0.17) 0px 12px 13px, rgba(0, 0, 0, 0.09) 0px -3px 5px;
}


/* 스크롤바의 폭 너비 */
.rooms-wrap::-webkit-scrollbar {
  width: 10px;
}

.rooms-wrap::-webkit-scrollbar-thumb {
  background: #f1b4bb; /* 스크롤바 색상 */
  border-radius: 10px; /* 스크롤바 둥근 테두리 */
}

.rooms-wrap::-webkit-scrollbar-track {
  background: rgba(220, 20, 60, .1); /*스크롤바 뒷 배경 색상*/
}

.room .title {
  max-width: 9rem;
}
</style>
