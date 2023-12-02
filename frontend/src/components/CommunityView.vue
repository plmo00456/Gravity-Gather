<script>


import {FontAwesomeIcon} from "@fortawesome/vue-fontawesome";
import {useCommunityStore} from "@/stores/community";
import {ContextMenu, ContextMenuItem} from "@imengyu/vue3-context-menu";
import {router} from "@/router";
import {getCurrentInstance, onMounted, ref} from "vue";

export default {
    components: { ContextMenuItem, ContextMenu, FontAwesomeIcon},
    data() {
      return {
          articleCondition: {
              master_seq: 0,
              order: 'desc',
              search: '',
              currentPage: 1,
              totalArticleCnt: 0,
              lastPage: 1,
          },
          setting: {
              refreshLoading: false,
              order: {
                  label: {
                      desc: '최신순',
                      asc: '오래된순',
                      like: '추천순',
                      comment: '댓글순',
                      view: '조회순',
                  },
                  contextIsShow: false,
                  contextOption: {
                      zIndex: 100,
                      minWidth: 100,
                      theme: 'flat',
                      x: 100,
                      y: 300,
                  }
              }
          }
      }
    },
    computed: {
        surroundingPages() {
            let start = Math.max(this.articleCondition.currentPage - 2, 1);
            let end = Math.min(start + 4, this.articleCondition.lastPage);
            start = Math.max(end - 4, 1);
            return Array.from({ length: end - start + 1 }, (v, i) => i + start);
        }
    },
    setup() {
        const communityStore = useCommunityStore();
        const articleMasterList = ref([]);
        const articleList = ref([]);
        const articleAllCnt = ref(0);
        const instance = getCurrentInstance();

        let fn =  {};

        communityStore.getArticleMaster()
        .then((result) => {
            articleMasterList.value = result;
        })

        onMounted(() => {
            fn.getArticles = (manualCondition) => {
                communityStore.getArticles(manualCondition ? manualCondition : instance.data.articleCondition)
                .then(async (result) => {
                    articleList.value = result;
                    await communityStore.getArticlesAllCnt(instance.data.articleCondition)
                    .then(cnt => {
                        articleAllCnt.value = cnt.allCnt;
                        instance.data.articleCondition.lastPage = cnt.lastPage;
                    })
                })
            };

            fn.getArticles({
                order: 'desc',
            });
        })


        return {
            communityStore,
            articleMasterList,
            articleList,
            articleAllCnt,
            fn
        }
    },
    methods: {
        async refreshClick(){
            if(!this.setting.refreshLoading){
                this.setting.refreshLoading = true;
                await this.fn.getArticles();

                setTimeout(() => {
                    this.setting.refreshLoading = false;
                }, 2000);
            }
        },
        async clickArticleTab(mode) {
            this.articleCondition = {
                master_seq: 0,
                order: 'desc',
                search: '',
                currentPage: 1,
                totalArticleCnt: 0,
                lastPage: 1,
            };
            this.articleCondition.master_seq = mode;
            await this.fn.getArticles();
        },
        clickOrderBtn(e){
            const el = e.target.closest('button');
            const xy = this.getAbsolutePosition(el);
            this.setting.order.contextIsShow = true;
            this.setting.order.contextOption.x = xy.left;
            this.setting.order.contextOption.y = xy.top + el.clientHeight + 10;
        },
        async clickOrderBtnAct(mode){
            this.articleCondition.order = mode;
            await this.fn.getArticles();
        },
        async clickSearchBtn(){
            await this.fn.getArticles();
            const titles = document.querySelectorAll(".articles .article-title");
            titles.forEach(title => {
                title.innerHTML = this.highlightKeyword(title.innerHTML);
            })
        },
        highlightKeyword(text) {
            if (this.articleCondition.search) {
                const re = new RegExp(this.articleCondition.search, 'gi')
                return text.replace(re, (matchedText) => `<mark>${matchedText}</mark>`)
            }
            return text
        },
        getAbsolutePosition(element) {
            var top = 0, left = 0;

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
        clickWriteBtn(){
            router.push({
                name: 'writeView',
                state: {
                    articleMasterSeq: this.articleCondition.master_seq
                }
            });
        },
        clickPageBtn(page){
            this.articleCondition.currentPage = page;
            this.fn.getArticles();
        },
        prevPage(){
            this.articleCondition.currentPage = this.articleCondition.currentPage === 1 ? 1 : this.articleCondition.currentPage - 1;
            this.fn.getArticles();
        },
        nextPage(){
            this.articleCondition.currentPage = this.articleCondition.currentPage === this.articleCondition.lastPage ? this.articleCondition.lastPage : this.articleCondition.currentPage + 1;
            this.fn.getArticles();
        }
    }
}
</script>

<template>
    <div id="main-view" class="flex items-center bg-main_background bg-cover h-[93%] justify-center">
        <div class="flex flex-col items-center w-full h-full bg-gray-300 bg-opacity-30">
            <div class=" flex flex-col items-center bg-white w-[70%] h-full overflow-y-auto rounded">
            <div class="flex flex-col items-start w-[85%]">
                <div class="relative overflow-hidden bg-pattern_2 bg-cover flex flex-col items-start bg-gray-300 w-full h-[6rem] my-5 p-5 rounded-3xl">
                    <span class="font-bold text-xl text">커뮤니티</span>
                    <span>이런 저런 이야기를 나누고, 정보를 공유해 보아요.</span>
                    <img class="absolute right-2 -top-3 w-[8rem]" src="@/assets/image/character/c3.png" alt="">
                </div>
                <div class="flex justify-between items-center w-full mb-5 relative">
                    <button
                        @click="clickWriteBtn"
                        class="text-white px-3 py-2 bg-blue-600 rounded text-sm hover:bg-blue-500" type="button">
                        <span class="mr-4">작성하기</span>
                        <font-awesome-icon class="fa-md font-bold" icon="fa-pen"></font-awesome-icon>
                    </button>
                    <div class="text-lg absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2">
                        <button
                                @click="clickArticleTab(0)"
                                :class="{'bg-gray-200 text-blue-400 font-bold': articleCondition.master_seq === 0}"
                                class="px-2 py-1 rounded hover:text-blue-400 mr-7">전체</button>
                        <template v-for="articleMaster in communityStore.articleMasterList" :key="articleMaster.seq">
                            <button
                                    @click="clickArticleTab(articleMaster.seq)"
                                    :title="articleMaster.description"
                                    :class="{'bg-gray-200 text-blue-400 font-bold': articleCondition.master_seq === articleMaster.seq}"
                                    class="px-2 py-1 rounded hover:text-blue-400 mr-7">
                                {{ articleMaster.title }}
                            </button>
                        </template>
                    </div>
                    <button
                            @click="clickOrderBtn($event)"
                            class="text-black border border-gray-300 px-3 py-2 rounded text-sm hover:border-gray-400" type="button">
                        <font-awesome-icon class="fa-md font-bold mr-4" icon="fa-arrow-down-wide-short"></font-awesome-icon>
                        <span class="">{{ setting.order.label[articleCondition.order] }}</span>
                    </button>
                </div>
                <div class="z-0 sticky-element bg-white flex justify-between items-center border-gray-300 border-y w-full h-[5rem] px-2">
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
                                :class="{'text-gray-600 hover:text-blue-400 cursor-pointer':articleCondition.currentPage !== 1,'text-gray-300' : articleCondition.currentPage === 1}">
                                <span class="p-1 ml-1">
                                    <font-awesome-icon icon="arrow-left"/>
                                </span>
                            </label>
                            <span>{{ this.articleCondition.currentPage }} / {{ this.articleCondition.lastPage }} 페이지</span>
                            <label @click="nextPage()"
                                :class="{'text-gray-600 hover:text-blue-400 cursor-pointer':articleCondition.currentPage !== articleCondition.lastPage,'text-gray-300' : articleCondition.currentPage === articleCondition.lastPage}">
                                <span class="p-1 ml-1">
                                    <font-awesome-icon icon="arrow-right"/>
                                </span>
                            </label>
                        </div>
                    </div>
                    <div class="flex relative">
                        <form @submit.prevent="clickSearchBtn">
                            <input type="text" placeholder="검색" v-model.trim="articleCondition.search"
                                   class="w-[15rem] border border-gray-300 text-sm pl-10 pr-4 py-2 rounded-l focus:outline-none text-gray-400 focus:border-gray-400" maxlength="30">
                            <span class="flex justify-center absolute left-3 top-1/2 transform -translate-y-1/2">
                                <font-awesome-icon class="fa-sm text-gray-400" icon="magnifying-glass"></font-awesome-icon>
                            </span>
                            <button class="text-white px-3 py-2 bg-blue-600 rounded-r text-sm hover:bg-blue-500" type="button">
                                <font-awesome-icon class="fa-md font-bold" icon="magnifying-glass"></font-awesome-icon>
                            </button>
                        </form>
                    </div>
                </div>
                <div class="articles w-full">
                    <div v-if="communityStore.articleList.length === 0" class="flex justify-center items-center border-b border-gray-300 w-full h-[5.5rem] py-3 text-lg font-semibold">
                        <span class="mr-1">
                            <font-awesome-icon icon="fa-regular fa-face-sad-tear"/>
                        </span>
                        <span>조회된 글이 없습니다.</span>
                    </div>
                    <template v-else>
                        <div v-for="( article, index ) in communityStore.articleList" :key="article.seq"
                             :class="{ 'border-b border-gray-300': index < communityStore.articleList.length - 1 }"
                             class="flex justify-between w-full h-[5.5rem] py-3">
                            <div class="w-[80%] flex flex-col pl-2">
                                <div class="flex items-center text-gray-500">
                                    <div class="w-7 h-7 rounded-full overflow-hidden mr-2">
                                        <img class="w-full h-full object-cover bg-white"
                                             :src="`${$env.protocol}${$env.serverIP}:${$env.port}${article.photo}`"
                                             alt="프로필 사진" v-if="article.photo">
                                        <div v-if="!article.photo"
                                             class=" rounded-3xl w-full h-full flex justify-center items-center font-bold text-lg shadow-2xl text-white bg-green-700">
                                            <span>{{ article.nickname[0] }}</span>
                                        </div>
                                    </div>
                                    <span class="mr-3">{{article.nickname}}</span>
                                    <div class="flex items-center border-l border-gray-300 pl-3 text-gray-400 text-xs">
                                        <span class="flex items-center mr-1.5">
                                            <font-awesome-icon icon="fa-clock"/>
                                        </span>
                                        <span class="">{{ this.utils.timeAgoStr(article.created_at) }}</span>
                                    </div>
                                </div>
                                <div class="flex mt-2">
                                    <span
                                        :title="article.title"
                                        class="truncate article-title text-gray-700 text-lg cursor-pointer hover:text-blue-400">
                                        <router-link :to="`/community/${article.seq}`">{{ article.title }}</router-link>
                                    </span>
                                </div>
                            </div>
                            <div class="w-[20%] pr-2 flex justify-end items-end text-gray-500">
                                <div class="flex mr-2">
                                    <span class="mr-1">
                                        <font-awesome-icon icon="fa-regular fa-comment-dots"/>
                                    </span>
                                    <span>{{ article.comment_count }}</span>
                                </div>
                                <div class="flex mr-2">
                                    <span class="mr-1">
                                        <font-awesome-icon icon="fa-regular fa-eye"/>
                                    </span>
                                    <span>{{ article.view_count }}</span>
                                </div>
                                <div class="flex">
                                    <span class="mr-1">
                                        <font-awesome-icon icon="fa-regular fa-heart"/>
                                    </span>
                                    <span>{{ article.likes }}</span>
                                </div>
                            </div>
                        </div>
                        <div class="bg-white flex justify-between items-start border-gray-300 border-t w-full h-[5rem] px-2 my-10">
                            <div class="flex justify-between items-center w-full select-none">
                                <label @click="prevPage()"
                                    :class="{'text-gray-600 hover:text-blue-400 cursor-pointer':articleCondition.currentPage !== 1,'text-gray-300' : articleCondition.currentPage === 1}">
                                    <span class="p-1 mr-1">
                                        <font-awesome-icon icon="arrow-left"/>
                                    </span>
                                    <span>이전</span>
                                </label>
                                <div class="flex items-center">
                                    <button class="mr-2" v-if="articleCondition.currentPage > 3" @click="clickPageBtn(1)">1</button>
                                    <span class="" v-if="articleCondition.currentPage > 3">...</span>

                                    <button v-for="page in surroundingPages"
                                            :key="page"
                                            @click="clickPageBtn(page)"
                                            class="p-4"
                                            :class="{ 'text-blue-500 border-t-4 border-blue-400': articleCondition.currentPage === page }">{{ page }}</button>

                                    <span v-if="articleCondition.currentPage < lastPage - 2">...</span>
                                    <button class="ml-2" v-if="articleCondition.currentPage < lastPage - 2"
                                            @click="clickPageBtn(lastPage)">{{ lastPage }}</button>
                                </div>
                                <label>
                                    <label @click="nextPage()"
                                        :class="{'text-gray-600 hover:text-blue-400 cursor-pointer':articleCondition.currentPage !== articleCondition.lastPage,'text-gray-300' : articleCondition.currentPage === articleCondition.lastPage}">
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
        <context-menu
                v-model:show="setting.order.contextIsShow"
                :options="setting.order.contextOption"
        >
            <context-menu-item :label="setting.order.label['desc']" @click="clickOrderBtnAct('desc')" class="cursor-pointer"/>
            <context-menu-item :label="setting.order.label['asc']" @click="clickOrderBtnAct('asc')" class="cursor-pointer"/>
            <context-menu-item :label="setting.order.label['like']" @click="clickOrderBtnAct('like')" class="cursor-pointer"/>
            <context-menu-item :label="setting.order.label['comment']" @click="clickOrderBtnAct('comment')" class="cursor-pointer"/>
            <context-menu-item :label="setting.order.label['view']" @click="clickOrderBtnAct('view')" class="cursor-pointer"/>
        </context-menu>
    </div>
</template>

<style>
#main-view > div > div::-webkit-scrollbar {
    display: none;
}
#main-view > div > div{
    -ms-overflow-style: none;
    scrollbar-width: none;
}

.sticky-element {
    position: sticky;
    top: 0;
    z-index: 100;
}
</style>
