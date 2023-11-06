<template>
    <div id="main-room-view" class="flex items-center bg-room_1_background bg-right-top bg-[length:110%_120%] text-white">
        <div class="left-window w-1/5 h-full">
        </div>
        <div class="flex items-center center-window w-3/5 h-full">
            <div class="main-window border border-red-600 w-full">
                asd
            </div>
        </div>
        <div class="right-window w-1/5 h-full">
            <div class="chat-log flex flex-col items-start px-5 py-3 rounded overflow-y-auto h-full text-sm">
                <p class="other flex flex-col">
                    <span class="title flex ml-1">안녕</span>
                    <span class="content">반갑습니다.</span>
                    <span class="datetime text-xs text-gray-200">오후 2:00</span>
                </p>
                <p class="you">
                    <span class="content">ㅎㅇㅎㅇ</span>
                    <span class="datetime text-xs text-gray-200">오후 2:01</span>
                </p>
            </div>
        </div>
    </div>
</template>

<script>
import {useUserStore} from "@/stores/user";
import {getCurrentInstance} from "vue";
// import {useRoomStore} from "@/stores/room";

export default {
    name: "MeetRoomView",
    setup() {
        const userStore = useUserStore();
        const user = userStore.userInfo;
        const socket = new WebSocket('ws://' + process.env.VUE_APP_SERVER_IP + ':8080/gg');
        const instance = getCurrentInstance();

        socket.onopen = async () => {
        };

        socket.onmessage = (event) => {
            const data = JSON.parse(event.data);
            if (data.type1 === 'room') {
                if (data.type2 === 'chat') {

                    instance.data.slideShow = false;
                    instance.appContext.config.globalProperties.utils.notify.success("미팅 방이 생성되었습니다.", "생성 완료!");
                }else if (data.type2 === 'delete') {
                    this.chatRooms = this.chatRooms.filter(room => room.id !== data.chatRoomId);
                }
            }
        };

        socket.onerror = (error) => {
            console.error(`WebSocket Error : ${error}`);
            instance.appContext.config.globalProperties.utils.msgError("오류가 발생했습니다. 관리자에게 문의해주세요.");
        };

        socket.onclose = () => {
            console.log('웹소켓 연결 종료');
        };

        return {
            userStore, user, socket
        };
    },
}
</script>

<style scoped>
    #main-room-view {
        height: 93%;
    }

    #main-room-view .center-window .main-window {
        height: 75%;
    }

    #main-room-view .right-window .chat-log {
        background: rgb(255 255 255 / 25%);
        box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.37);
        backdrop-filter: blur(9.5px);
        -webkit-backdrop-filter: blur(9.5px);
        border-radius: 10px;
        border: 1px solid rgba(255, 255, 255, 0.18);
    }

    #main-room-view .right-window .chat-log::-webkit-scrollbar {
        width: 10px;
    }

    #main-room-view .right-window .chat-log::-webkit-scrollbar-thumb {
        background: #f1b4bb; /* 스크롤바 색상 */
        border-radius: 10px; /* 스크롤바 둥근 테두리 */
    }

    #main-room-view .right-window .chat-log::-webkit-scrollbar-track {
        background: rgba(220, 20, 60, .1); /*스크롤바 뒷 배경 색상*/
    }

    .chat-log p {
        max-width: 60%;
        display: flex;
        position: relative;
    }

    .chat-log p .content {
        color: #000;
        max-width: 100%;
        margin-bottom: 10px;
        padding: 7px 10px;
        border-radius: 5px;
        position: relative;
    }

    .chat-log .other {
        align-self: flex-start;
    }

    .chat-log .other .content {
        background-color: #f0f0f0;
    }

    .chat-log .other .datetime {
        position: absolute;
        bottom: 10px;
        right: -55px;
    }

    .chat-log .you {
        align-self: flex-end;
    }

    .chat-log .you .content {
        background-color: #aee1e1;
    }

    .chat-log .you .datetime {
        position: absolute;
        bottom: 10px;
        left: -55px;
    }

    .chat-log .other .content::after {
        content: "";
        position: absolute;
        left: -10px;
        top: 10px;
        border: 10px solid;
        border-color: #f0f0f0 transparent transparent transparent;
    }

    .chat-log .you .content::after {
        content: "";
        position: absolute;
        right: -10px;
        top: 10px;
        border: 10px solid;
        border-color: #aee1e1 transparent transparent transparent;
    }

</style>