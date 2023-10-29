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
        <div class="rooms-wrap flex flex-wrap content-start w-4/6 mt-10 h-4/6 overflow-auto py-5" v-if="rooms">

            <div v-for="(room, index) in filteredRoom" :key="index" class="flex room h-1/4 mb-10 rounded mx-5 flex-col px-3 py-4 relative" :data-lock="room.isLocked">
                <div class="w-10 h-10 rounded-3xl overflow-hidden absolute -left-5 -top-5 tooltip bg-white border border-gray-500" :data-tooltip="room.user.nickname">
                    <img class="w-full h-full object-cover" :src="room.user.photo" alt="프로필 사진" v-if="room.user.photo">
                    <div v-if="!room.user.photo" class="w-full h-full flex justify-center items-center font-bold text-xl bg-green-700 text-white">
                        <span v-if="room.user.nickname">{{room.user.nickname[0]}}</span>
                        <span v-if="!room.user.nickname">{{room.user.name[0]}}</span>
                    </div>
                </div>
                <div class="flex justify-center items-center font-bold">
                    <font-awesome-icon v-if="room.isLocked" class="fa-md font-bold text-orange-400" icon="fa-lock"></font-awesome-icon>
                    <p class="title text-white truncate max-w-sm mx-1 tooltip" data-tooltip="">
                        <span class="w-full" v-html="highlightKeyword(room.title)"></span>
                    </p>
                    <span class="text-sm" :class="{'text-red-300':room.isFull, 'text-blue-300':!room.isFull}">( {{room.currentParticipant}} / {{room.maxParticipant}} )</span>
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
import {useRoomStore} from "@/stores/room";
import {onBeforeUnmount, getCurrentInstance, ref, watch, computed, onMounted, watchEffect, nextTick} from "vue";

export default {
    name: 'MainView',
    data() {
        return {
        };
    },
    setup() {
        const userStore = useUserStore();
        const user = userStore.userInfo;
        const socket = new WebSocket('ws://' + process.env.VUE_APP_SERVER_IP + ':8080/gg');
        const instance = getCurrentInstance();
        const roomStore = useRoomStore();
        let rooms = ref([]);
        let search = ref('');

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

            if (data.type === 'CREATE') {
                this.chatRooms.push(data.chatRoom);
            } else if (data.type === 'DELETE') {
                this.chatRooms = this.chatRooms.filter(room => room.id !== data.chatRoomId);
            }
        };

        socket.onerror = (error) => {
            console.error(`WebSocket Error : ${error}`);
            instance.appContext.config.globalProperties.utils.msgError("오류가 발생했습니다. 관리자에게 문의해주세요.");
        };

        socket.onclose = () => {
            console.log('웹소켓 연결 종료');
        };

        const filteredRoom = computed(() => {
            return rooms.value.filter(r => {
                return r.title.toLowerCase().includes(search.value) || r.topic.toLowerCase().includes(search.value);
            });
        });

        watchEffect(() => {
            if (rooms.value.length > 0) {
                nextTick(() => {
                    const tooltips = instance.appContext.config.globalProperties.utils.tooltips;
                    tooltips('tooltip');
                });
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
            console.log(1);
            console.log(document.querySelectorAll(".tooltip"));
            instance.appContext.config.globalProperties.utils.tooltips('tooltip');
            console.log(2);
        });

        return {
            userStore, user, socket, rooms, search, filteredRoom
        };
    },
    methods: {
        highlightKeyword(text) {
            if (this.search) {
                const re = new RegExp(this.search, 'gi')
                return text.replace(re, (matchedText) => `<mark>${matchedText}</mark>`)
            }
            return text
        }
    },
}
</script>

<style>
#main-view {
    height: 93%;
}

.room{
    width: 15rem;
    background: rgb(255 255 255 / 25%);
    box-shadow: 0 8px 32px 0 rgba( 31, 38, 135, 0.37 );
    backdrop-filter: blur( 9.5px );
    -webkit-backdrop-filter: blur( 9.5px );
    border-radius: 10px;
    border: 1px solid rgba( 255, 255, 255, 0.18 );
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