import { createStore } from "vuex";
import { login, logout } from "../apis/axios";
import router from "../router/index";

const saveUserInLocal = function (user) {
  sessionStorage.setItem("user", JSON.stringify(user));
};

const getUserInLocal = function () {
  return JSON.parse(sessionStorage.getItem("user"));
};

const removeUserInLocal = function () {
  sessionStorage.removeItem("user");
};

const store = createStore({
  state: {
    user: null,
    onlineSocket: null,
    onlineUsers: [], // 온라인 사용자 목록
    messages: []  // WebSocket으로 받은 메시지를 저장
  },
  mutations: {
    saveUser(state, payload) {
      state.user = payload;
      saveUserInLocal(payload);
      if (state.user && state.user.userId === "admin") {
        router.push("/admin");
      } else {
        router.push("/");
      }
    },
    removeUser(state) {
      state.user = null;
      removeUserInLocal();
      location.href = "/";
    },
    setOnlineSocket(state, socket) {
      state.onlineSocket = socket;
    },
    closeOnlineSocket(state) {
      if (state.onlineSocket) {
        state.onlineSocket.send(
          JSON.stringify({ type: "logout", userId: state.user?.userId })
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
        context.commit("saveUser", response.data.content);
        context.dispatch("connectOnlineStatus");
      }
    },
    async logoutRequest(context) {
      const response = await logout();
      if (response.status === 200) {
        context.commit("closeOnlineSocket");
        context.commit("removeUser");
      }
    },
    connectOnlineStatus(context) {
      if (!context.state.user) return;

      const socket = new WebSocket("ws://localhost:8080/ws");

      socket.onopen = () => {
        console.log("✅ 온라인 상태 웹소켓 연결됨");
        socket.send(
          JSON.stringify({ type: "login", userId: context.state.user.userId })
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