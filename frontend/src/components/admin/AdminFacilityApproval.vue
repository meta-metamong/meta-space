<template>
  <div>
    <h2>시설 승인</h2>
    <table>
      <thead>
        <tr>
          <th>이메일</th>
          <th>회원구분</th>
          <th>계정상태</th>
          <th>예약수</th>
          <th>가입일</th>
          <th>정지</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="member in members" :key="member.email">
          <td>{{ member.email }}</td>
          <td>{{ member.role }}</td>
          <td>{{ member.accountStatus }}</td>
          <td>{{ member.reservationCount }}</td>
          <td>{{ member.createdAt }}</td>
          <td>
            <button @click="banMember(member.memId)">정지</button>
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
      members: [],
    };
  },
  created() {
    this.fetchMembers();
  },
  methods: {
    // 회원 목록을 가져오는 메소드
    async fetchMembers() {
      try {
        const response = await get('/admin/searchMembers');  
        console.log(response);
        this.members = response.data.content;  
      } catch (error) {
        console.error('Error fetching members:', error);
      }
    },
    
    // 선택한 회원을 정지하는 메소드
    async banMember(memId) {
      try {
        // 정지할 회원 ID 목록 생성 (여기서는 하나의 ID만 전송)
        const reportedIds = [memId];
        
        // POST 요청을 통해 서버로 ID 목록 전송
        const response = await post('/admin/banMembers', reportedIds);
        console.log(response.data);
        alert('회원이 정지되었습니다.');
        // 정지 작업 후 다시 회원 목록을 불러옵니다.
        this.fetchMembers();
      } catch (error) {
        console.error('Error banning member:', error);
        alert('정지 작업 중 오류가 발생했습니다.');
      }
    }
  }
};
</script>
