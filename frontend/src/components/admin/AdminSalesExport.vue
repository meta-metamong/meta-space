<template>
  <div class="container">
    <h2>매출 현황 조회</h2>
    <!-- 매출 내역과 총 매출을 포함하는 하나의 테이블 -->
    <table id="payment-table">
      <thead>
        <tr>
          <th>시설명</th>
          <th>Zone명</th>
          <th>결제일</th>
          <th>결제금액</th>
          <th>시설제공자명</th>
          <th>결제방법</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="payment in payments" :key="payment.fctId">
          <td class="left-align">{{ payment.fctName }}</td> <!-- 문자열은 왼쪽 정렬 -->
          <td class="left-align">{{ payment.zoneName }}</td> <!-- 문자열은 왼쪽 정렬 -->
          <td class="middle-align">{{ formatDate(payment.payDate) }}</td> <!-- 문자열은 왼쪽 정렬 -->
          <td class="right-align">{{ formatPrice(payment.payPrice) }}</td> <!-- 숫자는 오른쪽 정렬 -->
          <td class="left-align">{{ payment.memName }}</td> <!-- 문자열은 왼쪽 정렬 -->
          <td class="left-align">{{ payment.payMethod }}</td> <!-- 문자열은 왼쪽 정렬 -->
        </tr>
        <!-- 총 매출을 마지막에 추가 -->
        <tr>
          <td colspan="3" style="text-align: right; font-weight: bold;">총 매출:</td>
          <td colspan="3" class="right-align">{{ formatPrice(totalRevenue) }}</td> <!-- 숫자는 오른쪽 정렬 -->
        </tr>
      </tbody>
    </table>

    <!-- 엑셀 다운로드 버튼 추가 -->
    <button @click="downloadExcel">엑셀 다운로드</button>
  </div>
</template>

<script>
import { get } from '../../apis/axios'; // axios 경로를 수정하세요.
import * as XLSX from 'xlsx'; // xlsx 라이브러리 import

export default {
  data() {
    return {
      payments: [], // 결제 내역 리스트
      totalRevenue: 0, // 총 매출
    };
  },
  created() {
    this.fetchPaymentDetails(); // 결제 내역 조회
    this.fetchTotalRevenue(); // 총 매출 조회
  },
  methods: {
    // 결제 내역을 가져오는 메소드
    async fetchPaymentDetails() {
      try {
        const response = await get('/admin/salesExport'); // 결제 내역 API 호출
        if (response && response.data) {
          this.payments = response.data; // 결제 내역 데이터 할당
        }
      } catch (error) {
        console.error('Error fetching payment details:', error);
      }
    },

    // 총 매출을 가져오는 메소드
    async fetchTotalRevenue() {
      try {
        const response = await get('/admin/totalAmt'); // 총 매출 API 호출
        if (response && response.data) {
          this.totalRevenue = response.data; // 총 매출 데이터 할당
        }
      } catch (error) {
        console.error('Error fetching total revenue:', error);
      }
    },

    // 결제일 포맷팅 메소드
    formatDate(date) {
      const options = { year: 'numeric', month: '2-digit', day: '2-digit' };
      return new Date(date).toLocaleDateString('ko-KR', options);
    },

    // 결제금액 포맷팅 메소드 (소수점 2자리 금액)
    formatPrice(price) {
      return price ? price.toLocaleString() : '0';
    },

    // 엑셀 다운로드 기능
    downloadExcel() {
      // 테이블 데이터 추출
      const table = document.getElementById('payment-table');
      const ws = XLSX.utils.table_to_sheet(table); // 테이블을 워크시트로 변환
      const wb = XLSX.utils.book_new(); // 새로운 워크북 생성
      XLSX.utils.book_append_sheet(wb, ws, '매출 현황'); // 워크북에 워크시트 추가

      // 엑셀 파일 다운로드
      XLSX.writeFile(wb, '매출_현황.xlsx');
    }
  }
};
</script>

<style scoped>
/* 스타일 추가 */
.container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  text-align: center;
  margin-top: 20px;
}

table {
  width: 80%; /* 테이블의 너비를 설정하여 화면에 맞게 조정 */
  border-collapse: collapse;
  margin-top: 20px;
}

th, td {
  padding: 10px;
  border: 1px solid #ddd;
}

th {
  background-color: #f4f4f4;
  text-align: center; /* 열 이름을 가운데 정렬 */
}

.left-align {
  text-align: left; /* 문자열은 왼쪽 정렬 */
}

.right-align {
  text-align: right; /* 숫자는 오른쪽 정렬 */
}

button {
  margin-top: 20px;
  padding: 10px 20px;
  background-color: #4CAF50;
  color: white;
  border: none;
  cursor: pointer;
}

button:hover {
  background-color: #45a049;
}
</style>
