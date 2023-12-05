import axios from "axios";
import BACKEND_API_URL from "./backend";
import store from '../stores/user';

const api = axios.create({
  baseURL: BACKEND_API_URL,
});

// 요청 인터셉터 설정
api.interceptors.request.use((config) => {
  const token = store.token.access;
  if (token) {
    config.headers['Authorization'] = `Bearer ${token}`;
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
  if (error.response.status === 401 && !originalRequest._retry) {
    originalRequest._retry = true;
    const accessToken = await refreshAccessToken(); // 새로운 토큰 받아오는 함수
    store.token = accessToken; // 새로운 토큰 저장
    originalRequest.headers['Authorization'] = 'Bearer ' + accessToken;
    return api(originalRequest); // 원래의 요청 재시도
  }
  return Promise.reject(error);
});

async function refreshAccessToken() {
  // 백엔드에게 새로운 토큰을 요청하는 코드를 작성하세요.
  // 예를 들어, 리프레시 토큰을 이용하여 새로운 토큰을 받아올 수 있습니다.
  // 이 함수는 새로운 토큰을 반환해야 합니다.
}

export default api;
