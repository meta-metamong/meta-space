<template>
  <div>
    <!-- 서버로부터 받은 메시지를 표시 -->
    <div v-for="(message, index) in messages" :key="index">
      <strong>{{ message.from }}:</strong> {{ message.text }}
    </div>

    <!-- 사용자 ID와 입력 필드를 함께 표시 -->
    <div class="input-container">
      <span class="user-id">{{ userId }}</span>  <!-- 사용자 ID 표시 -->
      <input v-model="messageText" @keyup.enter="sendMessage" placeholder="Enter message" />
    </div>
  </div>
</template>

<script>
import { getUserIdFromToken } from  "../../apis/axios";  // api.js 파일 경로에 맞게 수정

export default {
  data() {
    return {
      socket: null,  // WebSocket 객체
      messageText: '',  // 사용자가 입력한 메시지
      messages: [],  // 서버에서 받은 메시지들을 저장
      username: 'Admin', // 사용자 이름
      userId: null,  // 사용자 ID
    };
  },
  created() {
    this.connect();  // 컴포넌트 생성 시 WebSocket 연결
    this.userId = getUserIdFromToken();  // 토큰에서 사용자 ID 추출
    console.log("사용자 ID:", this.userId);  // 디코딩 결과 출력
  },
  methods: {
    // WebSocket 연결 설정
    connect() {
      this.socket = new WebSocket('ws://localhost:8080/ws');  // WebSocket 서버 URL
      this.socket.withCredentials = true;
      this.socket.onopen = () => {
        console.log('Connected to WebSocket server');
      };
      // 서버로부터 메시지를 수신했을 때 처리
      this.socket.onmessage = (event) => {
        const message = JSON.parse(event.data);

        // message.text가 JSON 형식인지 확인하고 추가 파싱
        let parsedText = message.text;
        if (typeof parsedText === "string") {
          try {
            const innerMessage = JSON.parse(parsedText);
            parsedText = innerMessage.text || innerMessage; // 내부 메시지의 text 필드 사용
          } catch (e) {
            // JSON 파싱 실패 시 원본 유지
          }
        }

        this.messages.push({ from: message.from, text: parsedText });
      };

      this.socket.onerror = (error) => {
        console.error('WebSocket error: ', error);
      };
      this.socket.onclose = () => {
        console.log('WebSocket connection closed');
      };
    },

    // 사용자 입력 메시지를 서버로 전송
    sendMessage() {
      if (this.socket && this.socket.readyState === WebSocket.OPEN) {
        const message = {
          from: this.username,
          text: this.messageText,
          userId: this.userId,
        };
        this.socket.send(JSON.stringify(message));  // JSON 형식으로 메시지 전송
        console.log('Message sent:', message);
        this.messageText = '';  // 입력창 초기화
      } else {
        console.error('WebSocket is not open.');
      }
    },
  },
  beforeDestroy() {
    if (this.socket) {
      this.socket.close();  // 컴포넌트 파괴 전에 WebSocket 연결 종료
    }
  },
};
</script>

<style scoped>
/* 스타일을 추가하여 입력창과 메시지 출력 영역 꾸미기 */
div {
  margin-bottom: 15px;
}

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
