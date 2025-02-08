<template>
    <div>
      <h2>회원 목록</h2>
      <table>
        <thead>
          <tr>
            <th>이메일</th>
            <th>회원구분</th>
            <th>계정상태</th>
            <th>예약수</th>
            <th>가입일</th>
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
          this.members = response.data.content;  
        } catch (error) {
          console.error('Error fetching members:', error);
        }
      }
    }
  };
  </script>
  