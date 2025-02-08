import { createStore } from "vuex";
import { login, logout, removeAccessToken } from "../apis/axios";
import router from "../router/index";

const saveUserIdInLocal = function (userId) {
  sessionStorage.setItem("userId", userId);
};

export const getUserIdInLocal = function () {
  return sessionStorage.getItem("userId");
};

const removeUserIdInLocal = function () {
  sessionStorage.removeItem("userId");
};

const store = createStore({
  state: {
    userId: null,
    onlineSocket: null,
    onlineUsers: [], // 온라인 사용자 목록
    messages: []  // WebSocket으로 받은 메시지를 저장
  },
  mutations: {
    saveUserId(state, payload) {
      state.userId = payload;
      saveUserIdInLocal(payload);
      if (state.userId === "admin") {
        router.push("/admin");
      } else {
        router.push("/");
      }
    },
    removeUserId(state) {
      state.userId = null;
      removeUserIdInLocal();
      location.href = "/";
    },
    initUserId(state, payload) {
      state.userId = payload;
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
    }
  },
  actions: {
    async loginRequest(context, payload) {
      const response = await login(payload);
      if (response.status === 200) {
        context.commit("saveUserId", response.data.content);
        context.dispatch("connectOnlineStatus");
      }else{
        return response.response.data.message;
      }
    },
    async logoutRequest(context) {
      const response = await logout();
      if (response.status === 200) {
        context.commit("removeUserId");
        context.commit("closeOnlineSocket");
      }
    },
    connectOnlineStatus(context) {
      if (!context.state.userId) return;  // userId가 없으면 리턴

      const socket = new WebSocket("ws://localhost:8080/ws");

      socket.onopen = () => {
        console.log("✅ 온라인 상태 웹소켓 연결됨");
        socket.send(
          JSON.stringify({ type: "login", userId: context.state.userId }) // userId로 수정
        );
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
