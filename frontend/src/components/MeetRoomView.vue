<template>
    <div id="main-room-view" class="flex justify-center bg-room_1_background bg-right-top bg-[length:110%_120%] text-white">
        <div class="left-window absolute w-1/5 h-full">
        </div>
        <div class="flex flex-col justify-center center-window w-5/6 h-full">
            <div class="flex justify-center w-5/6 py-1 self-center border glass mb-5">
                {{roomInfo.title}}
            </div>
            <div class="main-window border border-red-600 w-full">
                asd
            </div>
        </div>
        <div class="right-window absolute -right-5 flex items-center justify-center w-2/5 h-full transition-transform" :class="{'translate-x-[calc(100%-5rem)]': isRightSlide}">
            <div class="flex justify-center items-center w-7 h-16 bg-white bg-opacity-50 rounded-l cursor-pointer" @click="isRightSlide = !isRightSlide">
                <font-awesome-icon v-if="isRightSlide" icon="chevron-left"/>
                <font-awesome-icon v-if="!isRightSlide" icon="chevron-right"/>
            </div>
            <div class="right-menu flex flex-col w-5/6 pl-5 pr-2 pt-3 pb-16 rounded overflow-hidden h-5/6 text-sm relative transition-transform">
                <div class="chat-log flex flex-col overflow-x-hidden overflow-y-auto h-[calc(100%-3rem)] pr-3" v-if="rightCurrentTab === 'chat'">
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
                                !isSameMinute(chat.oriDatetime, chats[index + 1].oriDatetime)">{{ chat.datetime }}</span>
                        </div>
                        <div v-else class="other flex flex-col break-words pl-14 relative" :class="{'first_chat': chats[index - 1].type2 !== 'chat' || chats[index - 1].senderSeq !== chat.senderSeq}">
                            <div v-if="chats[index - 1].type2 !== 'chat' || chats[index - 1].senderSeq !== chat.senderSeq" class="w-10 h-10 absolute left-0 bottom-2 flex items-center">
                              <img class="w-full h-full object-cover bg-white rounded-3xl" :src="chat.senderPhoto" alt="프로필 사진" v-if="chat.senderPhoto">
                              <div v-if="!chat.senderPhoto" class=" rounded-3xl w-full h-full flex justify-center items-center font-bold text-lg bg-green-700 text-white">
                                <span>{{ chat.sender[0] }}</span>
                              </div>
                            </div>
                            <span v-if="chats[index - 1].type2 !== 'chat' || chats[index - 1].senderSeq !== chat.senderSeq" class="title flex ml-1">{{ chat.sender }}</span>
                            <span class="content text-start mr-2">{{ chat.content }}</span>
                            <span class="datetime text-xs text-gray-200"
                                  v-if="index === chats.length - 1 ||
                                  chat.senderSeq !== chats[index + 1].senderSeq ||
                                  !isSameMinute(chat.oriDatetime, chats[index + 1].oriDatetime)">{{ chat.datetime }}</span>
                        </div>
                    </template>
                    <form @submit.prevent="chatSend">
                        <div class="absolute w-full left-0 bottom-12">
                            <input type="text" v-model.trim="chatMsg" class="w-5/6 py-2 pl-3 pr-9 text-black rounded-3xl" maxlength="500" placeholder="채팅을 입력하세요.">
                            <font-awesome-icon class="absolute select-none right-14 bottom-3 text-blue-300 cursor-pointer hover:text-blue-400 active:text-blue-500" icon="fa-solid fa-paper-plane" v-on:click="chatSend"/>
                        </div>
                    </form>
                    <div class="absolute left-0 bottom-24 w-full select-none" v-if="isNewChat">
                        <span class="px-2 py-0.5 bg-gray-700 rounded-2xl bg-opacity-50 cursor-pointer" @click="scrollToBottom"><font-awesome-icon icon="chevron-down"/> 새로운 채팅이 있습니다.</span>
                    </div>
                </div>
                <div class="participant-list flex flex-col overflow-x-hidden overflow-y-auto h-[calc(100%-3rem)] pr-3" v-if="rightCurrentTab === 'participant'">
                  <div v-for="participant in participants" :key="participant.seq">
                    <div class="flex items-center justify-between py-3 border-b border-b-gray-400">
                      <div class="flex items-center">
                        <div class="w-10 h-10 rounded-3xl overflow-hidden mr-2 border border-gray-500 relative">
                          <img class="w-full h-full object-cover bg-white" :src="participant.photo" alt="프로필 사진" v-if="participant.photo">
                          <span class="w-full h-full flex justify-center items-center font-bold text-lg bg-green-700 text-white" v-if="!participant.photo">{{ participant.nickname[0] }}</span>
                        </div>
                        <span>
                          {{participant.nickname}}
                        </span>
                      </div>
                      <font-awesome-icon v-if="roomInfo.ownerSeq == participant.seq" class="text-2xl text-yellow-300 mr-2" icon="fa-solid fa-crown"/>
                    </div>
                  </div>
                </div>
                <div class="absolute w-full left-0 bottom-0 bg-white h-10 text-xl">
                    <ul class="flex text-black justify-around items-center h-full">
                        <li class="flex justify-center px-3 py-1 cursor-pointer" :class="{'currentTab': rightCurrentTab==='chat'}" @click="rightCurrentTab='chat'">
                            <font-awesome-icon icon="fa-regular fa-comment-dots"/>
                            <span class="text-sm px-2 slide-in hidden" v-if="rightCurrentTab === 'chat'">채팅</span>
                        </li>
                        <li class="flex justify-center px-3 py-1 cursor-pointer" :class="{'currentTab': rightCurrentTab==='participant'}" @click="participantTab">
                            <font-awesome-icon icon="fa-regular fa-circle-user"/>
                            <span class="text-sm px-2 slide-in hidden" v-if="rightCurrentTab === 'participant'">참여자</span>
                        </li>
                        <li class="flex justify-center px-3 py-1 cursor-pointer" :class="{'currentTab': rightCurrentTab==='3'}" @click="rightCurrentTab='3'">
                            <font-awesome-icon icon="fa-regular fa-comment-dots"/>
                            <span class="text-sm px-2 slide-in hidden" v-if="rightCurrentTab === '3'">ss</span>
                        </li>
                        <li class="flex justify-center px-3 py-1 cursor-pointer" :class="{'currentTab': rightCurrentTab==='4'}" @click="rightCurrentTab='4'">
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
            isRightSlide: false,
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

        // 채팅방 사용자 가져오기
        const getRoomParticipants = async () => {
          try {
            await roomStore.getRoomParticipants(roomInfo.seq);
            if (roomStore?.dataResponse.status === 200) {
              const sortArr = roomStore.currentRoomParticipants;
              sortArr.sort((a, b) => {
                const result = a.seq === roomInfo.ownerSeq ? -1 : b.seq === roomInfo.ownerSeq ? 1 : 0;
                console.log(`a.seq: ${a.seq}, b.seq: ${b.seq}, result: ${result}`);
                return result;
              });

              console.log(sortArr);
              participants.value = sortArr;
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
        });

        watch(chats, () => {
            if (chatLogElement.value &&
                chatLogElement.value.scrollTop + chatLogElement.value.clientHeight < chatLogElement.value.scrollHeight) {
                isNewChat.value = true;
            }
        }, { deep: true });

        socket.onopen = async () => {
            chatSocketSend('enter');
        };

        socket.onmessage = (event) => {
            const data = JSON.parse(event.data);
            if (data.type1 === 'room') {
                if(data.type2 === 'chat' || data.type2 === 'enter' || data.type2 === 'leave'){
                    data.oriDatetime = data.datetime;
                    if(data.datetime){
                        var dateObj = new Date(data.datetime);
                        let date = new Date(dateObj);
                        let options = { hour: 'numeric', minute: 'numeric', hour12: true };
                        data.datetime = date.toLocaleTimeString('ko-KR', options);
                    }
                    if(data.type2 === 'enter' || data.type2 === 'leave'){
                        getRoomParticipants();
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

        return {
            userStore, user, socket, roomInfo, chats, chatSocketSend, isNewChat, scrollToBottom, getRoomParticipants, participants
        };
    },methods: {
        chatSend() {
            if(this.chatMsg !== ''){
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
        }
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

    #main-room-view .right-window .right-menu .chat-log{
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

    .right-menu .chat-log .first_chat{
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

    .right-window .currentTab{
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
</style>
