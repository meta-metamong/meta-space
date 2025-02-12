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
    socketClient: null
  },
  mutations: {
    openWebSocket(state, payload) {
      console.log("openWebSocket");
      if (!state.socket) {
        let socket = new WebSocket(`${import.meta.env.VITE_WS_URL}?x-authorization=${sessionStorage.getItem("accessToken")}`);
        socket.onopen = (e) => {
          console.log("WebSocket session opened!");
        }

        socket.onclose = (e) => {
          console.log("WebSocket closed");
          console.log(e);
          reissue().then((response) =>{
            console.log(response);
            console.log("after reissue");
            socket = new WebSocket(`${import.meta.env.VITE_WS_URL}?x-authorization=${sessionStorage.getItem("accessToken")}`);

            socket.onopen = (e) => {
              console.log("WebSocket session opened!");
            }

            socket.onerror = (e) => {
              console.error(e);
            }
    
            socket.onmessage = (msg) => {
              console.log(JSON.parse(msg.data));
            }
    
            state.socketClient = socket;
          })
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
    }
  },
  actions: {
    async loginRequest(context, payload) {
      const response = await login(payload);
      if (response.status === 200) {
        context.commit("saveUserId", response.data.content);
        context.commit("openWebSocket");
        // context.dispatch("connectOnlineStatus");
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
    connectOnlineStatus(context) {
      if (!context.state.userId) return;  // userId가 없으면 리턴

      const accessToken = sessionStorage.getItem("accessToken");
      
      const socket = new WebSocket(`${import.meta.env.VITE_WS_URL}?x-authorization=${accessToken}`);

      socket.onopen = () => {
        console.log("✅ 온라인 상태 웹소켓 연결됨");
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

            this.socketClient = socket;

            // socket.close();
      };

      socket.onmessage = (event) => {
        const message = JSON.parse(event.data);
        console.log("서버로부터 받은 메시지:", message);

        if (message.type === "updateOnlineUsers") {
          context.commit("setOnlineUsers", message.users);
        } else if (message.type === "chatMessage") {
          // 서버에서 받은 채팅 메시지를 Vuex에 저장
          context.commit("addMessage", message);
        }
      };

      socket.onclose = () => {
        console.log("❌ 온라인 상태 웹소켓 연결 해제됨");
      };

      socket.onerror = (error) => {
        console.error("❌ 웹소켓 오류:", error);
      };

      context.commit("setOnlineSocket", socket);
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
