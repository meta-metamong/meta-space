<template>
    <div class="online-users">
      <h2>🟢 접속 중인 사용자</h2>
      <ul v-if="onlineUsers.length">
        <li v-for="userId in onlineUsers" :key="userId">
          <span>{{ userId }}</span>
          <button @click="startChat(userId)">채팅하기</button>
        </li>
      </ul>
      <p v-else>현재 온라인인 사용자가 없습니다.</p>
    </div>
  </template>
  
  <script>
  import { mapState } from "vuex";
  import { toRaw } from "vue";
  
  export default {
    computed: {
      ...mapState(["onlineUsers", "onlineSocket", "user"]),
      user() {
        return toRaw(this.$store.state.user);
      }
    },
    methods: {
      startChat(targetUserId) {
        if (this.user !== "admin") {
          console.error("관리자만 채팅을 시작할 수 있습니다.");
          return;
        }
        
        if (this.onlineSocket && this.onlineSocket.readyState === WebSocket.OPEN) {
          const message = {
            type: "startChat",
            from: this.user, // 현재 로그인한 사용자 (관리자)
            to: targetUserId, // 채팅을 시작할 대상 사용자
            text: "채팅 요청"
          };
  
          this.onlineSocket.send(JSON.stringify(message));
          console.log(`관리자가 ${targetUserId} 에게 채팅 요청을 보냈습니다.`);
        } else {
          console.error("WebSocket is not open.");
        }
      }
    }
  };
  </script>
  
  <style scoped>
  .online-users {
    padding: 20px;
    border: 1px solid #ddd;
    border-radius: 5px;
    width: 300px;
    background-color: #f9f9f9;
  }
  
  h2 {
    font-size: 18px;
    margin-bottom: 10px;
  }
  
  ul {
    list-style: none;
    padding: 0;
  }
  
  li {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 8px 0;
  }
  
  button {
    background-color: #4caf50;
    color: white;
    border: none;
    padding: 5px 10px;
    border-radius: 5px;
    cursor: pointer;
  }
  
  button:hover {
    background-color: #45a049;
  }
  </style>
  