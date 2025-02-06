<template>
  <div>
    <!-- 서버로부터 받은 메시지를 표시 -->
    <div v-for="(message, index) in messages" :key="index">
      <strong v-if="message.text">({{ message.userId }}):</strong> {{ message.text }}
    </div>

    <!-- 사용자 입력을 받는 부분 -->
    <div class="input-container">
      <span class="user-id">({{ user.userId }})</span>
      <input v-model="messageText" @keyup.enter="sendMessage" placeholder="Enter message" />
    </div>
  </div>
</template>

<script>
import { toRaw } from 'vue';

export default {
  data() {
    return {
      messageText: ''
    };
  },
  computed: {
    user() {
      return toRaw(this.$store.state.user);
    },
    socket() {
      return this.$store.state.onlineSocket;
    },
    messages() {
      return this.$store.state.messages; // ✅ Vuex 상태 사용
    }
  },
  methods: {
    sendMessage() {
      if (this.socket && this.socket.readyState === WebSocket.OPEN) {
        const message = {
          userId: this.user.userId,
          text: this.messageText
        };

        // ✅ 메시지를 WebSocket을 통해 서버로 전송
        this.$store.dispatch("sendMessage", message);

        // ✅ 자기 자신이 보낸 메시지를 즉시 Vuex에 추가
        this.$store.commit("addMessage", message);

        this.messageText = ''; // 입력창 초기화
      } else {
        console.error('WebSocket is not open.');
      }
    }
  },
  watch: {
    socket(newSocket) {
      if (newSocket) {
        newSocket.onmessage = (event) => {
          const message = JSON.parse(event.data);
  
          let parsedText;
          try {
            parsedText = JSON.parse(message.text);
          } catch (e) {
            parsedText = message.text;
          }

          // ✅ Vuex의 messages에 추가 (this.messages.push 사용 X)
          this.$store.commit("addMessage", { userId: message.userId, text: parsedText.text || parsedText });
        };
      }
    }
  }
};
</script>

<style scoped>
.input-container {
  display: flex;
  align-items: center;
}
.user-id {
  margin-right: 10px;
  font-weight: bold;
  color: #333;
}
input {
  width: 100%;
  padding: 8px;
  margin-top: 10px;
}
</style>
