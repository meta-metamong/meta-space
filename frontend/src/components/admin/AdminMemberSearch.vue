<template>
  <div>
    <h2>회원 조회</h2>
    <table class="table table-striped">
      <thead>
        <tr>
          <th>No</th>
          <th>이메일</th>
          <th>회원구분</th>
          <th>계정상태</th>
          <th>예약 개수</th>
          <th>예약취소 개수</th>
          <th>신고당한 횟수</th>
          <th>가입일</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(member, index) in members" :key="member.email">
          <td>{{ index + 1 }}</td> <!-- 인덱스 값에 1을 더해서 순번 표시 -->
          <td>{{ member.email }}</td>
          <td>{{ member.role }}</td>
          <td>{{ member.accountStatus }}</td>
          <td>{{ member.reservationCount }}</td>
          <td>{{ member.cancelledReservationCount }}</td> <!-- 예약 취소 개수 수정 -->
          <td>{{ member.reportCount }}</td> <!-- 신고당한 횟수 수정 -->
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
table {
  width: 100%; /* 테이블이 화면 전체 너비를 차지하도록 설정 */
  border-collapse: collapse; /* 테이블의 셀들이 겹쳐지지 않도록 설정 */
  border: 2px solid #d2d1d1; /* 테이블 바깥 테두리 추가 */
}
table th, table td {
  text-align: center; /* 모든 th와 td 가운데 정렬 */
  padding: 15px; /* 셀 안쪽 여백을 12px로 설정하여 간격을 늘림 */
}

table th {
  font-weight: bold; /* 열 제목을 굵게 */
}

table td {
  word-wrap: break-word; /* 긴 단어를 자동으로 줄 바꿈 처리 */
}
h2 {
  text-align: center; /* 텍스트를 가로로 가운데 정렬 */
  margin-top: 20px; /* 상단 여백 추가 */
  margin-bottom: 20px; /* 상단 여백 추가 */
  
  
}

</style>
