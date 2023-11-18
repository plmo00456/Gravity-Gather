<template>
    <div id="main-view" class="flex flex-col items-center bg-main_background bg-cover text-black">
        <div class="flex w-4/6 h-full text-white py-10 justify-between">
            <div class="w-1/6">

            </div>
            <div class="w-5/6">
                <div id="editor" v-cloak></div>
            </div>
        </div>
    </div>
</template>


<script>
import { onMounted, ref} from 'vue';
import Editor from '@toast-ui/editor';

export default {
    setup() {
        const editor = ref(null);
        const editorValid = ref(null);
        const templateValue = ref("");

        onMounted(async () => {
            await import("@toast-ui/editor/dist/toastui-editor.css");
            await import("@toast-ui/editor/dist/theme/toastui-editor-dark.css");

            editor.value = new Editor({
                el: document.querySelector("#editor"),
                height: "100%",
                initialValue: templateValue.value,
                initialEditType: "markdown",
                language: "ko-KR",
                autofocus: true,
                theme: 'dark'
            });
        });

        return { editor, editorValid, templateValue };
    },
};
</script>

<style
>
#main-view {
    height: 93%;
}

[v-cloak] {
    display: none;
}

</style>