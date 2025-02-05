<template>
  <div>
    <!-- 서버로부터 받은 메시지를 표시 -->
    <div v-for="(message, index) in messages" :key="index">
      <strong>({{ message.userId }}):</strong> {{ message.text }}
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
      socket: null,
      messageText: '',
      messages: [],
    };
  },
  created() {
    this.connect();
  },
  computed: {
    user() {
      return toRaw(this.$store.state.user);
    },
  },
  methods: {
    connect() {
      this.socket = new WebSocket('ws://localhost:8080/ws');
      this.socket.withCredentials = true;
      this.socket.onopen = () => {
        console.log('Connected to WebSocket server');
      };
      this.socket.onmessage = (event) => {
        const message = JSON.parse(event.data);
        let parsedText = message.text;
        try {
          parsedText = JSON.parse(message.text);
        } catch (e) {
          parsedText = message.text;
        }
        console.log("서버로부터 수신된 메시지:", parsedText);
        this.messages.push({ userId: message.userId, text: parsedText.text || parsedText });
      };
      this.socket.onerror = (error) => {
        console.error('WebSocket error: ', error);
      };
      this.socket.onclose = () => {
        console.log('WebSocket connection closed');
      };
    },
    sendMessage() {
      if (this.socket && this.socket.readyState === WebSocket.OPEN) {
        const message = {
          userId: this.user.userId,
          text: this.messageText,
        };
        this.socket.send(JSON.stringify(message));
        console.log('보낸 메시지:', message);
        this.messages.push({ userId: message.userId, text: message.text }); // 보낸 메시지를 즉시 추가
        this.messageText = '';
      } else {
        console.error('WebSocket is not open.');
      }
    },
  },
  beforeDestroy() {
    if (this.socket) {
      this.socket.close();
    }
  },
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
