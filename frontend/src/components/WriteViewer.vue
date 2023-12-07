<template>
  <div id="main-view" class="flex flex-col items-center bg-main_background bg-cover text-black h-[93%]">
    <div class="flex flex-col items-center w-full h-full bg-gray-300 bg-opacity-30">
      <div class="flex flex-col items-center bg-white w-[70%] h-full overflow-y-auto rounded">
        <div class="flex w-[85%] h-full inset-0">
          <div class="flex h-full flex-col w-full">
            <div class="flex items-center relative w-full h-[2rem] mt-10">
              <div class="absolute left-0 top-4 border-b border-gray-300 w-full"></div>
              <div class="bg-white px-4 ml-7 text-sm font-thin z-10">
                <span class="text-gray-600">커뮤니티 / </span>
                <span class="text-blue-500">Q&A</span>
              </div>
            </div>
            <div class="flex text-start justify-start text-xl w-full font-bold mt-[2rem] mb-5">
              {{ article.title }}
            </div>
            <div class="flex items-center text-gray-500 pb-5 border-b border-gray-300 relative">
              <div class="flex w-full h-[3rem]">
                <div class="flex w-1/2 items-end">
                  <div class="w-10 h-10 rounded-full overflow-hidden mr-2">
                    <img class="w-full h-full object-cover bg-white"
                         :src="`${$env.protocol}${$env.serverIP}:${$env.port}${article.photo}`"
                         alt="프로필 사진" v-if="article.photo">
                    <div v-if="!article.photo"
                         class=" rounded-3xl w-full h-full flex justify-center items-center font-bold text-lg shadow-2xl text-white bg-green-700">
                      <span>{{ article.nickname && article.nickname[0] }}</span>
                    </div>
                  </div>
                  <div class="flex flex-col justify-start items-start">
                    <span class="text-sm">{{ article.nickname }}</span>
                    <div class="flex items-center text-xs text-gray-400">
                      <div class="flex items-center">
                        <span class="flex items-center mr-1">
                          <font-awesome-icon icon="fa-clock"/>
                        </span>
                        <span class="mr-2">{{ this.utils.timeAgoStr(article.created_at) }}</span>
                        <div class="flex mr-2">
                          <span class="mr-1">
                            <font-awesome-icon icon="fa-regular fa-eye"/>
                          </span>
                          <span>{{ utils.numberCommas(article.view_count) }}</span>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="flex w-1/2 content-end justify-end items-end">
                  <div class="flex mr-2 h-[1.5rem]">
                    <span class="mr-1">
                        <font-awesome-icon icon="fa-regular fa-comment-dots"/>
                    </span>
                    <span>{{ utils.numberCommas(article.comment_count) }}</span>
                  </div>
                  <div class="flex">
                    <span class="mr-1 h-[1.5rem]">
                        <font-awesome-icon icon="fa-regular fa-heart"/>
                    </span>
                    <span>{{ utils.numberCommas(article.likes) }}</span>
                  </div>

                  <div v-if=" article.user_seq == user.seq " class="flex items-center">
                    <font-awesome-icon
                        @click="clickArticleSettingBtn($event)"
                        class="ml-4 cursor-pointer h-[1.5rem] hover:text-blue-400"
                        icon="ellipsis"></font-awesome-icon>
                    <context-menu
                        v-model:show="setting.articleContextOption.isShow"
                        :options="setting.articleContextOption"
                        @close="setting.articleContextOption.isShow = null"
                    >
                      <context-menu-item label="수정" @click="clickArticleEditBtn()" class="cursor-pointer">
                        <template #icon>
                          <font-awesome-icon class="fa-md font-bold text-gray-600"
                                             icon="pen-to-square"></font-awesome-icon>
                        </template>
                      </context-menu-item>
                      <context-menu-item label="삭제" @click="clickArticleDeleteBtn()" class="cursor-pointer">
                        <template #icon>
                          <font-awesome-icon class="fa-md font-bold text-gray-600"
                                             icon="fa-regular fa-trash-can"></font-awesome-icon>
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
                  </div>
                </div>
              </div>
            </div>
            <div v-html="article.content"
                 id="article-content"
                 class="w-full pt-10 pb-5 text-start prose">
            </div>
            <div class="flex items-center justify-center mb-5 h-[3rem]">
              <button
                  @click="clickArticleLikeBtn(true)"
                  class="p-3 border-y border-l border-gray-300 rounded-l-2xl hover:bg-blue-100 h-full">
                <svg width="1.5rem" height="1.5rem" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                  <path d="M12 5.50063L11.4596 6.02073C11.463 6.02421 11.4664 6.02765 11.4698 6.03106L12 5.50063ZM8.96173 18.9109L8.49742 19.4999L8.96173 18.9109ZM15.0383 18.9109L14.574 18.3219L15.0383 18.9109ZM7.00061 16.4209C6.68078 16.1577 6.20813 16.2036 5.94491 16.5234C5.68169 16.8432 5.72758 17.3159 6.04741 17.5791L7.00061 16.4209ZM2.34199 13.4115C2.54074 13.7749 2.99647 13.9084 3.35988 13.7096C3.7233 13.5108 3.85677 13.0551 3.65801 12.6917L2.34199 13.4115ZM13.4698 8.03034C13.7627 8.32318 14.2376 8.32309 14.5304 8.03014C14.8233 7.7372 14.8232 7.26232 14.5302 6.96948L13.4698 8.03034ZM2.75 9.1371C2.75 6.98623 3.96537 5.18252 5.62436 4.42419C7.23607 3.68748 9.40166 3.88258 11.4596 6.02073L12.5404 4.98053C10.0985 2.44352 7.26409 2.02539 5.00076 3.05996C2.78471 4.07292 1.25 6.42503 1.25 9.1371H2.75ZM8.49742 19.4999C9.00965 19.9037 9.55955 20.3343 10.1168 20.6599C10.6739 20.9854 11.3096 21.25 12 21.25V19.75C11.6904 19.75 11.3261 19.6293 10.8736 19.3648C10.4213 19.1005 9.95208 18.7366 9.42605 18.3219L8.49742 19.4999ZM15.5026 19.4999C16.9292 18.3752 18.7528 17.0866 20.1833 15.4758C21.6395 13.8361 22.75 11.8026 22.75 9.1371H21.25C21.25 11.3345 20.3508 13.0282 19.0617 14.4798C17.7469 15.9603 16.0896 17.1271 14.574 18.3219L15.5026 19.4999ZM22.75 9.1371C22.75 6.42503 21.2153 4.07292 18.9992 3.05996C16.7359 2.02539 13.9015 2.44352 11.4596 4.98053L12.5404 6.02073C14.5983 3.88258 16.7639 3.68748 18.3756 4.42419C20.0346 5.18252 21.25 6.98623 21.25 9.1371H22.75ZM14.574 18.3219C14.0479 18.7366 13.5787 19.1005 13.1264 19.3648C12.6739 19.6293 12.3096 19.75 12 19.75V21.25C12.6904 21.25 13.3261 20.9854 13.8832 20.6599C14.4405 20.3343 14.9903 19.9037 15.5026 19.4999L14.574 18.3219ZM9.42605 18.3219C8.63014 17.6945 7.82129 17.0963 7.00061 16.4209L6.04741 17.5791C6.87768 18.2624 7.75472 18.9144 8.49742 19.4999L9.42605 18.3219ZM3.65801 12.6917C3.0968 11.6656 2.75 10.5033 2.75 9.1371H1.25C1.25 10.7746 1.66995 12.1827 2.34199 13.4115L3.65801 12.6917ZM11.4698 6.03106L13.4698 8.03034L14.5302 6.96948L12.5302 4.97021L11.4698 6.03106Z"
                        :fill="isLike === true ? 'blue' : '#1C274C'"/>
                </svg>
              </button>
              <span class="flex items-center px-5 border-y border-gray-300 h-full">{{ utils.numberCommas(article.likes) }}</span>
              <button
                  @click="clickArticleLikeBtn(false)"
                  class="p-3 border-y border-r border-gray-300 rounded-r-2xl hover:bg-red-100 h-full">
                <svg width="1.5rem" height="1.5rem" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                  <path d="M8.96173 18.9109L9.42605 18.3219L8.96173 18.9109ZM12 5.50063L11.4596 6.02073C11.601 6.16763 11.7961 6.25063 12 6.25063C12.2039 6.25063 12.399 6.16763 12.5404 6.02073L12 5.50063ZM15.0383 18.9109L15.5026 19.4999L15.0383 18.9109ZM7.00061 16.4209C6.68078 16.1577 6.20813 16.2036 5.94491 16.5234C5.68169 16.8432 5.72758 17.3159 6.04741 17.5791L7.00061 16.4209ZM2.34199 13.4115C2.54074 13.7749 2.99647 13.9084 3.35988 13.7096C3.7233 13.5108 3.85677 13.0551 3.65801 12.6917L2.34199 13.4115ZM2.75 9.1371C2.75 6.98623 3.96537 5.18252 5.62436 4.42419C7.23607 3.68748 9.40166 3.88258 11.4596 6.02073L12.5404 4.98053C10.0985 2.44352 7.26409 2.02539 5.00076 3.05996C2.78471 4.07292 1.25 6.42503 1.25 9.1371H2.75ZM8.49742 19.4999C9.00965 19.9037 9.55954 20.3343 10.1168 20.6599C10.6739 20.9854 11.3096 21.25 12 21.25V19.75C11.6904 19.75 11.3261 19.6293 10.8736 19.3648C10.4213 19.1005 9.95208 18.7366 9.42605 18.3219L8.49742 19.4999ZM15.5026 19.4999C16.9292 18.3752 18.7528 17.0866 20.1833 15.4758C21.6395 13.8361 22.75 11.8026 22.75 9.1371H21.25C21.25 11.3345 20.3508 13.0282 19.0617 14.4798C17.7469 15.9603 16.0896 17.1271 14.574 18.3219L15.5026 19.4999ZM22.75 9.1371C22.75 6.42503 21.2153 4.07292 18.9992 3.05996C16.7359 2.02539 13.9015 2.44352 11.4596 4.98053L12.5404 6.02073C14.5983 3.88258 16.7639 3.68748 18.3756 4.42419C20.0346 5.18252 21.25 6.98623 21.25 9.1371H22.75ZM14.574 18.3219C14.0479 18.7366 13.5787 19.1005 13.1264 19.3648C12.6739 19.6293 12.3096 19.75 12 19.75V21.25C12.6904 21.25 13.3261 20.9854 13.8832 20.6599C14.4405 20.3343 14.9903 19.9037 15.5026 19.4999L14.574 18.3219ZM9.42605 18.3219C8.63014 17.6945 7.82129 17.0963 7.00061 16.4209L6.04741 17.5791C6.87768 18.2624 7.75472 18.9144 8.49742 19.4999L9.42605 18.3219ZM3.65801 12.6917C3.0968 11.6656 2.75 10.5033 2.75 9.1371H1.25C1.25 10.7746 1.66995 12.1827 2.34199 13.4115L3.65801 12.6917Z"
                        :fill="isLike === false ? 'red' : '#1C274C'"/>
                  <path d="M12 5.50073L10.5 8.5001L14 11.0001L11 14.5001L13 16.5001L12 20.5001"
                        :stroke="isLike === false ? 'red' : '#1C274C'" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </button>
            </div>
            <div class="flex flex-col items-center text-gray-700 border-t border-gray-300 py-5">
              <div class="flex justify-between w-full items-center">
                <span class="self-start h-[2rem]">{{comments ? utils.numberCommas(comments.length) : 0}}개의 댓글이 있습니다.</span>
                <span
                    @click="refreshClick()"
                    class="flex justify-center select-none px-3 py-2 rounded cursor-pointer hover:bg-gray-200 hover:text-blue-400 mr-2 h-[2rem]">
                            <font-awesome-icon
                                :class="{'animate-spin': setting.refreshLoading, 'text-blue-400': setting.refreshLoading}"
                                class="" icon="rotate-right"/>
                </span>
              </div>
              <div class="flex flex-col justify-center items-center border w-full py-3 px-8 mt-5 mb-3 min-h-[15rem] rounded">
                <writeEditor
                    ref="commentEditor"
                    class="border-gray-300 hover:border-gray-400 shadow min-h-[8.5rem] w-full"
                    v-model.trim="comment.value.content"/>
                <div class="self-end">
                  <button
                          @click="clickCommentBtn()"
                          class="text-white px-5 py-2 bg-blue-600 rounded text-sm hover:bg-blue-500 mt-3 mr-3"
                          type="button">
                    <span class="">등록</span>
                  </button>
                </div>
              </div>
              <div class="flex flex-col w-full">

                <div v-for="item in comments" :key="item.seq"
                     :class="{'w-[95%] self-end border-l pl-5 bg-gray-50': item.parent_comment_seq !== null, 'w-full': item.parent_comment_seq === null }"
                     class="flex flex-col py-5 relative">
                  <font-awesome-icon v-if="item.parent_comment_seq !== null"
                                     class="absolute -left-10 top-5 text-xl rotate-180"
                                     icon="reply"/>
                  <div class="flex w-full">
                    <div class="flex w-[80%] h-fit">
                      <div class="w-10 h-10 rounded-full overflow-hidden mr-2">
                        <img class="w-full h-full object-cover bg-white"
                             :src="`${$env.protocol}${$env.serverIP}:${$env.port}${item.photo}`"
                             alt="프로필 사진" v-if="item.photo">
                        <div v-if="!item.photo"
                             class=" rounded-3xl w-full h-full flex justify-center items-center font-bold text-lg shadow-2xl text-white bg-green-700">
                          <span>{{ item.nickname && item.nickname[0] }}</span>
                        </div>
                      </div>
                      <div class="flex flex-col justify-start items-start">
                        <span class="flex text-sm">
                          {{ item.nickname }}
                          <b class="flex items-center ml-1 px-1 bg-blue-400 rounded-2xl text-xs text-white" v-if="item.user_seq === article.user_seq">작성자</b>
                        </span>
                        <div class="flex items-center text-xs text-gray-400">
                          <div class="flex items-center">
                          <span class="flex items-center mr-1">
                            <font-awesome-icon icon="fa-clock"/>
                          </span>
                            <span class="mr-2">{{ this.utils.timeAgoStr(item.created_at) }}</span>
                          </div>
                        </div>
                      </div>
                    </div>
                    <div class="flex items-center justify-center mb-5 w-[20%]">
                      <button
                          @click="clickCommentLikeBtn(true, item)"
                          class="p-3 border-y border-l border-gray-300 rounded-l-2xl hover:bg-blue-100">
                        <svg width="1rem" height="1rem" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                          <path d="M12 5.50063L11.4596 6.02073C11.463 6.02421 11.4664 6.02765 11.4698 6.03106L12 5.50063ZM8.96173 18.9109L8.49742 19.4999L8.96173 18.9109ZM15.0383 18.9109L14.574 18.3219L15.0383 18.9109ZM7.00061 16.4209C6.68078 16.1577 6.20813 16.2036 5.94491 16.5234C5.68169 16.8432 5.72758 17.3159 6.04741 17.5791L7.00061 16.4209ZM2.34199 13.4115C2.54074 13.7749 2.99647 13.9084 3.35988 13.7096C3.7233 13.5108 3.85677 13.0551 3.65801 12.6917L2.34199 13.4115ZM13.4698 8.03034C13.7627 8.32318 14.2376 8.32309 14.5304 8.03014C14.8233 7.7372 14.8232 7.26232 14.5302 6.96948L13.4698 8.03034ZM2.75 9.1371C2.75 6.98623 3.96537 5.18252 5.62436 4.42419C7.23607 3.68748 9.40166 3.88258 11.4596 6.02073L12.5404 4.98053C10.0985 2.44352 7.26409 2.02539 5.00076 3.05996C2.78471 4.07292 1.25 6.42503 1.25 9.1371H2.75ZM8.49742 19.4999C9.00965 19.9037 9.55955 20.3343 10.1168 20.6599C10.6739 20.9854 11.3096 21.25 12 21.25V19.75C11.6904 19.75 11.3261 19.6293 10.8736 19.3648C10.4213 19.1005 9.95208 18.7366 9.42605 18.3219L8.49742 19.4999ZM15.5026 19.4999C16.9292 18.3752 18.7528 17.0866 20.1833 15.4758C21.6395 13.8361 22.75 11.8026 22.75 9.1371H21.25C21.25 11.3345 20.3508 13.0282 19.0617 14.4798C17.7469 15.9603 16.0896 17.1271 14.574 18.3219L15.5026 19.4999ZM22.75 9.1371C22.75 6.42503 21.2153 4.07292 18.9992 3.05996C16.7359 2.02539 13.9015 2.44352 11.4596 4.98053L12.5404 6.02073C14.5983 3.88258 16.7639 3.68748 18.3756 4.42419C20.0346 5.18252 21.25 6.98623 21.25 9.1371H22.75ZM14.574 18.3219C14.0479 18.7366 13.5787 19.1005 13.1264 19.3648C12.6739 19.6293 12.3096 19.75 12 19.75V21.25C12.6904 21.25 13.3261 20.9854 13.8832 20.6599C14.4405 20.3343 14.9903 19.9037 15.5026 19.4999L14.574 18.3219ZM9.42605 18.3219C8.63014 17.6945 7.82129 17.0963 7.00061 16.4209L6.04741 17.5791C6.87768 18.2624 7.75472 18.9144 8.49742 19.4999L9.42605 18.3219ZM3.65801 12.6917C3.0968 11.6656 2.75 10.5033 2.75 9.1371H1.25C1.25 10.7746 1.66995 12.1827 2.34199 13.4115L3.65801 12.6917ZM11.4698 6.03106L13.4698 8.03034L14.5302 6.96948L12.5302 4.97021L11.4698 6.03106Z"
                                :fill="item.is_up === true ? 'blue' : '#1C274C'"/>
                        </svg>
                      </button>
                      <span class="flex items-center px-5 border-y border-gray-300 h-full text-sm">{{ utils.numberCommas(item.likes) }}</span>
                      <button
                          @click="clickCommentLikeBtn(false, item)"
                          class="p-3 border-y border-r border-gray-300 rounded-r-2xl hover:bg-red-100">
                        <svg width="1rem" height="1rem" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                          <path d="M8.96173 18.9109L9.42605 18.3219L8.96173 18.9109ZM12 5.50063L11.4596 6.02073C11.601 6.16763 11.7961 6.25063 12 6.25063C12.2039 6.25063 12.399 6.16763 12.5404 6.02073L12 5.50063ZM15.0383 18.9109L15.5026 19.4999L15.0383 18.9109ZM7.00061 16.4209C6.68078 16.1577 6.20813 16.2036 5.94491 16.5234C5.68169 16.8432 5.72758 17.3159 6.04741 17.5791L7.00061 16.4209ZM2.34199 13.4115C2.54074 13.7749 2.99647 13.9084 3.35988 13.7096C3.7233 13.5108 3.85677 13.0551 3.65801 12.6917L2.34199 13.4115ZM2.75 9.1371C2.75 6.98623 3.96537 5.18252 5.62436 4.42419C7.23607 3.68748 9.40166 3.88258 11.4596 6.02073L12.5404 4.98053C10.0985 2.44352 7.26409 2.02539 5.00076 3.05996C2.78471 4.07292 1.25 6.42503 1.25 9.1371H2.75ZM8.49742 19.4999C9.00965 19.9037 9.55954 20.3343 10.1168 20.6599C10.6739 20.9854 11.3096 21.25 12 21.25V19.75C11.6904 19.75 11.3261 19.6293 10.8736 19.3648C10.4213 19.1005 9.95208 18.7366 9.42605 18.3219L8.49742 19.4999ZM15.5026 19.4999C16.9292 18.3752 18.7528 17.0866 20.1833 15.4758C21.6395 13.8361 22.75 11.8026 22.75 9.1371H21.25C21.25 11.3345 20.3508 13.0282 19.0617 14.4798C17.7469 15.9603 16.0896 17.1271 14.574 18.3219L15.5026 19.4999ZM22.75 9.1371C22.75 6.42503 21.2153 4.07292 18.9992 3.05996C16.7359 2.02539 13.9015 2.44352 11.4596 4.98053L12.5404 6.02073C14.5983 3.88258 16.7639 3.68748 18.3756 4.42419C20.0346 5.18252 21.25 6.98623 21.25 9.1371H22.75ZM14.574 18.3219C14.0479 18.7366 13.5787 19.1005 13.1264 19.3648C12.6739 19.6293 12.3096 19.75 12 19.75V21.25C12.6904 21.25 13.3261 20.9854 13.8832 20.6599C14.4405 20.3343 14.9903 19.9037 15.5026 19.4999L14.574 18.3219ZM9.42605 18.3219C8.63014 17.6945 7.82129 17.0963 7.00061 16.4209L6.04741 17.5791C6.87768 18.2624 7.75472 18.9144 8.49742 19.4999L9.42605 18.3219ZM3.65801 12.6917C3.0968 11.6656 2.75 10.5033 2.75 9.1371H1.25C1.25 10.7746 1.66995 12.1827 2.34199 13.4115L3.65801 12.6917Z"
                                :fill="item.is_up === false ? 'red' : '#1C274C'"/>
                          <path d="M12 5.50073L10.5 8.5001L14 11.0001L11 14.5001L13 16.5001L12 20.5001"
                                :stroke="item.is_up === false ? 'red' : '#1C274C'" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
                        </svg>
                      </button>
                    </div>
                  </div>
                  <div v-html="item.content" class="comment-content flex flex-col items-start">
                  </div>
                  <span
                      @click="clickReplyBtn(item.seq)"
                      class="self-start mt-2 text-sm text-gray-400 cursor-pointer">댓글 달기</span>
                  <div
                      v-if="comment.reply.value.parent_comment_seq === item.seq"
                      class="flex flex-col justify-start items-start mt-5 ml-16 w-full border-2 border-gray-200 rounded-[.5rem] p-4">
                    <span class="font-bold">{{ user.nickname }}</span>
                    <textarea
                        @focus.self="this.comment.reply.setting.isShowEmoji = false"
                        v-model.trim="comment.reply.value.content"
                        placeholder="댓글을 남겨보세요!"
                        oninput="this.style.height = ''; this.style.height = this.scrollHeight + 'px'"
                        class="w-full outline-none resize-none max-h-[30rem]"/>
                    <div class="flex flex-col items-start w-full">
                      <div
                          @click="comment.reply.value.imageUrl = null"
                          v-if="comment.reply.value.imageUrl != null"
                          class="w-14 h-14 border rounded my-1 relative overflow cursor-pointer">
                        <img :src="comment.reply.value.imageUrl" alt="댓글 이미지" class="w-full h-full bg-cover rounded">
                        <span class="absolute top-0.5 right-0.5 flex justify-center items-center w-5 h-5 bg-black bg-opacity-70 rounded-full">
                          <font-awesome-icon class="text-xs font-bold text-white" icon="xmark"></font-awesome-icon>
                        </span>
                      </div>
                      <div class="flex justify-between w-full">
                        <input type="file" class="hidden" @change="replyImageUpload($event)" ref="replyImgInput" accept="image/*">
                        <div class="relative">
                          <span class="cursor-pointer text-lg" @click="clickReplyImageBtn">
                            <font-awesome-icon icon="fa-regular fa-image"></font-awesome-icon>
                          </span>
                          <span class="cursor-pointer text-lg ml-2" @click="clickReplyEmojiBtn">
                            <font-awesome-icon icon="fa-regular fa-face-smile p-1 border"></font-awesome-icon>
                          </span>
                          <EmojiPicker class="absolute" v-if="comment.reply.setting.isShowEmoji" :native="true" @select="onSelectEmoji" />
                        </div>
                        <span>
                          <button class="mr-8 hover:text-blue-400" @click="resetReply()">취소</button>
                          <button class="hover:text-blue-400" @click="clickReplyAddBtn()">등록</button>
                        </span>
                      </div>
                    </div>
                  </div>
                </div>

              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>


<script>

import {useRoute} from "vue-router";
import {useCommunityStore} from "@/stores/community";
import {getCurrentInstance, nextTick, onMounted, ref} from "vue";
import {FontAwesomeIcon} from "@fortawesome/vue-fontawesome";
import writeEditor from "@/components/WriteEditor.vue";
import {useCommonStore} from "@/stores/common";
import EmojiPicker from 'vue3-emoji-picker'
import 'vue3-emoji-picker/css'
import {useUserStore} from "@/stores/user";
import interact from 'interactjs'
import {ContextMenu, ContextMenuItem, ContextMenuSeparator} from "@imengyu/vue3-context-menu";
import {router} from "@/router";

export default {
  components: {
    EmojiPicker, writeEditor, FontAwesomeIcon,
    ContextMenuSeparator, ContextMenuItem, ContextMenu
  },
  data() {
    return {
      isLike: null,
      comment: {
        reply: {
          setting: {
            isShowEmoji: false,
          },
          value: {
            article_seq: null,
            parent_comment_seq: null,
            content: null,
            imageUrl: null,
          },
        },
        value: {
          article_seq: null,
          content: null,
        },
      },
      setting: {
        refreshLoading : false,
        articleContextOption: {
          isShow: false,
          zIndex: 10,
          minWidth: 200,
          theme: 'mac',
          x: 100,
          y: 300,
        },
      }
    }
  },
  setup() {
    const route = useRoute();
    const seq = route.params.seq;
    const communityStore = useCommunityStore();
    const userStore = useUserStore();
    const article = ref({});
    const comments = ref([]);
    const user = userStore.userInfo;
    const instance = getCurrentInstance();

    const fn = {

      getComments: () =>{
        const communityStore = useCommunityStore();
        communityStore.getComments({
          article_seq: seq,
          user_seq: user.seq,
        })
        .then(result => {
          comments.value = result;
          nextTick(() => {
            document.querySelectorAll(".comment-content img, .comment-content iframe.ql-video").forEach(el => {
              let wrapper = document.createElement('div');
              wrapper.className = 'relative flex items-center';
              wrapper.style.cursor = 'default';

              let leftDiv = document.createElement('div');
              leftDiv.className = 'transition-opacity ease-in duration-200 opacity-0 absolute h-full w-3 -left-1 rounded hover:bg-gray-500 hover:opacity-100 z-10';
              leftDiv.style.cursor = 'ew-resize';

              let rightDiv = document.createElement('div');
              rightDiv.className = 'transition-opacity ease-in duration-200 opacity-0 absolute h-full w-3 -right-1 rounded hover:bg-gray-500 hover:opacity-100 z-10';
              rightDiv.style.cursor = 'ew-resize';

              wrapper.appendChild(leftDiv);
              el.parentNode.replaceChild(wrapper, el);
              wrapper.appendChild(el);
              wrapper.appendChild(rightDiv);

              interact(wrapper)
              .resizable({
                edges: { top: false, left: true, bottom: false, right: true },
                modifiers: [
                  interact.modifiers.restrictSize({
                    min: { width: 50, height: 50 }
                  })
                ],
                listeners: {
                  move: function (event) {
                    let { x, y } = event.target.dataset
                    x = (parseFloat(x) || 0) + event.deltaRect.left
                    y = (parseFloat(y) || 0) + event.deltaRect.top

                    if (event.deltaRect.left !== 0) {
                      x -= event.deltaRect.left;
                      el.style.left = x + "px";
                    }

                    let newWidth = event.rect.width;

                    if (el.tagName.toLowerCase() === 'img') {
                      let originalWidth = el.naturalWidth;
                      let originalHeight = el.naturalHeight;
                      let ratio = originalHeight / originalWidth;

                      let newHeight = newWidth * ratio;

                      Object.assign(el.style, {
                        width: `${newWidth}px`,
                        height: `${newHeight}px`,
                        transform: `translate(${x}px, ${y}px)`
                      });
                    } else if (el.tagName.toLowerCase() === 'iframe') {
                      el.setAttribute('width', newWidth);
                      el.setAttribute('height', newWidth * 9 / 16);  // 16:9 비율로 높이 계산
                    }

                    Object.assign(el.dataset, { x, y });
                  }
                },
                cursorChecker: () => 'default' // Change the cursor of the interact.js to default
              })
            });
          })
        })
        .catch((error) => {
          instance.appContext.config.globalProperties.utils.msgError(
              (error?.response?.data)
              || instance.appContext.config.globalProperties.utils.normalErrorMsg);
        });
      },
      getArticle: () => {
        communityStore.getArticle(seq)
        .then(result => {
          article.value = result;
          nextTick(() => {
            document.querySelectorAll("#article-content img, #article-content iframe.ql-video").forEach(el => {
              let wrapper = document.createElement('div');
              wrapper.className = 'relative flex items-center w-fit h-fit';
              wrapper.style.cursor = 'default';

              let leftDiv = document.createElement('div');
              leftDiv.className = 'transition-opacity ease-in duration-200 opacity-0 absolute h-full w-3 -left-1 rounded hover:bg-gray-500 hover:opacity-100 z-10';
              leftDiv.style.cursor = 'ew-resize';

              let rightDiv = document.createElement('div');
              rightDiv.className = 'transition-opacity ease-in duration-200 opacity-0 absolute h-full w-3 -right-1 rounded hover:bg-gray-500 hover:opacity-100 z-10';
              rightDiv.style.cursor = 'ew-resize';

              wrapper.appendChild(leftDiv);
              el.parentNode.replaceChild(wrapper, el);
              wrapper.appendChild(el);
              wrapper.appendChild(rightDiv);

              interact(wrapper)
              .resizable({
                edges: { top: false, left: true, bottom: false, right: true },
                modifiers: [
                  interact.modifiers.restrictSize({
                    min: { width: 100, height: 100 }
                  })
                ],
                listeners: {
                  move: function (event) {
                    let { x, y } = event.target.dataset
                    x = (parseFloat(x) || 0) + event.deltaRect.left
                    y = (parseFloat(y) || 0) + event.deltaRect.top

                    if (event.deltaRect.left !== 0) {
                      x -= event.deltaRect.left;
                      el.style.left = x + "px";
                    }

                    let newWidth = event.rect.width;

                    if (el.tagName.toLowerCase() === 'img') {
                      let originalWidth = el.naturalWidth;
                      let originalHeight = el.naturalHeight;
                      let ratio = originalHeight / originalWidth;

                      let newHeight = newWidth * ratio;

                      Object.assign(el.style, {
                        width: `${newWidth}px`,
                        height: `${newHeight}px`,
                        transform: `translate(${x}px, ${y}px)`
                      });
                    } else if (el.tagName.toLowerCase() === 'iframe') {
                      el.setAttribute('width', newWidth);
                      el.setAttribute('height', newWidth * 9 / 16);  // 16:9 비율로 높이 계산
                    }

                    Object.assign(el.dataset, { x, y });
                  }
                },
                cursorChecker: () => 'default' // Change the cursor of the interact.js to default
              })
            });
          })
        })
        .catch((error) => {
          instance.appContext.config.globalProperties.utils.msgError(
              (error?.response?.data)
              || instance.appContext.config.globalProperties.utils.normalErrorMsg);
        });
      },
      getArticleLike: () =>{
        communityStore.getLike({
          mode: 'article',
          content_seq: seq,
          user_seq: user.seq
        }).then(result => {
          if(result && result.is_up) instance.data.isLike = true;
          else if(result && !result.is_up) instance.data.isLike = false;
          fn.getArticle();
        })
      },
      getCommentLike: (comment_seq) =>{
        communityStore.getLike({
          mode: 'comment',
          content_seq: comment_seq,
          user_seq: user.seq
        }).then(result => {
          return result ? result.is_up : null;
        })
      },

    }

    onMounted(() => {
      fn.getArticleLike();
      fn.getComments();
    })

    return {
      article,
      comments,
      user,
      fn,
    }
  },
  methods: {
    resetComment(){
      this.$refs.commentEditor.clearEditor();
      this.comment.value = {
        article_seq: null,
        content: null,
        user_seq: null,
      };
    },
    resetReply(){
      this.comment.reply.setting = {
        isShowEmoji: false
      };
      this.comment.reply.value = {
                parentSeq: null,
                content: null,
                imageUrl: null,
              };
    },
    clickReplyBtn(seq){
      if(this.comment.reply.value.parent_comment_seq != null){
        if(this.comment.reply.value.parent_comment_seq !== seq){
          this.resetReply();
          this.comment.reply.value.parent_comment_seq = seq;
        }else{
          this.resetReply();
        }
      }else{
        this.comment.reply.value.parent_comment_seq = seq;
      }
    },
    clickArticleLikeBtn(isUp) {
      const communityStore = useCommunityStore();
      const data = {
        content_seq: this.article.seq,
        is_up: isUp,
      };
      communityStore.likeHandle(data)
      .then(result => {
        this.isLike = result.is_up;
        this.fn.getArticleLike(data);
      });
    },
    clickCommentLikeBtn(isUp, item){
      const communityStore = useCommunityStore();
      const data = {
        mode: 'comment',
        content_seq: item.seq,
        is_up: isUp,
      };
      communityStore.likeHandle(data)
      .then(() => {
        // this.isLike = result.is_up;
        // const result2 = this.fn.getCommentLike(data);
        // item.isLike = result2;
        this.fn.getComments();
      });
    },
    refreshClick(){
      if(!this.setting.refreshLoading){
        this.setting.refreshLoading = true;
        this.fn.getComments();

        setTimeout(() => {
          this.setting.refreshLoading = false;
        }, 2000);
      }
    },
    clickReplyImageBtn() {
      this.$refs.replyImgInput[0].click();
    },
    replyImageUpload(event) {
      if(event.target.files.length === 0) return;
      const formData = new FormData();
      formData.append("imageFile", event.target.files[0]);

      const common = useCommonStore();
      common.imageUpload(formData)
      .then((result) => {
        this.comment.reply.value.imageUrl = `${process.env.VUE_APP_PROTOCOL}${process.env.VUE_APP_SERVER_IP}:${process.env.VUE_APP_PORT}${result}`;
      })
      .catch((error) => {
        console.error("Error:", error);
      });
    },
    clickReplyEmojiBtn(){
      this.comment.reply.setting.isShowEmoji = !this.comment.reply.setting.isShowEmoji;
    },
    onSelectEmoji(emoji) {
      if(!this.comment.reply.value.content)
        this.comment.reply.value.content = emoji.i;
      else
        this.comment.reply.value.content += emoji.i;
    },
    clickCommentBtn(){
      this.comment.value.article_seq = this.article.seq;
      const communityStore = useCommunityStore();
      communityStore.addComment(this.comment.value)
      .then((result) => {
        if(result) {
          this.resetComment();
          this.utils.notify.success('댓글을 달았습니다!', '등록 완료!')
          this.fn.getComments();
        }
      })
      .catch((error) => {
          this.utils.notify.error(error, '');
      });
    },
    clickReplyAddBtn(){
      this.comment.reply.value.article_seq = this.article.seq;
      const communityStore = useCommunityStore();
      communityStore.addComment(this.comment.reply.value)
      .then((result) => {
        if(result) {
          this.resetReply();
          this.utils.notify.success('댓글을 달았습니다!', '등록 완료!')
          this.fn.getComments();
        }
      })
      .catch((error) => {
        this.utils.notify.error(error, '');
      });
    },
    getAbsolutePosition(element) {
      let top = 0, left = 0;

      while(element) {
        top += element.offsetTop  || 0;
        left += element.offsetLeft || 0;
        element = element.offsetParent;
      }

      return {
        top: top,
        left: left
      };
    },
    clickArticleSettingBtn(e){
      if(this.setting.articleContextOption.isShow === false){
        const el = e.target.closest('div');
        const xy = this.getAbsolutePosition(el);
        this.setting.articleContextOption.isShow = true;
        this.setting.articleContextOption.x = xy.left;
        this.setting.articleContextOption.y = xy.top + el.clientHeight + 10;
      }else{
        this.setting.articleContextOption.isShow = false;
      }
    },
    clickArticleEditBtn(){
      router.push({
        name: 'writeView',
        state: {
          mode: 'edit',
          seq: this.article.seq,
        },
      });
    },
    clickArticleDeleteBtn(){
      this.$swal.fire({
        title: '게시글 삭제',
        html: `<b>[${this.article.title}]</b> 게시글을 삭제 하시겠습니까?`,
        icon: "warning",
        showCancelButton: true,
        confirmButtonText: "삭제",
        cancelButtonText: "취소"
      }).then(async (result) => {
        if (result.isConfirmed) {
          const communityStore = useCommunityStore();
          try {
            await communityStore.deleteArticle({
              seq: this.article.seq,
            })
            if (communityStore?.dataResponse.status === 200) {
              this.utils.notify.success("삭제되었습니다.", "삭제 완료!");
              this.$router.push({
                path: `/community`,
              });
            } else {
              this.utils.msgError(typeof communityStore.dataResponse.data ==='object' ? this.utils.normalErrorMsg : communityStore.dataResponse.data);
            }
          } catch (error) {
            console.log(error);
            this.utils.msgError((error?.response?.data) || this.utils.normalErrorMsg);
          }
        }
      }).catch((error) => {
        console.log(error);
        this.utils.msgError((error?.response?.data) || this.utils.normalErrorMsg);
      });
    },

  }
};
</script>

<style>
#article-content h1,
#article-content h2,
#article-content h3,
#article-content h4,
#article-content h5,
#article-content h6,
#article-content p{
    margin: 0;
}
</style>
