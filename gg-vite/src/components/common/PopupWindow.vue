<template>
    <div v-if="show" @click.self="$emit('close')"
         class="w-full h-full absolute left-0 top-0 bg-black bg-opacity-40 cursor-pointer z-50">
            <div
                v-bind:class="`${widthClass} ${heightClass} ${alignClass}`"
                class="cursor-default absolute p-5 flex-col rounded-2xl shadow-hard bg-white self-center left-[50%] top-[50%] flex translate-x-[-50%] translate-y-[-50%]">
            <div class="text-3xl font-semibold">
                {{ title }}
                <span
                    v-if="subTitle != null"
                    class="text-sm text-gray-400"
                >{{subTitle}}</span>
            </div>
            <slot></slot>
        </div>
    </div>
</template>

<script>
export default {
    props: {
        show: {
            type: Boolean,
            default: false
        },
        title: {
            type: String,
        },
        subTitle: {
            type: String,
        },
        widthClass: {
            type: String,
            default: 'w-2/5'
        },
        heightClass: {
            type: String,
            default: 'h-2/3'
        },
        alignClass: {
            type: String,
            default: 'justify-center'
        },
    },
};
</script>

<style scoped>
.slide-enter-active, .slide-leave-active {
    transition: transform 0.5s;
}

.slide-enter-from, .slide-leave-to {
    transform: translateX(-100%);
}

.slide-enter-to, .slide-leave-from {
    transform: translateX(0);
}

.background {
    background-color: rgba(0, 0, 0, 0.5);
    z-index: 999;
}

.slide-window {
    z-index: 1000;
}

</style>
