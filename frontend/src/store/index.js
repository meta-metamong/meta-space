import { createStore } from "vuex";
import { login, logout, reissue, removeAccessToken } from "../apis/axios";
import router from "../router/index";
import Swal from "sweetalert2";

const saveUserIdInLocal = function (userId) {
  sessionStorage.setItem("userId", userId);
};

export const getUserIdInLocal = function () {
  return sessionStorage.getItem("userId");
};

const saveUserRoleInLocal = function (userRole) {
  sessionStorage.setItem("userRole", userRole);
};

export const getUserRoleInLocal = function () {
  return sessionStorage.getItem("userRole");
};

const saveUsernameInLocal = function (username) {
  sessionStorage.setItem("userName", username);
};

export const getUsernameInLocal = function () {
  return sessionStorage.getItem("userName");
};

const removeUserIdInLocal = function () {
  sessionStorage.removeItem("userId");
};

const removeUserNameInLocal = function () {
  sessionStorage.removeItem("userName");
};

const removeUserRoleInLocal = function () {
  sessionStorage.removeItem("userRole");
};

const removeUser = function () {
  removeUserIdInLocal();
  removeUserNameInLocal();
  removeUserRoleInLocal();
  location.href = "/";
}

const store = createStore({
  state: {
    userId: null,
    userRole: null,
    userName: null,
    onlineSocket: null,
    onlineUsers: [], // 온라인 사용자 목록
    messages: [],  // WebSocket으로 받은 메시지를 저장
    socketClient: null,
    loc: {
      lat: 37.5717571,
      lon: 127.0009843
    }
  },
  mutations: {
    openWebSocket(state, payload) {
      console.log("openWebSocket");
      if (!state.socket) {
        
        let socket = new WebSocket(`${import.meta.env.VITE_WS_URL}?mem-id=${getUserIdInLocal()}`);
        socket.onopen = (e) => {
          console.log("WebSocket session opened!");
        }

        socket.onclose = (e) => {
          console.log("WebSocket closed");
          console.log(e);
        }

        socket.onerror = (e) => {
          console.error(e);
        }

        socket.onmessage = (msg) => {
          console.log(JSON.parse(msg));
        }

        state.socketClient = socket;
      }
    },
    saveUserId(state, payload) {
      state.userId = payload.memId;
      state.userRole = payload.role;
      state.userName = payload.name;
      saveUserIdInLocal(payload.memId);
      saveUserRoleInLocal(payload.role);
      saveUsernameInLocal(payload.name);
      
      Swal.fire({
        title: `환영합니다\n ${state.userName}님`,
        icon: 'success',
        width: '300px'
      });

      if (state.userRole === "ROLE_ADMN") {
        router.push("/admin");
      } else {
        router.push("/");
      }
    },
    initUserId(state, payload) {
      state.userId = payload;
    },
    initUserRole(state, payload) {
      state.userRole = payload;
    },
    initUsername(state, payload) {
      state.userName = payload;
    },
    setOnlineSocket(state, socket) {
      state.onlineSocket = socket;
    },
    closeOnlineSocket(state) {
      if (state.onlineSocket) {
        state.onlineSocket.send(
          JSON.stringify({ type: "logout", userId: state.userId }) // userId로 수정
        );
        state.onlineSocket.close();
        state.onlineSocket = null;
      }
    },
    setOnlineUsers(state, users) {
      state.onlineUsers = users; // 온라인 사용자 목록 업데이트
    },
    addMessage(state, message) {
      state.messages.push(message); // 메시지 추가
    },
    deleteUser(){
      removeUser();
      removeAccessToken();
      location.href = "/";
    },
    saveLoc(state, payload) {
      state.loc = payload;
    }
  },
  actions: {
    async loginRequest(context, payload) {
      const response = await login(payload);
      if (response.status === 200) {
        context.commit("saveUserId", response.data.content);
        context.commit("openWebSocket");
      }else{
        return response.response.data.message;
      }
    },
    async logoutRequest(context) {
      const response = await logout();
      if (response.status === 200) {
        Swal.fire({
          title: `로그아웃\n되었습니다.`,
          icon: 'success',
          width: '300px'
        });
        removeUser();
        context.commit("closeOnlineSocket");
      }
    },
    sendMessage(context, message) {
      if (context.state.onlineSocket && context.state.onlineSocket.readyState === WebSocket.OPEN) {
        context.state.onlineSocket.send(JSON.stringify({ type: "chatMessage", ...message }));
      } else {
        console.error("웹소켓이 열려 있지 않습니다.");
      }
    }
  }
});

export default store;
