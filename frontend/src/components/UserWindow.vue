<template>
  <div class="w-full max-w-xs mx-auto">
    <div class="bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4">
      <div>
        <label class="block text-gray-700 text-sm font-bold mb-2">
          사용자 아이디
        </label>
        <p class="text-gray-500">{{ user.id }}</p>
      </div>

      <div>
        <label class="block text-gray-700 text-sm font-bold mb-2">
          이메일
        </label>
        <p class="text-gray-500">{{ user.email }}</p>
      </div>

      <div>
        <label class="block text-gray-700 text-sm font-bold mb-2">
          이름
        </label>
        <input class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" v-model="user.name">
      </div>

      <div>
        <label class="block text-gray-700 text-sm font-bold mb-2">
          닉네임
        </label>
        <input class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" v-model="user.nickname">
      </div>

      <div>
        <label class="block text-gray-700 text-sm font-bold mb-2">
          패스워드
        </label>
        <input type="password" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" v-model="user.password">
      </div>

      <div>
        <label class="block text-gray-700 text-sm font-bold mb-2">
          패스워드 확인
        </label>
        <input type="password" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" v-model="confirmPassword">
      </div>

      <div class="mt-4">
        <label class="block text-gray-700 text-sm font-bold mb-2">
          프로필 사진
        </label>
        <img :src="user.profilePicture" @click="uploadImage" class="w-32 h-32 object-cover cursor-pointer">
        <input type="file" ref="fileInput" class="hidden" @change="previewImage">
      </div>

      <div class="flex items-center justify-between mt-4">
        <button class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline" type="button" @click="updateProfile">
          프로필 수정
        </button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      user: {
        id: 'user123',
        email: 'user123@example.com',
        name: '',
        nickname: '',
        password: '',
        profilePicture: '',
      },
      confirmPassword: '',
    }
  },
  methods: {
    updateProfile() {
      // 프로필 수정 로직을 여기에 작성합니다.
      console.log(this.user);
    },
    uploadImage() {
      this.$refs.fileInput.click();
    },
    previewImage(event) {
      const file = event.target.files[0];
      if (file) {
        this.user.profilePicture = URL.createObjectURL(file);
      }
    },
  },
}
</script>

<style scoped>
</style>
