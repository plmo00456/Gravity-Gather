import {defineStore} from "pinia";
import http from "@/api/http";
import {router} from "@/router/index.js"

export const useUserStore = defineStore({
    id: 'user',
    state: () => ({
        isLoggedIn: false,
        userInfo: null,
        dataResponse: null,
        friendList: [],
        token: {
            access: null,
            refresh: null,
        },
    }),
    persist: {
        key: 'token',
        storage: sessionStorage,
        path: ['token.access', 'token.refresh', 'isLoggedIn'],

    },
    getters: {
        username() {
            return this.userInfo ? this.userInfo.username : '';
        },
        getDataResponse: (state) => {
            state.dataResponse;
        }
    },
    actions: {
        async login(credentials) {
            return http.post('/user/login', credentials, {
                headers: {
                    'Content-Type': 'application/json',
                }
            })
            .then(response => {
                if (response.status === 200) {
                    if(response.headers['authorization']){
                        this.token.access = response.headers['authorization'];
                        this.token.refresh = response.headers['refresh'];
                        if(response.data.status !== 'UNVERIFIED')
                            this.isLoggedIn = true;

                        this.userInfo = response.data;
                    }
                    this.dataResponse = response;
                }
            })
            .catch(error => {
                this.dataResponse = error.response;
            });
        },

        async logout() {
            try {
                this.isLoggedIn = false;
                this.userInfo = null;
                this.token.access = null;
                this.token.refresh = null;

                if (router.currentRoute.value.path === '/') {
                    await router.go();
                } else {
                    await router.push({ path: '/' });
                }
            } catch (error) {
                console.error(error)
                throw error
            }

        },

        async sendMail(email) {
            return http.post('/user/emails/verification-requests', email, {
                headers: {
                    'Content-Type': 'application/json'
                }
            })
            .then(response => {
                if (response.status === 200) {
                    this.dataResponse = response;
                }
            })
            .catch(error => {
                this.dataResponse = error.response;
            });
        },

        async emailVerify(emailAndCode) {
            return http.post('/user/emails/verifications', emailAndCode, {
                headers: {
                    'Content-Type': 'application/json'
                }
            })
            .then(response => {
                if (response.status === 200) {
                    this.dataResponse = response;
                }
            })
            .catch(error => {
                this.dataResponse = error.response;
            });
        },

        async userInfoStateUpdate() {
            return http.post(`/user/info`, {
                headers: {
                    'Content-Type': 'application/json'
                }
            })
            .then(response => {
                if (response.status === 200) {
                    this.dataResponse = response;

                    let chkPhoto = false;
                    for (let key of Object.keys(response.data)) {
                        if (response.data[key] !== null && response.data[key] !== undefined) {
                            if(key === 'photo') chkPhoto = true;
                            this.userInfo[key] = response.data[key];
                        }
                    }
                    if(!chkPhoto)
                        this.userInfo['photo'] = null;
                }
            })
            .catch(error => {
                this.dataResponse = error.response;
            });
        },

        async userInfoUpdate(userInfo){
            return http.post(`/user/update`, userInfo, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            })
            .then(response => {
                if (response.status === 200) {
                    this.dataResponse = response;
                }
            })
            .catch(error => {
                this.dataResponse = error.response;
            });
        },

        async getFriends(friend){
            return http.post(`/user/friend/get`, friend, {
                headers: {
                    'Content-Type': 'application/json'
                }
            })
            .then(response => {
                if (response.status === 200) {
                    this.dataResponse = response;
                    this.friendList = response.data;
                }
            })
            .catch(error => {
                this.dataResponse = error.response;
            });
        },

        async addFriend(friend){
            return http.post(`/user/friend/add`, friend, {
                headers: {
                    'Content-Type': 'application/json'
                }
            })
            .then(response => {
                if (response.status === 200) {
                    this.dataResponse = response;
                    this.friendList = response.data;
                }
            })
            .catch(error => {
                this.dataResponse = error.response;
            });
        },

        async deleteFriend(friend){
            return http.post(`/user/friend/delete`, friend, {
                headers: {
                    'Content-Type': 'application/json'
                }
            })
            .then(response => {
                if (response.status === 200) {
                    this.dataResponse = response;
                    this.friendList = response.data;
                }
            })
            .catch(error => {
                this.dataResponse = error.response;
            });
        },
        async checkEmailDuplication(user){
            return http.post(`/user/login/checkEmailDuplication`, user, {
                headers: {
                    'Content-Type': 'application/json'
                }
            })
            .then(response => {
                if (response.status === 200) {
                    this.dataResponse = response;
                    return response.data;
                }
            })
            .catch(error => {
                this.dataResponse = error.response;
            });
        },
        async checkIdDuplication(user){
            return http.post(`/user/login/checkIdDuplication`, user, {
                headers: {
                    'Content-Type': 'application/json'
                }
            })
            .then(response => {
                if (response.status === 200) {
                    this.dataResponse = response;
                    return response.data;
                }
            })
            .catch(error => {
                this.dataResponse = error.response;
            });
        },
        async register(user){
            return http.post(`/user/login/register`, user, {
                headers: {
                    'Content-Type': 'application/json'
                }
            })
            .then(response => {
                if (response.status === 200) {
                    this.dataResponse = response;
                    return response.data;
                }
            })
            .catch(error => {
                this.dataResponse = error.response;
            });
        }
    }
})
