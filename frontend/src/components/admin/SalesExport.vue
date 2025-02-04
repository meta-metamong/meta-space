<template>
    <div class="container">
      <h3 class="text-center mb-4">달력 조회</h3>
  
      <!-- 날짜 선택 -->
      <div class="d-flex justify-content-center mb-3">
        <v-date-picker v-model="startDate" label="시작 날짜" class="mr-2" />
        <v-date-picker v-model="endDate" label="끝 날짜" />
      </div>
  
      <!-- 검색 버튼 -->
      <div class="d-flex justify-content-center mb-4">
        <v-btn color="primary" @click="fetchData">검색</v-btn>
      </div>
  
      <!-- 결과 테이블 -->
      <v-data-table
        v-if="tableData.length"
        :headers="headers"
        :items="tableData"
        item-key="id"
        class="elevation-1"
      >
        <template v-slot:item.date="{ item }">
          <span>{{ formatDate(item.date) }}</span>
        </template>
        <template v-slot:item.amount="{ item }">
          <span>{{ item.amount.toLocaleString() }} 원</span>
        </template>
      </v-data-table>
  
      <!-- 데이터 없을 때 메시지 -->
      <div v-else class="text-center">검색된 데이터가 없습니다.</div>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        startDate: null,
        endDate: null,
        headers: [
          { text: "날짜", value: "date" },
          { text: "금액", value: "amount" },
        ],
        tableData: [],
      };
    },
    methods: {
      async fetchData() {
        if (!this.startDate || !this.endDate) {
          this.$toast.error("시작 날짜와 끝 날짜를 모두 선택해 주세요.");
          return;
        }
  
        try {
          // API 호출 예시
          const response = await fetch(`/api/data?startDate=${this.startDate}&endDate=${this.endDate}`);
          const data = await response.json();
  
          // 예시로 받아온 데이터 처리 (필요에 맞게 수정)
          this.tableData = data.map((item) => ({
            id: item.id,
            date: item.date,
            amount: item.amount,
          }));
        } catch (error) {
          console.error("데이터 조회 오류:", error);
          this.$toast.error("데이터 조회에 실패했습니다.");
        }
      },
      formatDate(date) {
        const formattedDate = new Date(date);
        return `${formattedDate.getFullYear()}-${formattedDate.getMonth() + 1}-${formattedDate.getDate()}`;
      },
    },
  };
  </script>
  
  <style scoped>
  .container {
    max-width: 800px;
    margin: 0 auto;
  }
  
  .v-date-picker {
    max-width: 250px;
  }
  
  .v-btn {
    min-width: 150px;
  }
  </style>
  