<template>
    <div>
      <h2>신고된 사용자 관리</h2>
      <table class="table">
        <thead>
          <tr>
            <th>신고당한 아이디</th>
            <th>신고유형</th>
            <th>신고내용</th>
            <th>계정상태</th>
            <th>계정정지</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in reportedUsers" :key="user.id">
            <td>{{ user.id }}</td>
            <td>{{ user.reportType }}</td>
            <td>{{ user.reportContent }}</td>
            <td>{{ user.isReported ? '신고됨' : '정상' }}</td>
            <td>
              <button 
                :disabled="!user.isReported" 
                @click="suspendAccount(user.id)" 
                class="btn btn-danger">
                계정 정지
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </template>
  
  <script>
  import { get, post } from '../../apis/axios.js';
  
  export default {
    data() {
      return {
        reportedUsers: [],
      };
    },
    created() {
      this.fetchReportedUsers();
    },
    methods: {
      async fetchReportedUsers() {
        try {
          const response = await get('/admin/searchReportedMembers');
          console.log(response);
          this.reportedUsers = response.data.content; // 서버에서 반환된 신고된 사용자 목록
        } catch (error) {
          console.error('Error fetching reported users:', error);
        }
      },
      async suspendAccount(userId) {
        try {
          // 계정 정지 요청 API
          const response = await post(`/admin/suspendAccount/${userId}`);
          if (response.data.success) {
            alert('계정이 정지되었습니다.');
            this.fetchReportedUsers();  // 사용자 목록 새로고침
          } else {
            alert('계정 정지에 실패했습니다.');
          }
        } catch (error) {
          console.error('Error suspending account:', error);
        }
      }
    }
  };
  </script>
  
  <style scoped>
  .table {
    width: 100%;
    margin-top: 20px;
  }
  
  .table th, .table td {
    text-align: center;
  }
  
  .btn-danger {
    background-color: red;
    color: white;
  }
  </style>
  