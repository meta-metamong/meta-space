<template>
    <div>
      <h2>회원 목록</h2>
      <table>
        <thead>
          <tr>
            <th>이메일</th>
            <th>역할</th>
            <th>계정 상태</th>
            <th>예약 수</th>
            <th>가입 일자</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="member in members" :key="member.email">
            <td>{{ member.email }}</td>
            <td>{{ member.role }}</td>
            <td>{{ member.accountStatus }}</td>
            <td>{{ member.reservationCount }}</td>
            <td>{{ member.createdAt }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </template>
  
  <script>
  import { get } from '../../apis/axios.js';  
  
  export default {
    data() {
      return {
        members: [],
      };
    },
    created() {
      this.fetchMembers();
    },
    methods: {
      async fetchMembers() {
        try {
          const response = await get('/admin/searchMembers');  
          console.log(response);
          this.members = response.data.data;  
        } catch (error) {
          console.error('Error fetching members:', error);
        }
      }
    }
  };
  </script>
  