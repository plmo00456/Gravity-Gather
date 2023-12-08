<script>


import {FontAwesomeIcon} from "@fortawesome/vue-fontawesome";
import {getCurrentInstance, onMounted, ref} from "vue";
import {useUserStore} from "@/stores/user";
import {useCommonStore} from "@/stores/common";

export default {
  components: {FontAwesomeIcon},
  data() {
    return {
      scrapCondition: {
        search: '',
        currentPage: 1,
        totalArticleCnt: 0,
        lastPage: 1,
      },
      setting: {
        refreshLoading: false,
      }
    }
  },
  computed: {
    surroundingPages() {
      let start = Math.max(this.scrapCondition.currentPage - 2, 1);
      let end = Math.min(start + 4, this.scrapCondition.lastPage);
      start = Math.max(end - 4, 1);
      return Array.from({length: end - start + 1}, (v, i) => i + start);
    }
  },
  setup() {
    const userStore = useUserStore();
    const user = userStore.userInfo
    const commonStore = useCommonStore();
    const scrapList = ref([]);
    const scrapAllCnt = ref(0);
    const instance = getCurrentInstance();

    let fn = {};

    onMounted(() => {
      fn.getScraps = (manualCondition) => {
        commonStore.getScraps(manualCondition ? manualCondition : instance.data.scrapCondition)
        .then(async (result) => {
          scrapList.value = result;
          await commonStore.getScrapsAllCnt(instance.data.scrapCondition)
          .then(cnt => {
            if (cnt) {
              scrapAllCnt.value = cnt.allCnt;
              instance.data.scrapCondition.lastPage = cnt.lastPage;
            }
          })
        })
      };

      fn.getScraps();
    })

    return {
      commonStore,
      scrapList,
      scrapAllCnt,
      fn,
      user,
    }
  },
  methods: {
    async refreshClick() {
      if (!this.setting.refreshLoading) {
        this.setting.refreshLoading = true;
        await this.fn.getScraps();

        setTimeout(() => {
          this.setting.refreshLoading = false;
        }, 2000);
      }
    },
    async clickSearchBtn() {
      await this.fn.getScraps();
      const titles = document.querySelectorAll(".articles .article-title");
      titles.forEach(title => {
        title.innerHTML = this.highlightKeyword(title.innerHTML);
      })
    },
    highlightKeyword(text) {
      if (this.scrapCondition.search) {
        const re = new RegExp(this.scrapCondition.search, 'gi')
        return text.replace(re, (matchedText) => `<mark>${matchedText}</mark>`)
      }
      return text
    },
    getAbsolutePosition(element) {
      var top = 0, left = 0;

      while (element) {
        top += element.offsetTop || 0;
        left += element.offsetLeft || 0;
        element = element.offsetParent;
      }

      return {
        top: top,
        left: left
      };
    },
    clickPageBtn(page) {
      this.scrapCondition.currentPage = page;
      this.fn.getScraps();
    },
    prevPage() {
      this.scrapCondition.currentPage = this.scrapCondition.currentPage === 1 ? 1
          : this.scrapCondition.currentPage - 1;
      this.fn.getScraps();
    },
    nextPage() {
      this.scrapCondition.currentPage = this.scrapCondition.currentPage
      === this.scrapCondition.lastPage ? this.scrapCondition.lastPage
          : this.scrapCondition.currentPage + 1;
      this.fn.getScraps();
    }
  }
}
</script>

<template>
  <div id="main-view" class="flex items-center bg-main_background bg-cover h-[93%] justify-center">
    <div class="flex flex-col items-center w-full h-full bg-gray-300 bg-opacity-30">
      <div class=" flex flex-col items-center bg-white w-[70%] h-full overflow-y-auto rounded">
        <div class="flex flex-col items-start w-[85%]">
          <div
              class="relative overflow-hidden bg-pattern_3 bg-cover flex flex-col items-start bg-gray-300 w-full h-[6rem] my-5 p-5 rounded-3xl">
            <span class="font-bold text-xl text">스크랩</span>
            <span>{{ user.nickname }}님께서 스크랩 하신 내역입니다!</span>
            <img class="absolute right-2 -top-0 w-[7rem]" src="@/assets/image/character/c4.png"
                 alt="">
          </div>
          <div
              class="z-0 sticky-element bg-white flex justify-between items-center border-gray-300 border-y w-full h-[5rem] px-2">
            <div class="flex items-center">
                        <span
                            @click="refreshClick()"
                            class="select-none p-3 rounded cursor-pointer hover:bg-gray-200 hover:text-blue-400 mr-2">
                            <font-awesome-icon
                                :class="{'animate-spin': setting.refreshLoading, 'text-blue-400': setting.refreshLoading}"
                                class="" icon="rotate-right"/>
                        </span>
              <div>
                <label @click="prevPage()"
                       :class="{'text-gray-600 hover:text-blue-400 cursor-pointer':scrapCondition.currentPage !== 1,'text-gray-300' : scrapCondition.currentPage === 1}">
                                <span class="p-1 ml-1">
                                    <font-awesome-icon icon="arrow-left"/>
                                </span>
                </label>
                <span>{{ this.scrapCondition.currentPage }} / {{
                    this.scrapCondition.lastPage
                  }} 페이지</span>
                <label @click="nextPage()"
                       :class="{'text-gray-600 hover:text-blue-400 cursor-pointer':scrapCondition.currentPage !== scrapCondition.lastPage,'text-gray-300' : scrapCondition.currentPage === scrapCondition.lastPage}">
                                <span class="p-1 ml-1">
                                    <font-awesome-icon icon="arrow-right"/>
                                </span>
                </label>
              </div>
            </div>
            <div class="flex relative">
              <form @submit.prevent="clickSearchBtn">
                <input type="text" placeholder="검색" v-model.trim="scrapCondition.search"
                       class="w-[15rem] border border-gray-300 text-sm pl-10 pr-4 py-2 rounded-l focus:outline-none text-gray-400 focus:border-gray-400"
                       maxlength="30">
                <span
                    class="flex justify-center absolute left-3 top-1/2 transform -translate-y-1/2">
                                <font-awesome-icon class="fa-sm text-gray-400"
                                                   icon="magnifying-glass"></font-awesome-icon>
                            </span>
                <button class="text-white px-3 py-2 bg-blue-600 rounded-r text-sm hover:bg-blue-500"
                        type="button">
                  <font-awesome-icon class="fa-md font-bold"
                                     icon="magnifying-glass"></font-awesome-icon>
                </button>
              </form>
            </div>
          </div>
          <div class="articles w-full">
            <div v-if="commonStore.scrapList.length === 0"
                 class="flex justify-center items-center border-b border-gray-300 w-full h-[5.5rem] py-3 text-lg font-semibold">
                        <span class="mr-1">
                            <font-awesome-icon icon="fa-regular fa-face-sad-tear"/>
                        </span>
              <span>조회된 글이 없습니다.</span>
            </div>
            <template v-else>
              <div v-for="( article, index ) in commonStore.scrapList" :key="article.seq"
                   :class="{ 'border-b border-gray-300': index < commonStore.scrapList.length - 1 }"
                   class="flex justify-between w-full h-[5.5rem] py-3 relative">
                <span class="text-xs px-2 py-0.5 bg-blue-400 text-white rounded-3xl absolute top-3 right-1">{{ article.master_title }}</span>
                <div class="w-[80%] flex flex-col pl-2">
                  <div class="flex items-center text-gray-500">
                    <div class="w-7 h-7 rounded-full overflow-hidden mr-2">
                      <img class="w-full h-full object-cover bg-white"
                           :src="`${$env.protocol}${$env.serverIP}:${$env.port}${article.photo}`"
                           alt="프로필 사진" v-if="article.photo">
                      <div v-if="!article.photo"
                           class=" rounded-3xl w-full h-full flex justify-center items-center font-bold text-md shadow-2xl text-white bg-green-700">
                        <span>{{ article.nickname[0] }}</span>
                      </div>
                    </div>
                    <span class="mr-3">{{ article.nickname }}</span>
                    <div
                        class="flex items-center border-l border-gray-300 pl-3 text-gray-400 text-xs">
                                        <span class="flex items-center mr-1.5">
                                            스크랩 시간:
                                        </span>
                      <span class="">{{ this.utils.timeAgoStr(article.created_at) }}</span>
                    </div>
                  </div>
                  <div class="flex mt-2">
                                    <span
                                        :title="article.title"
                                        class="truncate article-title text-gray-700 text-lg cursor-pointer hover:text-blue-400">
                                        <router-link :to="`/community/${article.seq}`">{{
                                            article.title
                                          }}</router-link>
                                    </span>
                  </div>
                </div>
                <div class="w-[20%] pr-2 flex justify-end items-end text-gray-500">
                  <div class="flex mr-2">
                                    <span class="mr-1">
                                        <font-awesome-icon icon="fa-regular fa-comment-dots"/>
                                    </span>
                    <span>{{ utils.numberCommas(article.comment_count) }}</span>
                  </div>
                  <div class="flex mr-2">
                                    <span class="mr-1">
                                        <font-awesome-icon icon="fa-regular fa-eye"/>
                                    </span>
                    <span>{{ utils.numberCommas(article.view_count) }}</span>
                  </div>
                  <div class="flex">
                                    <span class="mr-1">
                                        <font-awesome-icon icon="fa-regular fa-heart"/>
                                    </span>
                    <span>{{ utils.numberCommas(article.likes) }}</span>
                  </div>
                </div>
              </div>
              <div
                  class="bg-white flex justify-between items-start border-gray-300 border-t w-full h-[5rem] px-2 my-10">
                <div class="flex justify-between items-center w-full select-none">
                  <label @click="prevPage()"
                         :class="{'text-gray-600 hover:text-blue-400 cursor-pointer':scrapCondition.currentPage !== 1,'text-gray-300' : scrapCondition.currentPage === 1}">
                                    <span class="p-1 mr-1">
                                        <font-awesome-icon icon="arrow-left"/>
                                    </span>
                    <span>이전</span>
                  </label>
                  <div class="flex items-center">
                    <button class="mr-2" v-if="scrapCondition.currentPage > 3"
                            @click="clickPageBtn(1)">1
                    </button>
                    <span class="" v-if="scrapCondition.currentPage > 3">...</span>

                    <button v-for="page in surroundingPages"
                            :key="page"
                            @click="clickPageBtn(page)"
                            class="p-4"
                            :class="{ 'text-blue-500 border-t-4 border-blue-400': scrapCondition.currentPage === page }">
                      {{ page }}
                    </button>

                    <span v-if="scrapCondition.currentPage < lastPage - 2">...</span>
                    <button class="ml-2" v-if="scrapCondition.currentPage < lastPage - 2"
                            @click="clickPageBtn(lastPage)">{{ lastPage }}
                    </button>
                  </div>
                  <label>
                    <label @click="nextPage()"
                           :class="{'text-gray-600 hover:text-blue-400 cursor-pointer':scrapCondition.currentPage !== scrapCondition.lastPage,'text-gray-300' : scrapCondition.currentPage === scrapCondition.lastPage}">
                      <span>다음</span>
                      <span class="p-1 ml-1">
                                            <font-awesome-icon icon="arrow-right"/>
                                        </span>
                    </label>
                  </label>
                </div>
              </div>
            </template>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style>
#main-view > div > div::-webkit-scrollbar {
  display: none;
}

#main-view > div > div {
  -ms-overflow-style: none;
  scrollbar-width: none;
}

.sticky-element {
  position: sticky;
  top: 0;
  z-index: 100;
}
</style>
