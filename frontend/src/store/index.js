import { createStore } from "vuex";
import { login, logout } from "../apis/axios";
import router from "../router/index";

const saveUserInLocal = function(user) {
    sessionStorage.setItem("user", JSON.stringify(user));
}

const getUserInLocal = function(){
    return JSON.parse(sessionStorage.getItem("user"));
}

const removeUserInLocal = function() {
    sessionStorage.removeItem("user");
}

const store = createStore({
    // 공동 상태
    state: {
        user: null
    },
    // 동기적 로직 - 주로 state 변경
    mutations: {
        saveUser(state, payload){
            state.user = payload;
            router.push("/");
        },
        removeUser(state){
            state.user = null;
            location.href = "/";
        }
    },
    // state 값을 계산한 후 state로 저장
    getters:{

    },
    // 비동기적 로직 - 주로 api 요청, mutations에 대한 commit 가능능
    actions:{
        async loginRequest(context, payload){
            const response = await login(payload);
            if(response.status === 200) context.commit("saveUser", response.data.content);
        },
        async logoutRequest(context){
            const response = await logout();
            if(response.status === 200) context.commit("removeUser");
        }
    }
})

export default store;