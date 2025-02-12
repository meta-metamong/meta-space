<template>
  <div>
    <h2>신고 회원 정지</h2>
    <!-- 신고된 회원 목록 테이블 -->
    <table>
      <thead>
        <tr>
          <th><input type="checkbox" @change="toggleSelectAll" :checked="isAllSelected" /></th>
          <th>아이디</th>
          <th>신고당한 횟수</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="report in reports" :key="report.memId">
          <td>
            <input type="checkbox" v-model="selectedReports" :value="report.memId" @click.stop="handleRowClick(report.memId)" />
          </td>
          <td>{{ report.email }}</td>
          <td>{{ report.reportedCnt }}</td>
        </tr>
      </tbody>
    </table>

    <button @click="suspendSelectedAccounts" class="suspend-all-button">회원 정지</button>

    <!-- 신고 상세 내역 테이블 -->
    <h3>신고 상세 내역</h3>
    <table>
      <thead>
        <tr>
          <th>아이디</th>
          <th>신고유형</th>
          <th>신고내용</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="detail in selectedReportDetails" :key="detail.memId">
          <td>{{ detail.email }}</td>
          <td>{{ detail.reportType }}</td>
          <td>{{ detail.reportMsg }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import { get, post } from '../../apis/axios'; // axios 경로 수정

export default {
  data() {
    return {
      reports: [], // 신고된 회원 목록
      selectedReports: [], // 선택된 회원 memId 목록
      selectedReportDetails: [], // 선택된 회원에 대한 상세 내역
      isAllSelected: false // 전체 선택 여부
    };
  },
  created() {
    this.fetchReports(); // 컴포넌트 생성 시 신고된 회원 리스트 조회
  },
  methods: {
    // 신고된 회원 목록을 가져오는 메소드
    async fetchReports() {
      try {
        const response = await get('/admin/reportedMembers');
        console.log('response', response);
        if (response && response.data) {
          this.reports = response.data.content;

          if (this.reports.length > 0) {
            const firstMemId = this.reports[0].memId;
            this.fetchReportDetails(firstMemId);
          }else {
            console.log('개수 비어있음');
            // 첫 번째 데이터가 없다면 빈 테이블 처리
            this.selectedReportDetails = [];
          }
        }
      } catch (error) {
        console.error('Error fetching reported users:', error);
      }
    },

    // 특정 신고 회원의 상세 정보를 가져오는 메소드
    async fetchReportDetails(memId) {
      try {
        const response = await get(`/admin/reportDetails/${memId}`);
        if (response && response.data) {
          this.selectedReportDetails = response.data;
        }
      } catch (error) {
        console.error('Error fetching report details:', error);
      }
    },

    // 행 클릭 시 상세 정보를 가져오는 메소드
    handleRowClick(memId) {
      this.fetchReportDetails(memId);
    },

    // 전체 선택/해제 기능
    toggleSelectAll() {
      if (this.isAllSelected) {
        this.selectedReports = this.reports.map(report => report.memId);
      } else {
        this.selectedReports = [];
      }
    },

    // 선택된 회원들의 계정을 정지하는 메소드
// 선택된 회원들의 계정을 정지하는 메소드
async suspendSelectedAccounts() {
  if (this.selectedReports.length === 0) {
    this.$toast.error('정지할 회원을 선택해 주세요.');
    return;
  }

  const confirmed = window.confirm('선택한 회원들의 계정을 정지하시겠습니까?');
  if (confirmed) {
    try {
      // 서버에 보낼 데이터 준비
      const reportedIds = this.selectedReports;

      // selectedReports에서 reportedCnt 값을 찾는 방법
      const reportCounts = this.selectedReports.map(memId => {
        const report = this.reports.find(r => r.memId === memId);
        return report ? report.reportedCnt : 0; // report가 없다면 기본값 0
      });

      // 확인된 값들 출력 (디버깅용)
      console.log('reportedIds:', reportedIds);
      console.log('reportCounts:', reportCounts);

      // 서버 요청
      const response = await post('/admin/banMembers', {
        reportedIds,  // 신고된 회원 ID 목록
        reportCounts  // 신고된 횟수 정보
      });

      console.log('response:', response);
      console.log('response.data:', response.data);

      if (response && response.data) {
        // 회원들이 정지되었다는 알림
        alert('선택한 회원들이 정지되었습니다!');
        
        // 신고된 회원 목록 갱신
        await this.fetchReports(); // 목록 갱신 후

        // 목록에 첫 번째 데이터가 있을 경우 상세 정보를 자동으로 가져오기
        if (this.reports.length > 0) {
          const firstMemId = this.reports[0].memId;
          this.fetchReportDetails(firstMemId); // 첫 번째 회원의 상세 내역을 가져옴
        } else {
          // 목록에 첫 번째 데이터가 없다면 빈 테이블 처리
          this.selectedReportDetails = [];
        }
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
        this.selectedReports = value
          ? this.reports.map(report => report.memId)
          : [];
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
button.suspend-all-button {
  background-color: #f44336;
  color: white;
  padding: 10px 20px;
  border: none;
  cursor: pointer;
  border-radius: 4px;
  margin-top: 20px;
}
button.suspend-all-button:hover {
  background-color: #e53935;
}
input[type="checkbox"] {
  cursor: pointer;
}
</style>
