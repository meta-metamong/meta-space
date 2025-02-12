<template>
  <div class="container">
    <h2>신고 회원 정지</h2>
    
    <!-- 신고된 회원 목록 테이블 (스크롤 추가) -->
    <div class="table-box">
      <table>
        <thead>
          <tr>
            <th><input type="checkbox" @change="toggleSelectAll" :checked="isAllSelected" /></th>
            <th>아이디</th>
            <th>신고당한 횟수</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="report in reports" :key="report.memId" @click="handleRowClick(report.memId)" :class="{'selected-row': selectedRow === report.memId}">
            <td>
              <!-- 체크박스를 클릭했을 때 handleRowClick이 호출되지 않도록 @click.stop 추가 -->
              <input type="checkbox" v-model="selectedReports" :value="report.memId" @click.stop />
            </td>
            <td>{{ report.email }}</td>
            <td>{{ report.reportedCnt }}</td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 신고 상세 내역 테이블 (스크롤 추가) -->
    <div class="table-box">
      <table v-if="selectedReportDetails.length > 0">
        <thead>
          <tr>
            <th>아이디</th>
            <th>신고유형</th>
            <th>신고내용</th>
            <th>신고날짜</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="detail in selectedReportDetails" :key="detail.memId">
            <td>{{ detail.email }}</td>
            <td>{{ detail.reportType }}</td>
            <td>{{ detail.reportMsg }}</td>
            <td>{{ detail.reportDate }}</td>
          </tr>
        </tbody>
      </table>
      <p v-else>선택한 회원의 신고 내역이 없습니다.</p>
    </div>

    <!-- 회원 정지 버튼 -->
    <button @click="suspendSelectedAccounts" class="suspend-all-button">회원 정지</button>
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
      selectedRow: null, // 클릭된 행의 ID 추적
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
      if (response && response.data) {
        this.reports = response.data.content;

        if (this.reports.length > 0) {
          const firstMemId = this.reports[0].memId;
          this.fetchReportDetails(firstMemId); // 첫 번째 회원의 신고 상세 내역 가져오기
        } else {
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
    if (this.selectedRow === memId) {
      // 이미 선택된 행을 다시 클릭하면 상세 내역을 그대로 유지
      return;  // 이미 클릭된 행이라면 상세 내역을 새로 가져오지 않음
    } else {
      this.selectedRow = memId; // 클릭된 행의 memId 저장
      this.fetchReportDetails(memId); // 클릭된 회원의 상세 정보 가져오기
    }
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
  async suspendSelectedAccounts() {
    if (this.selectedReports.length === 0) {
      alert('선택한 회원이 없습니다.');
      return;
    }

    const confirmed = window.confirm('선택한 회원들의 계정을 정지하시겠습니까?');
    if (confirmed) {
      try {
        const reportedIds = this.selectedReports;
        const reportCounts = this.selectedReports.map(memId => {
          const report = this.reports.find(r => r.memId === memId);
          return report ? report.reportedCnt : 0; // report가 없다면 기본값 0
        });

        const response = await post('/admin/banMembers', {
          reportedIds,
          reportCounts
        });

        if (response && response.data) {
          alert('선택한 회원들이 정지되었습니다!');
          await this.fetchReports();

          if (this.reports.length > 0) {
            const firstMemId = this.reports[0].memId;
            this.fetchReportDetails(firstMemId);
          } else {
            this.selectedReportDetails = [];
          }
        }
      } catch (error) {
        console.error('Error suspending selected accounts:', error);
      }
    }
  }
}
,
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
/* 기존 스타일 유지 */
.container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-align: center;
  margin-top: 20px;
}

/* 테이블 스타일 */
table {
  width: 100%;
  border-collapse: collapse;
}

th, td {
  padding: 10px;
  text-align: center;
  border: 1px solid #97bfff;
}

/* 테이블이 포함될 네모 박스 스타일 */
.table-box {
  width: 80%;
  max-height: 300px;
  overflow-y: auto;
  margin-top: 20px;
  border: 1px solid #c7c7c7;
  border-radius: 4px;
  height: 500px;
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

/* 선택된 행 배경색 */
.selected-row {
  background-color: #f1f1f1;
}

h2 {
  text-align: center;
  font-size: 24px;
  margin-bottom: 20px;
}
</style>
