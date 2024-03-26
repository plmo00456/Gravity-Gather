<template>
    <div id="main-room-view" class="flex justify-center bg-right-top bg-cover text-white relative"
         v-bind:style="{ backgroundImage: `url(${getImageUrl(user.roomMap)})` }">
        <div class="left-window absolute left-[1%] flex items-center justify-start w-[30rem] h-full transition-transform z-10"
             :class="{'translate-x-[-97%]': isLeftSlide}">
            <div class="flex-col w-full h-5/6 text-black glass">
                <div class="w-full h-[6%] flex justify-center items-center bg-white text-xl rounded-t border border-white">할 일 목록</div>
                <font-awesome-icon v-if='todo.isLoading' class="fa-md text-blue-500 fa-spin" icon="fa-spinner"></font-awesome-icon>
                <div v-if="!todo.isLoading" class="items flex flex-col h-[89%] overflow-y-auto">
                    <div v-for="(item, index) in todos" :key="index">
                        <span v-if="item.date" class="text-gray-200 text-xs mt-2">{{ item.date }}</span>
                        <div v-else class="item flex flex-col" @click.prevent.stop="toggleTodoContent($event, item.content)">
                            <div :class="{'brightness-95': item.is_complete}"
                                class="w-[93%] min-h-[2rem] bg-white rounded-xl mt-2 m-auto p-3 cursor-pointer">
                                <div class="flex justify-between items-center select-none">
                                    <span class="truncate" :class="{'line-through': item.is_complete }">{{ item.title }}</span>
                                    <span class="text-gray-400 text-sm">[{{ item.receive_seq ? item.receive_nickname : item.user_nickname }}]</span>
                                </div>
                                <div class="content flex text-start w-full mt-1 border-t p-1 break-all text-gray-600 text-sm cursor-default bg-white overflow-y-hidden"
                                     v-html="item.content"
                                     @click.stop="">
                                </div>
                                <div v-if="item.user_seq === user.seq || item.receive_seq === user.seq || user.seq === roomInfo.ownerSeq" class="flex justify-between pt-3">
                                    <span @click.stop="">
                                        <ToggleSwitch @change="completeTodo(item.seq, item.is_complete)" v-model="item.is_complete" class="flex -ml-2" size="sm"></ToggleSwitch>
                                    </span>
                                    <span @click.stop="">
                                        <font-awesome-icon @click="clickUpdateTodo(item)" class="font-bold text-xl text-gray-400 hover:text-blue-500 mr-2" icon="fa-regular fa-pen-to-square"/>
                                        <font-awesome-icon @click="deleteTodo(item.seq, item.title)" class="font-bold text-xl text-red-300 hover:text-red-500" icon="fa-regular fa-trash-can"/>
                                    </span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="item flex items-center w-full px-3 rounded-b cursor-pointer h-[5%] text-white hover:bg-white hover:text-blue-500" @click="todo.isShow = 'add'">
                    <span class="flex mr-2">
                        <font-awesome-icon class="self-start" icon="fa-plus"/>
                    </span>
                    <span class="flex">새 할일</span>
                </div>
            </div>
            <div class="flex justify-center items-center w-7 h-16 bg-white bg-opacity-50 rounded-r cursor-pointer"
                 @click="isLeftSlide = !isLeftSlide">
                <font-awesome-icon v-if="isLeftSlide" icon="chevron-right"/>
                <font-awesome-icon v-if="!isLeftSlide" icon="chevron-left"/>
            </div>
        </div>
        <div class="flex flex-col justify-center center-window w-4/6 h-full">
            <div class="flex shadow-hard justify-center w-5/6 py-1 self-center border glass mb-5">
                {{ roomInfo.title }}
            </div>
            <div id="main-window" class="main-window w-5/6 relative">

                <div v-bind:style="{'left': `${participant.x}px`, 'bottom' : `${participant.y}px`}"
                     class="character select-none  absolute flex flex-col justify-center items-center w-[30rem]"
                     v-bind:class="`animation-move-astronaut${characterRandomNum1}`"
                     v-for="participant in participants" :key="participant.seq">
                    <VueDragResize :isResizable="false" :w="180" :h="150">
                        <div :id="`character-chat-${participant.seq}`"
                             class="shadow-hard w-fit absolute bottom-[100%] hidden chat-text break-words  bg-gray-100 text-black px-3 py-1 rounded-xl mb-3 border border-gray-500 z-10">
                            <span class="w-full max-w-[30rem]"></span>
                        </div>
                        <div class="character-img cursor-grab active:cursor-grabbing w-36">
                            <img class="w-36"
                                 v-bind:class="`animation-rotate-astronaut${characterRandomNum2}`"
                                 :src="getCharacterImage(participant.roomCharacter)" alt="캐릭터">
                        </div>
                        <span class="rounded-3xl bg-gray-800 bg-opacity-60 px-3 py-0.5 mt-3 text-sm">
                            <font-awesome-icon v-if="roomInfo.ownerSeq === participant.seq" class=" text-yellow-300 mr-1"
                                               icon="fa-solid fa-crown"/>
                            {{ participant.nickname }}
                            <b :id="`character-nickname-${participant.seq}`" v-show="participant.status === 'leave'" class="text-red-500"> [자리비움]</b>
                        </span>
                    </VueDragResize>
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
            <div class="right-menu flex flex-col w-5/6 pl-5 pr-2 pt-3 pb-16 rounded overflow-hidden h-5/6 text-sm relative transition-transform">
                <div class="chat-log flex flex-col overflow-x-hidden overflow-y-auto h-[calc(100%-3rem)] pr-3"
                     v-if="rightCurrentTab === 'chat'">
                    <template v-for="(chat, index) in chats" :key="chat.senderSeq">
                        <div v-if="chat.type2 === 'enter' || chat.type2 === 'leave'" class="self-center text-xs mb-2">
                            {{ chat.content }}
                        </div>
                        <div v-else-if="chat.senderSeq == user.seq" class="you break-words"
                             :class="{'first_chat': chats[index - 1].type2 !== 'chat' || chats[index - 1].senderSeq !== chat.senderSeq}">
                            <span class="content text-start mr-2">{{ chat.content }}</span>
                            <span class="datetime text-xs text-gray-200 mr-1"
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
                                <img class="w-full h-full object-cover bg-white rounded-3xl"
                                     :src="`${$env.protocol}${$env.serverIP}:${$env.port}${chat.senderPhoto}`"
                                     alt="프로필 사진" v-if="chat.senderPhoto">
                                <div v-if="!chat.senderPhoto"
                                     class=" rounded-3xl w-full h-full flex justify-center items-center font-bold text-lg shadow-2xl text-white bg-green-700">
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
                            <font-awesome-icon
                                @click="isChatEmoji = !isChatEmoji"
                                class="absolute text-lg select-none left-[3rem] bottom-2.5 text-gray-500 cursor-pointer hover:text-blue-400 active:text-blue-500"
                                icon="fa-regular fa-face-smile"/>
                            <EmojiPicker class="absolute left-2 bottom-10" v-if="isChatEmoji" :native="true" @select="onSelectEmoji"/>
                            <input type="text" v-model.trim="chatMsg"
                                   class="w-5/6 py-2 pl-8 pr-9 text-black rounded-3xl" maxlength="500"
                                   placeholder="채팅을 입력하세요.">
                            <font-awesome-icon
                                class="absolute select-none right-[4rem] bottom-3 text-blue-300 cursor-pointer hover:text-blue-400 active:text-blue-500"
                                icon="fa-solid fa-paper-plane" v-on:click="chatSend"/>
                        </div>
                    </form>
                    <div class="absolute left-0 bottom-24 w-full select-none animate-bounce" v-if="isNewChat">
                        <span class="px-2 py-0.5 bg-gray-700 rounded-2xl bg-opacity-50 cursor-pointer"
                              @click="scrollToBottom"><font-awesome-icon icon="chevron-down"/> 새로운 채팅이 있습니다.</span>
                    </div>
                </div>
                <div class="participant-list flex flex-col overflow-x-hidden overflow-y-auto h-[calc(100%-3rem)] pr-3"
                     v-show="rightCurrentTab === 'participant'">
                    <div v-for="participant in participants" :key="participant.seq">
                        <div
                            @click="participantTabSetting.context.currentClickUser = participant; participantUserClick($event)"
                            :class="{'cursor-pointer' : user.seq !== participant.seq}"
                            class="flex items-center justify-between py-3 border-b border-b-gray-400">
                            <div class="flex items-center">
                                <div
                                    :class="{'border-2' : participantTabSetting.context.currentClickUser != null && participantTabSetting.context.currentClickUser.seq === participant.seq,
                                         'border-yellow-400' : participantTabSetting.context.currentClickUser != null && participantTabSetting.context.currentClickUser.seq === participant.seq}"
                                    class="w-10 h-10 rounded-3xl overflow-hidden mr-2 border border-gray-500 relative hover:border-2 hover:border-yellow-400">
                                    <img class="w-full h-full object-cover bg-white"
                                         :src="`${$env.protocol}${$env.serverIP}:${$env.port}${participant.photo}`"
                                         alt="프로필 사진" v-if="participant.photo">
                                    <span class="w-full h-full flex justify-center items-center font-bold text-lg bg-green-700 text-white"
                                          v-if="!participant.photo">{{ participant.nickname[0] }}</span>
                                </div>
                                <span>
                                    {{ participant.nickname }}
                                    <b :id="`participant-nickname-${participant.seq}`"
                                        :class="{'text-red-500': participant.status === 'leave', 'text-green-500': participant.status === 'enter'}"
                                        class="ml-1 font-bold">
                                        [{{participant.status === 'enter' ? '접속중' : '자리비움'}}]
                                    </b>
                                </span>
                            </div>
                            <font-awesome-icon
                                v-if="roomInfo.ownerSeq == participant.seq"
                                class="text-2xl text-yellow-300 mr-2"
                                icon="fa-solid fa-crown"/>
                            <button v-else-if="roomInfo.ownerSeq === user.seq"
                                    type="button"
                                    class="flex items-center px-3 py-1 bg-red-500 rounded text-sm hover:bg-red-400 ml-2"
                                    @click.stop="kick(participant.seq, participant.nickname)">
                                <font-awesome-icon class="text-sm mr-2" icon="arrow-right-from-bracket"/>
                                추방하기
                            </button>
                        </div>
                    </div>
                </div>
                <div class="participant-list flex flex-col overflow-x-hidden overflow-y-auto h-[calc(100%-3rem)] pr-3"
                     v-if="rightCurrentTab === 'friend'">
                    <div v-for="fr in friend.list" :key="fr.seq">
                        <div class="flex items-center justify-between py-3 border-b border-b-gray-400">
                            <div class="flex items-center">
                                <div class="w-10 h-10 rounded-3xl overflow-hidden mr-2 border border-gray-500 relative hover:border-2 hover:border-yellow-400">
                                    <img class="w-full h-full object-cover bg-white"
                                         :src="`${$env.protocol}${$env.serverIP}:${$env.port}${fr.photo}`"
                                         alt="프로필 사진" v-if="fr.photo">
                                    <span class="w-full h-full flex justify-center items-center font-bold text-lg bg-green-700 text-white"
                                          v-if="!fr.photo">{{ fr.user_nm[0] }}</span>
                                </div>
                                <span>{{ fr.user_nm }}</span>
                            </div>
                            <div v-if="fr.isInviteLoading" role="status">
                                <svg aria-hidden="true" class="w-8 h-8 text-gray-200 animate-spin dark:text-gray-600 fill-blue-600" viewBox="0 0 100 101" fill="none" xmlns="http://www.w3.org/2000/svg">
                                    <path d="M100 50.5908C100 78.2051 77.6142 100.591 50 100.591C22.3858 100.591 0 78.2051 0 50.5908C0 22.9766 22.3858 0.59082 50 0.59082C77.6142 0.59082 100 22.9766 100 50.5908ZM9.08144 50.5908C9.08144 73.1895 27.4013 91.5094 50 91.5094C72.5987 91.5094 90.9186 73.1895 90.9186 50.5908C90.9186 27.9921 72.5987 9.67226 50 9.67226C27.4013 9.67226 9.08144 27.9921 9.08144 50.5908Z" fill="currentColor"/>
                                    <path d="M93.9676 39.0409C96.393 38.4038 97.8624 35.9116 97.0079 33.5539C95.2932 28.8227 92.871 24.3692 89.8167 20.348C85.8452 15.1192 80.8826 10.7238 75.2124 7.41289C69.5422 4.10194 63.2754 1.94025 56.7698 1.05124C51.7666 0.367541 46.6976 0.446843 41.7345 1.27873C39.2613 1.69328 37.813 4.19778 38.4501 6.62326C39.0873 9.04874 41.5694 10.4717 44.0505 10.1071C47.8511 9.54855 51.7191 9.52689 55.5402 10.0491C60.8642 10.7766 65.9928 12.5457 70.6331 15.2552C75.2735 17.9648 79.3347 21.5619 82.5849 25.841C84.9175 28.9121 86.7997 32.2913 88.1811 35.8758C89.083 38.2158 91.5421 39.6781 93.9676 39.0409Z" fill="currentFill"/>
                                </svg>
                                <span class="sr-only">Loading...</span>
                            </div>

                            <button v-if="fr.isCurrentRoom !== true && fr.isInviteLoading !== true"
                                    @click="inviteRoom(fr)"
                                    class="px-2 py-1 bg-blue-400 rounded hover:bg-blue-300">
                                <font-awesome-icon class="mirror" icon="fa-comment-medical"></font-awesome-icon>
                                <span class="ml-2">초대하기</span>
                            </button>
                        </div>
                    </div>
                </div>
                <div class="setting flex flex-col overflow-x-hidden overflow-y-auto h-[calc(100%-3rem)] pr-3"
                     v-if="rightCurrentTab === 'setting'">
                    <div class="absolute bg-white bg-opacity-40 w-full left-0 top-0 h-full p-5">
                        <div class="h-5/6">
                            <div class="flex items-center content-center text-center mb-2">
                                <span class="flex justify-end pr-3 font-bold w-3/6">채팅 알림 음 : </span>
                                <div class="flex w-3/6">
                                    <ToggleSwitch v-model="refSetting.isChatSound" :isLocked="false"
                                                  on-str="활성화"
                                                  off-str="비 활성화"
                                                  on-text-color="text-lime-200"
                                                  off-text-color="text-rose-600"></ToggleSwitch>
                                </div>
                            </div>
                            <div class="flex items-center content-center text-center mb-2">
                                <span class="flex justify-end pr-3 font-bold w-3/6">백그라운드 채팅 알림 음 : </span>
                                <div class="flex w-3/6">
                                    <ToggleSwitch v-model="refSetting.isBackgroundChatSound" :isLocked="false"
                                                  on-str="활성화"
                                                  off-str="비 활성화"
                                                  on-text-color="text-lime-200"
                                                  off-text-color="text-rose-600"></ToggleSwitch>
                                </div>
                            </div>
                        </div>
                        <div class="h-1/6 flex justify-end">
                            <div class="flex justify-end items-center content-center">
                                  <span class="flex justify-end pr-3 font-bold w-full">
                                        <button type="button"
                                                v-if="user.seq === roomInfo.ownerSeq"
                                                class="flex items-center px-5 py-2 bg-red-500 rounded text-sm hover:bg-red-400 ml-2"
                                                @click="deleteRoom">
                                          <font-awesome-icon class="text-xl mr-2"
                                                             icon="fa-solid fa-trash-can"></font-awesome-icon>
                                          미팅 룸 삭제하기
                                        </button>
                                        <button type="button" v-else
                                              class="flex items-center px-5 py-2 bg-red-500 rounded text-sm hover:bg-red-400 ml-2"
                                              @click="outRoom">
                                          <font-awesome-icon class="text-xl mr-2" icon="arrow-right-from-bracket"></font-awesome-icon>
                                          나가기
                                        </button>
                                  </span>
                            </div>
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
                            :class="{'currentTab': rightCurrentTab==='friend'}" @click="friendTab">
                            <font-awesome-icon icon="fa-regular fa-face-smile"/>
                            <span class="text-sm px-2 slide-in hidden" v-if="rightCurrentTab === 'friend'">친구</span>
                        </li>
                        <li class="flex justify-center px-3 py-1 cursor-pointer"
                            :class="{'currentTab': rightCurrentTab==='setting'}" @click="rightCurrentTab='setting'">
                            <font-awesome-icon icon="fa-ellipsis"/>
                            <span class="text-sm px-2 slide-in hidden" v-if="rightCurrentTab === 'setting'">설정</span>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
        <context-menu
                v-model:show="participantTabSetting.context.isShow"
                :options="participantTabSetting.context"
                @close="participantTabSetting.context.currentClickUser = null"
        >
            <context-menu-item
                    :label="participantTabSetting.context.currentClickUserIsFriend ? '친구 삭제' : '친구 추가'"
                    @click="participantTabSetting.context.currentClickUserIsFriend ? deleteFriend() : addFriend()"
                    class="cursor-pointer">
                <template #icon>
                    <font-awesome-icon v-if="!participantTabSetting.context.currentClickUserIsFriend" class="fa-md font-bold text-gray-600" icon="fa-regular fa-smile"></font-awesome-icon>
                    <font-awesome-icon v-if="participantTabSetting.context.currentClickUserIsFriend" class="fa-md font-bold text-gray-600" icon="fa-regular fa-face-sad-tear"></font-awesome-icon>
                </template>
            </context-menu-item>
            <context-menu-separator/>
            <context-menu-item label="닫기" class="cursor-pointer">
                <template #icon>
                    <font-awesome-icon class="fa-md font-bold text-gray-600"
                                       icon="xmark"></font-awesome-icon>
                </template>
            </context-menu-item>
        </context-menu>
        <PopupWindow
            class="text-black"
            :show="todo.isShow != null"
            :title="`할 일 ${todo.isShow ? todo[todo.isShow].title : ''}`"
            :widthClass="'w-[50%]'"
            :heightClass="'h-[40rem]'"
            :alignClass="'justify-start'"
        >
            <form ref="todoForm" @submit.prevent="addTodo">
                <div class="flex flex-col items-center p-5 w-full h-full">
                    <div class="flex w-full mb-3">
                        <div :class="{'w-[70%]': roomInfo.ownerSeq === user.seq, 'w-full': roomInfo.ownerSeq !== user.seq}"
                            class="flex flex-col items-start">
                            <span class="text-lg">제목<b class="text-red-500">*</b></span>
                            <input
                                maxlength="30"
                                v-model="todo.value.title"
                                placeholder="제목을 입력해주세요."
                                class="rounded text-black px-3 py-1.5 text-xl border border-gray-300 w-full "
                                type="text">
                        </div>
                        <div v-if="roomInfo.ownerSeq === user.seq" class="flex flex-col items-start w-[30%] ml-2 relative">
                            <span class="text-lg">할 일 부여</span>
                            <Multiselect
                                v-model="todo.receiveSeq"
                                track-by="seq"
                                label="nickname"
                                placeholder="대상을 선택해주세요."
                                tag-placeholder="대상을 선택해주세요."
                                :select-label="''"
                                :options="participants"
                                @select="isSelfSelect"
                            />
                            <button type="button" @click="todo.receiveSeq = {}" class="absolute bottom-1 right-1.5 text-red-500">
                              <font-awesome-icon class="p-1 bg-white" icon="xmark"/>
                            </button>
                        </div>
                    </div>
                    <p class="flex w-full h-[25rem] mb-3 rounded">
                        <writeEditor ref="todoContent" class="w-full max-h-[25rem]"
                                     v-model="todo.value.content"/>
                    </p>

                    <div class="flex justify-between text-white w-full">
                        <div>
                            <button class="px-7 py-3 bg-blue-600 rounded text-sm hover:bg-blue-500 mr-3"
                                    type="button"
                                    @click="todo[todo.isShow].clickFn">
                                <span> {{ todo.isShow ? todo[todo.isShow].title : '' }} </span>
                            </button>
                            <button class="px-7 py-3 bg-gray-500 rounded text-sm hover:bg-gray-400"
                                    type="button"
                                    @click="cancelTodo">취소
                            </button>
                        </div>
                    </div>

                </div>
            </form>
        </PopupWindow>
    </div>
</template>

<script>
import {useUserStore} from "@/stores/user.js";
import {getCurrentInstance, onBeforeUnmount, onMounted, ref, watch} from "vue";
import {FontAwesomeIcon} from "@fortawesome/vue-fontawesome";
import {useRoomStore} from "@/stores/room.js";
import ToggleSwitch from "@/components/common/ToggleSwitch.vue";
import {ContextMenu, ContextMenuItem, ContextMenuSeparator} from "@imengyu/vue3-context-menu";
import {router} from "@/router/index.js";
import writeEditor from "@/components/article/WriteEditor.vue";
import PopupWindow from "@/components/common/PopupWindow.vue";
import Multiselect from "vue-multiselect";
import {useTodoStore} from "@/stores/todo.js";
import EmojiPicker from "vue3-emoji-picker";
import VueDragResize from 'vue-drag-resize/src/components/vue-drag-resize.vue';

export default {
    name: "MeetRoomView",
    components: {
        VueDragResize, EmojiPicker, Multiselect, PopupWindow, writeEditor,
        ToggleSwitch, FontAwesomeIcon, ContextMenuSeparator, ContextMenuItem, ContextMenu},
    data() {
        return {
            isLeftSlide: true,
            todo: {
                isShow: null,
                isLoading: false,
                value: {
                    title: null,
                    content: null,
                },
                add: {
                    title: '등록',
                    clickFn: this.addTodo,
                },
                update: {
                    title: '수정',
                    clickFn: this.updateTodo,
                    seq: null,
                },
                receiveSeq: {},
            },
            isRightSlide: true,
            rightCurrentTab: 'chat',
            isChatEmoji: false,
            chatMsg: '',
            characterRandomNum1: 1,
            characterRandomNum2: 1,
            setting: {
                isChatSound: true,
            },
            participantTabSetting: {
                context: {
                    isShow: false,
                    currentClickUser: null,
                    currentClickUserIsFriend: false,
                    zIndex: 10,
                    minWidth: 200,
                    theme: 'mac',
                    x: 100,
                    y: 300,
                }
            },
            friend: {
                list: [],
            }
        }
    },
    setup() {

        const userStore = useUserStore();
        const roomStore = useRoomStore();
        const todoStore = useTodoStore();
        const user = userStore.userInfo;
        const roomInfo = JSON.parse(history.state.roomInfo);
        const socket = new WebSocket('ws://' + import.meta.env.VITE_SERVER_IP + ':' + import.meta.env.VITE_PORT + '/gg');
        const instance = getCurrentInstance();
        let chats = ref([]);
        let participants = ref([]);

        let isNewChat = ref(false);
        let chatLogElement = ref(null);
        let mainWindowElement = ref(null);

        let todos = ref([]);

        const refSetting = ref({
            isChatSound: true,
            isBackgroundChatSound: true,
        });

        const getRoomParticipants = async () => {
            try {
                await roomStore.getRoomParticipants(roomInfo.seq);
                if (roomStore?.dataResponse.status === 200) {
                    participants.value = participants.value.filter(participant => {
                        return roomStore.currentRoomParticipants.some(data => data.seq === participant.seq);
                    });

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
                                    participant.status = data.status;
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

        const getTodos = async () => {
            instance.data.isLoading = true;
            todoStore.getTodos(roomInfo.seq)
            .then(data => {
                let result = [];
                let currentDate = null;
                for (let todo of data) {
                    let todoDate = new Date(todo.created_at);
                    if (!currentDate || currentDate.getDate() !== todoDate.getDate()) {
                        currentDate = todoDate;
                        result.push({ date: `${currentDate.getFullYear()}년 ${currentDate.getMonth() + 1}월 ${currentDate.getDate()}일` });
                    }

                    result.push(todo);
                }
                todos.value = result;
                instance.data.isLoading = false;
            }).catch(() => {
                // instance.appContext.config.globalProperties.utils.notify.error("목록 가져오는 중 오류가 발생했습니다.", "오류!");
                instance.data.isLoading = false;
            })
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

            getTodos();
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
                if (data.type2 === 'chat' || data.type2 === 'enter' || data.type2 === 'leave' || data.type2 === 'out') {
                    data.oriDatetime = data.datetime;
                    if (data.datetime) {
                        var dateObj = new Date(data.datetime);
                        let date = new Date(dateObj);
                        let options = {hour: 'numeric', minute: 'numeric', hour12: true};
                        data.datetime = date.toLocaleTimeString('ko-KR', options);
                    }
                    if (data.type2 === 'enter' || data.type2 === 'leave' || data.type2 === 'out') {
                        const seq = "seq" + data.senderSeq;
                        if (data.type2 === 'leave') {
                            participants.value.forEach(participant => {
                                if(data.senderSeq == participant.seq){
                                    participant.status = data.type2;
                                }
                            })
                        } else if(data.type2 === 'out'){
                            participants.value = participants.value.filter(participant => participant.seq != data.senderSeq);
                            delete (participantsCharacter[seq]);
                        } else if(data.type2 === 'enter'){
                            participants.value.forEach(participant => {
                              if(data.senderSeq == participant.seq){
                                participant.status = data.type2;
                              }
                            })

                            const chatElement = document.querySelector("#character-chat-" + data.senderSeq);
                            participantsCharacter[seq] = {
                                text: data.content,
                                timeoutFN: setTimeout(function () {
                                    chatElement.classList.add("hidden");
                                }, participantsCharacter.showTextMs)
                            };
                            clearTimeout(participantsCharacter[seq].timeoutFN);
                        }
                        getRoomParticipants();
                    }
                    if (data.type2 === 'chat') {
                        const chatElement = document.querySelector("#character-chat-" + data.senderSeq);
                        const chatElementText = chatElement.querySelector("span");
                        const seq = "seq" + data.senderSeq;
                        const isChatSound = refSetting.value.isChatSound;
                        const isBackgroundChatSound = refSetting.value.isBackgroundChatSound;
                        if (isChatSound && data.senderSeq != user.seq) {
                            if (!document.hidden || (isBackgroundChatSound && document.hidden)) {
                                instance.appContext.config.globalProperties.utils.chatSound.play();
                            }
                        }

                        // b on

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
                    }
                    chats.value.push(data);
                }

                if (data.type2 === 'kick'){
                    if(data.receiveSeq == user.seq){
                        router.push({
                            name: 'MainView',
                        });
                        instance.appContext.config.globalProperties.utils.msg(`[${roomInfo.title}] 방에서 추방 당하였습니다.`);
                    }else{
                        getRoomParticipants();
                        instance.appContext.config.globalProperties.utils.notify.success(`${data.receiveNickname}님이 추방 당하였습니다.`, '추방 안내');
                    }
                }

                if (data.type2 === 'updateUserMsg') {
                    let participant = participants.value.find(p => p.seq === data.userInfo.seq);
                    if (participant) {
                        let chkPhoto = false;
                        for (let key in data.userInfo) {
                            if (participant[key] !== undefined && data.userInfo[key] !== undefined) {
                                if (key === 'photo') chkPhoto = true;
                                participant[key] = data.userInfo[key];
                            }
                        }
                        if (!chkPhoto)
                            participant['photo'] = null;
                    }
                }

                if (data.type2 === 'delete') {
                    this.chatRooms = this.chatRooms.filter(room => room.id !== data.chatRoomId);
                }

                if (data.type2 === 'todoUpdMsg'){
                    getTodos();
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

        const inviteRoom = friend => {
            friend.isInviteLoading = true;
            const msg = {
                type1: 'room',
                type2: 'invite',
                roomId: roomInfo.seq,
                senderSeq: user.seq,
                receiveSeq: friend.friend_seq,
                content: user.nickname + '님이 "' + roomInfo.title + '" 미팅 룸에 초대하였습니다.',
            }
            socket.send(JSON.stringify(msg));
            setTimeout(() => {
                friend.isInviteLoading = false;
            }, 10000)
        }

        const randomCharacterLocation = () => {
            const minus = 200;
            const box = mainWindowElement.value;
            const range = {
                x_st: 0,
                x_ed: box.clientWidth-100,
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
            randomCharacterLocation,
            refSetting,
            inviteRoom,
            todos,
            getTodos
        };
    },
    created() {
        this.characterRandomNum1 = this.randomNumber(1, 4);
        this.characterRandomNum2 = this.randomNumber(1, 2);
    },
    methods: {
        deleteRoom() {
            this.$swal.fire({
                title: "미팅 룸을 삭제하시겠습니까?",
                showCancelButton: true,
                confirmButtonText: "확인",
                cancelButtonText: "취소",
                icon: "warning",
            }).then(async (result) => {
                if (result.isConfirmed) {
                    const roomStore = useRoomStore();
                    try {
                        await roomStore.deleteRoom(this.roomInfo.seq);
                        if (roomStore?.dataResponse.status === 200) {
                            this.utils.notify.success("미팅 룸이 삭제되었습니다.", "삭제 완료!");
                            this.$router.push({
                                name: 'MainView',
                            });
                        } else {
                            this.utils.msgError(this.dataResponse.data.custom ? this.dataResponse.data.message : this.utils.normalErrorMsg);
                        }
                    } catch (error) {
                        this.utils.msgError((error?.response?.data) || this.utils.normalErrorMsg);
                    }
                }
            }).catch((error) => {
                this.utils.msgError((error?.response?.data) || this.utils.normalErrorMsg);
            });
        },
        chatSend() {
            if (this.chatMsg !== '') {
                this.chatSocketSend('chat', this.chatMsg);
                this.chatMsg = '';
                this.isChatEmoji = false;
            }
        },
        participantTab() {
            this.rightCurrentTab = 'participant';
            this.getRoomParticipants();
        },
        async friendTab(){
            this.rightCurrentTab = 'friend';
            const userStore = useUserStore();
            await userStore.getFriends();
            this.friend.list = userStore.friendList;

            this.friend.list.forEach(fr => {
                const containsKeyValuePair = this.participants.some(
                        obj => obj['seq'] === fr.friend_seq
                );
                if(containsKeyValuePair) fr.isCurrentRoom = true;
            })
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
            const images = import.meta.glob('/src/assets/image/character/*.png');
            console.log(new URL(`/src/assets/image/character/c${roomCharacter}.png`, import.meta.url).href);
            return new URL(`/src/assets/image/character/c${roomCharacter}.png`, import.meta.url).href;
        },
        randomNumber: function (st, ed) {
            return Math.floor((Math.random() * (ed - st + 1)) + st);
        },
        async participantUserClick(e){
            if(this.participantTabSetting.context.currentClickUser){
                if(this.user.seq === this.participantTabSetting.context.currentClickUser.seq){
                    this.participantTabSetting.context.currentClickUser = null;
                    return;
                }

                const userStore = useUserStore();

                try {
                    await userStore.getFriends({
                        friend_seq : this.participantTabSetting.context.currentClickUser.seq
                    });
                } catch (error) {
                    console.error(error);
                    this.utils.notify.error((error?.response?.data) || this.utils.normalErrorMsg);
                }

                this.participantTabSetting.context.currentClickUserIsFriend = userStore.friendList.length != 0;
                this.participantTabSetting.context.x = e.clientX;
                this.participantTabSetting.context.y = e.clientY;
                this.participantTabSetting.context.isShow = true;
            }
        },
        async addFriend(){
            try {
                const userStore = useUserStore();

                const friend = {
                    friend_seq : this.participantTabSetting.context.currentClickUser.seq
                }
                await userStore.addFriend(friend);
                if (userStore?.dataResponse.status === 200) {
                    this.utils.notify.success("친구 추가되었습니다.", "친구추가 완료!");
                } else {
                    this.utils.msgError(userStore.dataResponse.custom ? this.dataResponse.data.message : this.utils.normalErrorMsg);
                }
            } catch (error) {
                console.log(error);
                this.utils.msgError((error?.response?.data) || this.utils.normalErrorMsg);
            }
        },
        async deleteFriend(){
            try {
                const userStore = useUserStore();

                const friend = {
                    friend_seq : this.participantTabSetting.context.currentClickUser.seq
                }
                await userStore.deleteFriend(friend);
                if (userStore?.dataResponse.status === 200) {
                    this.utils.notify.success("친구 삭제되었습니다.", "친구삭제 완료!");
                } else {
                    this.utils.msgError(userStore.dataResponse.custom ? this.dataResponse.data.message : this.utils.normalErrorMsg);
                }
            } catch (error) {
                console.log(error);
                this.utils.msgError((error?.response?.data) || this.utils.normalErrorMsg);
            }
        },
        kick(userSeq, nickname){
            this.$swal.fire({
              title: '추방하기',
              html: `<b>[${nickname?nickname:''}]</b>님을 추방 하시겠습니까?`,
              icon: "warning",
              showCancelButton: true,
              confirmButtonText: "삭제",
              cancelButtonText: "취소"
            }).then(async (result) => {
                if (result.isConfirmed) {
                  const roomStore = useRoomStore();
                  roomStore.kick({
                    userSeq: userSeq,
                    seq: this.roomInfo.seq
                  })
                }
            });
        },
        async outRoom(){
            const roomStore = useRoomStore();
            roomStore.outRoom()
            .then(() => {
                this.chatSocketSend('out');
                this.$router.push({
                    name: 'MainView',
                });
            })
        },
        toggleTodoContent(event, content){
          if(content && content.replaceAll(' ', '') !== '<p></p>'){
            const item = event.target.closest(".item").querySelector(".content");
            if(!item.classList.contains('open')) {
              item.classList.add('open')
              item.style.height = "auto"

              const height = item.clientHeight + "px"

              item.style.height = "0px"

              setTimeout(() => {
                item.style.height = height
              }, 0)
            } else {
              item.style.height = "0px"
              item.addEventListener('transitionend', () => {
                item.classList.remove('open')
              }, {once: true})
            }
          }
        },
        isSelfSelect(selectItem){
            if(selectItem.seq === this.user.seq){
                this.utils.notify.error("자신을 선택 할 수 없습니다.", "오류!");
                this.todo.receiveSeq = {};
            }
        },
        getImageUrl(imgUrl) {
            const images = import.meta.glob('/src/assets/background/*.jpg');
            return new URL(`/src/assets/background/room_bg_${imgUrl}.jpg`, import.meta.url).href;
        },
        cancelTodo(){
          this.todo.receiveSeq = {};
          this.todo.update.seq = null;
          this.todo.isShow = null;
          this.todo.value =  {};
        },
        addTodo(){
            if(!this.todo.value.title){
              this.utils.msgError("제목을 입력해주세요.");
              return;
            }
            const todoStore = useTodoStore();
            todoStore.addTodo({
                title: this.todo.value.title,
                content: this.todo.value.content,
                room_seq: this.roomInfo.seq,
                receive_seq: this.todo.receiveSeq.seq,
            }).then(result => {
                if(result) {
                    this.utils.notify.success("할 일을 등록했습니다.", "등록 완료!");
                    this.cancelTodo();
                }else
                    this.utils.notify.error(this.utils.normalErrorMsg, "오류!");
            }).catch(() => {
                this.utils.notify.error(this.utils.normalErrorMsg, "오류!");
            })
        },
        completeTodo(seq, complete){
            const todoStore = useTodoStore();
            todoStore.completeTodo({
                seq: seq,
                is_complete: complete
            }).then(result => {
                if(!result)
                    this.utils.notify.error(this.utils.normalErrorMsg, "오류!");
            }).catch(() => {
                this.utils.notify.error(this.utils.normalErrorMsg, "오류!");
            })
        },
        deleteTodo(seq, title){
            this.$swal.fire({
                title: '할일 삭제',
                html: `<b>[${title?title:''}]</b> 할 일을 삭제 하시겠습니까?`,
                icon: "warning",
                showCancelButton: true,
                confirmButtonText: "삭제",
                cancelButtonText: "취소"
            }).then(async (result) => {
                if (result.isConfirmed) {
                  const todoStore = useTodoStore();
                  todoStore.deleteTodo({
                    seq: seq
                  }).then(result => {
                    if(result){
                      this.utils.notify.success("삭제 되었습니다.", "삭제 완료!");
                    }else{
                      this.utils.notify.error(this.utils.normalErrorMsg, "오류!");
                    }
                  }).catch(() => {
                    this.utils.notify.error(this.utils.normalErrorMsg, "오류!");
                  });
                }
            }).catch(() => {
              this.utils.notify.error(this.utils.normalErrorMsg, "오류!");
            });
        },
        clickUpdateTodo(todo){
            this.todo.receiveSeq = {
              seq: todo.receive_seq,
              nickname: todo.receive_nickname,
            }
            this.todo.update.seq = todo.seq;
            this.todo.value.title = todo.title;
            this.todo.value.content = todo.content;
            this.todo.isShow = 'update';
        },
        updateTodo(){
            if(!this.todo.value.title){
              this.utils.msgError("제목을 입력해주세요.");
              return;
            }
            const todoStore = useTodoStore();
            todoStore.updateTodo({
              seq: this.todo.update.seq,
              title: this.todo.value.title,
              content: this.todo.value.content,
              receive_seq: this.todo.receiveSeq.seq,
            }).then(result => {
              if(result) {
                this.utils.notify.success("수정되었습니다.", "수정 완료!");
                this.cancelTodo();
              }else
                this.utils.notify.error(this.utils.normalErrorMsg, "오류!");
            }).catch(() => {
              this.utils.notify.error(this.utils.normalErrorMsg, "오류!");
            })
        },
        onSelectEmoji(emoji){
            this.chatMsg += emoji.i;
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
    bottom: 2px;
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
    bottom: 2px;
    left: -60px;
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

@-moz-keyframes move-astronaut1 {
    100% {
        -moz-transform: translate(-160px, -160px);
    }
}

@-webkit-keyframes move-astronaut1 {
    100% {
        -webkit-transform: translate(-160px, -160px);
    }
}

@keyframes move-astronaut1 {
    100% {
        -webkit-transform: translate(-160px, -160px);
        transform: translate(-160px, -160px);
    }
}

@-moz-keyframes move-astronaut2 {
    100% {
        -moz-transform: translate(160px, 160px);
    }
}

@-webkit-keyframes move-astronaut2 {
    100% {
        -webkit-transform: translate(160px, 160px);
    }
}

@keyframes move-astronaut2 {
    100% {
        -webkit-transform: translate(160px, 160px);
        transform: translate(160px, 160px);
    }
}

@-moz-keyframes move-astronaut3 {
    100% {
        -moz-transform: translate(160px, -160px);
    }
}

@-webkit-keyframes move-astronaut3 {
    100% {
        -webkit-transform: translate(160px, -160px);
    }
}

@keyframes move-astronaut3 {
    100% {
        -webkit-transform: translate(160px, -160px);
        transform: translate(160px, -160px);
    }
}

@-moz-keyframes move-astronaut4 {
    100% {
        -moz-transform: translate(-160px, 160px);
    }
}

@-webkit-keyframes move-astronaut4 {
    100% {
        -webkit-transform: translate(-160px, 160px);
    }
}

@keyframes move-astronaut4 {
    100% {
        -webkit-transform: translate(-160px, 160px);
        transform: translate(-160px, 160px);
    }
}

@keyframes float {
    0% {
        transform: translate(0, 0);
    }
    25% {
        transform: translate(50px, 100px);
    }
    50% {
        transform: translate(75px, 125px);
    }
    75% {
        transform: translate(50px, 100px);
    }
    100% {
        transform: translate(0, 0);
    }
}

@-moz-keyframes rotate-astronaut1 {
    100% {
        -moz-transform: rotate(-720deg);
    }
}

@-webkit-keyframes rotate-astronaut1 {
    100% {
        -webkit-transform: rotate(-720deg);
    }
}

@keyframes rotate-astronaut1 {
    100% {
        -webkit-transform: rotate(-720deg);
        transform: rotate(-720deg);
    }
}

@-moz-keyframes rotate-astronaut2 {
    100% {
        -moz-transform: rotate(720deg);
    }
}

@-webkit-keyframes rotate-astronaut2 {
    100% {
        -webkit-transform: rotate(720deg);
    }
}

@keyframes rotate-astronaut2 {
    100% {
        -webkit-transform: rotate(720deg);
        transform: rotate(720deg);
    }
}

.animation-move-astronaut1 {
    animation: move-astronaut1 50s infinite linear both alternate;
}

.animation-move-astronaut2 {
    animation: move-astronaut2 50s infinite linear both alternate;
}

.animation-move-astronaut3 {
    animation: move-astronaut3 50s infinite linear both alternate;
}

.animation-move-astronaut4 {
    animation: move-astronaut4 50s infinite linear both alternate;
}

.animation-rotate-astronaut1 {
    animation: rotate-astronaut1 200s infinite linear both alternate;
    will-change: transform;
}

.animation-rotate-astronaut2 {
    animation: rotate-astronaut2 200s infinite linear both alternate;
    will-change: transform;
}

.left-window .item .content:not(.open) {
    display: none;
}
.left-window .item .content {
    transition:height ease-out .2s 0s;
}

.left-window .items::-webkit-scrollbar {
    width: 6px;
}

.left-window .items::-webkit-scrollbar-thumb {
    background: #f1b4bb; /* 스크롤바 색상 */
    border-radius: 10px; /* 스크롤바 둥근 테두리 */
}

.left-window .items::-webkit-scrollbar-track {
    background: rgba(220, 20, 60, .1); /*스크롤바 뒷 배경 색상*/
}

</style>
