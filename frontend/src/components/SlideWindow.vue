<template>
  <div v-if="show" class="background absolute left-0 top-0 w-full h-screen" @click="$emit('close')"></div>
  <transition name="slide">
    <div v-if="show" class="slide-window absolute left-0 top-0 bg-white w-1/3 h-screen z-10 shadow-2xl p-10 flex flex-col">
      <div class="text-3xl mb-10 font-semibold">
        {{title}}
      </div>
      <slot></slot>
    </div>
  </transition>
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
    }
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

.slide-window{
  z-index: 1000;
}

</style>
