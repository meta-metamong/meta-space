<template>
    <div>
      <!-- 서버로부터 받은 메시지를 보여주는 textarea -->
      <textarea v-model="message" placeholder="회원으로부터 받은 메시지가 여기에 표시됩니다..." readonly></textarea>
      <button @click="submitAnswer">답변 등록</button>
      <p v-if="message">{{ message }}</p> <!-- 서버로부터 받은 메시지를 화면에 표시 -->
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        socket: null,  // WebSocket 객체
        message: '',   // 서버로부터 받은 메시지
        answerText: '',  // 사용자가 입력한 답변
      };
    },
    methods: {
      // WebSocket 연결 설정
      connectWebSocket() {
        this.socket = new WebSocket('ws://localhost:8080/ws'); // WebSocket 엔드포인트
        this.socket.withCredentials = true;
        
        // WebSocket 연결 성공 시
        this.socket.onopen = () => {
          console.log('WebSocket 연결 성공');
        };
  
        // 서버로부터 메시지 수신 시
        this.socket.onmessage = (event) => {
          this.message = event.data;  // 서버에서 받은 메시지를 message에 저장
        };
  
        // WebSocket 연결 오류 시
        this.socket.onerror = (error) => {
          console.error('WebSocket 오류:', error);
        };
  
        // WebSocket 연결 종료 시
        this.socket.onclose = () => {
          console.log('WebSocket 연결 종료');
        };
      },
  
      // 답변 등록 버튼 클릭 시 호출되는 메서드
      submitAnswer() {
        if (this.socket && this.socket.readyState === WebSocket.OPEN) {
          // 사용자 입력 답변을 WebSocket을 통해 서버로 전송
          const message = {
            type: 'answer',
            content: this.answerText,
          };
  
          this.socket.send(JSON.stringify(message));  // 메시지를 JSON 형식으로 전송
          console.log('Answer submitted via WebSocket:', message);
  
          // WebSocket을 통해 전송한 후, 사용자 입력을 초기화
          this.answerText = '';
        } else {
          console.error('WebSocket이 열려 있지 않습니다.');
        }
      },
    },
  
    mounted() {
      this.connectWebSocket();  // WebSocket 연결 시도
      console.log('WebSocket 상태:', this.socket.readyState);
    },
  
    beforeDestroy() {
      if (this.socket) {
        this.socket.close();  // 컴포넌트가 파괴되기 전에 WebSocket 연결 종료
      }
    },
  };
  </script>
  
  <style scoped>
  /* 스타일을 여기에 추가 */
  textarea {
    width: 100%;
    height: 100px;
  }
  button {
    margin-top: 10px;
  }
  </style>
  