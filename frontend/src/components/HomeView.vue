<template>
    <div id="main-view" class="flex flex-col items-center bg-main_background bg-cover">
        <div class="flex w-4/6 text-white py-10 justify-between">
            <div>
                <button class="px-3 py-2 bg-blue-600 rounded text-sm hover:bg-blue-500" type="button">
                    <span class="mr-4">미팅 생성하기</span>
                    <font-awesome-icon class="fa-md font-bold" icon="fa-plus"></font-awesome-icon>
                </button>
            </div>
            <div class="flex">
                <div class="flex relative">
                    <input type="text" placeholder=""
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
        <div class="rooms-wrap flex flex-wrap content-start w-4/6 mt-10 h-4/6 overflow-auto py-5">

            <div v-for="(room, index) in meetingRooms" :key="index" class="flex room bg-white h-1/4 mb-10 rounded mx-5 shadow-2xl flex-col px-3 py-4 relative" :data-lock="room.isLocked">
                <div class="w-10 h-10 rounded-3xl overflow-hidden absolute -left-5 -top-5 tooltip" :data-tooltip="room.manager.nickname">
                    <img class="w-full h-full object-cover" :src="room.manager.photo" alt="프로필 사진">
                </div>
                <div class="flex justify-center items-center font-bold">
                    <font-awesome-icon v-if="room.isLocked" class="fa-md font-bold text-orange-400" icon="fa-lock"></font-awesome-icon>
                    <p class="title truncate max-w-sm mx-1 tooltip" data-tooltip="">
                        <span class="w-full">{{room.title}}</span>
                    </p>
                    <span class="text-sm" :class="{'text-red-300':room.isFull, 'text-blue-300':!room.isFull}">( {{room.currentCapacity}} / {{room.maxCapacity}} )</span>
                </div>
                <div>
                    <p class="truncate tooltip">
                        <span class="text-gray-400 text-sm" title=room.topic>{{room.topic}}</span>
                    </p>
                </div>
                <div class="mt-2">
                    <button class="font-bold text-white px-2.5 py-1.5 bg-gray-600 rounded text-sm hover:bg-gray-500" type="button">
                        <span class="mx-1">입장 하기</span>
                    </button>
                </div>
            </div>

        </div>
    </div>
</template>

<script>
import {useUserStore} from "@/stores/user";
import {onBeforeUnmount, getCurrentInstance} from "vue";

export default {
    name: 'MainView',
    data() {
        return {
            meetingRooms: [
                {
                    title: "테스트테스트테스트테스트테스트테스트테스트테스트테스트",
                    topic: "무엇을 무엇을 해볼까요?",
                    isLocked: true,
                    maxCapacity: 5,
                    currentCapacity: 1,
                    isFull: false,
                    manager: {
                        photo: "https://health.chosun.com/site/data/img_dir/2023/07/17/2023071701753_0.jpg",
                        nickname: "티이모"
                    }
                },{
                    title: "타이틀",
                    topic: "토픽",
                    isLocked: true,
                    maxCapacity: 5,
                    currentCapacity: 1,
                    isFull: false,
                    manager: {
                        photo: "https://i.namu.wiki/i/pRQPYLHlam-IJBOTdkfmOTLBQWoUa2n8wEM1qj-arz3nV9yiq3xb4KT1FCbc1jgjrnDjvHwohfYxmY6YLjEbUA.webp",
                        nickname: "ㅁㅁㅁ"
                    }
                }
                ,{
                    title: "멋진사람들의 모임",
                    topic: "내가 제일 잘나가",
                    isLocked: true,
                    maxCapacity: 1,
                    currentCapacity: 1,
                    isFull: true,
                    manager: {
                        photo: "https://ichef.bbci.co.uk/news/640/cpsprodpb/E172/production/_126241775_getty_cats.png",
                        nickname: "티이모"
                    }
                }
            ]
        };
    },
    setup() {
        const userStore = useUserStore();
        const user = userStore.userInfo;
        const socket = new WebSocket('ws://' + process.env.VUE_APP_SERVER_IP + ':8080/gg');
        const instance = getCurrentInstance();


        socket.onopen = () => {
            console.log('웹소켓 연결 성공');
            const tooltips = instance.appContext.config.globalProperties.utils.tooltips;
            tooltips('tooltip', "테스트");
        };

        socket.onmessage = (event) => {
            const data = JSON.parse(event.data);

            if (data.type === 'CREATE') {
                this.chatRooms.push(data.chatRoom);
            } else if (data.type === 'DELETE') {
                this.chatRooms = this.chatRooms.filter(room => room.id !== data.chatRoomId);
            }
        };

        socket.onerror = (error) => {
            console.error(`WebSocket Error : ${error}`);
            this.utils.msgError("오류가 발생했습니다. 관리자에게 문의해주세요.");
        };

        socket.onclose = () => {
            console.log('웹소켓 연결 종료');
        };

        onBeforeUnmount(() => {
            if (socket) {
                socket.close();
            }
        });

        return {
            userStore, user, socket
        };
    }
}
</script>

<style>
#main-view {
    height: 93%;
}

.room{
    width: 15rem;
}

.rooms-wrap {
    height: 300px;
    overflow-y: scroll; /*  */
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
    background: rgba(220, 20, 60, .1);  /*스크롤바 뒷 배경 색상*/
}

.room .title{
    max-width: 9rem;
}
</style>