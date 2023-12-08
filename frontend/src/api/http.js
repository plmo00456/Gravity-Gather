import axios from "axios";
import BACKEND_API_URL from "./backend";
import {useUserStore} from '@/stores/user';
import {router} from "@/router";
import Swal from 'sweetalert2'

const api = axios.create({
  baseURL: BACKEND_API_URL,
});

// 요청 인터셉터 설정
api.interceptors.request.use((config) => {
  const userStore = useUserStore()
  const token = userStore.token.access;
  const refresh = userStore.token.refresh;
  if (token) {
    config.headers['authorization'] = `Bearer ${token}`;
    config.headers['refresh'] = refresh;
  }
  return config;
}, (error) => {
  return Promise.reject(error);
});

// 응답 인터셉터 설정
api.interceptors.response.use((response) => {
  return response;
}, async (error) => {
  const originalRequest = error.config;
  if (error.response.status === 401 && !originalRequest._retry
      && error.response.data.error === 'UnableToken' || error.response.data.error === 'ExpiredToken') {
    originalRequest._retry = true;
    const userStore = useUserStore();
    const accessToken = await refreshAccessToken();
    userStore.token.access = accessToken;
    originalRequest.headers['authorization'] = 'Bearer ' + accessToken;
    return api(originalRequest);
  }else if(error.response.status === 401 && error.response.data.error === 'NotAuthorization'){
    const option  = {
      icon: 'error',
      confirmButtonText: '확인',
      confirmButtonColor: '#3c82f6',
      text: '로그인이 만료되었습니다.',
    };
    Swal.fire(option);
    const userStore = useUserStore();
    await userStore.logout();
    router.push('/user/login');
  }
  return Promise.reject(error);
});

async function refreshAccessToken() {
  const userStore = useUserStore()
  api.post('/user/refreshAccessToken', {
    headers: {
      'Content-Type': 'application/json',
      'refresh': userStore.token.refresh,
    }
  })
  .then(result => {
    userStore.token.access = result.headers['authorization'];
    userStore.token.refresh = result.headers['refresh'];
  }).catch((error) => {
    const option  = {
      icon: 'error',
      confirmButtonText: '확인',
      confirmButtonColor: '#3c82f6',
      text: error,
    };
    Swal.fire(option);
    router.push({
      name: 'LoginView',
    })
  })

}

export default api;
