<template>
    <div id="main-room-view" class="flex items-center bg-room_1_background bg-right-top bg-[length:110%_120%] text-white">
        <div class="left-window w-1/5 h-full">
        </div>
        <div class="flex flex-col justify-center center-window w-3/5 h-full">
            <div class="flex justify-center w-5/6 py-1 self-center border glass mb-5">
                {{roomInfo.title}}
            </div>
            <div class="main-window border border-red-600 w-full">
                asd
            </div>
        </div>
        <div class="right-window flex items-center justify-center w-1/5 h-full transition-transform relative" :class="{'translate-x-[calc(100%-3rem)]': isRightSlide}">
            <div class="flex justify-center items-center w-7 h-16 bg-white bg-opacity-50 rounded-l cursor-pointer" @click="isRightSlide = !isRightSlide">
                <font-awesome-icon v-if="isRightSlide" icon="chevron-left"/>
                <font-awesome-icon v-if="!isRightSlide" icon="chevron-right"/>
            </div>
            <div class="right-menu flex flex-col w-5/6 pl-5 pr-2 pt-3 pb-16 rounded overflow-hidden h-5/6 text-sm relative transition-transform">
                <div class="chat-log flex flex-col overflow-x-hidden overflow-y-auto h-[calc(100%-3rem)] pr-3" v-if="rightCurrentTab === 'chat'">
                    <template v-for="chat in chats" :key="chat.senderSeq">
                        <p v-if="chat.type2 === 'enter' || chat.type2 === 'leave'" class="self-center text-xs mb-2">
                            {{ chat.content }}
                        </p>
                        <p v-else-if="chat.senderSeq == user.seq" class="you break-words">
                            <span class="content text-start ml-2">{{ chat.content }}</span>
                            <span class="datetime text-xs text-gray-200">{{ chat.datetime }}</span>
                        </p>
                        <p v-else class="other flex flex-col break-words ">
                            <span class="title flex ml-1">{{ chat.sender }}</span>
                            <span class="content text-start mr-2">{{ chat.content }}</span>
                            <span class="datetime text-xs text-gray-200">{{ chat.datetime }}</span>
                        </p>
                    </template>
                    <form @submit.prevent="chatSend">
                        <div class="absolute w-full left-0 bottom-12">
                            <input type="text" v-model.trim="chatMsg" class="w-5/6 py-2 pl-3 pr-9 text-black rounded-3xl" maxlength="500" placeholder="채팅을 입력하세요.">
                            <font-awesome-icon class="absolute right-10 bottom-3 text-blue-300 cursor-pointer hover:text-blue-400 active:text-blue-500" icon="fa-solid fa-paper-plane" v-on:click="chatSend"/>
                        </div>
                    </form>
                    <div class="absolute left-0 bottom-24 w-full" v-if="isNewChat">
                        <span class="px-2 py-0.5 bg-gray-700 rounded-2xl bg-opacity-50 cursor-pointer" @click="scrollToBottom"><font-awesome-icon icon="chevron-down"/> 새로운 채팅이 있습니다.</span>
                    </div>
                </div>
                <div class="absolute w-full left-0 bottom-0 bg-white h-10 text-xl">
                    <ul class="flex text-black justify-around items-center h-full">
                        <li class="flex justify-center px-3 py-1 cursor-pointer" :class="{'currentTab': rightCurrentTab==='chat'}" @click="rightCurrentTab='chat'">
                            <font-awesome-icon icon="fa-regular fa-comment-dots"/>
                            <span class="text-sm px-2 slide-in hidden" v-if="rightCurrentTab === 'chat'">채팅</span>
                        </li>
                        <li class="flex justify-center px-3 py-1 cursor-pointer" :class="{'currentTab': rightCurrentTab==='2'}" @click="rightCurrentTab='2'">
                            <font-awesome-icon icon="fa-regular fa-comment-dots"/>
                            <span class="text-sm px-2 slide-in hidden" v-if="rightCurrentTab === '2'">aa</span>
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
        const user = userStore.userInfo;
        const socket = new WebSocket('ws://' + process.env.VUE_APP_SERVER_IP + ':8080/gg');
        const instance = getCurrentInstance();
        const roomInfo = JSON.parse(history.state.roomInfo);
        let chats = ref([]);
        let isNewChat = ref(false);
        let chatLogElement = ref(null);

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
                    if(data.datetime){
                        var dateObj = new Date(data.datetime);
                        let date = new Date(dateObj);
                        let options = { hour: 'numeric', minute: 'numeric', hour12: true };
                        data.datetime = date.toLocaleTimeString('ko-KR', options);
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
                content: msg,
            }
            socket.send(JSON.stringify(chatMsg));
        };

        return {
            userStore, user, socket, roomInfo, chats, chatSocketSend, isNewChat, scrollToBottom
        };
    },methods: {
        chatSend() {
            if(this.chatMsg !== ''){
                this.chatSocketSend('chat', this.chatMsg);
                this.chatMsg = '';
            }
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

    .right-menu p.you, .right-menu p.other {
        max-width: 60%;
        display: flex;
        position: relative;
    }
    .right-menu p .content {
        color: #000;
        max-width: 100%;
        margin-bottom: 10px;
        padding: 7px 10px;
        border-radius: 5px;
        position: relative;
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

    .right-menu .chat-log .other .content::after {
        content: "";
        position: absolute;
        left: -10px;
        top: 10px;
        border: 10px solid;
        border-color: #f0f0f0 transparent transparent transparent;
    }

    .right-menu .chat-log .you .content::after {
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