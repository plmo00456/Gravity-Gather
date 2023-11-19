<template>
    <div id="main-view" class="flex flex-col items-center bg-main_background bg-cover text-black">
        <div class="flex w-4/6 h-full text-white py-10 justify-between">
            <div class="flex h-full flex-col w-full">
              <div class="h-[4.5rem] flex flex-col items-start w-full mb-2">
                <span class="text-lg">주제</span>
                <Multiselect
                    v-model="topicValue"
                    label="name"
                    :searchable="false"
                    :close-on-select="true"
                    :allow-empty="false"
                    :multiple="false"
                    :options="topic"
                />
              </div>
              <div class="h-[4.5rem] flex flex-col items-start w-full mb-3">
                <span class="text-lg">제목</span>
                <input
                    placeholder="제목을 입력해주세요."
                    class="rounded text-black px-3 py-1.5 text-xl border-b-gray-300 w-full "
                    type="text">
              </div>
              <div class="h-[calc(100%-14rem)] mb-2">
                <writeEditor v-model="content"/>
              </div>
              <div class="h-[3rem] self-end ">
                <button class="px-7 py-3 bg-gray-500 rounded text-sm hover:bg-gray-400 mr-3" type="button"
                        @click="slideShow = false">취소
                </button>
                <button class="px-7 py-3 bg-blue-600 rounded text-sm hover:bg-blue-500" type="button"
                        @click="openSlide('createRoom')">
                  <span class="">등록</span>
                </button>
              </div>
            </div>
        </div>
    </div>
</template>


<script>
import writeEditor from "@/components/WriteEditor.vue";
import Multiselect from "vue-multiselect";
import {ref} from "vue";

export default {
    components: {
      Multiselect,
      writeEditor
    },
    data() {
      return {
        topicValue: {name: '기술', topicValue: 1},
        topic: [
          {name: '기술', topicValue: 1},
          {name: '커리어', topicValue: 2},
          {name: '기타', topicValue: 3},
        ]
      }
    },
    setup() {
      const content = ref('');
      return {
        content
      }
    }
};
</script>

<style>
#main-view {
    height: 93%;
}

</style>
<style src="vue-multiselect/dist/vue-multiselect.css"></style>
