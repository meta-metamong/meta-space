<template>
  <div>
    <h2>회원 조회</h2>
    <table class="table table-striped">
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

<style scoped>
table th, table td {
  text-align: center; /* 모든 th와 td 가운데 정렬 */
  padding: 12px; /* 셀 안쪽 여백을 12px로 설정하여 간격을 늘림 */
}

table th {
  font-weight: bold; /* 열 제목을 굵게 */
}

table td {
  word-wrap: break-word; /* 긴 단어를 자동으로 줄 바꿈 처리 */
}
</style>
