<template>      
  <!-- 상단 네비게이션 -->
  <nav class="navbar fixed-top navbar-light bg-white border-bottom shadow-sm">
    <div class="container-fluid d-flex justify-content-between">          
      <!-- 중앙 로고 -->
      <router-link to="/" class="text-center me-5">
        <img src="../../resource/image/logo.png" alt="MetaSpace Logo" class="navbar-logo">
      </router-link>

      <!-- 우측 알림 아이콘 & 로그아웃 버튼 -->
      <div class="d-flex align-items-center" v-if="isLogin" >
        <button class="btn me-2" @click="checkNotifications">
          <i class="bi bi-bell"></i> 
          <span v-if="notificationCount > 0" class="badge">{{ notificationCount }}</span>
        </button>
        <button class="btn" @click="logout" v-text="$t('member.logout')" />
      </div>
      <div>
        <select class="custom-select" @change="handleLanguageChange">
          <option value="ko">KO</option>
          <option value="en">EN</option>
        </select>
      </div>
    </div>
  </nav>
</template>

<script>
import { ref, computed, onMounted } from "vue";
import { useStore } from "vuex";
import axios from "axios";
import { getUserIdInLocal } from "../../store";

export default {
name: "Header",
setup() {
  const store = useStore();
  const notificationCount = ref(0);
  const socket = computed(() => store.state.onlineSocket);
  const userId = computed(() => store.state.userId);

  // 알림 개수 조회 (웹소켓 연결 안 된 경우만)
  const checkNotifications = async () => {
    console.log("🆔 현재 userId:", userId.value);
    if (!userId.value) return;

    // 🔥 웹소켓이 연결된 경우 API 호출 X
    if (socket.value && socket.value.readyState === WebSocket.OPEN) {
      console.log("🚀 웹소켓 연결됨. API 호출 생략.");
      return;
    }

    try {
      const { data } = await axios.get(`/api/notifications/unread-count?userId=${userId.value}`);
      console.log("🔔 API 응답 알림 개수:", data);
      notificationCount.value = data;
    } catch (error) {
      console.error("❌ 알림 개수 조회 실패:", error);
    }
  };

  // 웹소켓 메시지 수신 시 알림 개수 업데이트
  const handleMessage = (event) => {
    const count = JSON.parse(event.data);
    console.log("📩 웹소켓 메시지 수신. 알림 개수 업데이트:", count);
    notificationCount.value = count;
  };

  // 초기 로그인 상태 설정
  const initUserId = () => {
    const localUserId = getUserIdInLocal();
    if (localUserId) {
      store.commit("initUserId", localUserId);
    }
  };

  // 로그인 상태 확인 및 초기화
  const isLogin = computed(() => store.state.userId !== null && store.state.userId !== undefined);

  // 로그아웃 처리
  const logout = () => {
    store.dispatch("logoutRequest");
  };

  // 언어 변경 처리
  const handleLanguageChange = (event) => {
    store.commit("setLanguage", event.target.value);
  };

  // 웹소켓 연결 상태 확인 및 알림 개수 초기화
  onMounted(() => {
    console.log("🔌 WebSocket 상태:", socket.value ? socket.value.readyState : "없음");

    initUserId();
    checkNotifications(); // ✅ 웹소켓이 없으면 API 호출

    if (socket.value) {
      socket.value.onmessage = handleMessage; // ✅ 웹소켓 연결되면 실시간 업데이트
    }
  });

  return {
    notificationCount,
    checkNotifications,
    isLogin,
    logout,
    handleLanguageChange
  };
}
};
</script>

<style scoped>
button {
color: #19319D;
border: 1px solid #19319D;
position: relative;
}

.navbar {
height: 55px;
}

.navbar-logo {
height: 25px; /* 로고 크기 조절 */
}

.badge {
position: absolute;
top: 0;
right: 0;
background: red;
color: white;
font-size: 12px;
border-radius: 50%;
padding: 3px 6px;
}

.custom-select {
width: 50px;
height: 30px;
border: 1px solid #19319D;
padding-left: 5px;
border-radius: 5px;
background-color: white;
font-size: 14px;
color: #19319D;
cursor: pointer;
outline: none;
}
</style>
