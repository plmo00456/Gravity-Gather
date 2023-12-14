<template>
  <div id="main-view" class="flex flex-col items-center bg-main_background bg-cover text-black h-[93%]">
    <div class="flex flex-col items-center w-full h-full bg-gray-300 bg-opacity-30">
      <div class=" flex flex-col items-center bg-white w-[70%] h-full overflow-y-auto rounded">
        <div class="flex w-[85%] h-full justify-between">
          <form class="w-full" @submit.prevent="mode === 'write' ? clickWriteBtn() : clickEditBtn()">
            <div class="flex h-full flex-col w-full">
              <div class="flex flex-col items-start my-8">
                <span class="text-[2.5rem] font-light">커뮤니티 게시판</span>
                <span>개발자들과 함께 나누고 싶은 IT 뉴스, 정보가 있다면 공유해주세요.</span>
              </div>
              <div class="h-[4.5rem] flex flex-col items-start w-full mb-5">
                <span class="text-lg">주제</span>
                <Multiselect
                    v-model="articleMaster"
                    label="title"
                    class="border border-gray-300 rounded hover:border-gray-400"
                    :searchable="false"
                    :close-on-select="true"
                    :allow-empty="false"
                    :multiple="false"
                    :options="communityStore.articleMasterList"
                    :select-label="''"
                />
              </div>
              <div class="h-[4.5rem] flex flex-col items-start w-full mb-5">
                <span class="text-lg">제목</span>
                <input
                    maxlength="100"
                    v-model.trim="value.title"
                    @focus="validation.title = false"
                    placeholder="제목을 입력해주세요."
                    class="rounded text-black px-3 py-1.5 text-md border border-gray-300 w-full focus:border-gray-400 outline-none shadow"
                    type="text">
                <span v-if="validation.title" class="flex justify-start items-center text-red-400 text-sm">
                  <font-awesome-icon class="mr-1" icon="circle-exclamation"></font-awesome-icon>
                  제목을 최소 5자 이상 입력해주세요.
                </span>
              </div>
              <div class="flex flex-col h-[calc(100%-27rem)] mb-2">
                <span class="self-start text-lg">본문</span>
                <writeEditor
                    ref="editor"
                    @focus="validation.content = false"
                    class="border-gray-300 hover:border-gray-400 shadow"
                             v-model.trim="value.content"/>
              </div>
              <div class="h-[3rem] self-end text-white">
                <button class="px-7 py-3 bg-blue-600 rounded text-sm hover:bg-blue-500 mr-3"
                        type="submit">
                  <span v-if="mode === 'write'">등록</span>
                  <span v-if="mode === 'edit'">수정</span>
                </button>
                <button class="px-7 py-3 bg-gray-500 rounded text-sm hover:bg-gray-400"
                        type="button"
                        @click="$router.go(-1)">취소
                </button>
              </div>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>


<script>
import writeEditor from "@/components/article/WriteEditor.vue";
import Multiselect from "vue-multiselect";
import {getCurrentInstance, onMounted, ref} from "vue";
import {useCommunityStore} from "@/stores/community.js";
import {useUserStore} from "@/stores/user.js";
import {FontAwesomeIcon} from "@fortawesome/vue-fontawesome";

export default {
  components: {
    FontAwesomeIcon,
    Multiselect,
    writeEditor
  },
  data() {
    return {
      articleMaster: {},
      value: {
        set: null,
        master_seq: null,
        title: null,
        content: null,
      },
      validation: {
        title: false,
      },
    }
  },
  setup() {
    const mode = history.state.mode ? history.state.mode : 'write';
    const seq = history.state.seq ? history.state.seq : 0;
    const userStore = useUserStore();
    const user = userStore.userInfo;
    const state = history.state;
    const content = ref('');
    const communityStore = useCommunityStore();
    const instance = getCurrentInstance();
    communityStore.getArticleMaster();

    onMounted(() => {
      const stateArticleMasterSeq = state.articleMasterSeq ? state.articleMasterSeq : 1;
      const idx = communityStore.articleMasterList.findIndex((c) => c.seq === stateArticleMasterSeq)
      instance.data.articleMaster = communityStore.articleMasterList[idx];

      if(mode === 'edit'){
        communityStore.getArticle(seq)
        .then(result => {
          instance.data.value.master_seq = result.master_seq;
          instance.data.value.title = result.title;
          instance.data.value.content = result.content;
          instance.refs.editor.setContent(result.content);
        })
      }

    });

    return {
      content,
      communityStore,
      user,
      mode,
      seq
    }
  },
  methods: {
    async clickWriteBtn() {
      let flag = false;
      if(!this.value.title || this.value.title.length <= 4) {
        this.validation.title = true;
        flag = true;
      }
      if(!this.value.content || this.value.content.length <= 4) {
        this.utils.notify.error('본문을 최소 5자 이상 입력해 주세요.', ' ')
        flag = true;
      }

      if(flag) return;


      const communityStore = useCommunityStore();
      try {
        this.value.master_seq = this.articleMaster.seq;

        await communityStore.writeArticle(this.value);
        if (communityStore?.dataResponse.status === 200) {
          this.utils.notify.success("생성되었습니다.", "생성 완료!");
          this.$router.push({
            name: 'communityView',
          });
        } else {
          this.utils.msgError(communityStore.dataResponse.custom ? this.dataResponse.data.message : this.utils.normalErrorMsg);
        }
      } catch (error) {
        this.utils.msgError((error?.response?.data) || this.utils.normalErrorMsg);
      }
    },
    async clickEditBtn() {
      let flag = false;
      if(!this.value.title || this.value.title.length <= 4) {
        this.validation.title = true;
        flag = true;
      }
      if(!this.value.content || this.value.content.length <= 4) {
        this.utils.notify.error('본문을 최소 5자 이상 입력해 주세요.', ' ')
        flag = true;
      }

      if(flag) return;

      const communityStore = useCommunityStore();
      try {
        this.value.master_seq = this.articleMaster.seq;
        this.value.seq = this.seq;
        await communityStore.updateArticle(this.value);
        if (communityStore?.dataResponse.status === 200) {
          this.utils.notify.success("수정되었습니다.", "수정 완료!");
          this.$router.push({
            path: `/community/${this.seq}`,
          });
        } else {
          this.utils.msgError(typeof communityStore.dataResponse.data ==='object' ? this.utils.normalErrorMsg : communityStore.dataResponse.data);
        }
      } catch (error) {
        console.log(error);
        this.utils.msgError((error?.response?.data) || this.utils.normalErrorMsg);
      }
    },
  }
};
</script>
