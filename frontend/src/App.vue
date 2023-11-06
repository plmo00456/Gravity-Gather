<template>
  <div id="app" class="h-screen w-screen">
    <navView v-if="!$route.meta.hideMenu"/>
    <router-view/>
  </div>
</template>

<script>
import NavView from "@/components/NavView.vue";

export default {
  name: 'App',
  components: {NavView},
}
</script>

<style>
* {
  font-family: 'Noto Sans CJK KR Regular', sans-serif;
  margin: 0;
  padding: 0;
}

#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
</style>
