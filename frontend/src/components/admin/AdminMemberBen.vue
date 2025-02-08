<template>
  <div>
    <h2>신고 회원 정지</h2>
    <table>
      <thead>
        <tr>
          <th><input type="checkbox" @change="toggleSelectAll" :checked="isAllSelected" /></th>
          <th>아이디</th>
          <th>신고유형</th>
          <th>신고내용</th>
          <!-- <th>계정정지</th> -->
        </tr>
      </thead>
      <tbody>
        <tr v-for="report in reports" :key="report.id">
          <td><input type="checkbox" v-model="selectedReports" :value="report.id" /></td>
          <td>{{ report.email }}</td>
          <td>{{ report.reportType }}</td>
          <td>{{ report.reportMsg }}</td>
          <!-- <td>
            <button @click="confirmSuspendAccount(report.id)" class="suspend-button">계정정지</button>
          </td> -->
        </tr>
      </tbody>
    </table>
    <button @click="suspendSelectedAccounts" class="suspend-all-button">계정 정지</button>
  </div>
</template>

<script>
import { get, post } from '../../apis/axios'; // axios 경로를 수정하세요.

export default {
  data() {
    return {
      reports: [], // 신고된 회원 목록
      selectedReports: [], // 선택된 회원 IDs
      isAllSelected: false // 전체 선택 여부
    };
  },
  created() {
    this.fetchReports(); // 컴포넌트 생성 시 신고된 회원 리스트를 조회
  },
  methods: {
    // 신고된 회원 목록을 가져오는 메소드
    async fetchReports() {
      try {
        const response = await get('/admin/reportedMembers'); // 신고된 회원 API 호출
        if (response && response.data) {
          this.reports = response.data.content; // 응답에서 신고된 회원 목록을 받는다.
        }
      } catch (error) {
        console.error('Error fetching reported users:', error);
      }
    },
    // 계정 정지 확인 후 처리하는 메소드
    confirmSuspendAccount(userId) {
      const confirmed = window.confirm('이 계정을 정지하시겠습니까?');
      if (confirmed) {
        this.suspendAccount(userId); // 계정 정지 처리
      }
    },
    // 선택한 회원들의 계정을 정지 처리하는 메소드
    async suspendAccount(userId) {
      try {
        const response = await post(`/admin/suspendAccount/${userId}`); // 계정 정지 API 호출
        if (response && response.data) {
          this.$toast.success('회원이 정지되었습니다!');
          this.fetchReports(); // 정지 후 목록 갱신
        }
      } catch (error) {
        console.error('Error suspending account:', error);
        this.$toast.error('계정 정지 중 오류가 발생했습니다.');
      }
    },
    // 전체 선택/해제 기능
    toggleSelectAll() {
      if (this.isAllSelected) {
        this.selectedReports = this.reports.map(report => report.id); // 모든 회원 선택
      } else {
        this.selectedReports = []; // 모든 회원 선택 해제
      }
    },
    // 선택된 회원들의 계정을 정지하는 메소드
    async suspendSelectedAccounts() {
      if (this.selectedReports.length === 0) {
        this.$toast.error('정지할 회원을 선택해 주세요.');
        return;
      }
      
      const confirmed = window.confirm('선택한 회원들의 계정을 정지하시겠습니까?');
      if (confirmed) {
        try {
          const response = await post('/admin/suspendAccounts', { userIds: this.selectedReports }); // 여러 회원 정지 API 호출
          if (response && response.data) {
            this.$toast.success('선택한 회원들이 정지되었습니다!');
            this.fetchReports(); // 정지 후 목록 갱신
          }
        } catch (error) {
          console.error('Error suspending selected accounts:', error);
          this.$toast.error('회원 정지 중 오류가 발생했습니다.');
        }
      }
    }
  },
  computed: {
    // 전체 선택 상태를 계산
    isAllSelected: {
      get() {
        return this.selectedReports.length === this.reports.length;
      },
      set(value) {
        this.selectedReports = value ? this.reports.map(report => report.id) : [];
      }
    }
  }
};
</script>

<style scoped>
/* 스타일 추가 */
table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}
th, td {
  padding: 10px;
  text-align: left;
  border: 1px solid #ddd;
}
button.suspend-button {
  background-color: #f44336;
  color: white;
  padding: 8px 16px;
  border: none;
  cursor: pointer;
  border-radius: 4px;
}
button.suspend-button:hover {
  background-color: #e53935;
}
button.suspend-all-button {
  background-color: #4CAF50;
  color: white;
  padding: 10px 20px;
  border: none;
  cursor: pointer;
  border-radius: 4px;
  margin-top: 20px;
}
button.suspend-all-button:hover {
  background-color: #45a049;
}
input[type="checkbox"] {
  cursor: pointer;
}
</style>
