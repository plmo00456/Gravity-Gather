<template>
  <div id="main-room-view" class="flex justify-center bg-right-top bg-[length:110%_120%] text-white"
       v-bind:style="{ backgroundImage: `url(${require('@/assets/background/room_bg_' + user.roomMap + '.jpg')})` }">
    <div class="left-window absolute w-1/5 h-full">
    </div>
    <div class="flex flex-col justify-center center-window w-4/6 h-full">
      <div class="flex shadow-hard justify-center w-5/6 py-1 self-center border glass mb-5">
        {{ roomInfo.title }}
      </div>
      <div id="main-window" class="main-window w-full relative">
        <div v-bind:style="{'left': `${participant.x}px`, 'bottom' : `${participant.y}px`}"
             class="absolute flex flex-col justify-center items-center w-[30rem]"
             v-for="participant in participants" :key="participant.seq">
          <div :id="`character-chat-${participant.seq}`"
               class="shadow-hard w-fit absolute bottom-[100%] hidden chat-text break-words  bg-gray-100 text-black px-3 py-1 rounded-xl mb-3 border border-gray-500">
            <span class="w-full max-w-[30rem]"></span>
          </div>
          <img class="w-36" :src="getCharacterImage(participant.roomCharacter)" alt="캐릭터">
          <span class="rounded-3xl bg-gray-800 bg-opacity-60 px-3 py-0.5 mt-2 text-sm">
            <font-awesome-icon v-if="roomInfo.ownerSeq === participant.seq" class=" text-yellow-300 mr-1"
                               icon="fa-solid fa-crown"/>
            {{ participant.nickname }}
          </span>
        </div>
      </div>
    </div>
    <div class="right-window absolute right-5 flex items-center justify-end w-2/5 h-full transition-transform"
         :class="{'translate-x-[calc(100%-7rem)]': isRightSlide}">
      <div class="flex justify-center items-center w-7 h-16 bg-white bg-opacity-50 rounded-l cursor-pointer"
           @click="isRightSlide = !isRightSlide">
        <font-awesome-icon v-if="isRightSlide" icon="chevron-left"/>
        <font-awesome-icon v-if="!isRightSlide" icon="chevron-right"/>
      </div>
      <div
          class="right-menu flex flex-col w-5/6 pl-5 pr-2 pt-3 pb-16 rounded overflow-hidden h-5/6 text-sm relative transition-transform">
        <div class="chat-log flex flex-col overflow-x-hidden overflow-y-auto h-[calc(100%-3rem)] pr-3"
             v-if="rightCurrentTab === 'chat'">
          <template v-for="(chat, index) in chats" :key="chat.senderSeq">
            <div v-if="chat.type2 === 'enter' || chat.type2 === 'leave'" class="self-center text-xs mb-2">
              {{ chat.content }}
            </div>
            <div v-else-if="chat.senderSeq == user.seq" class="you break-words"
                 :class="{'first_chat': chats[index - 1].type2 !== 'chat' || chats[index - 1].senderSeq !== chat.senderSeq}">
              <span class="content text-start mr-2">{{ chat.content }}</span>
              <span class="datetime text-xs text-gray-200"
                    v-if="index === chats.length - 1 ||
                                chat.senderSeq !== chats[index + 1].senderSeq ||
                                !isSameMinute(chat.oriDatetime, chats[index + 1].oriDatetime)">{{
                  chat.datetime
                }}</span>
            </div>
            <div v-else class="other flex flex-col break-words pl-14 relative"
                 :class="{'first_chat': chats[index - 1].type2 !== 'chat' || chats[index - 1].senderSeq !== chat.senderSeq}">
              <div v-if="chats[index - 1].type2 !== 'chat' || chats[index - 1].senderSeq !== chat.senderSeq"
                   class="w-10 h-10 absolute left-0 bottom-2 flex items-center">
                <img class="w-full h-full object-cover bg-white rounded-3xl" :src="chat.senderPhoto"
                     alt="프로필 사진" v-if="chat.senderPhoto">
                <div v-if="!chat.senderPhoto"
                     class=" rounded-3xl w-full h-full flex justify-center items-center font-bold text-lg shadow-2xl text-white">
                  <span>{{ chat.sender[0] }}</span>
                </div>
              </div>
              <span v-if="chats[index - 1].type2 !== 'chat' || chats[index - 1].senderSeq !== chat.senderSeq"
                    class="title flex ml-1">{{ chat.sender }}</span>
              <span class="content text-start mr-2">{{ chat.content }}</span>
              <span class="datetime text-xs text-gray-200"
                    v-if="index === chats.length - 1 ||
                                  chat.senderSeq !== chats[index + 1].senderSeq ||
                                  !isSameMinute(chat.oriDatetime, chats[index + 1].oriDatetime)">{{
                  chat.datetime
                }}</span>
            </div>
          </template>
          <form @submit.prevent="chatSend">
            <div class="absolute w-full left-0 bottom-12">
              <input type="text" v-model.trim="chatMsg"
                     class="w-5/6 py-2 pl-3 pr-9 text-black rounded-3xl" maxlength="500"
                     placeholder="채팅을 입력하세요.">
              <font-awesome-icon
                  class="absolute select-none right-14 bottom-3 text-blue-300 cursor-pointer hover:text-blue-400 active:text-blue-500"
                  icon="fa-solid fa-paper-plane" v-on:click="chatSend"/>
            </div>
          </form>
          <div class="absolute left-0 bottom-24 w-full select-none animate-bounce" v-if="isNewChat">
            <span class="px-2 py-0.5 bg-gray-700 rounded-2xl bg-opacity-50 cursor-pointer "
                  @click="scrollToBottom"><font-awesome-icon icon="chevron-down"/> 새로운 채팅이 있습니다.</span>
          </div>
        </div>
        <div class="participant-list flex flex-col overflow-x-hidden overflow-y-auto h-[calc(100%-3rem)] pr-3"
             v-if="rightCurrentTab === 'participant'">
          <div v-for="participant in participants" :key="participant.seq">
            <div class="flex items-center justify-between py-3 border-b border-b-gray-400">
              <div class="flex items-center">
                <div class="w-10 h-10 rounded-3xl overflow-hidden mr-2 border border-gray-500 relative">
                  <img class="w-full h-full object-cover bg-white" :src="participant.photo"
                       alt="프로필 사진" v-if="participant.photo">
                  <span class="w-full h-full flex justify-center items-center font-bold text-lg bg-green-700 text-white"
                        v-if="!participant.photo">{{ participant.nickname[0] }}</span>
                </div>
                <span>
                          {{ participant.nickname }}
                        </span>
              </div>
              <font-awesome-icon v-if="roomInfo.ownerSeq == participant.seq"
                                 class="text-2xl text-yellow-300 mr-2" icon="fa-solid fa-crown"/>
            </div>
          </div>
        </div>
        <div class="absolute w-full left-0 bottom-0 bg-white h-10 text-xl">
          <ul class="flex text-black justify-around items-center h-full">
            <li class="flex justify-center px-3 py-1 cursor-pointer"
                :class="{'currentTab': rightCurrentTab==='chat'}" @click="rightCurrentTab='chat'">
              <font-awesome-icon icon="fa-regular fa-comment-dots"/>
              <span class="text-sm px-2 slide-in hidden" v-if="rightCurrentTab === 'chat'">채팅</span>
            </li>
            <li class="flex justify-center px-3 py-1 cursor-pointer"
                :class="{'currentTab': rightCurrentTab==='participant'}" @click="participantTab">
              <font-awesome-icon icon="fa-regular fa-circle-user"/>
              <span class="text-sm px-2 slide-in hidden"
                    v-if="rightCurrentTab === 'participant'">참여자</span>
            </li>
            <li class="flex justify-center px-3 py-1 cursor-pointer"
                :class="{'currentTab': rightCurrentTab==='3'}" @click="rightCurrentTab='3'">
              <font-awesome-icon icon="fa-regular fa-comment-dots"/>
              <span class="text-sm px-2 slide-in hidden" v-if="rightCurrentTab === '3'">ss</span>
            </li>
            <li class="flex justify-center px-3 py-1 cursor-pointer"
                :class="{'currentTab': rightCurrentTab==='4'}" @click="rightCurrentTab='4'">
              <font-awesome-icon icon="fa-ellipsis"/>
              <span class="text-sm px-2 slide-in hidden" v-if="rightCurrentTab === '4'">dd</span>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {useUserStore} from "@/stores/user";
import {getCurrentInstance, onBeforeUnmount, onMounted, ref, watch} from "vue";
import {FontAwesomeIcon} from "@fortawesome/vue-fontawesome";
import {useRoomStore} from "@/stores/room";
// import {useRoomStore} from "@/stores/room";

export default {
  name: "MeetRoomView",
  components: {FontAwesomeIcon},
  data() {
    return {
      isRightSlide: true,
      rightCurrentTab: 'chat',
      chatMsg: '',
    }
  },
  setup() {
    const userStore = useUserStore();
    const roomStore = useRoomStore();
    const user = userStore.userInfo;
    const socket = new WebSocket('ws://' + process.env.VUE_APP_SERVER_IP + ':8080/gg');
    const instance = getCurrentInstance();
    const roomInfo = JSON.parse(history.state.roomInfo);
    let chats = ref([]);
    let participants = ref([]);

    let isNewChat = ref(false);
    let chatLogElement = ref(null);
    let mainWindowElement = ref(null);

    // 채팅방 사용자 가져오기
    const getRoomParticipants = async () => {
      try {
        await roomStore.getRoomParticipants(roomInfo.seq);
        if (roomStore?.dataResponse.status === 200) {
          const sortArr = roomStore.currentRoomParticipants;
          sortArr.forEach(data => {
            if (participants.value.every(participant => participant.seq !== data.seq)) {
              const xy = randomCharacterLocation();
              data.x = xy.x;
              data.y = xy.y;
              participants.value.push(data);
            } else {
              participants.value.map(participant => {
                if (participant.seq === data.seq) {
                  const ori_x = participant.x;
                  const ori_y = participant.y;
                  participant = data;

                  if (!ori_x || !ori_y) {
                    const xy = randomCharacterLocation();
                    participant.x = xy.x;
                    participant.y = xy.y;
                  } else {
                    participant.x = ori_x;
                    participant.y = ori_y;
                  }
                }
                return participant;
              })

            }
          });

          participants.value.sort((a, b) => {
            const result = a.seq === roomInfo.ownerSeq ? -1 : b.seq === roomInfo.ownerSeq ? 1 : 0;
            return result;
          });
        } else {
          instance.appContext.config.globalProperties.utils.msgError(this.dataResponse.data || instance.appContext.config.globalProperties.utils.normalErrorMsg);
        }
      } catch (error) {
        console.error(error);
        instance.appContext.config.globalProperties.utils.msgError((error?.response?.data) || instance.appContext.config.globalProperties.utils.normalErrorMsg);
      }
    }

    const checkScroll = () => {
      if (chatLogElement.value.scrollTop + chatLogElement.value.clientHeight >= chatLogElement.value.scrollHeight) {
        isNewChat.value = false;
      }
    };

    const scrollToBottom = () => {
      chatLogElement.value.scrollTop = chatLogElement.value.scrollHeight;
    };

    onMounted(() => {
      chatLogElement.value = document.querySelector('.chat-log');
      chatLogElement.value.addEventListener('scroll', checkScroll);
      mainWindowElement.value = document.querySelector('#main-window');
    });

    watch(chats, () => {
      if (chatLogElement.value &&
          chatLogElement.value.scrollTop + chatLogElement.value.clientHeight < chatLogElement.value.scrollHeight) {
        isNewChat.value = true;
      }
    }, {deep: true});

    socket.onopen = async () => {
      chatSocketSend('enter');
    };

    let participantsCharacter = {
      showTextMs: 5000,
    };
    socket.onmessage = (event) => {
      const data = JSON.parse(event.data);
      if (data.type1 === 'room') {
        if (data.type2 === 'chat' || data.type2 === 'enter' || data.type2 === 'leave') {
          data.oriDatetime = data.datetime;
          if (data.datetime) {
            var dateObj = new Date(data.datetime);
            let date = new Date(dateObj);
            let options = {hour: 'numeric', minute: 'numeric', hour12: true};
            data.datetime = date.toLocaleTimeString('ko-KR', options);
          }
          if (data.type2 === 'enter' || data.type2 === 'leave') {
            const seq = "seq" + data.senderSeq;
            if (data.type2 === 'leave') {
              participants.value = participants.value.filter(participant => participant.seq != data.senderSeq);
              delete (participantsCharacter[seq]);
            } else {
              const chatElement = document.querySelector("#character-chat-" + data.senderSeq);
              participantsCharacter[seq] = {
                text: data.content,
                timeoutFN: setTimeout(function () {
                  chatElement.classList.add("hidden");
                }, participantsCharacter.showTextMs)
              };
              console.log(participantsCharacter);
              clearTimeout(participantsCharacter[seq].timeoutFN);
            }
            getRoomParticipants();
          }
          if (data.type2 === 'chat') {
            const chatElement = document.querySelector("#character-chat-" + data.senderSeq);
            const chatElementText = chatElement.querySelector("span");
            const seq = "seq" + data.senderSeq;
            console.log(participantsCharacter);
            if (participantsCharacter[seq]) {
              clearTimeout(participantsCharacter[seq].timeoutFN);
            }

            chatElementText.innerText = data.content;
            chatElement.classList.remove("hidden");

            if (!participantsCharacter[seq]) {
              participantsCharacter[seq] = {};
            }
            participantsCharacter[seq].timeoutFN = setTimeout(function () {
              chatElement.classList.add("hidden");
            }, participantsCharacter.showTextMs);

            console.log(chatElement);
          }
          chats.value.push(data);
        }

        if (data.type2 === 'delete') {
          this.chatRooms = this.chatRooms.filter(room => room.id !== data.chatRoomId);
        }
      }
    };

    socket.onerror = (error) => {
      console.error(`WebSocket Error : ${error}`);
      instance.appContext.config.globalProperties.utils.msgError("오류가 발생했습니다. 관리자에게 문의해주세요.");
    };

    socket.onclose = () => {
      chatSocketSend('leave');
    };

    onBeforeUnmount(() => {
      if (chatLogElement.value) {
        chatLogElement.value.removeEventListener('scroll', checkScroll);
      }
      chatSocketSend('leave');
      socket.close();
    });

    const chatSocketSend = (mode, msg) => {
      const chatMsg = {
        type1: 'room',
        type2: mode,
        roomId: roomInfo.seq,
        sender: user.nickname,
        senderSeq: user.seq,
        senderPhoto: user.photo,
        content: msg,
      }
      socket.send(JSON.stringify(chatMsg));
    };

    const randomCharacterLocation = () => {
      const minus = 200;
      const box = mainWindowElement.value;
      console.log(box);
      const range = {
        x_st: 0,
        x_ed: box.clientWidth,
        y_st: 300,
        y_ed: box.clientHeight,
      }
      let xy = {
        x: Math.floor(Math.random() * (range.x_ed - range.x_st)) + range.x_st,
        y: Math.floor(Math.random() * (range.y_ed - range.y_st)) + range.y_st,
      }
      xy.x = xy.x - minus < 0 ? 0 : xy.x - minus;
      xy.y = xy.y - minus < 0 ? 0 : xy.y - minus;
      return xy;
    }

    return {
      userStore,
      user,
      socket,
      roomInfo,
      chats,
      chatSocketSend,
      isNewChat,
      scrollToBottom,
      getRoomParticipants,
      participants,
      randomCharacterLocation
    };
  }, methods: {
    chatSend() {
      if (this.chatMsg !== '') {
        this.chatSocketSend('chat', this.chatMsg);
        this.chatMsg = '';
      }
    },
    participantTab() {
      this.rightCurrentTab = 'participant';
      this.getRoomParticipants();
    },
    isSameMinute(datetime1, datetime2) {
      const date1 = new Date(datetime1);
      const date2 = new Date(datetime2);
      return date1.getFullYear() === date2.getFullYear() &&
          date1.getMonth() === date2.getMonth() &&
          date1.getDate() === date2.getDate() &&
          date1.getHours() === date2.getHours() &&
          date1.getMinutes() === date2.getMinutes();
    },
    getCharacterImage(roomCharacter) {
      const images = require.context('@/assets/image/character', false, /\.png$/);
      return images(`./c${roomCharacter}.png`);
    },
  },

}
</script>

<style scoped>
#main-room-view {
  height: 93%;
}

#main-room-view .center-window .main-window {
  height: 77%;
}

#main-room-view .right-window .right-menu {
  background: rgb(255 255 255 / 25%);
  box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.37);
  backdrop-filter: blur(9.5px);
  -webkit-backdrop-filter: blur(9.5px);
  border-radius: 10px;
  border: 1px solid rgba(255, 255, 255, 0.18);
}

#main-room-view .right-window .right-menu .chat-log {
  scrollbar-gutter: stable;
  scroll-behavior: smooth;
}

#main-room-view .right-window .right-menu .chat-log::-webkit-scrollbar {
  width: 6px;
}

#main-room-view .right-window .right-menu .chat-log::-webkit-scrollbar-thumb {
  background: #f1b4bb; /* 스크롤바 색상 */
  border-radius: 10px; /* 스크롤바 둥근 테두리 */
}

#main-room-view .right-window .right-menu .chat-log::-webkit-scrollbar-track {
  background: rgba(220, 20, 60, .1); /*스크롤바 뒷 배경 색상*/
}

.right-menu div.you, .right-menu div.other {
  max-width: 60%;
  display: flex;
  position: relative;
}

.right-menu .chat-log .content {
  color: #000;
  max-width: 100%;
  margin-bottom: 3px;
  padding: 7px 10px;
  border-radius: 5px;
  position: relative;
}

.right-menu .chat-log .first_chat {
  margin-top: 7px;
}

.right-menu .chat-log .other {
  align-self: flex-start;
}

.right-menu .chat-log .other .content {
  background-color: #f0f0f0;
}

.right-menu .chat-log .other .datetime {
  position: absolute;
  bottom: 10px;
  right: -55px;
}

.right-menu .chat-log .you {
  align-self: flex-end;
}

.right-menu .chat-log .you .content {
  background-color: #aee1e1;
}

.right-menu .chat-log .you .datetime {
  position: absolute;
  bottom: 10px;
  left: -55px;
}

.right-menu .chat-log .other.first_chat .content::after {
  content: "";
  position: absolute;
  left: -10px;
  top: 10px;
  border: 10px solid;
  border-color: #f0f0f0 transparent transparent transparent;
}

.right-menu .chat-log .you.first_chat .content::after {
  content: "";
  position: absolute;
  right: -10px;
  top: 10px;
  border: 10px solid;
  border-color: #aee1e1 transparent transparent transparent;
}

.right-window .currentTab {
  background: #fff3dd;
  color: orange;
  border-radius: 10px;
  transition: transform 0.5s;
}

@keyframes slideIn {
  0% {
    transform: translateX(-100%);
    opacity: 0;
  }
  100% {
    transform: translateX(0);
    opacity: 1;
  }
}

.slide-in {
  animation: slideIn 0.5s forwards;
  display: inline-block;
}

#main-window .chat-text {

}

#main-window .chat-text::after {
  content: "";
  position: absolute;
  left: 50%;
  bottom: 0;
  transform: translate(-50%, 100%);
  height: 0;
  width: 0;
  border-left: 10px solid transparent;
  border-right: 10px solid transparent;
  border-top: 10px solid rgb(243, 244, 246);
}


</style>
