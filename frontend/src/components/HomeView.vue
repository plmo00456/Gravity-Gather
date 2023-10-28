<template>
  <div>
    안눙 {{user.name}}
  </div>
</template>

<script>
import {useUserStore} from "@/stores/user";
export default {
  name: 'MainView',
  setup() {
    const userStore = useUserStore();
    const user = userStore.userInfo;
    return {
      userStore, user
    };
  }
}
</script>
