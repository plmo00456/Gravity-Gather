<template>
  <div class="flex flex-col h-full bg-white dark:bg-slate-800 rounded overflow-y-auto">
    <quill-editor
        ref="editor"
        v-model:content="content"
        contentType="html"
        class="dark:bg-gray-800 text-black dark:text-white overflow-y-auto"
        :modules="modules"
        theme="snow"
        toolbar="#toolbar">
      <template #toolbar>
        <div id="toolbar" class="flex">
        <span class="ql-formats">
          <select class="ql-header">
            <option value="1">헤더 1</option>
            <option value="2">헤더 2</option>
            <option value="3">헤더 3</option>
            <option value="4">헤더 4</option>
            <option value="5">헤더 5</option>
            <option value="6">헤더 6</option>
          </select>
        </span>
          <span class="ql-formats">
            <button class="ql-bold"></button>
            <button class="ql-italic"></button>
            <button class="ql-underline"></button>
            <button class="ql-strike"></button>
            <button class="ql-blockquote"></button>
          </span>
          <span class="ql-formats">
          <select class="ql-color"></select>
          <select class="ql-background"></select>
        </span>
          <span class="ql-formats">
          <button class="ql-image"></button>
          <button class="ql-video"></button>
        </span>
          <span class="ql-formats">
          <button class="ql-clean"></button>
        </span>
        </div>
      </template>
    </quill-editor>
  </div>
</template>

<script>
import {QuillEditor} from "@vueup/vue-quill";
import QuillMarkdown from 'quilljs-markdown'

import 'quilljs-markdown/dist/quilljs-markdown-common-style.css'
import ("@vueup/vue-quill/dist/vue-quill.snow.css")
import BlotFormatter from 'quill-blot-formatter'
import {ImageDrop} from 'quill-image-drop-module';
import {getCurrentInstance, ref, watch} from "vue";
import ImageCompress from 'quill-image-compress';
import ImageUploader from "quill-image-uploader";
import 'quill-image-uploader/dist/quill.imageUploader.min.css';
import {useCommonStore} from "@/stores/common.js";

export default {
  props: ['modelValue'],
  emits: ['update:modelValue'],
  components: {QuillEditor},
  setup(props, {emit}) {
    const content = ref(props.modelValue);
    const instance = getCurrentInstance();

    const clearEditor = () => {
      instance.refs.editor.setContents('');
    };

    const setContent = (content) => {
      instance.refs.editor.setContents(content);
    };

    watch(content, newContent => {
      emit('update:modelValue', newContent);
      emit('input', newContent);
    });

    const modules = [
      {
        name: 'quillMarkdown',
        module: QuillMarkdown,
        options: {/* options */}
      },
      {
        name: 'blotFormatter',
        module: BlotFormatter,
        options: {/* options */}
      },
      {
        name: 'imageDrop',
        module: ImageDrop,
        options: {/* options */}
      },
      {
        name: 'imageCompress',
        module: ImageCompress,
        options: {
          imageType: 'image/png',
        },
      },
      {
        name: 'imageUploader',
        module: ImageUploader,
        options: {
          upload: (file) => {
            return new Promise((resolve, reject) => {
              const formData = new FormData();
              formData.append("imageFile", file);

              const common = useCommonStore();
              common.imageUpload(formData)
              .then((result) => {
                let imgUrl = `${import.meta.env.VITE_PROTOCOL}${import.meta.env.VITE_SERVER_IP}:${import.meta.env.VITE_PORT}${result}`;
                resolve(imgUrl);
              })
              .catch((error) => {
                reject("Upload failed");
                console.error("Error:", error);
              });

            });
          },
        }
      }

    ]

    return {
      modules, content, clearEditor, setContent
    }
  },
  methods: {
    submit() {
      this.$emit('submit', this.value);
    },
  },
}
</script>

<style>

html.dark .ql-toolbar .ql-stroke {
  fill: none;
  stroke: #fff !important;
}

html.dark .ql-toolbar .ql-fill {
  fill: #fff !important;
  stroke: none;
}

html.dark .ql-toolbar .ql-picker {
  color: #fff !important;
  background: #626262 !important;
}

html.dark .ql-toolbar .ql-picker .ql-picker-options {
  background: #747474;
}

.ql-tooltip.ql-editing{
  left: 30px !important;
  top: 10px !important;
}
</style>
