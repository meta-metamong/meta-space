<template>
    <div>
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
            <td>{{ payment.fctName }}</td>
            <td>{{ payment.zoneName }}</td>
            <td>{{ formatDate(payment.payDate) }}</td>
            <td>{{ formatPrice(payment.payPrice) }}</td>
            <td>{{ payment.fctName }}</td>
            <td>{{ payment.payMethod }}</td>
          </tr>
          <!-- 총 매출을 마지막에 추가 -->
          <tr>
            <td colspan="3" style="text-align: right; font-weight: bold;">총 매출:</td>
            <td colspan="3">{{ formatPrice(totalRevenue) }}</td>
          </tr>
          <!-- 시설 개수 및 Zone 개수를 마지막에 추가 -->
          <!-- <tr>
            <td colspan="3" style="text-align: right; font-weight: bold;">시설 개수:</td>
            <td colspan="3">{{ totalFacilities }}</td>
          </tr>
          <tr>
            <td colspan="3" style="text-align: right; font-weight: bold;">Zone 개수:</td>
            <td colspan="3">{{ totalZones }}</td>
          </tr> -->
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
        totalFacilities: 0, // 시설 개수
        totalZones: 0 // Zone 개수
      };
    },
    created() {
      this.fetchPaymentDetails(); // 결제 내역 조회
      this.fetchTotalRevenue(); // 총 매출 조회
      this.fetchTotalCounts(); // 시설 및 Zone 개수 조회
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
  
      // 시설 및 Zone 개수를 가져오는 메소드
      async fetchTotalCounts() {
        try {
          const response = await get('/admin/getFacilityZoneCounts'); // 시설 및 Zone 개수 API 호출
          if (response && response.data) {
            this.totalFacilities = response.data.totalFacilities; // 시설 개수
            this.totalZones = response.data.totalZones; // Zone 개수
          }
        } catch (error) {
          console.error('Error fetching total counts:', error);
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
  th {
    background-color: #f4f4f4;
  }
  td {
    text-align: right;
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
  