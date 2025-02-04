<template>
  <div>
    <!-- 서버로부터 받은 메시지를 표시 -->
    <div v-for="(message, index) in messages" :key="index">
      <strong>({{ message.userId }}):</strong> {{ message.text }}
    </div>

    <!-- 사용자 입력을 받는 부분 -->
    <input v-model="messageText" @keyup.enter="sendMessage" placeholder="Enter message" />
  </div>
</template>

<script>
export default {
  data() {
    return {
      socket: null,  // WebSocket 객체
      messageText: '',  // 사용자가 입력한 메시지
      messages: [],  // 서버에서 받은 메시지들을 저장
      username: 'Admin', // 사용자 이름
    };
  },
  created() {
    this.connect();  // 컴포넌트 생성 시 WebSocket 연결
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
  
  // message.text가 또 다른 JSON 문자열이라면 추가 파싱
  let parsedText;
  try {
    parsedText = JSON.parse(message.text);
  } catch (e) {
    parsedText = message.text; // JSON 파싱이 실패하면 그냥 문자열 유지
  }
  console.log("여기왔음");
  console.log("보내준아이디"+message.userId);
  this.messages.push({ from: message.from, userId: message.userId, text: parsedText.text || parsedText });
  console.log("보내준아이디"+message.userId);
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
input {
  width: 100%;
  padding: 8px;
  margin-top: 10px;
}
</style>
