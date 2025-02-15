<template>
    <div>
      <h2>회원 조회</h2>
      <table class="table table-striped">
        <thead>
          <tr>
            <th>No</th>
            <th>이메일</th>
            <th>회원 구분</th>
            <th>계정 상태</th>
            <th>예약 개수</th>
            <th>예약취소 개수</th>
            <th>신고당한 횟수</th>
            <th>가입일</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(member, index) in paginatedMembers" :key="member.email">
            <td>{{ (currentPage - 1) * pageSize + index + 1 }}</td>
            <td>{{ member.email }}</td>
            <td>{{ member.role }}</td>
            <td>{{ member.accountStatus }}</td>
            <td>{{ member.reservationCount }}</td>
            <td>{{ member.cancelledReservationCount }}</td>
            <td>{{ member.reportedCount }}</td>
            <td>{{ member.createdAt }}</td>
          </tr>
        </tbody>
      </table>
  
      <!-- 페이지네이션 버튼 -->
      <div class="pagination">
        <button @click="prevPage" :disabled="currentPage === 1">이전</button>
        <span>{{ currentPage }} / {{ totalPages }}</span>
        <button @click="nextPage" :disabled="currentPage === totalPages">다음</button>
      </div>
    </div>
  </template>
  
  <script>
  import { get } from '../../apis/axios.js';
  
  export default {
    data() {
      return {
        members: [],  // 전체 회원 데이터
        currentPage: 1,  // 현재 페이지
        pageSize: 10  // 한 페이지당 표시할 개수
      };
    },
    computed: {
      totalPages() {
        return Math.ceil(this.members.length / this.pageSize);
      },
      paginatedMembers() {
        const start = (this.currentPage - 1) * this.pageSize;
        return this.members.slice(start, start + this.pageSize);
      }
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
      },
      nextPage() {
        if (this.currentPage < this.totalPages) {
          this.currentPage++;
        }
      },
      prevPage() {
        if (this.currentPage > 1) {
          this.currentPage--;
        }
      }
    }
  };
  </script>
  
  <style scoped>
  table {
    width: 100%;
    border-collapse: collapse;
    border: 2px solid #d2d1d1;
  }
  table th, table td {
    text-align: center;
    padding: 20px;
  }
  
  table th {
    font-weight: bold;
  }
  
  table td {
    word-wrap: break-word;
  }
  
  h2 {
    text-align: center;
    margin: 20px 0;
  }
  
  /* 페이지네이션 스타일 */
  .pagination {
    display: flex;
    justify-content: center;
    align-items: center;
    margin-top: 20px;
  }
  
  .pagination button {
    background-color: #007bff;
    color: white;
    border: none;
    padding: 10px 15px;
    margin: 0 5px;
    cursor: pointer;
    border-radius: 5px;
  }
  
  .pagination button:disabled {
    background-color: #ccc;
    cursor: not-allowed;
  }
  
  .pagination span {
    font-size: 16px;
    font-weight: bold;
  }
  </style>
  